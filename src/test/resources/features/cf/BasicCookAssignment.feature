Feature: Basic Cook assignment to an order

   Background:
    Given an order has been submitted to be cooked

  Scenario: The order is assigned to a cook
    Given the list of free cooks that are working in the store
    And the list is not empty
    Then the order is assigned to one and one only cook of the free cooks

  Scenario: A cook retrieves the orders that he was assigned
    When A cook wants to retrieve the orders that he has to prepare
    Then the orders that were assigned to this cook are returned
