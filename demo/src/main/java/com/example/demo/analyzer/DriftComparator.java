package com.example.demo.analyzer;

import com.example.demo.model.DriftReport;
import com.example.demo.model.StructuralSignature;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DriftComparator {

    public DriftReport compare(StructuralSignature baseline,
                               StructuralSignature current) {

        Map<String, String> baseMap = baseline.getStructure();
        Map<String, String> currMap = current.getStructure();

        List<String> missing = new ArrayList<>();
        List<String> typeChanged = new ArrayList<>();
        List<String> newFields = new ArrayList<>();

        // Detect missing & type changes
        for (String key : baseMap.keySet()) {

            if (!currMap.containsKey(key)) {
                missing.add(key);
            } else if (!baseMap.get(key).equals(currMap.get(key))) {
                typeChanged.add(key);
            }
        }

        // Detect new fields
        for (String key : currMap.keySet()) {
            if (!baseMap.containsKey(key)) {
                newFields.add(key);
            }
        }

        return new DriftReport(missing, typeChanged, newFields);
    }
}