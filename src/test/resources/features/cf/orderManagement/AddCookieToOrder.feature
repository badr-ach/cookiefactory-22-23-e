Feature: Add a cookie to an order

 Background:
  Given an empty order

  Scenario Outline: A new cookie is added to the order
    Given a cookie named "<CookieName1>" priced <CookiePrice1>
    When a cookie named "<CookieName1>" is added 1 time to the order
    Then the number of order items is 1
    Then the cookie named "<CookieName1>" quantity is 1
    Then the calculated price is equal to <CookiePrice1>
    Examples:
      | CookieName1     | CookiePrice1 |
      | Dark Temptation | 15.0         |

  Scenario Outline: An existing cookie is added to the order
    Given a cookie named "<CookieName1>" priced <CookiePrice1> is in the order
    When a cookie named "<CookieName1>" is added 1 time to the order
    Then the number of order items is 1
    Then the cookie named "<CookieName1>" quantity is 2
    Then the calculated price is equal to <Total>
    Examples:
      | CookieName1     | CookiePrice1 | Total |
      | Dark Temptation | 15.0         | 30.00 |

 Scenario Outline: Different cookies are added to the order
    Given a cookie named "<CookieName1>" priced <CookiePrice1> is in the order
    And a cookie named "<CookieName2>" priced <CookiePrice2>
    When a cookie named "<CookieName2>" is added 1 time to the order
    Then the number of order items is 2
    Then the calculated price is equal to <Total>
    Then a cookie named "<CookieName1>" is in the order
    Then a cookie named "<CookieName2>" is in the order
    Examples:
      | CookieName1     | CookiePrice1 | CookieName2     | CookiePrice2 | Total |
      | Dark Temptation | 15.0         | Chocolalala     | 10.0         | 25.0  |


  Scenario Outline: Only one cookie quantity is incremented
    Given a cookie named "<CookieName1>" priced <CookiePrice1> is in the order
    And a cookie named "<CookieName2>" priced <CookiePrice2> is in the order
    When a cookie named "<CookieName2>" is added 2 time to the order
    Then the number of order items is 2
    Then the calculated price is equal to <Total>
    Then the cookie named "<CookieName1>" quantity is 1
    Then the cookie named "<CookieName2>" quantity is 3
    Examples:
      | CookieName1     | CookiePrice1 | CookieName2     | CookiePrice2 | Total |
      | Dark Temptation | 15.0         | Chocolalala     | 10.0         | 45.0  |


  Scenario Outline: Only one cookie quantity is decremented
    Given a cookie named "<CookieName1>" priced <CookiePrice1> is in the order
    And a cookie named "<CookieName1>" is added 2 time to the order
    And a cookie named "<CookieName2>" priced <CookiePrice2> is in the order
    When a cookie named "<CookieName1>" is removed 1 time from the order
    Then the number of order items is 2
    Then the calculated price is equal to <Total>
    Then the cookie named "<CookieName1>" quantity is 2
    Then the cookie named "<CookieName2>" quantity is 1
    Examples:
      | CookieName1     | CookiePrice1 | CookieName2     | CookiePrice2 | Total |
      | Dark Temptation | 15.0         | Chocolalala     | 10.0         | 40.0  |

  Scenario Outline: Only one cookie quantity is modified
    Given a cookie named "<CookieName1>" priced <CookiePrice1> is in the order
    And a cookie named "<CookieName2>" priced <CookiePrice2> is in the order
    When a cookie named "<CookieName1>" quantity is set to <ModifiedQuantity>
    Then the number of order items is <NumberItems>
    Then the calculated price is equal to <Total>
    Then the cookie named "<CookieName1>" quantity is <ExpectedQuantity>
    Then the cookie named "<CookieName2>" quantity is 1
    Examples:
      | CookieName1     | CookiePrice1 | CookieName2     | CookiePrice2 | ModifiedQuantity | ExpectedQuantity | NumberItems | Total |
      | Dark Temptation | 15.0         | Chocolalala     | 10.0         | 3                | 3                | 2           | 55.0  |
      | Dark Temptation | 15.0         | Chocolalala     | 10.0         | 0                | 0                | 1           | 10.0  |

  Scenario Outline: When an order item quantity is 0 it is removed from the order
    Given a cookie named "<CookieName1>" priced <CookiePrice1> is in the order
    When a cookie named "<CookieName1>" is removed 1 time from the order
    Then the number of order items is 0
    Then the calculated price is equal to 0

    Examples:
      | CookieName1     | CookiePrice1 |
      | Dark Temptation | 15.0         |
