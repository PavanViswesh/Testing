package com.example.demo.analyzer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.model.StructuralSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class StructuralExtractor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public StructuralSignature extract(String json) throws Exception {
        JsonNode rootNode = objectMapper.readTree(json);
        Map<String, String> structureMap = new HashMap<>();
        traverseNode(rootNode, "", structureMap);
        return new StructuralSignature(structureMap);
    }

    private void traverseNode(JsonNode node, String path, Map<String, String> structureMap) {

        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String newPath = path.isEmpty() ? entry.getKey() : path + "." + entry.getKey();
                traverseNode(entry.getValue(), newPath, structureMap);
            }
        }
        else if (node.isArray()) {
            structureMap.put(path, "ARRAY");
            if (node.size() > 0) {
                traverseNode(node.get(0), path + "[]", structureMap);
            }
        }
        else {
            structureMap.put(path, detectType(node));
        }
    }

    private String detectType(JsonNode node) {
        if (node.isTextual()) return "STRING";
        if (node.isNumber()) return "NUMBER";
        if (node.isBoolean()) return "BOOLEAN";
        if (node.isNull()) return "NULL";
        return "UNKNOWN";
    }
}
