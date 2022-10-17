package fr.unice.polytech.cf;

import java.util.ArrayList;

import fr.unice.polytech.cf.AccountService.Entities.ContactCoordinates;
import fr.unice.polytech.cf.CookieService.Entities.Cookie;
import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;
import fr.unice.polytech.cf.OrderService.Entities.Order;

public class CustomerSystem extends CookieOnDemandSystem {

  public void createOrder(ContactCoordinates contact, Cookie cookie){
    Order order = orderService.startOrder();
    order.setContact(contact);
    order.addItem(cookie);
  }

  public void payOrder(ContactCoordinates contact, String cardNumber){
    ArrayList<Order> orders = orderService.getOrders(contact, EOrderStatus.PENDING);
    if (orders.size() > 1) throw new Error("More than one order");
    if (orders.size() == 0) throw new Error("Order not found");
    Order order = orders.stream().findFirst().orElseThrow();
    orderService.makePayment(cardNumber, order);
  }
}
