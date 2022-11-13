package fr.unice.polytech.cf.storeservice.exceptions;

public class InvalidQuantityException extends RuntimeException{
    public InvalidQuantityException(String message) {
        super(message);
    }
}
