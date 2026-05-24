package com.showcase.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Main TestNG Runner class for executing BDD Cucumber scenarios.
 * Integrates TestNG for native parallel scenario execution.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.showcase.stepdefinitions", 
                "com.showcase.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-html-report.html",
                "json:target/cucumber-reports/cucumber.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        tags = "@AllTests"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    /**
     * Overrides TestNG scenarios provider and sets parallel = true to run 
     * multiple scenarios concurrently on different threads.
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
