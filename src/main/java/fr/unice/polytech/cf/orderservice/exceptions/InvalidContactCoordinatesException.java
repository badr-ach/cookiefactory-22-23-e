package fr.unice.polytech.cf.orderservice.exceptions;

public class InvalidContactCoordinatesException extends RuntimeException{
    public InvalidContactCoordinatesException(String message) {
        super(message);
    }
}
