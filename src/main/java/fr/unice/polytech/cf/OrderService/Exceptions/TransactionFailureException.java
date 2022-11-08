package fr.unice.polytech.cf.OrderService.Exceptions;

public class TransactionFailureException extends RuntimeException{
    public TransactionFailureException(String message){
        super(message);
    }
}
