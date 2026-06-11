package com.showcase.stepdefinitions;

import com.showcase.config.ConfigReader;
import com.showcase.context.TestContext;
import com.showcase.pages.LoginPage;
import com.showcase.pages.ProductsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

/**
 * Cucumber Step Definitions for Login operations.
 * Utilizes constructor dependency injection (PicoContainer) to share the TestContext.
 */
public class LoginSteps {
    private static final Logger log = LogManager.getLogger(LoginSteps.class);
    private final TestContext context;
    private final LoginPage loginPage;
    private final ProductsPage productsPage;

    /**
     * Instantiates the LoginSteps definition class.
     * Constructor injection automatically maps the shared TestContext session container.
     *
     * @param context PicoContainer shared session context.
     */
    public LoginSteps(TestContext context) {
        this.context = context;
        this.loginPage = context.getLoginPage();
        this.productsPage = context.getProductsPage();
    }

    /**
     * Navigates the browser to the application base URL.
     */
    @Given("the user is on the Swag Labs login landing page")
    public void navigateToLogin() {
        log.info("Step: Navigating to Swag Labs login landing page.");
        loginPage.navigateTo(ConfigReader.getUrl());
    }

    /**
     * Enters username and password credentials on the login screen.
     *
     * @param username Target username.
     * @param password Target password.
     */
    @When("the user enters username {string} and password {string}")
    public void enterCredentials(String username, String password) {
        log.info("Step: Entering credentials. Username: '{}'", username);
        loginPage.enterUsername(username).enterPassword(password);
    }

    /**
     * Clicks the login submit button.
     */
    @When("clicks the login button")
    public void clickLoginButton() {
        log.info("Step: Clicking login button.");
        loginPage.clickLogin();
    }

    /**
     * Verifies that redirection to the main Products catalog page was successful.
     */
    @Then("the user should be successfully redirected to the products catalog catalog page")
    public void verifyCatalogRedirection() {
        log.info("Step: Verifying successful redirection to product catalog page.");
        Assert.assertTrue(productsPage.isPageLoaded(), "Redirection failed: Products catalog title was not displayed.");
    }

    /**
     * Verifies that the login error container matches the expected error text description.
     *
     * @param expectedErrorMessage Expected error description string.
     */
    @Then("the user should see an authentication error saying {string}")
    public void verifyAuthenticationError(String expectedErrorMessage) {
        log.info("Step: Verifying login error message matches expected: '{}'", expectedErrorMessage);
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Expected authentication error message container was not found.");
        Assert.assertEquals(loginPage.getErrorMessage(), expectedErrorMessage, "Authentication error message text mismatch.");
    }

    /**
     * Pre-requisite step to directly log into the application storefront.
     *
     * @param username Login username.
     * @param password Login password.
     */
    @Given("the user is logged into Swag Labs as a {string} with password {string}")
    public void logIntoApplication(String username, String password) {
        log.info("Step: Direct login flow as '{}'.", username);
        loginPage.navigateTo(ConfigReader.getUrl()).login(username, password);
        Assert.assertTrue(productsPage.isPageLoaded(), "Initial pre-requisite login failed!");
    }
}
