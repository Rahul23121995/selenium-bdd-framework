package com.showcase.pages;

import com.showcase.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Base Page class providing a centralized, resilient engine for element synchronization and interaction.
 * Eliminates flaky test issues by replacing hard sleeps with dynamic explicit waits.
 */
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final Logger log = LogManager.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        int timeout = ConfigReader.getTimeout();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        PageFactory.initElements(driver, this);
    }

    protected WebElement findElement(By locator) {
        log.debug("Waiting for visibility of element located by: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> findElements(By locator) {
        log.debug("Waiting for presence of elements located by: {}", locator);
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    protected void click(By locator) {
        log.info("Clicking element: {}", locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void click(WebElement element, String elementName) {
        log.info("Clicking element: {}", elementName);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected void clickViaJS(By locator) {
        log.info("Clicking element via JavaScript: {}", locator);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        executeJavaScript("arguments[0].click();", element);
    }

    protected void clickViaJS(WebElement element, String elementName) {
        log.info("Clicking element via JavaScript: {}", elementName);
        wait.until(ExpectedConditions.visibilityOf(element));
        executeJavaScript("arguments[0].click();", element);
    }

    protected void writeText(By locator, String text) {
        log.info("Typing '{}' into element: {}", text, locator);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    protected void writeText(WebElement element, String text, String elementName) {
        log.info("Typing '{}' into element: {}", text, elementName);
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    protected String readText(By locator) {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
        log.debug("Read text '{}' from locator: {}", text, locator);
        return text;
    }

    protected String readText(WebElement element, String elementName) {
        String text = wait.until(ExpectedConditions.visibilityOf(element)).getText().trim();
        log.debug("Read text '{}' from element: {}", text, elementName);
        return text;
    }

    protected boolean isDisplayed(By locator) {
        try {
            boolean displayed = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
            log.debug("Locator '{}' visibility status: {}", locator, displayed);
            return displayed;
        } catch (Exception e) {
            log.warn("Locator '{}' was not visible within timeout period.", locator);
            return false;
        }
    }

    protected boolean isDisplayed(WebElement element, String elementName) {
        try {
            boolean displayed = wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
            log.debug("Element '{}' visibility status: {}", elementName, displayed);
            return displayed;
        } catch (Exception e) {
            log.warn("Element '{}' was not visible within timeout period.", elementName);
            return false;
        }
    }

    protected void executeJavaScript(String script, Object... args) {
        log.debug("Executing Javascript: {}", script);
        ((JavascriptExecutor) driver).executeScript(script, args);
    }

    protected void scrollToElement(WebElement element) {
        log.debug("Scrolling to element.");
        executeJavaScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }
}
