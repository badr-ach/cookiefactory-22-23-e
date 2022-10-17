package fr.unice.polytech.cf.OrderService;

import fr.unice.polytech.cf.AccountService.Entities.ContactCoordinates;
import fr.unice.polytech.cf.CookieService.Entities.Cookie;
import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Entities.Receipt;
import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;

import java.util.ArrayList;

public class OrderService {
  private PaymentService paymentService;
  private ArrayList<Order> orders;

  public OrderService() {
    paymentService = new PaymentService();
  }

  public Order startOrder() {
    Order order = new Order();
    orders.add(order);
    return order;
  }

  public Order addCookies(Order order, ArrayList<Cookie> cookies) {
    cookies.forEach(
        cookie -> {
          order.addItem(cookie);
        });
    return order;
  }

  public Receipt makePayment(String cardNumber, Order order) {
    return paymentService.makePayment(cardNumber, order);
  }

  public ArrayList<Order> getOrders(EOrderStatus status) {
    return new ArrayList<Order>(
        orders.stream().filter(order -> (order.getStatus() == status)).toList());
  }

  public ArrayList<Order> getOrders(ContactCoordinates contact) {
    return new ArrayList<Order>(
        orders.stream().filter(order -> (order.getContact() == contact)).toList());
  }

  public ArrayList<Order> getOrders(ContactCoordinates contact, EOrderStatus status) {
    return new ArrayList<Order>(
        orders.stream().filter(order -> (order.getContact() == contact && order.getStatus() == status)).toList());
  }
}
