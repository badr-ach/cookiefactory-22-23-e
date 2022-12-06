package fr.unice.polytech.cf.exceptions;

public class TransactionFailureException extends RuntimeException{
    public TransactionFailureException(String message){
        super(message);
    }
}
