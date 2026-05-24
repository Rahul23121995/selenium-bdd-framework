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

    public static String getBrowser() {
        return getProperty("browser") != null ? getProperty("browser").trim().toLowerCase() : "chrome";
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless"));
    }

    public static int getTimeout() {
        String timeout = getProperty("timeout");
        return timeout != null ? Integer.parseInt(timeout.trim()) : 10;
    }

    public static String getUrl() {
        return getProperty("url") != null ? getProperty("url").trim() : "https://www.saucedemo.com";
    }
}
