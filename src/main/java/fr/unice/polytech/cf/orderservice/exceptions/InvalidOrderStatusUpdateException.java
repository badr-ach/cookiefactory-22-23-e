package fr.unice.polytech.cf.orderservice.exceptions;

public class InvalidOrderStatusUpdateException extends RuntimeException{

    public InvalidOrderStatusUpdateException(String message){
        super(message);
    }
}
