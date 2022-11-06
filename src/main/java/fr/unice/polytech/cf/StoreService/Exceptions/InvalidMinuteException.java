package fr.unice.polytech.cf.StoreService.Exceptions;

public class InvalidMinuteException extends Throwable {
    public InvalidMinuteException(String invalid_minute_input) {
        super(invalid_minute_input);
    }
}
