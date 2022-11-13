#Feature: Store Schedule
#
#    Background:
#      Scenario Outline: Orders can be programmed for pickup during store opening hours
#        Given a store opened from "<OpeningHour>" to "<ClosingHour>"
#        And an order associated to the store
#        And a timeslot from "<PreparationStart>" to "<PreparationEnd>"
#        When the order is set for pick up
#        Then the order is correctly added
#      Examples:
#        | OpeningHour     | ClosingHour | PreparationStart | PreparationEnd |
#        | 08:00           | 22:00       | 09:00            | 09:30          |
#        | 08:00           | 22:00       | 08:00            | 22:00          |
#
#      Scenario Outline: Orders cannot be programmed for pickup outside of the store opening hours
#        Given a store opened from "<OpeningHour>" to "<ClosingHour>"
#        And an order associated to the store
#        And a timeslot from "<PreparationStart>" to "<PreparationEnd>"
#        When the order is set for pick up
#        Then an exception is thrown
#      Examples:
#        | OpeningHour     | ClosingHour | PreparationStart | PreparationEnd |
#        | 08:00           | 22:00       | 05:00            | 05:30          |
#        | 08:00           | 22:00       | 07:45            | 08:15          |
#        | 08:00           | 22:00       | 07:45            | 08:30          |
#        | 08:00           | 22:00       | 21:45            | 22:15          |
#
#      Scenario Outline: If no cook is available the order is not added
#        Given a store opened from "<OpeningHour>" to "<ClosingHour>" with a Cook available from "<CookStartAvailableHour>" to "<CookEndAvailableHour>"
#        And an order associated to the store
#        And a timeslot from "<PreparationStart>" to "<PreparationEnd>"
#        When the order is set for pick up
#        Then an exception is thrown
#        Examples:
#          | OpeningHour     | ClosingHour | CookStartAvailableHour | CookEndAvailableHour | PreparationStart | PreparationEnd |
#          | 08:00           | 22:00       | 08:00                  | 09:00                | 09:15            | 09:30          |
#
#      Scenario Outline: The store schedule can be changed
#        Given a store opened from "<OpeningHour>" to "<ClosingHour>"
#        When the store schedule is set to open at "<OpeningHour2>" and close at "<ClosingHour2>" on day "<ChangedDay>"
#        Then the store opening hour is "<OpeningHour2>" and its closing hour is "<ClosingHour2>" on day "<ChangedDay>"
#        Then the store opening hour is "<OpeningHour>" and its closing hour is "<ClosingHour>" on day "<UnchangedDay>"
#        Examples:
#          | OpeningHour     | ClosingHour | OpeningHour2     | ClosingHour2 | ChangedDay | UnchangedDay |
#          | 08:00           | 22:00       | 05:00            | 05:30        | MONDAY     | THURSDAY     |
