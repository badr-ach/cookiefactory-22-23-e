Feature: Order Retrieval Workflow

  Background:

    Scenario Outline: It's been two hours and the customer hasn't retrieved his order yet
      Given an order with a "<retrieval date>"
      When the order status is updated with "<current date>"
      Then the order status is set as OBSOLETE
      Examples:
        |    retrieval date    |     current date     |
        | 2022-11-13 20:48     | 2022-11-13 22:49     |
        | 2022-11-13 20:00     | 2022-11-13 22:48     |

    Scenario Outline: It's been less than two hours and the customer hasn't retrieved his order yet
      Given an order with a "<retrieval date>"
      When the order status is updated with "<current date>"
      Then the order status is still PREPARED
      Examples:
        |    retrieval date    |     current date     |
        | 2022-11-13 20:48     | 2022-11-13 22:47     |
        | 2022-11-13 20:00     | 2022-11-13 21:48     |
