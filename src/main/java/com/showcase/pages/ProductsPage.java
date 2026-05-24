package com.showcase.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object Model for the main Products Inventory Page.
 * Showcases dynamic element location and robust assertions.
 */
public class ProductsPage extends BasePage {
    private static final Logger log = LogManager.getLogger(ProductsPage.class);

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartButton;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = ".product_sort_container")
    private WebElement sortDropdown;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> itemNames;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> itemPrices;

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle, "Inventory page title") && 
               readText(pageTitle, "Inventory page title text").equalsIgnoreCase("Products");
    }

    /**
     * Dynamically adds a product to the cart by matching its text name.
     * Demonstrates dynamic locator construction and element navigation.
     */
    public ProductsPage addProductToCart(String productName) {
        String formattedName = productName.toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .trim()
                .replaceAll("\\s+", "-");
        String buttonId = "add-to-cart-" + formattedName;
        log.info("Clicking dynamic add-to-cart button with ID: {}", buttonId);
        clickViaJS(By.id(buttonId));
        return this;
    }

    public String getCartCount() {
        if (isDisplayed(cartBadge, "Cart badge count")) {
            return readText(cartBadge, "Cart badge count text");
        }
        return "0";
    }

    public void clickCart() {
        clickViaJS(cartButton, "Shopping Cart link");
    }

    public ProductsPage selectSortOption(String optionText) {
        log.info("Selecting sort option '{}' and waiting for catalog to sort...", optionText);
        
        Select select = new Select(sortDropdown);
        select.selectByVisibleText(optionText);

        try {
            // Highly robust, deterministic synchronization based on expected product sorting outcome
            if (optionText.equalsIgnoreCase("Price (high to low)")) {
                wait.until(org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated(
                        By.cssSelector(".inventory_item_name"), "Sauce Labs Fleece Jacket"));
                log.info("Catalog sorting verified: 'Sauce Labs Fleece Jacket' is now the first item.");
            } else if (optionText.equalsIgnoreCase("Price (low to high)")) {
                wait.until(org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated(
                        By.cssSelector(".inventory_item_name"), "Sauce Labs Onesie"));
                log.info("Catalog sorting verified: 'Sauce Labs Onesie' is now the first item.");
            }
        } catch (Exception e) {
            log.warn("Visual sorting verification wait timed out. Proceeding with execution.");
        }
        return this;
    }

    public List<String> getProductNames() {
        List<String> names = new ArrayList<>();
        for (WebElement element : itemNames) {
            names.add(element.getText().trim());
        }
        return names;
    }

    public List<Double> getProductPrices() {
        List<Double> prices = new ArrayList<>();
        for (WebElement element : itemPrices) {
            String priceText = element.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(priceText));
        }
        return prices;
    }
}
