package com.example.demo.axiom;

public class AdditiveStabilityAxiom implements Axiom {

    @Override
    public String getName() {
        return "AdditiveStability";
    }

    @Override
    public double evaluate(AxiomContext context) {
        return 2.0 * context.newFields;
    }
}