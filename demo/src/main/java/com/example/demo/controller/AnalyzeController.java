package com.example.demo.controller;

import com.example.demo.analyzer.StructuralComplexityCalculator;
import com.fasterxml.jackson.databind.JsonNode;
import com.example.demo.analyzer.DriftComparator;
import com.example.demo.analyzer.StructuralExtractor;
import com.example.demo.model.*;
import com.example.demo.repository.AnalysisResultRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.service.RuleEngineService;
import com.example.demo.service.RuleTemplateService;
import com.example.demo.axiom.AxiomEngine;
import com.example.demo.axiom.AxiomContext;
import com.example.demo.graph.ImpactGraphService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/analyze")
public class AnalyzeController {

    private final StructuralExtractor extractor;
    private final DriftComparator comparator;
    private final RuleEngineService ruleEngineService;
    private final ImpactGraphService impactGraphService;
    private final ProjectRepository projectRepository;
    private final AnalysisResultRepository analysisResultRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public AnalyzeController(
            StructuralExtractor extractor,
            DriftComparator comparator,
            RuleEngineService ruleEngineService,
            ImpactGraphService impactGraphService,
            AnalysisResultRepository analysisResultRepository,
            ProjectRepository projectRepository) {

        this.extractor = extractor;
        this.comparator = comparator;
        this.ruleEngineService = ruleEngineService;
        this.impactGraphService = impactGraphService;
        this.analysisResultRepository = analysisResultRepository;
        this.projectRepository = projectRepository;
    }

    @PostMapping
    public Object analyze(@RequestBody ApiTestRequest request) {

        double CRI =0;
        Set<String> systemicImpacts = new HashSet<>();
        HttpHeaders headers = new HttpHeaders();
        if (request.getHeaders() != null) {
            request.getHeaders().forEach(headers::set);
        }

        HttpEntity<?> entity = new HttpEntity<>(request.getBody(), headers);

        ResponseEntity<String> response;
        long responseTime;

        try {
            long start = System.currentTimeMillis();

            response = restTemplate.exchange(
                    request.getUrl(),
                    HttpMethod.valueOf(request.getMethod()),
                    entity,
                    String.class
            );

            responseTime = System.currentTimeMillis() - start;

        } catch (HttpStatusCodeException ex) {
            return Map.of(
                    "statusCode", ex.getStatusCode().value(),
                    "errorBody", ex.getResponseBodyAsString(),
                    "message", "Target API returned error"
            );
        } catch (Exception ex) {
            return Map.of(
                    "statusCode", 500,
                    "message", "Analyzer could not reach target API",
                    "error", ex.getMessage()
            );
        }

        // ===============================
        // JSON PARSING
        // ===============================

        StructuralSignature current;
        Map<String, Object> rawJson;
        JsonNode rootNode;

        boolean functionalPass = true;
        List<String> functionalMessages = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();

            current = extractor.extract(response.getBody());
            rootNode = mapper.readTree(response.getBody());
            Object parsed = mapper.readValue(response.getBody(), Object.class);

            if (parsed instanceof Map) {
                rawJson = (Map<String, Object>) parsed;
            }
            else if (parsed instanceof List list && !list.isEmpty() && list.get(0) instanceof Map) {
                rawJson = (Map<String, Object>) list.get(0);
            }
            else {
                rawJson = new HashMap<>();
            }

        } catch (Exception e) {
            return Map.of("statusCode", 500, "message", "JSON parse failed");
        }

        // ===============================
        // QUICK TEST LOGIC (NEW)
        // ===============================

        int responseSize = response.getBody().length();

        if (request.getMaxResponseSize() != null &&
                responseSize > request.getMaxResponseSize()) {

            functionalPass = false;
            functionalMessages.add(
                    "Response size exceeded limit: " + responseSize + " bytes"
            );
        }

        if (request.getExpectedSchema() != null) {

            for (Map.Entry<String, String> expected :
                    request.getExpectedSchema().entrySet()) {

                String actualType =
                        current.getStructure().get(expected.getKey());

                if (actualType == null ||
                        !actualType.equalsIgnoreCase(expected.getValue())) {

                    functionalPass = false;
                    functionalMessages.add(
                            "Schema mismatch: " + expected.getKey()
                    );
                }
            }
        }

        if (Boolean.TRUE.equals(request.getEnableNullCheck())) {

            for (Map.Entry<String, Object> entry : rawJson.entrySet()) {

                if (entry.getValue() == null) {

                    functionalPass = false;
                    functionalMessages.add(
                            "Null field detected: " + entry.getKey()
                    );
                }
            }
        }

        // ===============================
        // STRUCTURAL COMPLEXITY
        // ===============================

        StructuralComplexityCalculator scsCalculator =
                new StructuralComplexityCalculator();

        AnalyzeResponse.StructuralComplexityResult scsResult =
                scsCalculator.compute(rootNode);

        // ===============================
        // VERSION COMPARISON
        // ===============================

        DriftReport report = new DriftReport(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        ObjectMapper mapper = new ObjectMapper();
        String snapshotJson = "";

        try {
            snapshotJson = mapper.writeValueAsString(current);
        } catch (Exception ignored) {}

        boolean versionMode = request.getVersion() != null &&
                !request.getVersion().isBlank();

        if (versionMode && request.getCompareWith() != null) {

            Optional<AnalysisResult> previousResult =
                    analysisResultRepository
                            .findTopByProjectIdAndToVersionOrderByIdDesc(
                                    request.getProjectId(),
                                    request.getCompareWith()
                            );

            if (previousResult.isPresent() &&
                    previousResult.get().getStructuralSnapshot() != null) {

                try {
                    StructuralSignature previous =
                            mapper.readValue(
                                    previousResult.get().getStructuralSnapshot(),
                                    StructuralSignature.class
                            );

                    report = comparator.compare(previous, current);

                } catch (Exception ignored) {}
            }
        }

        AnalyzeResponse.EvolutionResult evolution = null;

        if (versionMode && request.getCompareWith() != null) {

            Optional<AnalysisResult> previous =
                    analysisResultRepository
                            .findTopByProjectIdAndToVersionOrderByIdDesc(
                                    request.getProjectId(),
                                    request.getCompareWith()
                            );

            if (previous.isPresent()) {

                evolution = new AnalyzeResponse.EvolutionResult();

                double previousSCS = previous.get().getScsScore();
                double currentSCS = scsResult.getScore();

                double growth = 0;

                if (previousSCS > 0) {
                    growth = ((currentSCS - previousSCS) / previousSCS) * 100;
                }

                evolution.setComplexityGrowthPercentage(
                        Math.round(growth * 100.0) / 100.0
                );

                // Evolution Pattern
                if (report.getMissingFields() > report.getNewFields()) {
                    evolution.setEvolutionPattern("Destructive Mutation");
                } else if (report.getNewFields() > report.getMissingFields()) {
                    evolution.setEvolutionPattern("Expansive Evolution");
                } else {
                    evolution.setEvolutionPattern("Balanced Mutation");
                }

                // Systemic Amplification Level
                int systemicCount = systemicImpacts.size();

                if (systemicCount == 0)
                    evolution.setSystemicAmplificationLevel("Low");
                else if (systemicCount <= 3)
                    evolution.setSystemicAmplificationLevel("Moderate");
                else
                    evolution.setSystemicAmplificationLevel("High");

                // Contract Stability
                if (CRI <= 10)
                    evolution.setContractStability("Stable");
                else if (CRI <= 40)
                    evolution.setContractStability("Declining");
                else
                    evolution.setContractStability("Unstable");
            }
        }

        // ===============================
        // FIELD IMPACT BREAKDOWN
        // ===============================

        Map<String, Double> fieldImpactBreakdown = new HashMap<>();

        for (String field : report.getMissingFieldPaths()) {
            fieldImpactBreakdown.put(field, 10.0);
        }

        for (String field : report.getTypeChangedPaths()) {
            fieldImpactBreakdown.put(field, 6.0);
        }

        for (String field : report.getNewFieldPaths()) {
            fieldImpactBreakdown.put(field, 2.0);
        }

        String primaryRootCause = null;
        double maxImpact = 0.0;

        for (Map.Entry<String, Double> entry : fieldImpactBreakdown.entrySet()) {
            if (entry.getValue() > maxImpact) {
                maxImpact = entry.getValue();
                primaryRootCause = entry.getKey();
            }
        }

        // ===============================
        // RISK ENGINE
        // ===============================

        AxiomEngine axiomEngine = new AxiomEngine();

        AxiomContext context = new AxiomContext(
                report.getMissingFields(),
                report.getTypeChanges(),
                report.getNewFields(),
                functionalPass,
                responseTime,
                request.getMaxResponseTimeMs()
        );

        Map<String, Double> contributions =
                axiomEngine.evaluateAll(context);

        CRI = axiomEngine.computeCRI(contributions);

        double breakProbability =
                Math.round((1 - Math.exp(-CRI / 40.0)) * 10000.0) / 100.0;

        String riskCategory =
                CRI <= 5 ? "Stable" :
                        CRI <= 20 ? "Low Risk" :
                                CRI <= 50 ? "Moderate Risk" :
                                        "Critical";

        // ===============================
        // SAVE IF VERSION MODE
        // ===============================

        if (versionMode) {

            Project project = projectRepository.findByName("SYSTEM_DEFAULT")
                    .orElseGet(() -> {
                        Project p = new Project();
                        p.setName("SYSTEM_DEFAULT");
                        p.setEnvironment("AUTO");
                        return projectRepository.save(p);
                    });

            AnalysisResult entityResult = new AnalysisResult();

            entityResult.setProject(project);
            entityResult.setCri(CRI);
            entityResult.setBreakProbability(breakProbability);
            entityResult.setRiskCategory(riskCategory);
            entityResult.setMissingFields(report.getMissingFields());
            entityResult.setTypeChanges(report.getTypeChanges());
            entityResult.setNewFields(report.getNewFields());
            entityResult.setFromVersion(request.getCompareWith());
            entityResult.setToVersion(request.getVersion());
            entityResult.setStructuralSnapshot(snapshotJson);

            analysisResultRepository.save(entityResult);
        }

        // ===============================
        // RESPONSE
        // ===============================

        AnalyzeResponse result = new AnalyzeResponse();

        result.setStatusCode(response.getStatusCode().value());
        result.setResponseTimeMs(responseTime);
        result.setResponseSize(responseSize);

        AnalyzeResponse.FunctionalResult functional =
                new AnalyzeResponse.FunctionalResult();

        functional.setStatus(functionalPass ? "PASS" : "FAIL");

        if (functionalMessages.isEmpty()) {
            functional.setMessage("PASS");
        } else {
            functional.setMessage(String.join(" | ", functionalMessages));
        }

        result.setFunctional(functional);

        AnalyzeResponse.StructuralResult structure =
                new AnalyzeResponse.StructuralResult();

        structure.setMissingFields(report.getMissingFields());
        structure.setTypeChanges(report.getTypeChanges());
        structure.setNewFields(report.getNewFields());
        structure.setMissingFieldPaths(report.getMissingFieldPaths());
        structure.setTypeChangedPaths(report.getTypeChangedPaths());
        structure.setNewFieldPaths(report.getNewFieldPaths());

        result.setStructure(structure);

        AnalyzeResponse.RiskResult risk =
                new AnalyzeResponse.RiskResult();

        risk.setCRI(CRI);
        risk.setBreakProbability(breakProbability);
        risk.setRiskCategory(riskCategory);
        risk.setAxiomContributions(contributions);

        result.setRisk(risk);
        result.setStructuralComplexity(scsResult);

        result.setFieldImpactBreakdown(fieldImpactBreakdown);
        result.setPrimaryRootCauseField(primaryRootCause);
        result.setPrimaryImpactScore(maxImpact);
        result.setEvolution(evolution);

        return result;
    }
    @GetMapping("/versions/{projectId}")
    public List<String> getVersions(@PathVariable Long projectId) {
        return analysisResultRepository
                .findDistinctVersionsByProject(projectId);
    }
}
