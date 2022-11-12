Feature: Customer Account 
  Background:
    Given a customer system

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
      | JohnDoe   | John Doe      | securepass1  | securepass2 | 1                         |


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
