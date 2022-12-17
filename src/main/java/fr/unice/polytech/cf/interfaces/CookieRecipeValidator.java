package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Account;
import fr.unice.polytech.cf.entities.Cookie;
import org.springframework.stereotype.Repository;


public interface CookieRecipeValidator {
    void validateCookie(Cookie cookie, Account account);
}
