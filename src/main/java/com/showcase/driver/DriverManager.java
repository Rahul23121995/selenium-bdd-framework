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

    /**
     * Retrieves the active thread-bound WebDriver instance.
     * Guarantees that parallel scenarios running on different threads get their own isolated browser session.
     *
     * @return The active thread's WebDriver instance, or null if not yet initialized.
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Binds a newly created WebDriver instance to the current execution thread.
     *
     * @param webDriver The active WebDriver instance to associate with the current thread.
     */
    public static void setDriver(WebDriver webDriver) {
        log.debug("Setting WebDriver instance for Thread ID: {}", Thread.currentThread().getId());
        driver.set(webDriver);
    }

    /**
     * Safely closes the active thread-bound browser session and clears the ThreadLocal variable
     * to prevent memory leaks and thread pollution in thread-reused pools (like TestNG).
     */
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
