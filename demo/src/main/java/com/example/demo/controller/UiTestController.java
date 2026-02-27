package com.example.demo.controller;

import com.example.demo.model.UiTestResult;
import com.example.demo.service.UiTestService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ui-test")
@CrossOrigin(origins = "http://localhost:5173")
public class UiTestController {

    private final UiTestService uiTestService;

    public UiTestController(UiTestService uiTestService) {
        this.uiTestService = uiTestService;
    }

    @PostMapping("/run")
    public UiTestResult runTest(@RequestBody Map<String, String> request) {

        String url = request.get("url");
        return uiTestService.runSmokeTest(url);
    }
}