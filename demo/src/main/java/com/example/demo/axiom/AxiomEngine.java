package com.example.demo.axiom;

import java.util.*;

public class AxiomEngine {

    private final List<Axiom> axioms;

    public AxiomEngine() {
        axioms = List.of(
                new RemovalInstabilityAxiom(),
                new TypeMutationAxiom(),
                new AdditiveStabilityAxiom(),
                new FunctionalFailureAxiom(),
                new LatencyAmplificationAxiom()
        );
    }

    public Map<String, Double> evaluateAll(AxiomContext context) {

        Map<String, Double> results = new LinkedHashMap<>();

        for (Axiom axiom : axioms) {
            results.put(axiom.getName(),
                    axiom.evaluate(context));
        }

        return results;
    }

    public double computeCRI(Map<String, Double> contributions) {
        return contributions.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}