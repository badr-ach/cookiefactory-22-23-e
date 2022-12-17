Feature: Customer Account 
  Background:

  Scenario Outline: A new account is created if no account with the same username exists
    When A new account is created with username: "<Username>", password: "<Password>" and customer name: "<CustomerName>"
    Then the account with username: "<Username>" is present <NumberOfExistingAccounts> times

    Examples:
      | Username  | CustomerName  | Password     | NumberOfExistingAccounts  |
      | JohnDoe   | John Doe      | securepass1  | 1                         |

  Scenario Outline: A new account is not created if an account with the same username already exists
    Given an existing account with username: "<Username>", password: "<Password1>" and customer name: "<CustomerName>"
    When A new account is created with username: "<Username>", password: "<Password2>" and customer name: "<CustomerName>"
    Then the account with username: "<Username>" is present <NumberOfExistingAccounts> times

    Examples:
      | Username  | CustomerName  | Password1    | Password2   | NumberOfExistingAccounts  |
      | JohnDoe   | John Doe      | securepass1  | securepass1 | 1                         |


  Scenario Outline: Customer logs in with an existing account
    Given an existing account with username: "<Username1>", password: "<Password1>" and customer name: "<CustomerName1>"
    And no logged in account
    When a customer logs in with username: "<Username2>", password: "<Password2>"
    Then the customer is logged in with "<Username1>" "<Password1>"

    Examples:
      | Username1 | CustomerName1 | Password1    | Username2   | Password2   |
      | JohnDoe   | John Doe      | securepass1  | JohnDoe     | securepass1 |

  Scenario Outline: Customer logs in with an account that does not exist
    Given an existing account with username: "<Username1>", password: "<Password1>" and customer name: "<CustomerName1>"
    And no logged in account
    When a customer logs in with username: "<Username2>", password: "<Password2>"
    Then an runtime exception is thrown with message "Username or password is incorrect!"
    And the customer is not logged in

    Examples:
      | Username1 | CustomerName1 | Password1    | Username2   | Password2   |
      | JaneDoe   | Jane Doe      | securepass2  | JohnDoe     | securepass2 |
      | JohnDoe   | John Doe      | securepass1  | JohnDoe     | securepass2 |

  Scenario Outline: A logged in customer pays an order without entering his contact coordinates
    Given an existing account with username: "<Username>", password: "<Password>" and customer name: "<CustomerName>"
    And a logged in customer with username: "<Username>", password: "<Password>"
    And an order awaiting payment
    When a logged in customer pays his order with "<CreditCardNumber>"
    Then the order succeeds

    Examples:
      | Username | CustomerName | Password    | CreditCardNumber |
      | JohnDoe  | John Doe     | securepass2 | 123456789        |


  Scenario Outline: The orders made while logged in are in the accounts order history if the order is successful
    Given an existing account with username: "<Username>", password: "<Password>" and customer name: "<CustomerName>"
    And a logged in customer with username: "<Username>", password: "<Password>"
    And an order awaiting payment
    When a logged in customer pays his order with "<CreditCardNumber>"
    Then the order is in the account order history

    Examples:
      | Username | CustomerName | Password    | CreditCardNumber |
      | JohnDoe  | John Doe     | securepass2 | 123456789        |


  Scenario Outline: The orders made while logged are not added in the order history if the order fails
    Given an existing account with username: "<Username>", password: "<Password>" and customer name: "<CustomerName>"
    And a logged in customer with username: "<Username>", password: "<Password>"
    And an order awaiting payment
    When a logged in customer pays his order with "<CreditCardNumber>"
    Then the order is not in the account order history

    Examples:
      | Username | CustomerName | Password    | CreditCardNumber |
      | JohnDoe  | John Doe     | securepass2 | INVALIDCARD      |


  Scenario Outline: Customer subscribed to loyalty program discount makes a purchase
    Given an existing account with username: "<Username>", password: "<Password>" and customer name: "<CustomerName>" subscribed to the loyalty program
    And a logged in customer with username: "<Username>", password: "<Password>"
    And the customer made a previous order of <PreviousOrderCookieAmount1> cookies for the "<RetrievalDate1>"
    And the customer made a previous order of <PreviousOrderCookieAmount2> cookies for the "<RetrievalDate2>"
    And an order of <CookieNumber> cookies priced <Price>
    When a logged in customer pays his order with "<CreditCardNumber>"
    Then The order price is <TotalPrice>

    Examples:
      | Username | CustomerName | Password    | CreditCardNumber | PreviousOrderCookieAmount1 | PreviousOrderCookieAmount2 | RetrievalDate1   | RetrievalDate2   | CookieNumber | Price | TotalPrice |
      | JohnDoe  | John Doe     | securepass2 | 123456789        | 29                         | 1                          | 2022-11-12 20:48 | 2022-11-13 20:48 | 3            | 10.0  | 27.0       |
