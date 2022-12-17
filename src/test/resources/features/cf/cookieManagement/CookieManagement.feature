Feature: Management of the cookies

  Background:
    Given a SUBMITTED cookie
    And an Account with the BrandManager role
    And an Account with the KitchenChef role

  Scenario: cookie should be validated by BrandManager
    When the cookie is validated with an account with the BrandManager role
    Then the cookie status is VALIDATED

  Scenario: cookie should NOT be validated by KitchenChef
    When the cookie is validated with an account with the KitchenChef role
    Then an error message is thrown with "<error_message>"
    Then the cookie status stay SUBMITTED

  Scenario: cookie should be added to the available cookies if they are NOT validated before being set to available
    When the SUBMITTED cookie is set to AVAILABLE with an account with the KitchenChef role
    Then the cookie status stay SUBMITTED

  Scenario: cookie should be added to the available cookies if they are validated before being set to available
    Given a VALIDATED cookie
    When the VALIDATED cookie is set to AVAILABLE with an account with the KitchenChef role
    Then the recipe status becomes AVAILABLE

  Scenario: A submitted cookie can't be submitted another time
    Given a SUBMITTED cookie
    When The SUBMITTED cookie is submitted again
    Then an error message is thrown with "This cookie can't be submitted to the brand manager"
    Then the cookie status stay SUBMITTED

    Scenario: For submit a cookie he MUST be  a DEFAULT cookie
      Given a DEFAULT cookie
      When The DEFAULT cookie is SUBMITTED by a kitchenChef
      Then the cookie status change to SUBMITTED
