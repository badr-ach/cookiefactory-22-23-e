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

    Scenario Outline: Order status can set to FULFILLED with a previous status different than PAID
      Given an order with order id <OrderId> and status "<OrderStatus>"
      When the order with order id <OrderId> status is marked as FULFILLED
      Then an error message is thrown with message "Invalid Operation of Order Status Update"
      And the order with order id <OrderId> status is "<ExpectedNewOrderStatus>"
      Examples:
        | OrderId | OrderStatus | ExpectedNewOrderStatus |
        | 1       | PAID        | PAID                   |
        | 1       | PENDING     | PENDING                |

    Scenario Outline: Order status can set to FULFILLED with status PREPARED beforehand
        Given an order with order id <OrderId> and status "<OrderStatus>"
        When the order with order id <OrderId> status is marked as FULFILLED
        Then the order with order id <OrderId> status is "<ExpectedNewOrderStatus>"
        Examples:
          | OrderId | OrderStatus | ExpectedNewOrderStatus |
          | 1       | PREPARED    | FULFILLED              |


