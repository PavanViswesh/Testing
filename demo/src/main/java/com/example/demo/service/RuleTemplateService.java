package com.example.demo.service;

import java.util.*;

public class RuleTemplateService {

    private static final Map<String, String> templates = new HashMap<>();

    static {
        templates.put("NonNegativeAmount", "amount >= 0");
        templates.put("PositiveId", "id > 0");
    }

    public static String getTemplate(String name) {
        return templates.get(name);
    }
}