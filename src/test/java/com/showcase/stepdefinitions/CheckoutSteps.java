package com.showcase.stepdefinitions;

import com.showcase.context.TestContext;
import com.showcase.pages.CheckoutPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

/**
 * Cucumber Step Definitions for Checkout Funnel operations.
 */
public class CheckoutSteps {
    private static final Logger log = LogManager.getLogger(CheckoutSteps.class);
    private final TestContext context;
    private final CheckoutPage checkoutPage;

    /**
     * Instantiates the CheckoutSteps definition class.
     * Constructor injection automatically maps the shared TestContext session container.
     *
     * @param context PicoContainer shared session context.
     */
    public CheckoutSteps(TestContext context) {
        this.context = context;
        this.checkoutPage = context.getCheckoutPage();
    }

    /**
     * Inputs customer details inside the checkout form steps.
     *
     * @param firstName  Customer first name.
     * @param lastName   Customer last name.
     * @param postalCode Customer postal/zip code.
     */
    @When("the user enters checkout details with First Name {string}, Last Name {string}, and Postal Code {string}")
    public void enterCheckoutDetails(String firstName, String lastName, String postalCode) {
        log.info("Step: Filling checkout information form for: {} {}", firstName, lastName);
        checkoutPage.enterInformation(firstName, lastName, postalCode);
    }

    /**
     * Submits customer info and navigates to the checkout overview details.
     */
    @When("clicks the checkout continue button")
    public void clickContinueCheckout() {
        log.info("Step: Submitting checkout info and proceeding to review.");
        checkoutPage.clickContinue();
    }

    /**
     * Asserts that the checkout overview screen loads successfully with valid calculations.
     */
    @Then("the checkout overview page is displayed")
    public void verifyCheckoutOverview() {
        log.info("Step: Verifying checkout overview summary calculations are active.");
        double subtotal = checkoutPage.getSubtotal();
        log.debug("Subtotal displayed on Overview page: ${}", subtotal);
        Assert.assertTrue(subtotal > 0, "Failed to load Overview page calculations correctly: subtotal was 0.");
    }

    /**
     * Mathematically validates that subtotal + tax exactly equals the final charged total.
     */
    @Then("the checkout total calculations should be mathematically correct")
    public void verifyTotalCalculations() {
        log.info("Step: Validating mathematics of Tax and Subtotal calculations.");
        double subtotal = checkoutPage.getSubtotal();
        double tax = checkoutPage.getTax();
        double total = checkoutPage.getTotal();
        log.info("Financial validation: Subtotal (${}) + Tax (${}) = Total (${})", subtotal, tax, total);
        
        boolean calculationsCorrect = checkoutPage.verifyCalculations();
        Assert.assertTrue(calculationsCorrect, "Billing calculations mismatch! Tax plus subtotal does not sum up to total.");
        log.info("Financial calculations validated successfully.");
    }

    /**
     * Finalizes the order transaction by clicking the finish button.
     */
    @When("the user clicks the checkout finish button")
    public void clickFinishCheckout() {
        log.info("Step: Confirming and finishing purchase order.");
        checkoutPage.clickFinish();
    }

    /**
     * Asserts the order completion confirmation message matches expectations.
     *
     * @param expectedMessage Expected confirmation heading message.
     */
    @Then("the order confirmation screen should display {string}")
    public void verifyOrderConfirmation(String expectedMessage) {
        log.info("Step: Verifying checkout completion message is: '{}'", expectedMessage);
        String actualMessage = checkoutPage.getConfirmationHeader();
        Assert.assertEquals(actualMessage, expectedMessage, "Checkout completion message text mismatch.");
    }
}
