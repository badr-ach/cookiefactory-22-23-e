Feature: Cancel an order

  Background:

  Scenario: The customer tries to cancel a paid order yet its preparation is already in process
    Given the cook it was assigned to an order
    When the customer tries to cancel the chosen order
    Then an error is thrown with message "Can't cancel order in preparation"
    Then the cook is still preparing it
    Then the ingredients are still removed from the stock

  Scenario: The customer tries to cancel a paid order while its preparation isn't started yet
    Given the cook it was assigned to an another order
    When the customer tries to cancel an another order
    Then the cook is no longer preparing it
    Then the ingredients are back to the stock
    Then the order is marked as CANCELLED