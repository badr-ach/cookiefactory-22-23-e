Feature: Too Good to go
  Background:
    Given a TooGooToGoService

  Scenario Outline: Surprise baskets are made with obsolete orders 
    Given <OrderQuantity> orders with <CookieQuantity> cookies and a retrieval date "<RetrievalDate>"
    When the order status is updated with on the "<CurrentDate>"
    Then <NumberOfBasketsToGo> baskets of <NumberOfCookiesToGo> cookies are available for too good to go pickup
    Then SurpriseBaskets have an associated store
    Then SurpriseBaskets have an associated price

    Examples:
      | OrderQuantity | CookieQuantity | RetrievalDate    | CurrentDate      | NumberOfBasketsToGo | NumberOfCookiesToGo |
      | 1             | 1              | 2022-11-13 20:48 | 2022-11-13 22:49 | 1                   | 1                   |
      | 2             | 1              | 2022-11-13 20:48 | 2022-11-13 22:49 | 2                   | 1                   |
      | 1             | 2              | 2022-11-13 20:48 | 2022-11-13 22:49 | 1                   | 2                   |
      | 1             | 1              | 2022-11-13 20:48 | 2022-11-13 20:48 | 0                   | 0                   |


  Scenario Outline: Customers can be notified when surprise baskets are available
    Given <OrderQuantity> orders with <CookieQuantity> cookies priced <CookiePrice> and a retrieval date "<RetrievalDate>"
    And an account with username "johnDoe", password "securepass", name "John Doe" and email "john.doe@gmail.com"
    And the account is subscribed for ToGoodToGo notifications
    When the order status is updated with on the "<CurrentDate>"
    Then the customer is notified with "<NotificationText>"

    Examples:
      | OrderQuantity | CookieQuantity | CookiePrice | RetrievalDate    | CurrentDate      | NotificationText                     |
      | 1             | 2              | 5.0         | 2022-11-13 14:48 | 2022-11-13 17:49 | Surprise basket with 2 items for 10.0 |


   Scenario Outline: Customers can decide when they are notified (day and hours)
     Given <OrderQuantity> orders with <CookieQuantity> cookies priced <CookiePrice> and a retrieval date "<RetrievalDate>"
     And an account with username "johnDoe", password "securepass", name "John Doe" and email "john.doe@gmail.com"
     And the account is subscribed for ToGoodToGo notifications between "08:00" and "20:00"
     When the order status is updated with on the "<CurrentDate>"
     Then the number of notification the customer received is <NumberOfNotifications>

     Examples:
       | OrderQuantity | CookieQuantity | CookiePrice | RetrievalDate    | CurrentDate      | NumberOfNotifications |
       | 1             | 2              | 5.0         | 2022-11-13 14:48 | 2022-11-13 22:49 | 0                     |
       | 1             | 2              | 5.0         | 2022-11-13 14:48 | 2022-11-13 17:49 | 1                     |
