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

    public Map<String, Object> getScenarioContext() {
        return scenarioContext;
    }

    public void setContext(String key, Object value) {
        scenarioContext.put(key, value);
    }

    public Object getContext(String key) {
        return scenarioContext.get(key);
    }

    // Lazy initialization of Page Objects with the active ThreadLocal WebDriver instance
    
    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(DriverManager.getDriver());
        }
        return loginPage;
    }

    public ProductsPage getProductsPage() {
        if (productsPage == null) {
            productsPage = new ProductsPage(DriverManager.getDriver());
        }
        return productsPage;
    }

    public CartPage getCartPage() {
        if (cartPage == null) {
            cartPage = new CartPage(DriverManager.getDriver());
        }
        return cartPage;
    }

    public CheckoutPage getCheckoutPage() {
        if (checkoutPage == null) {
            checkoutPage = new CheckoutPage(DriverManager.getDriver());
        }
        return checkoutPage;
    }
}
