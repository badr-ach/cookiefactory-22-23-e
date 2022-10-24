package fr.unice.polytech.cf.StoreService.Entities;

import java.util.ArrayList;

import fr.unice.polytech.cf.OrderService.Entities.Order;

public class Cook {
  private int id;
  private Schedule schedule;
  private static int ID = 0;
  private ArrayList<Order> orders = new ArrayList<Order>();

  public Cook() {
    this.id = ID;
    ID++;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }

  public void assignOrder(Order order){
    orders.add(order);
  }

  public boolean hasOrder(Order order){
    return orders.stream().anyMatch(assignedOrder -> assignedOrder.getId() == order.getId());
  }
}
