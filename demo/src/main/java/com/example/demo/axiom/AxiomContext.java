package com.example.demo.axiom;

public class AxiomContext {

    public int missingFields;
    public int typeChanges;
    public int newFields;
    public boolean functionalPass;
    public long responseTime;
    public Long threshold;

    public AxiomContext(int missingFields,
                        int typeChanges,
                        int newFields,
                        boolean functionalPass,
                        long responseTime,
                        Long threshold) {

        this.missingFields = missingFields;
        this.typeChanges = typeChanges;
        this.newFields = newFields;
        this.functionalPass = functionalPass;
        this.responseTime = responseTime;
        this.threshold = threshold;
    }
}