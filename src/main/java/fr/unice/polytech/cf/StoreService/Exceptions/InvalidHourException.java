package fr.unice.polytech.cf.StoreService.Exceptions;

public class InvalidHourException extends Throwable {
    public InvalidHourException(String invalid_hour_input) {
        super(invalid_hour_input);
    }
}
