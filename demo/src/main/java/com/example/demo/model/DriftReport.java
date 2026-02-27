package com.example.demo.model;

import java.util.List;

public class DriftReport {

    private List<String> missingFieldPaths;
    private List<String> typeChangedPaths;
    private List<String> newFieldPaths;

    public DriftReport(List<String> missingFieldPaths,
                       List<String> typeChangedPaths,
                       List<String> newFieldPaths) {
        this.missingFieldPaths = missingFieldPaths;
        this.typeChangedPaths = typeChangedPaths;
        this.newFieldPaths = newFieldPaths;
    }

    public List<String> getMissingFieldPaths() {
        return missingFieldPaths;
    }

    public List<String> getTypeChangedPaths() {
        return typeChangedPaths;
    }

    public List<String> getNewFieldPaths() {
        return newFieldPaths;
    }

    public int getMissingFields() {
        return missingFieldPaths.size();
    }

    public int getTypeChanges() {
        return typeChangedPaths.size();
    }

    public int getNewFields() {
        return newFieldPaths.size();
    }
}