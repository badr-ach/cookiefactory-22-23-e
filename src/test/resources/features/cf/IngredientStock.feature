Feature: Add ingredient to the stock
  Background:
  Given a store

  Scenario: an new ingredient is added to the stock
    Given the Ingredient Stock does not contains the ingredient to be added
    When the ingredient is added to the stock
    Then a new ingredient is added to the stock
    Then the stock ingredient quantity is 1

  Scenario: an existing ingredient is added to the stock
    Given the Ingredient Stock already contains the ingredient to be added
    When the ingredient is added to the stock
    Then the stock ingredient quantity is 2

  Scenario: ingredients stock should be store specific
    Given an other Store
    When the ingredient is added to the stock
    Then the ingredient should not be in the stock of anotherStore

  Scenario: an ingredient is removed from the stock with quantity 30
    Given the ingredient Stock already contains the ingredient to be removed with a quantity of 40
    When 30 units of ingredient are removed from the stock
    Then the quantity of the ingredient in stock is 10