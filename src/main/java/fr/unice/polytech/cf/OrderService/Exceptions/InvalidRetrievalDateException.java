package fr.unice.polytech.cf.OrderService.Exceptions;

public class InvalidRetrievalDateException extends RuntimeException{

    public InvalidRetrievalDateException(String message){
        super(message);
    }
}
