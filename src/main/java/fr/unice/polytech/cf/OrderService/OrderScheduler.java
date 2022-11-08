package fr.unice.polytech.cf.OrderService;

import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Exceptions.InvalidRetrievalDateException;
import fr.unice.polytech.cf.StoreService.Entities.Cook;
import fr.unice.polytech.cf.StoreService.Entities.Store;
import fr.unice.polytech.cf.StoreService.Entities.TimeSlot;
import fr.unice.polytech.cf.StoreService.Exceptions.InsufficientTimeslotDurationException;
import fr.unice.polytech.cf.StoreService.Exceptions.TimeslotUnavailableException;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;


// classes probably should be renamed a scheduler and should have two services (store service and order service)
public class OrderScheduler {


//    public void selectTimeSlot(Order order) {
//        // TODO: this method should assign a cook that is available once scheduling is implemented
//
//        Store store = order.getStore();
//        ArrayList<Cook> cooks = store.getCooks();
//        Cook cook = cooks.get(0);
//        cook.assignOrder(order);
//    }

    public void getAvailableTimeSlots(Order order) {

    }

    // assigns a cook
    public boolean assignCook(Order order) {
        // should throw an error when not available
        // based on hastimeslotavailable
        return order.getStore().getCooks().get(0).assignOrder(order);
    }

    // frees the cook and the timeslot assigned to the order
    public boolean freeTimeSlots(Order order) {
        return order.getStore().getAssignedCook(order).get().removeOrder(order);
    }

    // checks if an order already had his timeslot assigned to it else it throws an error
    public boolean hasTimeSlotAvailable(Order order) {
        return order.getStore().getCooks().get(0).hasOrder(order);
    }

    // check if an order has his timeslot occupied already
    public boolean hasTimeSlotTaken(Order order) {
        return true;
    }

  public void selectTimeSlot(Order order){
    Store store = order.getStore();
    ArrayList<Cook> cooks = store.getCooks();
    Cook cook = cooks.get(0);
    cook.assignOrder(order);
  }

  public void selectTimeSlot(TimeSlot timeslot, Order order, DayOfWeek day) throws TimeslotUnavailableException, InsufficientTimeslotDurationException{

    Store store = order.getStore();
    ArrayList<Cook> cooks = store.getCooks();
    InsufficientTimeslotDurationException insufficientTimeslotDurationException = null;
    TimeslotUnavailableException timeslotAllocatedException = null;
    for (int i = 0; i < cooks.size(); i++) {
      try {
        cooks.get(i).assignOrder(day, order, timeslot);
        return;
      } catch (InsufficientTimeslotDurationException e) {
        insufficientTimeslotDurationException = e;
      } catch (TimeslotUnavailableException e) {
        timeslotAllocatedException = e;
      }
    }
    if(timeslotAllocatedException != null){
      throw timeslotAllocatedException;
    }
    if(insufficientTimeslotDurationException != null){
      throw insufficientTimeslotDurationException;
    }
  }

  public HashSet<TimeSlot> getAvailableTimeSlots(DayOfWeek day, Order order) {
    Store store = order.getStore();
    ArrayList<Cook> cooks = store.getCooks();

    HashSet<TimeSlot> availableTimeslots = new HashSet<TimeSlot>();

    for (int i = 0; i < cooks.size(); i++) {
      HashSet<TimeSlot> currentCookAvailableTimeslots =
          cooks.get(i).getAvailableTimeSlots(day, order.getPreparationDuration());
      availableTimeslots.addAll(currentCookAvailableTimeslots);
    }
    return availableTimeslots;
  }

}
