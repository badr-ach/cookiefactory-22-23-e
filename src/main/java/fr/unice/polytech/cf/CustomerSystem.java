package fr.unice.polytech.cf;

import java.util.ArrayList;

import fr.unice.polytech.cf.AccountService.Entities.ContactCoordinates;
import fr.unice.polytech.cf.CookieService.Entities.Cookie;
import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;
import fr.unice.polytech.cf.StoreService.Entities.Store;
import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Entities.Receipt;

public class CustomerSystem extends CookieOnDemandSystem {

  public Order createOrder(ContactCoordinates contact, Cookie cookie){
    Order order = orderService.startOrder();
    order.setContact(contact);
    order.addItem(cookie);
    return order;
  }

  public Order createOrder(ContactCoordinates contact, ArrayList<Cookie> cookies){
    Order order = orderService.startOrder();
    order.setContact(contact);
    cookies.forEach(cookie -> order.addItem(cookie));
    return order;
  }

  public Receipt payOrder(ContactCoordinates contact, String cardNumber){
    Order order = getActiveOrder(contact);
    Receipt receipt = orderService.makePayment(cardNumber, order);
    return receipt;
  }

  public Order selectStore(ContactCoordinates contact, Store store){
    Order order = getActiveOrder(contact);
    order.setStore(store);
    return order;
  }

  private Order getActiveOrder(ContactCoordinates contact){
    ArrayList<Order> orders = orderService.getOrders(contact, EOrderStatus.PENDING);
    if (orders.size() > 1) throw new Error("More than one order");
    if (orders.size() == 0) throw new Error("Order not found");
    Order order = orders.stream().findFirst().orElseThrow();
    return order;
  }
}
