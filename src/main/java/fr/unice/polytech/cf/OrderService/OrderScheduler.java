package fr.unice.polytech.cf.OrderService;

import java.util.ArrayList;

import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.StoreService.Entities.Cook;
import fr.unice.polytech.cf.StoreService.Entities.Store;

public class OrderScheduler {
  
  public void selectTimeSlot(Order order) {
    // TODO: this method should assign a cook that is available once scheduling is implemented

    Store store = order.getStore();
    ArrayList<Cook> cooks = store.getCooks();
    Cook cook = cooks.get(0);
    cook.assignOrder(order);
  }

  public void getAvailableTimeSlots(Order order){

  }
}
