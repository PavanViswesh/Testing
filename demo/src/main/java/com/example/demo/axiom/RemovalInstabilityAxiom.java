package com.example.demo.axiom;

public class RemovalInstabilityAxiom implements Axiom {

    @Override
    public String getName() {
        return "RemovalInstability";
    }

    @Override
    public double evaluate(AxiomContext context) {
        return 5.0 * context.missingFields;
    }
}