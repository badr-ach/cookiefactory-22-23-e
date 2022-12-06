package fr.unice.polytech.cf.components;

import java.util.List;

import fr.unice.polytech.cf.entities.Cookie;
import fr.unice.polytech.cf.components.AccountService;
import fr.unice.polytech.cf.components.OrderService;
import fr.unice.polytech.cf.connectors.PaymentService;
import fr.unice.polytech.cf.components.StoreService;
import fr.unice.polytech.cf.entities.Store;
import fr.unice.polytech.cf.components.CookieService;

public abstract class CookieOnDemandSystem {
  
  public OrderService orderService;
  public AccountService accountService;
  public CookieService cookieService;
  public StoreService storeService;

  protected PaymentService paymentService;

  public CookieOnDemandSystem(){
    accountService = new AccountService();
    cookieService = new CookieService();
    storeService = new StoreService();
    paymentService = new PaymentService();
    orderService = new OrderService(paymentService,storeService);
  }

  public List<Cookie> getCookies(){
    return cookieService.getCookies();
  }

  public List<Store> getStores(){
    return storeService.getStores();
  }
}
