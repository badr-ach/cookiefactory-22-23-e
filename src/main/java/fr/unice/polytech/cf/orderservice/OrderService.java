package fr.unice.polytech.cf.orderservice;

import fr.unice.polytech.cf.accountservice.entities.ContactCoordinates;
import fr.unice.polytech.cf.cookieservice.entities.Cookie;
import fr.unice.polytech.cf.orderservice.entities.Order;
import fr.unice.polytech.cf.orderservice.entities.Receipt;
import fr.unice.polytech.cf.orderservice.enums.EOrderStatus;
import fr.unice.polytech.cf.orderservice.exceptions.InvalidContactCoordinatesException;
import fr.unice.polytech.cf.orderservice.exceptions.InvalidOrderStatusUpdateException;
import fr.unice.polytech.cf.orderservice.exceptions.TransactionFailureException;
import fr.unice.polytech.cf.storeservice.entities.Scheduler;
import fr.unice.polytech.cf.storeservice.StoreService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class OrderService {
    private Scheduler scheduler;
    private PaymentService paymentService;
    private StoreService storeService;

    private ArrayList<Order> orders = new ArrayList<Order>();

    public OrderService(PaymentService paymentService, StoreService storeService) {
        this.scheduler = new Scheduler();
        this.paymentService = paymentService;
        this.storeService = storeService;
    }

    public Order startOrder() {
        Order order = new Order();
        orders.add(order);
        return order;
    }

    public Order addCookies(Order order, Map<Cookie,Integer> cookies) {
        for(Map.Entry<Cookie,Integer> cookie : cookies.entrySet()){
            order.addItemWithQuantity(cookie.getKey(),cookie.getValue());
        }
        return order;
    }

    public void attachContactCoordinates(ContactCoordinates contact, Order order){
        if(contact.getEmail().isEmpty()  && contact.getPhoneNumber().isEmpty())
            throw new InvalidContactCoordinatesException("Insufficient contact coordinates");
        order.setContact(contact);
    }

    public Receipt makePayment(ContactCoordinates contact, String cardNumber, Order order, double price) {
        Receipt receipt = null;
        try {
            attachContactCoordinates(contact,order);
            scheduler.assignOrderToACook(order);
            storeService.reserveStock(order.getStore(),order.getNecessaryIngredients());
            receipt = paymentService.makePayment(cardNumber,order, price);
            markOrderAsPaid(order);
        } catch ( TransactionFailureException ex) {
            storeService.cancelReservation(order.getStore(),order.getNecessaryIngredients());
            storeService.remove(order,order.getStore());
            order.setCook(null);
            System.out.println("msg"+ ex.getMessage());
            throw ex;
        }
        return receipt;
    }

    public ArrayList<Order> getOrders(EOrderStatus status) {
        return new ArrayList<Order>(
                orders.stream().filter(order -> (order.getStatus() == status)).toList());
    }

    public ArrayList<Order> getOrders(ContactCoordinates contact) {
        return new ArrayList<Order>(
                orders.stream().filter(order -> (order.getContact() == contact)).toList());
    }

    public ArrayList<Order> getOrders(ContactCoordinates contact, EOrderStatus status) {
        return new ArrayList<Order>(
                orders.stream()
                        .filter(order -> (order.getContact() == contact && order.getStatus() == status))
                        .toList());
    }

    public Optional<Order> getOrder(int id) {
        return orders.stream().filter(order -> (order.getId() == id)).findFirst();
    }

    public void updateOrdersStatus(LocalDateTime currentDate){
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if(order.getStatus() != EOrderStatus.PREPARED) continue;
            LocalDateTime orderDate = order.getRetrievalDateTime();
            Duration duration = Duration.between(orderDate, currentDate);
            Duration limit = Duration.ofHours(2);
            if(duration.compareTo(limit) > 0){
                markOrderAsObsolete(order);
            }

        }
    }

    public void markOrderAsPaid(Order order){
        if(order.getStatus().equals(EOrderStatus.PENDING))
            order.setStatus(EOrderStatus.PAID);
        else
            throw new InvalidOrderStatusUpdateException("Invalid Operation of Order Status Update");
    }

    public void markOrderAsPrepared(Order order){
        if(order.getStatus().equals(EOrderStatus.IN_PREPARATION))
            order.setStatus(EOrderStatus.PREPARED);
        else
            throw new InvalidOrderStatusUpdateException("Invalid Operation of Order Status Update");
    }

    public void markOrderAsInPreparation(Order order){
        if(order.getStatus().equals(EOrderStatus.PAID))
            order.setStatus(EOrderStatus.IN_PREPARATION);
        else
            throw new InvalidOrderStatusUpdateException("Invalid Operation of Order Status Update");
    }

    public void markOrderAsFulfilled(Order order){
        if(order.getStatus().equals(EOrderStatus.PREPARED))
            order.setStatus(EOrderStatus.FULFILLED);
        else
            throw new InvalidOrderStatusUpdateException("Invalid Operation of Order Status Update");
    }

    public void markOrderAsObsolete(Order order){
        if(order.getStatus().equals(EOrderStatus.PREPARED))
            order.setStatus(EOrderStatus.OBSOLETE);
        else
            throw new InvalidOrderStatusUpdateException("Invalid Operation of Order Status Update");
    }

}