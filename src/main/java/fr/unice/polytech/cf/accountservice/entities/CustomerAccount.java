package fr.unice.polytech.cf.accountservice.entities;

import fr.unice.polytech.cf.accountservice.enums.EAccountType;
import fr.unice.polytech.cf.orderservice.entities.Order;
import java.util.Optional;
import java.util.Stack;

public class CustomerAccount extends Account {
  Stack<Order> orderHistory = new Stack<Order>();
  int cookiesOrderedNumberSinceDiscount = 0;
  boolean hasLoyaltyProgram = false;

  public CustomerAccount(String username, String password, ContactCoordinates contactCoordinates) {
    super(username, password, EAccountType.CUSTOMER, contactCoordinates);
  }

  public CustomerAccount(
      String username,
      String password,
      ContactCoordinates contactCoordinates,
      boolean subscribeToLoyalty) {
    super(username, password, EAccountType.CUSTOMER, contactCoordinates);
    this.hasLoyaltyProgram = subscribeToLoyalty;
  }

  public Optional<Order> getLastOrder() {
    if (orderHistory.isEmpty()) Optional.of(null);
    return Optional.of(orderHistory.peek());
  }

  public Stack<Order> getHistory() {
    return orderHistory;
  }

  public void addOrder(Order order) {
    orderHistory.add(order);
    if (!hasLoyaltyProgram) return;
    if (cookiesOrderedNumberSinceDiscount > 30) cookiesOrderedNumberSinceDiscount = 0;
    cookiesOrderedNumberSinceDiscount += order.getTotalItems();
  }

  public void applyDiscount(Order order) {
    if (!hasDiscount()) return;
    double currentPrice = order.getPrice();
    double discountedPrice = currentPrice * 9 / 10;
    order.setDiscountedPrice(discountedPrice);
  }

  public boolean hasDiscount() {
    if (!hasLoyaltyProgram || cookiesOrderedNumberSinceDiscount < 30) return false;
    return true;
  }
  public void setHasLoyaltyProgram(boolean hasLoyaltyProgram){
    this.hasLoyaltyProgram = hasLoyaltyProgram;
  }
}
