package fr.unice.polytech.cf.storeservice.exceptions;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(String message) {
        super(message);
    }
}
