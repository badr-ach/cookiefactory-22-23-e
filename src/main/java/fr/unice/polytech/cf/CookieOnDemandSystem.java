package fr.unice.polytech.cf;

import java.util.ArrayList;

import fr.unice.polytech.cf.CookieService.Entities.Cookie;
import fr.unice.polytech.cf.AccountService.AccountService;
import fr.unice.polytech.cf.OrderService.OrderService;
import fr.unice.polytech.cf.StoreService.StoreService;
import fr.unice.polytech.cf.StoreService.Entities.Store;
import fr.unice.polytech.cf.CookieService.CookieService;

public abstract class CookieOnDemandSystem {
  
  protected OrderService orderService;
  protected AccountService accountService;
  protected CookieService cookieService;
  protected StoreService storeService;

  public CookieOnDemandSystem(){
    orderService = new OrderService();
    accountService = new AccountService();
    cookieService = new CookieService();
    storeService = new StoreService();
  }

  public ArrayList<Cookie> getCookies(){
    return cookieService.getCookies();
  }

  public ArrayList<Store> getStores(){
    return storeService.getStores();
  }


}
