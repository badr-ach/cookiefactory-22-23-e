package fr.unice.polytech.cf.exceptions;

public class InvalidOrderStatusUpdateException extends RuntimeException{

    public InvalidOrderStatusUpdateException(String message){
        super(message);
    }
}
