package com.example.demo;

import com.example.demo.service.RuleEngineService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class RuleEngineTest {

    private final RuleEngineService service =
            new RuleEngineService();

    @Test
    void testNumericGreaterThan() {
        assertTrue(service.evaluateRule(10, ">", 5));
    }

    @Test
    void testNumericLessThan() {
        assertTrue(service.evaluateRule(3, "<", 5));
    }

    @Test
    void testStringEquals() {
        assertTrue(service.evaluateRule("admin", "==", "admin"));
    }

    @Test
    void testStringNotEquals() {
        assertTrue(service.evaluateRule("user", "!=", "admin"));
    }

    @Test
    void testNestedValue() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("name", "Pavan");

        Map<String, Object> outer = new HashMap<>();
        outer.put("user", inner);

        Object value = service.getNestedValue(outer, "user.name");

        assertEquals("Pavan", value);
    }
}