package com.showcase.driver;

import com.showcase.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Factory class to instantiate Chrome, Firefox, or Edge WebDrivers based on configuration.
 */
public class DriverFactory {
    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    public static WebDriver createInstance() {
        WebDriver driver;
        String browser = ConfigReader.getBrowser();
        boolean headless = ConfigReader.isHeadless();

        log.info("Initializing WebDriver. Target Browser: {}, Headless Mode: {}", browser, headless);

        switch (browser) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--width=1920");
                firefoxOptions.addArguments("--height=1080");
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--remote-allow-origins=*");
                edgeOptions.addArguments("--window-size=1920,1080");
                if (headless) {
                    edgeOptions.addArguments("--headless");
                }
                driver = new EdgeDriver(edgeOptions);
                break;

            default:
                log.error("Browser type '{}' not supported! Defaulting to Chrome.", browser);
                ChromeOptions defaultOptions = new ChromeOptions();
                defaultOptions.addArguments("--remote-allow-origins=*");
                if (headless) {
                    defaultOptions.addArguments("--headless=new");
                }
                driver = new ChromeDriver(defaultOptions);
                break;
        }

        driver.manage().window().maximize();
        int timeout = ConfigReader.getTimeout();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeout * 2));
        
        log.info("WebDriver instance successfully created and configured. Session ID is: {}", 
                ((org.openqa.selenium.remote.RemoteWebDriver) driver).getSessionId());
                
        return driver;
    }
}
