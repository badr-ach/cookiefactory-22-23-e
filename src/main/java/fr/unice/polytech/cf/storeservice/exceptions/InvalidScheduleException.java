package fr.unice.polytech.cf.storeservice.exceptions;

public class InvalidScheduleException extends RuntimeException{
    public InvalidScheduleException(String message) {
        super(message);
    }
}
