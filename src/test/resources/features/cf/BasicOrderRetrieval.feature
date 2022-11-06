Feature: Retrieve a prepared order

  Background:
    Given an order service

    Scenario Outline: Orders can be filtered by status
      Given an order with order id <OrderId> and status "<OrderStatus>"
      When the orders are filtered by status "<SearchOrderStatus>"
      Then <NumberOrderFound> orders are found with order id: <OrderId>

      Examples:
      | OrderId | OrderStatus | SearchOrderStatus | NumberOrderFound |
      | 1       | PREPARED    | PREPARED          | 1                |
      | 2       | FULFILLED   | FULFILLED         | 1                |
      | 3       | FULFILLED   | PREPARED          | 0                |
      | 4       | PREPARED    | FULFILLED         | 0                |

    Scenario Outline: Orders can be retrieved by id
      Given an order with order id <OrderId> and status "<OrderStatus>"
      When the order id: <SearchOrderId> is searched
      Then <NumberOrderFound> orders are found

      Examples:
        | OrderId | OrderStatus | SearchOrderId | NumberOrderFound |
        | 1       | PREPARED    | 1             | 1                |
        | 2       | PREPARED    | 0             | 0                |

    Scenario Outline: Order status can be set to FULFILLED if and only if it was PREPARED beforehand
      Given an order with order id <OrderId> and status "<OrderStatus>"
      When the order with order id <OrderId> status is set to "<NewSetOrderStatus>"
      Then the order with order id <OrderId> status is "<ExpectedNewOrderStatus>"
      Examples:
        | OrderId | OrderStatus | NewSetOrderStatus | ExpectedNewOrderStatus |
        | 1       | PREPARED    | FULFILLED         | FULFILLED              |
        | 2       | PENDING     | FULFILLED         | PENDING                |
