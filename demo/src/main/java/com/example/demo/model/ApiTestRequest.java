package com.example.demo.model;

import java.util.List;
import java.util.Map;

public class ApiTestRequest{

    private String version;
    private String apiName;
    private String method;
    private String url;
    private Map<String, String> headers;
    private Object body;
    private Boolean enableBaseline;
    private Map<String,Object> expectedValues;
    private List<BusinessRule> businessRules;
    private String compareWith;
    private Long projectId;
    private Map<String, String> expectedSchema;
    private Integer maxResponseSize;

    public Boolean getEnableNullCheck() {
        return enableNullCheck;
    }

    public void setEnableNullCheck(Boolean enableNullCheck) {
        this.enableNullCheck = enableNullCheck;
    }

    public Integer getMaxResponseSize() {
        return maxResponseSize;
    }

    public void setMaxResponseSize(Integer maxResponseSize) {
        this.maxResponseSize = maxResponseSize;
    }

    public Map<String, String> getExpectedSchema() {
        return expectedSchema;
    }

    public void setExpectedSchema(Map<String, String> expectedSchema) {
        this.expectedSchema = expectedSchema;
    }

    private Boolean enableNullCheck;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
    public String getCompareWith() {
        return compareWith;
    }

    public void setCompareWith(String compareWith) {
        this.compareWith = compareWith;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<BusinessRule> getBusinessRules() {
        return businessRules;
    }

    public void setBusinessRules(List<BusinessRule> businessRules) {
        this.businessRules = businessRules;
    }

    public Map<String, Object> getExpectedValues() {
        return expectedValues;
    }

    public void setExpectedValues(Map<String, Object> expectedValues) {
        this.expectedValues = expectedValues;
    }

    public Boolean getEnableBaseline() {
        return enableBaseline;
    }

    public void setEnableBaseline(Boolean enableBaseline) {
        this.enableBaseline = enableBaseline;
    }



    private Integer expectedStatus;
    private List<String> expectedFields;

    // 🔥 Performance testing field
    private Long maxResponseTimeMs;

    // ========================
    // Getters & Setters
    // ========================

    public String getApiName() { return apiName; }
    public void setApiName(String apiName) { this.apiName = apiName; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }

    public Object getBody() { return body; }
    public void setBody(Object body) { this.body = body; }

    public Integer getExpectedStatus() { return expectedStatus; }
    public void setExpectedStatus(Integer expectedStatus) { this.expectedStatus = expectedStatus; }

    public List<String> getExpectedFields() { return expectedFields; }
    public void setExpectedFields(List<String> expectedFields) { this.expectedFields = expectedFields; }

    public Long getMaxResponseTimeMs() { return maxResponseTimeMs; }
    public void setMaxResponseTimeMs(Long maxResponseTimeMs) {
        this.maxResponseTimeMs = maxResponseTimeMs;
    }
}