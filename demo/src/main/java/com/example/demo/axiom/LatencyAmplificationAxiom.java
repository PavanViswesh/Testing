package com.example.demo.axiom;

public class LatencyAmplificationAxiom implements Axiom {

    @Override
    public String getName() {
        return "LatencyAmplification";
    }

    @Override
    public double evaluate(AxiomContext context) {

        if (context.threshold == null) return 0.0;

        if (context.responseTime > context.threshold) {
            return 5.0 * Math.log(
                    (double) context.responseTime / context.threshold
            );
        }

        return 0.0;
    }
}