package fr.unice.polytech.cf;

import java.util.List;

import fr.unice.polytech.cf.cookieservice.entities.Cookie;
import fr.unice.polytech.cf.accountservice.AccountService;
import fr.unice.polytech.cf.orderservice.OrderService;
import fr.unice.polytech.cf.orderservice.PaymentService;
import fr.unice.polytech.cf.storeservice.StoreService;
import fr.unice.polytech.cf.storeservice.entities.Store;
import fr.unice.polytech.cf.cookieservice.CookieService;

public abstract class CookieOnDemandSystem {
  
  protected OrderService orderService;
  protected AccountService accountService;
  protected CookieService cookieService;
  protected StoreService storeService;

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
