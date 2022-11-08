package fr.unice.polytech.cf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import fr.unice.polytech.cf.AccountService.Entities.ContactCoordinates;
import fr.unice.polytech.cf.CookieService.Entities.Cookie;
import fr.unice.polytech.cf.FacadeExceptions.InvalidStoreException;
import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;
import fr.unice.polytech.cf.StoreService.Entities.Store;
import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Entities.Receipt;

public class CustomerSystem extends CookieOnDemandSystem {

    // deemed necessary for any form of usage / interaction with the facade regardless whether used or not
    private Order activeOrder;
    // necessary for the rest of business work within session : cookie selection and what not
    private Store activeStore;

    public Order createOrder(Cookie cookie) {
        return this.createOrder(new ArrayList<>(Collections.singletonList(cookie)));
    }

    public Order createOrder(ArrayList<Cookie> cookies) {
        this.activeOrder = orderService.startOrder();
        cookies.forEach(cookie -> activeOrder.addItem(cookie));
        return activeOrder;
    }

    public Receipt payOrder(ContactCoordinates coordinates,String cardNumber) {
        return orderService.makePayment(coordinates, cardNumber, activeOrder);
    }

    public void selectStore(Store store) {
        if (storeService.getStores().contains(store)) {
            this.activeStore = store;
            activeOrder.setStore(activeStore);
        } else {
            throw new InvalidStoreException("Invalid Store Selected : inexistent");
        }
    }

    // temporary skeleton
    public void selectPickUpDate(Date date){
        activeOrder.setRetrievalDateTime(date);
    }
//
//    private Order getActiveOrder(ContactCoordinates contact){
//      ArrayList<Order> orders = orderService.getOrders(contact, EOrderStatus.PENDING);
//      if (orders.size() > 1) throw new Error("More than one order");
//      if (orders.size() == 0) throw new Error("Order not found");
//      Order order = orders.stream().findFirst().orElseThrow();
//      return order;
//    }
}
