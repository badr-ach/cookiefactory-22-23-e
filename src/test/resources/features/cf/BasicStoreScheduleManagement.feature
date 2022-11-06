Feature: Basic Store Schedule Management

    Background:
      Given a store

      Scenario: The store manager wants to update the store schedule
        Given a current store schedule
        When the store manager tries to update the schedule with valid data from the new one
        Then the store schedule is correctly updated

      Scenario: The store manager wants to update the store schedule
        Given a current store schedule
        When the store manager tries to update the schedule with invalid data from the new one
        Then the previous schedule is kept
        Then a exception is thrown