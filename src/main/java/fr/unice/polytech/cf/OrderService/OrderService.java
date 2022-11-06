package fr.unice.polytech.cf.OrderService;

import fr.unice.polytech.cf.AccountService.Entities.ContactCoordinates;
import fr.unice.polytech.cf.CookieService.Entities.Cookie;
import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Entities.Receipt;
import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;
import fr.unice.polytech.cf.OrderService.Exceptions.InvalidOrderStatusUpdateException;
import fr.unice.polytech.cf.OrderService.Exceptions.OrderNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

public class OrderService {
    private PaymentService paymentService;
    private OrderScheduler orderScheduler;
    private ArrayList<Order> orders = new ArrayList<Order>();

    public OrderService() {
        paymentService = new PaymentService();
        orderScheduler = new OrderScheduler();
    }

    public Order startOrder() {
        Order order = new Order();
        orders.add(order);
        return order;
    }

    public Order addCookies(Order order, ArrayList<Cookie> cookies) {
        cookies.forEach(
                cookie -> {
                    order.addItem(cookie);
                });
        return order;
    }

    public Receipt makePayment(String cardNumber, Order order) {
        Receipt receipt = null;

        try {
            receipt = paymentService.makePayment(cardNumber, order);
            order.setStatus(EOrderStatus.PAYED);
            orderScheduler.selectTimeSlot(order);

        } catch (Exception e) {

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

    public Order updateStatus(Order order, EOrderStatus status) throws OrderNotFoundException, InvalidOrderStatusUpdateException {
        Optional<Order> maybeOrderToUpdate = orders.stream().filter(o-> o.equals(order)).findFirst();
        if(maybeOrderToUpdate.isEmpty()) throw new OrderNotFoundException("Order Not found");

        Order orderToUpdate = maybeOrderToUpdate.get();

        if(
          orderToUpdate.getStatus() == EOrderStatus.PENDING && status == EOrderStatus.PAYED
          || orderToUpdate.getStatus() == EOrderStatus.PAYED && status == EOrderStatus.PREPARED
          || orderToUpdate.getStatus() == EOrderStatus.PREPARED && status == EOrderStatus.FULFILLED
        ){
            orderToUpdate.setStatus(status);
            return orderToUpdate;
        }
        throw new InvalidOrderStatusUpdateException("Invalid status update");
    }
}
