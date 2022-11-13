package fr.unice.polytech.cf.storeservice.exceptions;

public class InsufficientTimeslotDurationException extends RuntimeException{
    public InsufficientTimeslotDurationException(String message) {
        super(message);
    }
  
}
