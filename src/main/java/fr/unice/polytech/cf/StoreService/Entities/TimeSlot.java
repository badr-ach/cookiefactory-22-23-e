package fr.unice.polytech.cf.StoreService.Entities;

import fr.unice.polytech.cf.OrderService.Entities.Order;
import java.time.Duration;
import java.time.LocalTime;

public class TimeSlot {

  private LocalTime startTime;
  private LocalTime endTime;
  private Order order;

  public TimeSlot(String startTime, String endTime) {
    this.startTime = LocalTime.parse(startTime);
    this.endTime = LocalTime.parse(endTime);
  }

  public TimeSlot(LocalTime startTime, LocalTime endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public TimeSlot(String startTime, Duration duration) {
    this.startTime = LocalTime.parse(startTime);
    this.endTime = LocalTime.parse(startTime).plus(duration);
  }

  public TimeSlot(LocalTime startTime, Duration duration) {
    this.startTime = startTime;
    this.endTime = startTime.plus(duration);
  }

  public boolean isBetween(String timeToTest) {
    LocalTime testedTime = LocalTime.parse(timeToTest);
    if (!startTime.isBefore(testedTime)) return false;
    if (!endTime.isAfter(testedTime)) return false;
    return true;
  }

  public Duration duration() {
    return Duration.between(startTime, endTime);
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public String toString() {
    return "TimeSlot: " + startTime + " - " + endTime;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof TimeSlot)) return false;
    if ((TimeSlot) obj == this) return true;
    if (this.getStartTime().toNanoOfDay() == ((TimeSlot) obj).getStartTime().toNanoOfDay()
        && this.getEndTime().toNanoOfDay() == ((TimeSlot) obj).getEndTime().toNanoOfDay()) return true;
    return false;
  }
}
