Feature: Store Schedule

  Scenario Outline: Changing the store's weekly schedule
    Given a store with "<opening_hours>" on "<day>"
    When the store schedule is set to "<new_opening_hours>" on "<day>"
    Then the store opening hours are "<new_opening_hours>" on "<day>"
    Examples:
      | opening_hours                        | new_opening_hours                    | day            |
      | 08:00-22:00, 08:00-12:00;16:00-22:00 | 12:00-22:00, 09:00-13:00;15:00-16:00 | MONDAY,TUESDAY |

  Scenario Outline: Refusing invalid changes to the store's weekly schedule
    Given a store with "<opening_hours>" on "<day>"
    When the store schedule is set to "<new_opening_hours>" on "<day>"
    Then an error message is then thrown with message "Attempt to assign incoherent schedule"
    Then the store opening hours are "<opening_hours>" on "<day>"
    Examples:
      | opening_hours                        | new_opening_hours                    | day            |
      | 08:00-22:00, 08:00-12:00;16:00-22:00 | 12:00-08:00, 08:00-13:00;12:00-14:00 | MONDAY,TUESDAY |