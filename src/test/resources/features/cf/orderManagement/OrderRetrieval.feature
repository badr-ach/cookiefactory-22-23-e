Feature: Retrieve a prepared order

  Background:

    Scenario Outline: Orders can be filtered by status
      Given an order with order id "<OrderId>" and status "<OrderStatus>"
      When the orders are filtered by status "<SearchOrderStatus>"
      Then <NumberOrderFound> orders are found with order id: "<OrderId>"

      Examples:
      | OrderId                                    | OrderStatus | SearchOrderStatus | NumberOrderFound |
      | 123e4567-e89b-12d3-a456-426614174000       | PREPARED    | PREPARED          | 1                |
      | 123e4567-e89b-12d3-a456-426614174001       | FULFILLED   | FULFILLED         | 1                |
      | 123e4567-e89b-12d3-a456-426614174002       | FULFILLED   | PREPARED          | 0                |
      | 123e4567-e89b-12d3-a456-426614174003       | PREPARED    | FULFILLED         | 0                |

    Scenario Outline: Orders can be retrieved by id
      Given an order with order id "<OrderId>" and status "<OrderStatus>"
      When the order id: "<SearchOrderId>" is searched
      Then <NumberOrderFound> orders are found

      Examples:
        | OrderId | OrderStatus | SearchOrderId | NumberOrderFound |
        | 123e4567-e89b-12d3-a456-426614174000       | PREPARED    | 123e4567-e89b-12d3-a456-426614174000             | 1                |
        | 123e4567-e89b-12d3-a456-426614174001       | PREPARED    | 123e4567-e89b-12d3-a456-426614173999             | 0                |

    Scenario Outline: Order status can set to FULFILLED with a previous status different than PAID
      Given an order with order id "<OrderId>" and status "<OrderStatus>"
      When the order with order id "<OrderId>" status is marked as FULFILLED
      Then an error message is thrown with message "Invalid Operation of Order Status Update"
      And the order with order id "<OrderId>" status is "<ExpectedNewOrderStatus>"
      Examples:
        | OrderId | OrderStatus | ExpectedNewOrderStatus |
        | 123e4567-e89b-12d3-a456-426614174000       | PAID        | PAID                   |
        | 123e4567-e89b-12d3-a456-426614174000       | PENDING     | PENDING                |

    Scenario Outline: Order status can set to FULFILLED with status PREPARED beforehand
        Given an order with order id "<OrderId>" and status "<OrderStatus>"
        When the order with order id "<OrderId>" status is marked as FULFILLED
        Then the order with order id "<OrderId>" status is "<ExpectedNewOrderStatus>"
        Examples:
          | OrderId | OrderStatus | ExpectedNewOrderStatus |
          | 123e4567-e89b-12d3-a456-426614174000       | PREPARED    | FULFILLED              |


   Scenario Outline: Customers receive a notification 5 minutes and 2 hours after the retrieval time if they did not pick up their order
     Given contact coordinates name "John Doe" and email "john.doe@gmail.com"
     And An order scheduled for pick up at "<PickupTime>" with status "<OrderStatus>"
     When the order status is updated on the "<CurrentTime>"
     Then the customer received <NumberOfNotifications> notifications

     Examples:
       | OrderStatus | PickupTime       | CurrentTime      | NumberOfNotifications |
       | PREPARED    | 2022-11-13 14:55 | 2022-11-13 15:01 | 1                     |
       | PREPARED    | 2022-11-13 14:55 | 2022-11-13 15:20 | 0                     |
       | PREPARED    | 2022-11-13 14:55 | 2022-11-13 15:56 | 1                     |
       | FULFILLED   | 2022-11-13 14:55 | 2022-11-13 15:01 | 0                     |
       | PREPARED    | 2022-11-13 14:55 | 2022-11-13 14:54 | 0                     |
       | PREPARED    | 2022-11-13 14:55 | 2022-11-13 14:59 | 0                     |
       | PREPARED    | 2022-11-13 14:48 | 2022-11-13 22:49 | 0                     |

