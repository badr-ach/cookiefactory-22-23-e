package fr.unice.polytech.cf.exceptions;

public class CookieNotSubmittedException extends RuntimeException{
    public CookieNotSubmittedException(String message ){
        super(message);
    }
}
