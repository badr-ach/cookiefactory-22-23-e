Feature: Validate the order
  Background:
    Given a customer "Max" with email "max@gmail.com" and phone number "0711223344" and address "Imaginary address here"
    And our customer system
    And a scheduler

  Scenario: : Checkout without a specified store
    Given an order with missing store information
    When the customer proceeds to pay his order with "INVALIDCREDITCARD"
    Then order validation fails with an error message 'Store not specified'

  Scenario: : Checkout without a specified retrieval time
    Given an order with missing retrieval date and time
    When the customer proceeds to pay his order with "INVALIDCREDITCARD"
    Then order validation fails with an error message 'Retrieval time not specified'

  Scenario: : Checkout with an unavailable retrieval time
    Given a proper order
    And and the store it was assigned having gotten an order for the same retrieval time
    When the customer proceeds to pay his order with "INVALIDCREDITCARD"
    Then order validation fails with an error message 'The assigned retrieval time is not available'
    Then no cook is assigned to the order
    And the order status is pending

  Scenario: : Checkout payment fails with a proper order
    Given a proper order
    When the customer proceeds to pay his order with "INVALIDCREDITCARD"
    Then the assigned retrieval time is still available
    Then the order status is pending
    And no cook is assigned to the order

  Scenario: : Checkout passes with a proper order
    Given a proper order
    When the customer proceeds to pay his order with "123456789"
    Then the assigned retrieval time is not available
    Then the available cook is assigned the order
    Then the order has a cook
    Then the order status is PAID
    Then correct receipt is generated

  Scenario Outline: Checkout with insufficient stock
    Given The "<ingredients>" and their "<initial_state>" in the stock of the store
    And a "<recipe>" needing "<ingredients>" with "<ingredients_quantity>"
    And an order to that same store with "<amount>" cookies of that recipe
    When the customer proceeds to pay his order with "123456789"
    Then order validation fails with an error message 'Ingredient is not available in the stock'
    Then the final state of the stock of the store is "<initial_state>" for "<ingredients>"

    Examples:
    |ingredients|initial_state|recipe|ingredients_quantity|amount|
    |RegularDough,ChocolateFlavor|10,2|Chocolala  |5,2    |2     |

  Scenario Outline: Checkout passes with sufficient stock
    Given The "<ingredients>" and their "<initial_state>" in the stock of the store
    And a "<recipe>" needing "<ingredients>" with "<ingredients_quantity>"
    And an order to that same store with "<amount>" cookies of that recipe
    When the customer proceeds to pay his order with "123456789"
    Then the final state of the stock of the store is "<final_state>" for "<ingredients>"

    Examples:
      |ingredients|initial_state|recipe|ingredients_quantity|amount|final_state|
      |RegularDough,ChocolateFlavor|11,5|Chocolala  |5,2    |2     |1,1        |


