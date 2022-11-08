Feature: Validate the order
  Background:
    Given a customer "Max" with email "max@gmail.com" and phone number "0711223344" and address "Imaginary address here"
    And our customer system
    And a scheduler

  Scenario: : The customer proceeds to pay his order with its store not being specified
    Given an order with missing store information
    When the customer proceeds to pay his order with "INVALIDCREDITCARD"
    Then order validation fails with an error message 'Store not specified'

  Scenario: : The customer proceeds to pay his order with its retrieval date And time not being specified
    Given an order with missing retrieval date and time
    When the customer proceeds to pay his order with "INVALIDCREDITCARD"
    Then order validation fails with an error message 'Retrieval time not specified'

  Scenario: : The customer proceeds to pay his order with its retrieval date And time being no longer available
    Given a proper order
    And and the store it was assigned having gotten an order for the same retrieval time
    When the customer proceeds to pay his order with "INVALIDCREDITCARD"
    Then order validation fails with an error message 'Retrieval time no longer available'
    Then no cook is assigned to the order
    And the order status is pending

  Scenario: : The customer proceeds to pay his order with its retrieval date And time being available And the payment fails
    Given a proper order
    When the customer proceeds to pay his order with "INVALIDCREDITCARD"
    Then the order status is pending
    And no cook is assigned to the order

  Scenario: : The customer proceeds to pay his order with its retrieval date And time being available And the payment passes
    Given a proper order
    When the customer proceeds to pay his order with "123456789"
    Then the assigned retrieval time is still available
    Then the available cook is assigned the order
    Then the order has a cook
    Then the order status is PAID
    Then correct receipt is generated
