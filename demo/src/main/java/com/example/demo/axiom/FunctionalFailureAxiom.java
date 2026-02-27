package com.example.demo.axiom;

public class FunctionalFailureAxiom implements Axiom {

    @Override
    public String getName() {
        return "FunctionalFailure";
    }

    @Override
    public double evaluate(AxiomContext context) {
        return context.functionalPass ? 0.0 : 20.0;
    }
}