package com.example.demo.axiom;

public class TypeMutationAxiom implements Axiom {

    @Override
    public String getName() {
        return "TypeMutationAmplification";
    }

    @Override
    public double evaluate(AxiomContext context) {
        return 10.0 * context.typeChanges * context.typeChanges;
    }
}