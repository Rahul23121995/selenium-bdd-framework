package com.showcase.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object Model for the Checkout Funnel.
 * Manages the multi-step checkout process and verifies mathematical sum calculations.
 */
public class CheckoutPage extends BasePage {

    // --- STEP 1: Your Information ---
    @FindBy(id = "first-name")
    private WebElement firstNameInput;

    @FindBy(id = "last-name")
    private WebElement lastNameInput;

    @FindBy(id = "postal-code")
    private WebElement postalCodeInput;

    @FindBy(id = "continue")
    private WebElement continueButton;

    // --- STEP 2: Checkout Overview ---
    @FindBy(css = ".summary_subtotal_label")
    private WebElement subtotalLabel;

    @FindBy(css = ".summary_tax_label")
    private WebElement taxLabel;

    @FindBy(css = ".summary_total_label")
    private WebElement totalLabel;

    @FindBy(id = "finish")
    private WebElement finishButton;

    // --- STEP 3: Checkout Complete ---
    @FindBy(css = ".complete-header")
    private WebElement completeHeader;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage enterInformation(String firstName, String lastName, String postalCode) {
        writeText(firstNameInput, firstName, "First Name");
        writeText(lastNameInput, lastName, "Last Name");
        writeText(postalCodeInput, postalCode, "Postal Code");
        return this;
    }

    public void clickContinue() {
        click(continueButton, "Continue button");
    }

    public double getSubtotal() {
        String subtotalText = readText(subtotalLabel, "Subtotal label")
                .replaceAll("[^0-9.]", "");
        return Double.parseDouble(subtotalText);
    }

    public double getTax() {
        String taxText = readText(taxLabel, "Tax label")
                .replaceAll("[^0-9.]", "");
        return Double.parseDouble(taxText);
    }

    public double getTotal() {
        String totalText = readText(totalLabel, "Total label")
                .replaceAll("[^0-9.]", "");
        return Double.parseDouble(totalText);
    }

    /**
     * Verifies that the item subtotal + tax mathematically equals the final total charge.
     */
    public boolean verifyCalculations() {
        double subtotal = getSubtotal();
        double tax = getTax();
        double total = getTotal();
        double calculatedTotal = Math.round((subtotal + tax) * 100.0) / 100.0;
        return Double.compare(calculatedTotal, total) == 0;
    }

    public void clickFinish() {
        click(finishButton, "Finish button");
    }

    public String getConfirmationHeader() {
        return readText(completeHeader, "Completion header text");
    }
}
