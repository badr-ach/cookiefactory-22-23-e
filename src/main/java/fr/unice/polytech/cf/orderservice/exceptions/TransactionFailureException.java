package fr.unice.polytech.cf.orderservice.exceptions;

public class TransactionFailureException extends RuntimeException{
    public TransactionFailureException(String message){
        super(message);
    }
}
