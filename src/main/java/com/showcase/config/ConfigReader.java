package com.showcase.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class to read project global configurations from the properties file.
 */
public class ConfigReader {
    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            String configFilePath = "src/test/resources/config/config.properties";
            FileInputStream fileInputStream = new FileInputStream(configFilePath);
            properties.load(fileInputStream);
            fileInputStream.close();
            log.info("Successfully loaded project configuration from: {}", configFilePath);
        } catch (IOException e) {
            log.error("Failed to load config.properties file! Details: ", e);
            throw new RuntimeException("Could not read configuration properties. Terminating.");
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            log.warn("Property for key '{}' was not found in properties file.", key);
        }
        return value;
    }

    /**
     * Retrieves the configured browser name from configuration properties.
     * Defaults to "chrome" if not explicitly specified.
     *
     * @return Lowercase browser name string.
     */
    public static String getBrowser() {
        return getProperty("browser") != null ? getProperty("browser").trim().toLowerCase() : "chrome";
    }

    /**
     * Checks if the test execution is configured to run in headless browser mode.
     * Headless mode runs the browser background engine without opening a UI window.
     *
     * @return true if headless execution is enabled, false otherwise.
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless"));
    }

    /**
     * Retrieves the explicit timeout duration configured for dynamic sync waits.
     * Defaults to 10 seconds if parsing fails or property is missing.
     *
     * @return Timeout duration in seconds as integer.
     */
    public static int getTimeout() {
        String timeout = getProperty("timeout");
        return timeout != null ? Integer.parseInt(timeout.trim()) : 10;
    }

    /**
     * Retrieves the target application base URL configured for the test execution environment.
     * Defaults to the production URL of Saucedemo if not defined.
     *
     * @return Target URL string.
     */
    public static String getUrl() {
        return getProperty("url") != null ? getProperty("url").trim() : "https://www.saucedemo.com";
    }

    /**
     * Checks if the test execution session is configured to record video capture.
     * Note: Video capture only runs during headed local execution.
     *
     * @return true if video recording is enabled, false otherwise.
     */
    public static boolean isVideoRecordEnabled() {
        String record = getProperty("video.record");
        return record != null && Boolean.parseBoolean(record.trim());
    }
}
