package com.showcase.hooks;

import com.showcase.config.ConfigReader;
import com.showcase.driver.DriverFactory;
import com.showcase.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Setup and Teardown Cucumber hooks.
 * Isolates and manages WebDriver execution lifecycles, and automatically captures screenshots on failure.
 */
public class Hooks {
    private static final Logger log = LogManager.getLogger(Hooks.class);

    @Before
    public void setup(Scenario scenario) {
        log.info("========================================= START SCENARIO =========================================");
        log.info("Scenario Name: {}", scenario.getName());
        log.info("Tags: {}", scenario.getSourceTagNames());

        // Initialize driver instance for current execution thread
        WebDriver driver = DriverFactory.createInstance();
        DriverManager.setDriver(driver);

        // Open base application URL
        String url = ConfigReader.getUrl();
        log.info("Navigating thread browser to application URL: {}", url);
        driver.get(url);
    }

    @After
    public void teardown(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();

        if (driver != null) {
            try {
                // If scenario fails, capture browser screenshot and embed directly in Extent & Cucumber HTML reports
                if (scenario.isFailed()) {
                    log.error("Scenario failed! Capturing browser screenshot for: {}", scenario.getName());
                    final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    scenario.attach(screenshot, "image/png", "Failed_Scenario_Screenshot");
                } else {
                    log.info("Scenario completed successfully: {}", scenario.getName());
                }
            } catch (Exception e) {
                log.error("Failed to process teardown screenshot: ", e);
            } finally {
                // Clean up ThreadLocal driver instance
                DriverManager.quitDriver();
                log.info("========================================= END SCENARIO =========================================");
            }
        }
    }
}
