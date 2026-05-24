package com.showcase.stepdefinitions;

import com.showcase.context.TestContext;
import com.showcase.pages.CartPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.List;

/**
 * Cucumber Step Definitions for Shopping Cart operations.
 */
public class CartSteps {
    private static final Logger log = LogManager.getLogger(CartSteps.class);
    private final TestContext context;
    private final CartPage cartPage;

    public CartSteps(TestContext context) {
        this.context = context;
        this.cartPage = context.getCartPage();
    }

    @Then("the shopping cart page should display, showing {int} items")
    public void verifyCartPageDisplay(int expectedItemsCount) {
        log.info("Step: Verifying Cart page loads showing exact count of: {}", expectedItemsCount);
        Assert.assertTrue(cartPage.isPageLoaded(), "Shopping cart page was not loaded successfully.");
        Assert.assertEquals(cartPage.getCartItemsCount(), expectedItemsCount, 
                "Actual item count in cart container does not match expectations.");
    }

    @Then("the cart list should contain the following items:")
    public void verifyCartContainsItems(DataTable dataTable) {
        List<String> expectedItems = dataTable.asList();
        log.info("Step: Asserting cart contains expected items: {}", expectedItems);
        for (String itemName : expectedItems) {
            Assert.assertTrue(cartPage.isItemInCart(itemName), 
                    "Required item '" + itemName + "' was not found in the shopping cart list.");
        }
    }

    @When("the user clicks the checkout button on the cart page")
    public void clickCheckoutButton() {
        log.info("Step: Clicking on checkout initiation button.");
        cartPage.clickCheckout();
    }
}
