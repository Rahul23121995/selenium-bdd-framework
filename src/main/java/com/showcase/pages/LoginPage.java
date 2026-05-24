package com.showcase.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object Model for the login page of SauceDemo.
 * Demonstrates Fluent API design.
 */
public class LoginPage extends BasePage {

    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "h3[data-test='error']")
    private WebElement errorContainer;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage navigateTo(String url) {
        driver.get(url);
        return this;
    }

    public LoginPage enterUsername(String username) {
        writeText(usernameField, username, "Username field");
        return this;
    }

    public LoginPage enterPassword(String password) {
        writeText(passwordField, password, "Password field");
        return this;
    }

    public void clickLogin() {
        click(loginButton, "Login button");
    }

    /**
     * Helper to perform complete fluent login flow.
     */
    public void login(String username, String password) {
        enterUsername(username)
        .enterPassword(password)
        .clickLogin();
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorContainer, "Error container");
    }

    public String getErrorMessage() {
        return readText(errorContainer, "Error container text");
    }
}
