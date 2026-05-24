package com.showcase.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * Thread-safe DriverManager container that manages active WebDriver instances for parallel scenario runs.
 */
public class DriverManager {
    private static final Logger log = LogManager.getLogger(DriverManager.class);
    
    // ThreadLocal container guarantees driver isolation across threads in parallel execution
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver webDriver) {
        log.debug("Setting WebDriver instance for Thread ID: {}", Thread.currentThread().getId());
        driver.set(webDriver);
    }

    public static void quitDriver() {
        WebDriver activeDriver = driver.get();
        if (activeDriver != null) {
            log.info("Quitting WebDriver for Thread ID: {}", Thread.currentThread().getId());
            try {
                activeDriver.quit();
            } catch (Exception e) {
                log.error("Error occurred while closing active browser instance: ", e);
            } finally {
                driver.remove();
            }
        }
    }
}
