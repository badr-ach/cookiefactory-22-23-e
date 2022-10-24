package fr.unice.polytech.cf.OrderService.Exceptions;

public class OrderNotFoundException extends Exception{

    public OrderNotFoundException(String message){
        super(message);
    }
}
