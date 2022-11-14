Feature: Store taxes
  Background:
    And the cookie on demand system

  Scenario Outline: Store taxes should be added to the order total
    Given a customer "Max" with email "max@gmail.com", phone number "0711223344" and address "Imaginary address here"
    And an order with cookie named "<CookieName>" priced <CookiePrice> and quantity <CookieQuantity>
    And the <Taxes> of the store 
    When the order is paid
    Then the order price is equal to <Total>

    Examples:
      | CookieName      | CookiePrice | CookieQuantity | Taxes | Total |
      | Dark Temptation | 15.0        | 4              | 0     | 60    |
      | Dark Temptation | 15.0        | 4              | 20    | 72    |


  Scenario Outline: Only a store manager can change the store taxes
    Given an account with the "<AccountRole>" role
    And the <StartingTaxes> of the store 
    When the taxes are set to <NewTaxes> with the account
    Then the store taxes are <FinalTaxes>

    Examples:
      | AccountRole      | StartingTaxes | NewTaxes | FinalTaxes |
      | STORE_MANAGER    | 15.0          | 20.0     | 20.0       |
      | STORE_EMPLOYEE   | 15.0          | 20.0     | 15.0       |


  Scenario Outline: Multiple stores can have different taxes
    Given an account with the "<AccountRole>" role
    And the <StartingTaxes> of the store
    And the <StartingTaxes> of another store
    When the taxes are set to <NewTaxes> with the account
    Then the store taxes are <FinalTaxes>
    Then the other store taxes are <StartingTaxes>

    Examples:
      | AccountRole      | StartingTaxes | NewTaxes | FinalTaxes |
      | STORE_MANAGER    | 15.0          | 20.0     | 20.0       |
