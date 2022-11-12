package fr.unice.polytech.cf.CookieService;

import fr.unice.polytech.cf.AccountService.Entities.Account;
import fr.unice.polytech.cf.AccountService.Enums.EAccountType;
import fr.unice.polytech.cf.CookieService.Entities.Cookie;
import fr.unice.polytech.cf.CookieService.Entities.Ingredient;
import fr.unice.polytech.cf.CookieService.Enums.ECookieStatus;
import fr.unice.polytech.cf.CookieService.Enums.EIngredientType;

import java.util.ArrayList;

public class CookieService {
  private ArrayList<Cookie> cookies = new ArrayList<Cookie>();

  public CookieService() {
    ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
    Ingredient testIngredient = new Ingredient("Chocolate", 10.0, EIngredientType.FLAVOR);
    ingredients.add(testIngredient);
    Cookie cookie = new Cookie("Chocolataa", 12.0, ingredients);
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
