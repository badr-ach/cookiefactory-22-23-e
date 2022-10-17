Feature: Basic order retrieval

  Background:
    Given a store employee named "John"

    Scenario: John wants to confirm the pick of an "order"
      When John marks the "order" as fulfilled
      Then the order is not removed from the pending "order" list
