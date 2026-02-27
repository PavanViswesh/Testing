package com.example.demo.service;

import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.time.LocalDate;
import java.util.Map;

@Service
public class RuleEngineService {

    // ===============================
    // SIMPLE RULE (>, <, ==, etc.)
    // ===============================
    public boolean evaluateRule(Object actual, String operator, Object expected) {

        if (actual == null) return false;

        try {
            double actualNum = Double.parseDouble(actual.toString());
            double expectedNum = Double.parseDouble(expected.toString());

            switch (operator) {
                case ">": return actualNum > expectedNum;
                case "<": return actualNum < expectedNum;
                case ">=": return actualNum >= expectedNum;
                case "<=": return actualNum <= expectedNum;
                case "==": return actualNum == expectedNum;
                case "!=": return actualNum != expectedNum;
            }

        } catch (Exception e) {
            // String fallback
            if ("==".equals(operator))
                return actual.toString().equals(expected.toString());

            if ("!=".equals(operator))
                return !actual.toString().equals(expected.toString());
        }

        return false;
    }

    // ===============================
    // GET NESTED VALUE (user.name etc)
    // ===============================
    public Object getNestedValue(Map<String, Object> map, String path) {

        String[] keys = path.split("\\.");
        Object current = map;

        for (String key : keys) {

            if (!(current instanceof Map)) {
                return null;
            }

            current = ((Map<?, ?>) current).get(key);

            if (current == null) {
                return null;
            }
        }

        return current;
    }

    // ===============================
    // EXPRESSION RULE (price > 100 && stock > 0)
    // ===============================
    public boolean evaluateExpression(String expression,
                                      Map<String, Object> jsonMap) {

        try {
            String parsed = expression;

            for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                parsed = parsed.replaceAll(
                        "\\b" + entry.getKey() + "\\b",
                        entry.getValue().toString()
                );
            }

            ScriptEngine engine =
                    new ScriptEngineManager()
                            .getEngineByName("JavaScript");

            Object result = engine.eval(parsed);

            if (result instanceof Boolean) {
                return (Boolean) result;
            }

        } catch (Exception e) {
            return false;
        }

        return false;
    }

    // ===============================
    // DATE RULE (startDate < endDate)
    // ===============================
    public boolean evaluateDateRule(String field1,
                                    String operator,
                                    String field2,
                                    Map<String, Object> jsonMap) {

        try {
            Object v1 = jsonMap.get(field1);
            Object v2 = jsonMap.get(field2);

            if (v1 == null || v2 == null) return false;

            LocalDate d1 = LocalDate.parse(v1.toString());
            LocalDate d2 = LocalDate.parse(v2.toString());

            switch (operator) {
                case "<": return d1.isBefore(d2);
                case ">": return d1.isAfter(d2);
                case "==": return d1.isEqual(d2);
            }

        } catch (Exception e) {
            return false;
        }

        return false;
    }
}