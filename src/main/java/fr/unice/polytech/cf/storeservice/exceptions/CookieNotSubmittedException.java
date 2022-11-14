package fr.unice.polytech.cf.storeservice.exceptions;

public class CookieNotSubmittedException extends RuntimeException{
    public CookieNotSubmittedException(String message ){
        super(message);
    }
}
