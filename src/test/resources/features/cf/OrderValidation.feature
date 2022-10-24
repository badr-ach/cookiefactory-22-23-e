Feature: Validate the order

 Background:
   Given a CustomerSystem
   And a customer with name "John"
   And a "Chocolalala" Cookie priced 5.0
   And an Order with 1 Cookie

  Scenario: The order is payed
    When the customer pays the order with credit card number "123456789"
    Then the receipt is correct
    Then the order status is payed
    Then the order is assigned to a Cook

   Scenario:
    When the customer pays the order with credit card number "INVALIDCARDNUMBER"
    Then the order status is PENDING
