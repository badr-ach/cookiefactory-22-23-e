Feature: Add a basic cookie to an order

 Background:
   Given a payment service
   And a customer with name "John"
   And a "Chocolalala" Cookie priced 10.0
   And an Order with 1 Cookie

  Scenario: The order is payed
    When the customer pays the order with credit card number "123456789"
    Then the customer receives the receipt
    Then the order status is payed
