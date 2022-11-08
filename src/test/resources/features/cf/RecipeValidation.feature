Feature: Validate a recipe

  Background:
    Given a SUBMITTED Cookie
    And an Account with the BrandManager role
    And an Account with the KitchenChef role

  Scenario: cookie should be validated by BrandManager
    When the cookie is validated with an account with the BrandManager role
    Then the cookie status is VALIDATED

  Scenario: cookie should NOT be validated by KitchenChef
    When the cookie is validated with an account with the KitchenChef role
    Then the cookie status stay SUBMITTED

  Scenario: cookie should be added to the available cookies if they are NOT validated before being set to available
    When the SUBMITTED cookie is set to AVAILABLE with an account with the KitchenChef role
    Then the recipe status stay SUBMITTED

  Scenario: cookie should be added to the available cookies if they are validated before being set to available
    Given a VALIDATED cookie
    When the VALIDATED cookie is set to AVAILABLE with an account with the KitchenChef role
    Then the recipe status becomes AVAILABLE