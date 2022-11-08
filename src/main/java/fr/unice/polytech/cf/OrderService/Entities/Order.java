package fr.unice.polytech.cf.OrderService.Entities;

import fr.unice.polytech.cf.AccountService.Entities.ContactCoordinates;
import fr.unice.polytech.cf.CookieService.Entities.Cookie;
import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;
import fr.unice.polytech.cf.StoreService.Entities.Store;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;

public class Order {
  private int id;
  private EOrderStatus status;
  private ContactCoordinates contact;
  private ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
  private Store store;
  private static int ID = 0;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Order order)) return false;
    return id == order.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

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
            .filter(orderItem -> orderItem.getCookie().getId() == cookie.getId())
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
    if (foundItem.getQuantity() == 0) orderItems.remove(foundItem);
  }

  public void setItemQuantity(Cookie cookie, int quantity) {
    OrderItem foundItem =
            orderItems.stream()
                    .filter(orderItem -> orderItem.getCookie() == cookie)
                    .findFirst()
                    .orElse(null);
    if (foundItem == null) return;
    if (quantity <= 0) {
      orderItems.remove(foundItem);
    } else {
      foundItem.setQuantity(quantity);
    }
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

  public Duration getPreparationDuration() {
    Duration totalDuration = Duration.ZERO;
    for (int i = 0; i < orderItems.size(); i++) {
      Duration orderItemDuration = orderItems.get(i).getPreparationDuration();
      totalDuration = totalDuration.plus(orderItemDuration);
    }
    return totalDuration;
  }

  public Store getStore() {
    return store;
  }

  public ContactCoordinates getContact() {
    return contact;
  }

  public void setContact(ContactCoordinates contact) {
    this.contact = contact;
  }

  public void setStore(Store store) {
    this.store = store;
  }
}
