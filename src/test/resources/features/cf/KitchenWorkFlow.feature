Feature: Order Preparation
  Background:

    Given  a paid order

    Scenario: Assigned cook starts preparing the order
      Given the assigned cook to the order
      When the cook starts preparing the order
      Then the reserved stock for the order is removed
      And the order status is "IN_PREPARATION"

    Scenario: Assigned cook finishes preparing the order
      Given the assigned cook to the order
      When the cook marks an order as prepared
      Then he no longer has it
      And the order status is "PREPARED"

