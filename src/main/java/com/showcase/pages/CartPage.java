package com.showcase.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object Model for the Shopping Cart Page.
 */
public class CartPage extends BasePage {

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> cartItemNames;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Checks if the shopping cart page is loaded successfully by validating the title.
     *
     * @return true if the page title is displayed and matches "Your Cart", false otherwise.
     */
    public boolean isPageLoaded() {
        return isDisplayed(pageTitle, "Cart page title") && 
               readText(pageTitle, "Cart page title text").equalsIgnoreCase("Your Cart");
    }

    /**
     * Retrieves the text names of all items currently present in the shopping cart.
     * Useful for assertions comparing added catalog items with cart contents.
     *
     * @return List of strings representing the item names.
     */
    public List<String> getCartItemNames() {
        List<String> names = new ArrayList<>();
        for (WebElement nameElement : cartItemNames) {
            names.add(nameElement.getText().trim());
        }
        return names;
    }

    /**
     * Asserts whether a specific product name exists in the current cart listings.
     *
     * @param productName Expected product name.
     * @return true if product name is found in the cart, false otherwise.
     */
    public boolean isItemInCart(String productName) {
        return getCartItemNames().contains(productName);
    }

    /**
     * Dynamically removes a product from the shopping cart based on its name.
     * Reconstructs the exact item ID to target the DOM node cleanly and triggers JS click.
     *
     * @param productName Product name to remove.
     */
    public void removeProductFromCart(String productName) {
        String formattedName = productName.toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .trim()
                .replaceAll("\\s+", "-");
        String buttonId = "remove-" + formattedName;
        clickViaJS(By.id(buttonId));
    }

    public int getCartItemsCount() {
        return cartItems.size();
    }

    public void clickCheckout() {
        click(checkoutButton, "Checkout button");
    }

    public void clickContinueShopping() {
        click(continueShoppingButton, "Continue Shopping button");
    }
}
