package com.example.demo.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnalyzeResponse {

    private int statusCode;
    private long responseTimeMs;

    private FunctionalResult functional;
    private StructuralResult structure;
    private RiskResult risk;
    private EvolutionResult evolution;

    public EvolutionResult getEvolution() {
        return evolution;
    }

    public void setEvolution(EvolutionResult evolution) {
        this.evolution = evolution;
    }
    public Integer getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(Integer responseSize) {
        this.responseSize = responseSize;
    }

    private Integer responseSize;

    public Map<String, Double> getFieldImpactBreakdown() {
        return fieldImpactBreakdown;
    }

    public void setFieldImpactBreakdown(Map<String, Double> fieldImpactBreakdown) {
        this.fieldImpactBreakdown = fieldImpactBreakdown;
    }

    public String getPrimaryRootCauseField() {
        return primaryRootCauseField;
    }

    public void setPrimaryRootCauseField(String primaryRootCauseField) {
        this.primaryRootCauseField = primaryRootCauseField;
    }

    public Double getPrimaryImpactScore() {
        return primaryImpactScore;
    }

    public void setPrimaryImpactScore(Double primaryImpactScore) {
        this.primaryImpactScore = primaryImpactScore;
    }

    private Map<String, Double> fieldImpactBreakdown;
    private String primaryRootCauseField;
    private Double primaryImpactScore;

    private StructuralComplexityResult structuralComplexity;

    public StructuralComplexityResult getStructuralComplexity() {
        return structuralComplexity;
    }

    public void setStructuralComplexity(StructuralComplexityResult structuralComplexity) {
        this.structuralComplexity = structuralComplexity;
    }

    public static class StructuralComplexityResult {

        private int totalFields;
        private int maxDepth;
        private int arrayCount;
        private int objectCount;
        private double score;

        public int getTotalFields() { return totalFields; }
        public void setTotalFields(int totalFields) { this.totalFields = totalFields; }

        public int getMaxDepth() { return maxDepth; }
        public void setMaxDepth(int maxDepth) { this.maxDepth = maxDepth; }

        public int getArrayCount() { return arrayCount; }
        public void setArrayCount(int arrayCount) { this.arrayCount = arrayCount; }

        public int getObjectCount() { return objectCount; }
        public void setObjectCount(int objectCount) { this.objectCount = objectCount; }

        public double getScore() { return score; }
        public void setScore(double score) { this.score = score; }
    }

    public static class FunctionalResult {
        private String status;   // PASS / FAIL
        private List<String> failedRules;
        private String message;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public List<String> getFailedRules() { return failedRules; }
        public void setFailedRules(List<String> failedRules) { this.failedRules = failedRules; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class StructuralResult {
        private int missingFields;
        private int typeChanges;
        private int newFields;

        private List<String> missingFieldPaths;
        private List<String> typeChangedPaths;
        private List<String> newFieldPaths;

        public int getMissingFields() { return missingFields; }
        public void setMissingFields(int missingFields) { this.missingFields = missingFields; }

        public int getTypeChanges() { return typeChanges; }
        public void setTypeChanges(int typeChanges) { this.typeChanges = typeChanges; }

        public int getNewFields() { return newFields; }
        public void setNewFields(int newFields) { this.newFields = newFields; }

        public List<String> getMissingFieldPaths() { return missingFieldPaths; }
        public void setMissingFieldPaths(List<String> missingFieldPaths) { this.missingFieldPaths = missingFieldPaths; }

        public List<String> getTypeChangedPaths() { return typeChangedPaths; }
        public void setTypeChangedPaths(List<String> typeChangedPaths) { this.typeChangedPaths = typeChangedPaths; }

        public List<String> getNewFieldPaths() { return newFieldPaths; }
        public void setNewFieldPaths(List<String> newFieldPaths) { this.newFieldPaths = newFieldPaths; }
    }

    public static class RiskResult {
        private double CRI;
        private double breakProbability;
        private String riskCategory;
        private Map<String, Double> axiomContributions;
        private Set<String> systemicImpactFields;
        private int systemicImpactCount;

        public int getSystemicImpactCount() {
            return systemicImpactCount;
        }

        public void setSystemicImpactCount(int systemicImpactCount) {
            this.systemicImpactCount = systemicImpactCount;
        }

        public Set<String> getSystemicImpactFields() {
            return systemicImpactFields;
        }

        public void setSystemicImpactFields(Set<String> systemicImpactFields) {
            this.systemicImpactFields = systemicImpactFields;
        }

        public double getCRI() { return CRI; }
        public void setCRI(double CRI) { this.CRI = CRI; }

        public double getBreakProbability() { return breakProbability; }
        public void setBreakProbability(double breakProbability) { this.breakProbability = breakProbability; }

        public String getRiskCategory() { return riskCategory; }
        public void setRiskCategory(String riskCategory) { this.riskCategory = riskCategory; }

        public Map<String, Double> getAxiomContributions() { return axiomContributions; }
        public void setAxiomContributions(Map<String, Double> axiomContributions) {
            this.axiomContributions = axiomContributions;
        }
    }

    public static class EvolutionResult {

        private double complexityGrowthPercentage;
        private String evolutionPattern;
        private String systemicAmplificationLevel;
        private String contractStability;

        public double getComplexityGrowthPercentage() {
            return complexityGrowthPercentage;
        }

        public void setComplexityGrowthPercentage(double complexityGrowthPercentage) {
            this.complexityGrowthPercentage = complexityGrowthPercentage;
        }

        public String getEvolutionPattern() {
            return evolutionPattern;
        }

        public void setEvolutionPattern(String evolutionPattern) {
            this.evolutionPattern = evolutionPattern;
        }

        public String getSystemicAmplificationLevel() {
            return systemicAmplificationLevel;
        }

        public void setSystemicAmplificationLevel(String systemicAmplificationLevel) {
            this.systemicAmplificationLevel = systemicAmplificationLevel;
        }

        public String getContractStability() {
            return contractStability;
        }

        public void setContractStability(String contractStability) {
            this.contractStability = contractStability;
        }
    }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public long getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(long responseTimeMs) { this.responseTimeMs = responseTimeMs; }

    public FunctionalResult getFunctional() { return functional; }
    public void setFunctional(FunctionalResult functional) { this.functional = functional; }

    public StructuralResult getStructure() { return structure; }
    public void setStructure(StructuralResult structure) { this.structure = structure; }

    public RiskResult getRisk() { return risk; }
    public void setRisk(RiskResult risk) { this.risk = risk; }
}