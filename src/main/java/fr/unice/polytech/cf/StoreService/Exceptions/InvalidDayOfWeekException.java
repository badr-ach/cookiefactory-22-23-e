package fr.unice.polytech.cf.StoreService.Exceptions;

public class InvalidDayOfWeekException extends Exception {
    public InvalidDayOfWeekException(String invalid_day_number) {
        super(invalid_day_number);
    }
}
