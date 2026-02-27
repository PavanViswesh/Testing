package com.example.demo.controller;

import com.example.demo.analyzer.StructuralExtractor;
import com.example.demo.model.StructuralSignature;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    private final StructuralExtractor extractor;

    public TestController(StructuralExtractor extractor) {
        this.extractor = extractor;
    }

    @PostMapping("/extract")
    public StructuralSignature extractStructure(@RequestBody String json) throws Exception {
        return extractor.extract(json);
    }
}
