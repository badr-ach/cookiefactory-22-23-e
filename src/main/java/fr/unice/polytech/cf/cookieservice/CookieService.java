package fr.unice.polytech.cf.cookieservice;

import fr.unice.polytech.cf.accountservice.entities.Account;
import fr.unice.polytech.cf.accountservice.enums.EAccountType;
import fr.unice.polytech.cf.cookieservice.entities.Cookie;
import fr.unice.polytech.cf.cookieservice.entities.Ingredient;
import fr.unice.polytech.cf.cookieservice.enums.ECookieStatus;
import fr.unice.polytech.cf.cookieservice.enums.EIngredientType;

import java.util.ArrayList;
import java.util.HashMap;

public class CookieService {
  private ArrayList<Cookie> cookies = new ArrayList<Cookie>();

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

  public void validateCookie(Cookie cookie, Account account) {
    if(!(cookie.getStatus().equals(ECookieStatus.SUBMITTED))) return; //TODO : throw exception
    if(!(account.getType().equals(EAccountType.BRAND_MANAGER))) return; //TODO : throw exception
    cookie.setStatus(ECookieStatus.VALIDATED);
  }

  public void activateCookie(Cookie cookie, Account account) {
    if(!(cookie.getStatus().equals(ECookieStatus.VALIDATED))) return; //TODO : throw exception
    if(!(account.getType().equals(EAccountType.COOK))) return; //TODO : throw exception
    cookie.setStatus(ECookieStatus.ACTIVE);
  }

}
