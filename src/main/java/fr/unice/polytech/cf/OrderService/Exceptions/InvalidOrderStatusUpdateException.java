package fr.unice.polytech.cf.OrderService.Exceptions;

public class InvalidOrderStatusUpdateException extends Exception{

    public InvalidOrderStatusUpdateException(String message){
        super(message);
    }
}
