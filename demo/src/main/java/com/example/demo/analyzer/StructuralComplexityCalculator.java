package com.example.demo.analyzer;

import com.example.demo.model.AnalyzeResponse.StructuralComplexityResult;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;
import java.util.Map;

public class StructuralComplexityCalculator {

    private int totalFields = 0;
    private int maxDepth = 0;
    private int arrayCount = 0;
    private int objectCount = 0;

    public StructuralComplexityResult compute(JsonNode root) {

        // Reset counters
        totalFields = 0;
        maxDepth = 0;
        arrayCount = 0;
        objectCount = 0;

        traverse(root, 1); // start depth at 1 (root level)

        double score =
                (totalFields * 1.0) +
                        (maxDepth * 2.0) +
                        (arrayCount * 1.5) +
                        (objectCount * 1.2);

        StructuralComplexityResult result =
                new StructuralComplexityResult();

        result.setTotalFields(totalFields);
        result.setMaxDepth(maxDepth);
        result.setArrayCount(arrayCount);
        result.setObjectCount(objectCount);
        result.setScore(Math.round(score * 100.0) / 100.0);

        return result;
    }

    private void traverse(JsonNode node, int depth) {

        maxDepth = Math.max(maxDepth, depth);

        if (node.isObject()) {

            objectCount++;

            Iterator<Map.Entry<String, JsonNode>> fields =
                    node.fields();

            while (fields.hasNext()) {

                Map.Entry<String, JsonNode> entry =
                        fields.next();

                totalFields++;

                traverse(entry.getValue(), depth + 1);
            }
        }

        else if (node.isArray()) {

            arrayCount++;

            if(node.size()>0){
                traverse(node.get(0),depth+1);
            }
        }
    }
}