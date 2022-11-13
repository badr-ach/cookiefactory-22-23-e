Feature: Order Retrieval Workflow

  Background:
    Given a prepared order

    Scenario Outline: It's been two hours and the customer hasn't retrieved his order yet
      Given an order with a "<retrieval date>"
      When the scheduler filters out the prepared orders at the "<current date>" that exceeded the maximum time to be withdrawn which is two hours
      Then the order status is set as OBSOLETE
      Examples:
        |    retrieval date    |     current date     |
        | 13 Nov 20:48:24 2022 | 13 Nov 22:48:24 2022 |
        | 13 Nov 20:00:24 2022 | 13 Nov 22:48:24 2022 |

    Scenario Outline: It's been less than two hours and the customer hasn't retrieved his order yet
      Given an order with a "<retrieval date>"
      When the scheduler filters out the prepared orders at the "<current date>" that have not yet exceeded the maximum time to be withdrawn which is two hours
      Then the order status is still PREPARED
      Examples:
        |    retrieval date    |     current date     |
        | 13 Nov 21:48:24 2022 | 13 Nov 22:48:24 2022 |
        | 13 Nov 21:00:24 2022 | 13 Nov 22:48:24 2022 |
