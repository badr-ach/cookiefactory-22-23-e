package fr.unice.polytech.cf.orderservice.exceptions;

public class ImpossibleOrderCancelingException extends RuntimeException {
    public ImpossibleOrderCancelingException(String message) {
        super(message);
    }
}
