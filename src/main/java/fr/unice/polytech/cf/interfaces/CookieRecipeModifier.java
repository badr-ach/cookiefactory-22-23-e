package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Account;
import fr.unice.polytech.cf.entities.Cookie;
import fr.unice.polytech.cf.exceptions.UnauthorizedRoleException;


public interface CookieRecipeModifier {
    void addCookie(Cookie cookie);

    void deleteCookie(Cookie cookie, Account account) throws UnauthorizedRoleException;

    void activateCookie(Cookie cookie, Account account);
}
