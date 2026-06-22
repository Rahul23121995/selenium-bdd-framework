package com.showcase.context;

import com.showcase.driver.DriverManager;
import com.showcase.pages.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Dependency Injection context managed by PicoContainer. 
 * Allows sharing state (such as dynamic prices, order IDs) and Page Objects across step definition classes.
 */
public class TestContext {
    private final Map<String, Object> scenarioContext;
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    public TestContext() {
        this.scenarioContext = new HashMap<>();
    }

    /**
     * Retrieves the visual map containing all scenario context details.
     *
     * @return Map of context state variables.
     */
    public Map<String, Object> getScenarioContext() {
        return scenarioContext;
    }

    /**
     * Binds a session state variable dynamically to a scenario execution run.
     * Useful for storing dynamic values (e.g. total sum amounts, order numbers) generated at runtime.
     *
     * @param key   The variable identifier key.
     * @param value The object value.
     */
    public void setContext(String key, Object value) {
        scenarioContext.put(key, value);
    }

    /**
     * Retrieves a session state variable bound to the current scenario execution.
     *
     * @param key The variable identifier key.
     * @return The stored context value object.
     */
    public Object getContext(String key) {
        return scenarioContext.get(key);
    }

    // Lazy initialization of Page Objects with the active ThreadLocal WebDriver instance
    
    /**
     * Lazy-loads the LoginPage Page Object using the thread-bound WebDriver instance.
     *
     * @return Fully instantiated LoginPage object.
     */
    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(DriverManager.getDriver());
        }
        return loginPage;
    }

    /**
     * Lazy-loads the ProductsPage Page Object using the thread-bound WebDriver instance.
     *
     * @return Fully instantiated ProductsPage object.
     */
    public ProductsPage getProductsPage() {
        if (productsPage == null) {
            productsPage = new ProductsPage(DriverManager.getDriver());
        }
        return productsPage;
    }

    /**
     * Lazy-loads the CartPage Page Object using the thread-bound WebDriver instance.
     *
     * @return Fully instantiated CartPage object.
     */
    public CartPage getCartPage() {
        if (cartPage == null) {
            cartPage = new CartPage(DriverManager.getDriver());
        }
        return cartPage;
    }

    /**
     * Lazy-loads the CheckoutPage Page Object using the thread-bound WebDriver instance.
     *
     * @return Fully instantiated CheckoutPage object.
     */
    public CheckoutPage getCheckoutPage() {
        if (checkoutPage == null) {
            checkoutPage = new CheckoutPage(DriverManager.getDriver());
        }
        return checkoutPage;
    }
}
