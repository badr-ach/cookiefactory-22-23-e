package fr.unice.polytech.cf;

import java.util.ArrayList;

import fr.unice.polytech.cf.CookieService.Entities.Cookie;
import fr.unice.polytech.cf.AccountService.AccountService;
import fr.unice.polytech.cf.OrderService.OrderService;
import fr.unice.polytech.cf.CookieService.CookieService;

public abstract class CookieOnDemandSystem {
  
  protected OrderService orderService;
  protected AccountService accountService;
  protected CookieService cookieService;

  public CookieOnDemandSystem(){
    orderService = new OrderService();
    accountService = new AccountService();
    cookieService = new CookieService();
  }

  public ArrayList<Cookie> getCookies(){
    return cookieService.getCookies();
  }
}
