package fr.unice.polytech.cf.components;

import fr.unice.polytech.cf.entities.Account;
import fr.unice.polytech.cf.entities.enums.EAccountType;
import fr.unice.polytech.cf.entities.Cookie;
import fr.unice.polytech.cf.entities.Ingredient;
import fr.unice.polytech.cf.entities.enums.ECookieStatus;
import fr.unice.polytech.cf.entities.enums.EIngredientType;
import fr.unice.polytech.cf.exceptions.CookieNotSubmittedException;
import fr.unice.polytech.cf.exceptions.CookieNotValidatedException;
import fr.unice.polytech.cf.exceptions.UnhautorizedRoleException;

import java.util.ArrayList;
import java.util.HashMap;

public class CookieService {
    private ArrayList<Cookie> cookies = new ArrayList<Cookie>();
    private ArrayList<Cookie> cookieSubmitted = new ArrayList<Cookie>();

    public CookieService() {
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        Ingredient testIngredient = new Ingredient("Chocolate", 10.0, EIngredientType.FLAVOR);
        ingredients.add(testIngredient);
        Cookie cookie = new Cookie("Chocolataa", 12.0, new HashMap<>());
        cookies.add(cookie);
    }


    public ArrayList<Cookie> getCookies() {
        return cookies;
    }


    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }


    public void deleteCookie(Cookie cookie) {
        cookies.remove(cookie);
    }

    public void submitCookie(Cookie cookie, Account account) {
        if (!(cookie.getStatus().equals(ECookieStatus.DEFAULT)))
            throw new CookieNotSubmittedException("This cookie can't be submitted to the brand manager");
        if (!(account.getType().equals(EAccountType.CHEF)))
            throw new UnhautorizedRoleException("Your account can't validate a cookie");
        cookie.setStatus(ECookieStatus.SUBMITTED);
    }

    public void validateCookie(Cookie cookie, Account account) {
        if (!(cookie.getStatus().equals(ECookieStatus.SUBMITTED)))
            throw new CookieNotSubmittedException("This cookie isn't submitted to the brand manager");
        if (!(account.getType().equals(EAccountType.BRAND_MANAGER)))
            throw new UnhautorizedRoleException("Your account can't validate a cookie");
        cookie.setStatus(ECookieStatus.VALIDATED);
    }

    public void activateCookie(Cookie cookie, Account account) {
        if (!(cookie.getStatus().equals(ECookieStatus.VALIDATED)))
            throw new CookieNotValidatedException("This cookie isn't validate by the brand manager");
        if (!(account.getType().equals(EAccountType.CHEF)))
            throw new UnhautorizedRoleException("Your account can't activate a cookie");
        cookie.setStatus(ECookieStatus.ACTIVE);
    }

}
