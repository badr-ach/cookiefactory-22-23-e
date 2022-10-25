Feature: OrderPreparationValidation
  Background:
    Given an order
    Scenario:
      When the order is marked as prepared
      Then the order status is PREPARED