package fr.unice.polytech.cf.accountservice.entities;

import fr.unice.polytech.cf.accountservice.enums.EAccountType;
import fr.unice.polytech.cf.orderservice.entities.Order;
import fr.unice.polytech.cf.storeservice.entities.Schedule;
import fr.unice.polytech.cf.storeservice.entities.TimeSlot;
import fr.unice.polytech.cf.toogoodtogo.TooGoodToGo;
import fr.unice.polytech.cf.toogoodtogo.entities.SurpriseBasket;
import fr.unice.polytech.cf.toogoodtogo.interfaces.ISurpriseBasketObserver;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class CustomerAccount extends Account implements ISurpriseBasketObserver {
  Stack<Order> orderHistory = new Stack<Order>();
  int cookiesOrderedNumberSinceDiscount = 0;
  boolean hasLoyaltyProgram = false;
  Schedule notifyHours;

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

  public void setHasLoyaltyProgram(boolean hasLoyaltyProgram) {
    this.hasLoyaltyProgram = hasLoyaltyProgram;
  }

  public void startTooGoodToGoNotifications(TooGoodToGo tooGoodToGo, Schedule schedule) {
    this.notifyHours = schedule;
    tooGoodToGo.addObserver(this);
  }

  public void stopTooGoodToGoNotifications(TooGoodToGo tooGoodToGo) {
    tooGoodToGo.removeObserver(this);
  }

  public void update(List<SurpriseBasket> surpriseBaskets, LocalDateTime dateTime) {
    if(notifyHours == null) return;
    DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
    LocalTime time = dateTime.toLocalTime();
    List<TimeSlot> scheduledHours = notifyHours.getScheduledHours(dayOfWeek);
    for (TimeSlot scheduledHour : scheduledHours) {
      if (scheduledHour.contains(time)) {
        for (SurpriseBasket surpriseBasket : surpriseBaskets) {
          contactCoordinates.addNotifications(surpriseBasket.getDescription());
        }
        contactCoordinates.notifyCustomer();
        break;
      }
    }
  }
}
