package fr.unice.polytech.cf.exceptions;

public class TimeslotUnavailableException extends RuntimeException {
    public TimeslotUnavailableException(String message) {
        super(message);
    }
}
