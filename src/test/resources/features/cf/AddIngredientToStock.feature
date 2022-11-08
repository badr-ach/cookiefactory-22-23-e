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
