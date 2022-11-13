package fr.unice.polytech.cf.orderservice.exceptions;

public class InvalidRetrievalDateException extends RuntimeException{

    public InvalidRetrievalDateException(String message){
        super(message);
    }
}
