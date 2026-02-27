package com.example.demo.graph;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ImpactGraphService {

    private final Map<String, Set<String>> dependencyGraph = new HashMap<>();

    public void registerBaselineStructure(Set<String> fields) {

        for (String field1 : fields) {
            for (String field2 : fields) {

                if (!field1.equals(field2) &&
                        isRelated(field1, field2)) {

                    addDependency(field1, field2);
                }
            }
        }
    }

    private boolean isRelated(String f1, String f2) {

        String key1 = extractIdentityToken(f1);
        String key2 = extractIdentityToken(f2);

        return key1.equalsIgnoreCase(key2);
    }

    private String extractIdentityToken(String field) {

        String[] parts = field.split("\\.");

        return parts[parts.length - 1]
                .replaceAll("Id", "")
                .replaceAll("id", "")
                .toLowerCase();
    }

    private void addDependency(String source, String target) {
        dependencyGraph
                .computeIfAbsent(source, k -> new HashSet<>())
                .add(target);
    }

    public Set<String> computeImpact(String changedField) {

        Set<String> impacted = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.add(changedField);

        while (!queue.isEmpty()) {

            String current = queue.poll();

            if (dependencyGraph.containsKey(current)) {

                for (String dependent : dependencyGraph.get(current)) {

                    if (!impacted.contains(dependent)) {
                        impacted.add(dependent);
                        queue.add(dependent);
                    }
                }
            }
        }

        return impacted;
    }
}