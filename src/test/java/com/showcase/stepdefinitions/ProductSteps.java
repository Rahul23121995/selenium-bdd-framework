package com.showcase.stepdefinitions;

import com.showcase.context.TestContext;
import com.showcase.pages.ProductsPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.List;

/**
 * Cucumber Step Definitions for Product and Store Catalog operations.
 */
public class ProductSteps {
    private static final Logger log = LogManager.getLogger(ProductSteps.class);
    private final TestContext context;
    private final ProductsPage productsPage;

    public ProductSteps(TestContext context) {
        this.context = context;
        this.productsPage = context.getProductsPage();
    }

    @Given("the catalog page is loaded and displaying product listings")
    public void verifyCatalogLoaded() {
        log.info("Step: Checking if product listings catalog page is fully loaded.");
        Assert.assertTrue(productsPage.isPageLoaded(), "Products Page was not loaded successfully.");
    }

    @When("the user sorts the products by {string}")
    public void sortProductsByOption(String sortOption) {
        log.info("Step: Selecting catalog sorting option: '{}'", sortOption);
        productsPage.selectSortOption(sortOption);
        
        // Assert sorting logic works as a premium demonstration of dynamic catalog testing
        if (sortOption.equalsIgnoreCase("Price (high to low)")) {
            List<Double> prices = productsPage.getProductPrices();
            log.debug("Verifying price sorting high to low. Catalog Prices: {}", prices);
            for (int i = 0; i < prices.size() - 1; i++) {
                Assert.assertTrue(prices.get(i) >= prices.get(i + 1), 
                        "Store catalog was not sorted correctly from high to low. Check items at index " + i);
            }
        }
    }

    @When("the user adds the following items to the shopping cart:")
    public void addProductsList(DataTable dataTable) {
        List<String> items = dataTable.asList();
        log.info("Step: Adding items to the shopping cart: {}", items);
        for (String itemName : items) {
            productsPage.addProductToCart(itemName);
            // Settle time for React state changes and animations to prevent rapid click swallowing
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.warn("Settle sleep was interrupted: ", e);
            }
        }
    }

    @Then("the shopping cart badge count should display {string}")
    public void verifyCartBadgeCount(String expectedCount) {
        log.info("Step: Verifying active shopping cart badge displays quantity: '{}'", expectedCount);
        Assert.assertEquals(productsPage.getCartCount(), expectedCount, "Shopping cart badge count mismatch.");
    }

    @When("the user clicks on the shopping cart icon")
    public void clickShoppingCartIcon() {
        log.info("Step: Navigating to cart details via header icon click.");
        productsPage.clickCart();
    }
}
