Feature: Cook schedule

  Scenario Outline: Refusing to assign the cook an incoherent schedule
    Given a cook with "<working_hours>" on "<day>"
    When the cook's schedule is set to "<new_working_hours>" on "<day>"
    Then an error is thrown with message "Attempt to assign incoherent schedule"
    Then the cook's working hours are "<working_hours>" on "<day>"
    Examples:
      | working_hours                        | new_working_hours                    | day            |
      | 08:00-20:00, 08:00-12:00;14:00-22:00 | 12:00-08:00, 08:00-13:00;12:00-14:00 | MONDAY,TUESDAY |

  Scenario Outline: Refusing to assign the cook a schedule that lies outside of his store's schedule
    Given a store with "<opening_hours>" on "<day>"
    And a Cook that works in that store with "<working_hours>" on "<day>"
    When the cook's schedule is set to "<new_working_hours>" on "<day>"
    Then an error is thrown with message "Incompatible schedule"
    Then the cook's working hours are "<working_hours>" on "<day>"
    Examples:
      | opening_hours                        | working_hours                        | new_working_hours                    | day            |
      | 08:00-22:00, 08:00-12:00;16:00-22:00 | 08:00-20:00, 08:00-12:00;16:00-22:00 | 07:00-11:00, 08:00-13:00;14:00-15:00 | MONDAY,TUESDAY |

  Scenario Outline: Assigning the cook a schedule
    Given a store with "<opening_hours>" on "<day>"
    And a Cook that works in that store with "<working_hours>" on "<day>"
    When the cook's schedule is set to "<new_working_hours>" on "<day>"
    Then the cook's working hours are "<working_hours>" on "<day>"
    Examples:
      | opening_hours                        | working_hours                        | new_working_hours                    | day            |
      | 08:00-22:00, 08:00-12:00;16:00-22:00 | 08:00-20:00, 08:00-12:00;16:00-22:00 | 08:00-17:00, 09:00-12:00;16:00-22:00 | MONDAY,TUESDAY |