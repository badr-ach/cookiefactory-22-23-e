package fr.unice.polytech.cf.accountservice.entities;

import java.util.Optional;
import java.util.Stack;

import fr.unice.polytech.cf.accountservice.enums.EAccountType;
import fr.unice.polytech.cf.orderservice.entities.Order;

public class CustomerAccount extends Account {
  Stack<Order> orderHistory = new Stack<Order>();

  public CustomerAccount(String username, String password, ContactCoordinates contactCoordinates) {
    super(username, password, EAccountType.CUSTOMER, contactCoordinates);
  }

  public Optional<Order> getLastOrder() {
    if (orderHistory.isEmpty()) Optional.of(null);
    return Optional.of(orderHistory.peek());
  }

  @Override
  public Stack<Order> getHistory() {
    return orderHistory;
  }

  @Override
  public void addOrder(Order order) {
    orderHistory.add(order);
  }
}
