@ShoppingFeature @AllTests
Feature: Swag Labs E2E Shopping and Checkout Flow
  As a customer of Swag Labs
  I want to browse inventory, sort products, add items to my cart, and complete checkout
  So that I can purchase items securely with accurate billing totals.

  Background: User is logged into the application storefront
    Given the user is logged into Swag Labs as a "standard_user" with password "secret_sauce"

  @E2EPurchase @SmokeTest
  Scenario: Complete full end-to-end checkout with mathematical calculation checks
    Given the catalog page is loaded and displaying product listings
    When the user sorts the products by "Price (high to low)"
    And the user adds the following items to the shopping cart:
      | Sauce Labs Backpack      |
      | Sauce Labs Fleece Jacket |
    Then the shopping cart badge count should display "2"
    
    When the user clicks on the shopping cart icon
    Then the shopping cart page should display, showing 2 items
    And the cart list should contain the following items:
      | Sauce Labs Backpack      |
      | Sauce Labs Fleece Jacket |
      
    When the user clicks the checkout button on the cart page
    And the user enters checkout details with First Name "John", Last Name "Doe", and Postal Code "12345"
    And clicks the checkout continue button
    Then the checkout overview page is displayed
    And the checkout total calculations should be mathematically correct
    
    When the user clicks the checkout finish button
    Then the order confirmation screen should display "Thank you for your order!"
