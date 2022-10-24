Feature: Add a basic cookie to an order

 Background:
  Given a newly created order
  And a customer named "Bob"
  And a cookie named "Chocolalala" priced 10.0

  Scenario: A new cookie is added to the order
    When the customer adds the cookie
    Then a new order item is added to the order
    Then the order item quantity is 1

  Scenario: An existing cookie is added to the order
    Given the order already contains the cookie to be added
    When the customer adds the same cookie
    Then a new order item is not added to the order
    Then the order item quantity is 2
