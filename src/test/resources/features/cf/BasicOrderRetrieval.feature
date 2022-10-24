Feature: Retrieve a prepared order

  Background:
    Given a list of prepared orders


    Scenario Outline: Store employee checks the existence of a valid prepared order
      Given an "<order>" to be retrieved
      When the order is looked up in the prepared order list
      Then the prepared order exists

      Examples:
      |order|
      |2    |

    Scenario Outline: Store employee checks the existence of an invalid prepared order
      Given an "<order>" to be retrieved
      When the order is looked up in the prepared order list
      Then the order is not found
      Then an error is thrown

      Examples:
      |order|
      |5    |

    Scenario Outline: Store employee marks an order as retrieved (FULFILLED)
      Given a valid "<order>"
      When the store employee marks the order as fulfilled
      Then the status of the order is changed to fulfilled
      Then the order is removed from the prepared orders list
      Then the orders list no longer contains it

      Examples:
      |order|
      |6    |