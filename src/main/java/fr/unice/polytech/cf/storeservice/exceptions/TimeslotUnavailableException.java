package fr.unice.polytech.cf.storeservice.exceptions;

public class TimeslotUnavailableException extends RuntimeException {
    public TimeslotUnavailableException(String message) {
        super(message);
    }
}
