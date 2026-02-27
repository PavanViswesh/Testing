package com.example.demo.model;

import java.util.Map;

public class StructuralSignature {

    private Map<String, String> structure;
    private Map<String, Object> rawJsonMap;

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getArrayCount() {
        return arrayCount;
    }

    public void setArrayCount(int arrayCount) {
        this.arrayCount = arrayCount;
    }

    public int getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(int objectCount) {
        this.objectCount = objectCount;
    }

    private int maxDepth;
    private int arrayCount;
    private int objectCount;

    // ✅ REQUIRED for Jackson
    public StructuralSignature() {
    }

    public StructuralSignature(Map<String, String> structure) {
        this.structure = structure;
    }

    public Map<String, String> getStructure() {
        return structure;
    }

    public void setStructure(Map<String, String> structure) {
        this.structure = structure;
    }

    public Map<String, Object> getRawJsonMap() {
        return rawJsonMap;
    }

    public void setRawJsonMap(Map<String, Object> rawJsonMap) {
        this.rawJsonMap = rawJsonMap;
    }
}