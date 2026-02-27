package com.example.demo.service;

import com.example.demo.model.UiTestResult;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Service
public class UiTestService {

    public UiTestResult runSmokeTest(String url) {

        UiTestResult result = new UiTestResult();
        long start = System.currentTimeMillis();

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        options.setAcceptInsecureCerts(true);

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get(url);

            result.setSuccess(true);
            result.setPageTitle(driver.getTitle());

        } catch (Exception e) {

            result.setSuccess(false);
            result.setError(e.getMessage());

        } finally {
            driver.quit();
        }

        result.setExecutionTime(System.currentTimeMillis() - start);
        return result;
    }
}