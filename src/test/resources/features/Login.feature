@LoginFeature @AllTests
Feature: SauceDemo Authentication Verification
  As a registered Swag Labs customer
  I want to log in using my credentials
  So that I can access the storefront inventory and place orders.

  Background: User is on the login page
    Given the user is on the Swag Labs login landing page

  @PositiveLogin @SmokeTest
  Scenario: Success login with standard user credentials
    When the user enters username "standard_user" and password "secret_sauce"
    And clicks the login button
    Then the user should be successfully redirected to the products catalog catalog page

  @NegativeLogin
  Scenario: Blocked account login shows account locked error
    When the user enters username "locked_out_user" and password "secret_sauce"
    And clicks the login button
    Then the user should see an authentication error saying "Epic sadface: Sorry, this user has been locked out."

  @DataDrivenLogin
  Scenario Outline: Failed login with invalid or empty credentials
    When the user enters username "<username>" and password "<password>"
    And clicks the login button
    Then the user should see an authentication error saying "<error_message>"

    Examples:
      | username        | password       | error_message                                                             |
      | invalid_user    | secret_sauce   | Epic sadface: Username and password do not match any user in this service |
      | standard_user   | wrong_pass     | Epic sadface: Username and password do not match any user in this service |
      |                 | secret_sauce   | Epic sadface: Username is required                                        |
      | standard_user   |                | Epic sadface: Password is required                                        |
