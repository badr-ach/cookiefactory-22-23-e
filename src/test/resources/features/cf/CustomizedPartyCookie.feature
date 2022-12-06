Feature: Customizable Party Cookies
  Background:
    Given a store with a cook that can make Wedding cookies with Music theme
    And a Basic recipe "Chocolala" with 15 minutes cooking duration
    And that needs A Dough "Complete" of 400 g that costs 1 euros per unit 100 g
    And Flavor of "Chocolate" of 200 g that costs 2 euros per unit 100 g
    And one Topping "Chocolate raisins" 100 g that costs 1 euros per unit 100 g
    And a margin of 0


  Scenario Outline: Refusing to make a party cookie that no able chef can make
    Given an order of a party cookie of "<size>" based on Chocolala recipe for "<occasion>" with "<theme>"
    And "<ingredients_added>" added
    And store stock has "<ingredients_already_in_stock>"
    When the customer proceeds to pay his party cookie order order with "123456789"
    And the order status is PENDING
    And an error is then thrown with the message "No able cook available for this order"

    Examples:
    | size | occasion | theme   | ingredients_added                                            | ingredients_already_in_stock                                                                                     |
    |L|WEDDING|CARTOON|TOPPING,Chocolate raisins,1,100;TOPPING,Sprinkles,1,50|TOPPING,Chocolate raisins,1,1000;TOPPING,Sprinkles,1,5000;DOUGH,Complete,1,10000;FLAVOR,Chocolate,2,5000|

  Scenario Outline: Ordering a party cookie successfully
    Given an order of a party cookie of "<size>" based on Chocolala recipe for "<occasion>" with "<theme>"
    And "<ingredients_added>" added
    And store stock has "<ingredients_already_in_stock>"
    When the customer proceeds to pay his party cookie order order with "123456789"
    Then the order status becomes PAID
    And the order price is "<price>"

    Examples:
      | size    | occasion | theme | ingredients_added                                         | ingredients_already_in_stock                                                          | price |
      |L|WEDDING|MUSIC|TOPPING,Chocolate raisins,1,100;TOPPING,Sprinkles,1,100|TOPPING,Chocolate raisins,1,1000;TOPPING,Sprinkles,1,5000;DOUGH,Complete,1,10000;FLAVOR,Chocolate,2,5000 | 42.5 |
