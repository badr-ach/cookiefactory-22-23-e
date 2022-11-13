package fr.unice.polytech.cf.StoreService.Entities;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Exceptions.InvalidRetrievalDateException;
import fr.unice.polytech.cf.StoreService.Exceptions.InsufficientTimeslotDurationException;
import fr.unice.polytech.cf.StoreService.Exceptions.TimeslotUnavailableException;


public class Cook {
  private int id;
  private WeeklySchedule schedule;
  private static int ID = 0;
  private ArrayList<Order> orders = new ArrayList<Order>();
  public Cook() {
    this.id = ID;
    ID++;
  }

  public Cook(WeeklySchedule weeklySchedule) {
    this.schedule = weeklySchedule;
    this.id = ID;
    ID++;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public WeeklySchedule getSchedule() {
    return schedule;
  }

  public void setSchedule(WeeklySchedule schedule) {
    this.schedule = schedule;
  }

  public boolean assignOrder(Order order){
    for(Order or : orders){
      System.out.println("We have " + or.getRetrievalDateTime() + " against " + order.getRetrievalDateTime());
      if(or.getRetrievalDateTime().equals(order.getRetrievalDateTime())) {
        throw new InvalidRetrievalDateException("Retrieval time no longer available");
      }
    }
    return orders.add(order);
  }


  public boolean removeOrder(Order order) { return orders.remove(order); }

  // checks if he has the order passed itself in his assignemnts

  public void assignOrder(DayOfWeek day, Order order, TimeSlot timeslot) throws TimeslotUnavailableException, InsufficientTimeslotDurationException{
    schedule.getDayFromSchedule(day).addOrder(order, timeslot);
  }


  public boolean hasOrder(Order order){
    if(schedule != null && schedule.hasOrder(order)) return true;
    return orders.stream().anyMatch(assignedOrder -> assignedOrder.getId() == order.getId());
  }

  // check if he has an order assigned at the same time the first one is (temporary solution till scheduler is implemented)
  public boolean hasAnOrderAssigned(Order order) {
    return orders.stream().anyMatch(assignedOrder -> assignedOrder.getRetrievalDateTime().equals(order.getRetrievalDateTime()));
  }

  public HashSet<TimeSlot> getAvailableTimeSlots(DayOfWeek day, Duration duration){
    return schedule.getDayFromSchedule(day).getAvailableTimeSlots(duration);
  }
}
