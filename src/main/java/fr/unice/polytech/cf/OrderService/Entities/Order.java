package fr.unice.polytech.cf.OrderService.Entities;

import fr.unice.polytech.cf.AccountService.Entities.ContactCoordinates;
import fr.unice.polytech.cf.CookieService.Entities.Cookie;
import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;

import java.util.ArrayList;

public class Order {
  private int id;
  private EOrderStatus status;
  private ContactCoordinates contact;
  private ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
  private static int ID = 0;

  public Order() {
    status = EOrderStatus.PENDING;
    this.id = ID;
    ID++;
  }

  public Order(ArrayList<OrderItem> orderItems, EOrderStatus status) {
    this.orderItems = orderItems;
    this.status = status;
    this.id = ID;
    ID++;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void addItem(Cookie cookie) {
    OrderItem foundItem =
        orderItems.stream()
            .filter(orderItem -> orderItem.getCookie() == cookie)
            .findFirst()
            .orElse(null);

    if (foundItem != null) {
      foundItem.increase();
    } else {
      OrderItem orderItem = new OrderItem(cookie);
      orderItems.add(orderItem);
    }
  }

  public void removeItem(Cookie cookie) {
    OrderItem foundItem =
        orderItems.stream()
            .filter(orderItem -> orderItem.getCookie() == cookie)
            .findFirst()
            .orElse(null);
    if (foundItem == null) return;
    foundItem.decrease();
    if(foundItem.getQuantity() == 0) orderItems.remove(foundItem);
  }

  public EOrderStatus getStatus() {
    return status;
  }

  public void setStatus(EOrderStatus status) {
    this.status = status;
  }

  public ArrayList<OrderItem> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(ArrayList<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  public double getPrice() {
    return orderItems.stream()
        .reduce(0.0, (subtotal, orderItem) -> subtotal + orderItem.getPrice(), Double::sum);
  }

  public ContactCoordinates getContact() {
    return contact;
  }

  public void setContact(ContactCoordinates contact) {
    this.contact = contact;
  }
}
