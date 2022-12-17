package fr.unice.polytech.cf.components;

import fr.unice.polytech.cf.entities.ContactCoordinates;
import fr.unice.polytech.cf.entities.CustomerAccount;
import fr.unice.polytech.cf.entities.Ingredient;
import fr.unice.polytech.cf.entities.commands.MarkOrderAsObsolete;
import fr.unice.polytech.cf.entities.commands.MarkOrderAsPaid;
import fr.unice.polytech.cf.interfaces.*;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.OrderItem;
import fr.unice.polytech.cf.entities.Receipt;
import fr.unice.polytech.cf.exceptions.*;
import fr.unice.polytech.cf.entities.enums.EOrderStatus;
import fr.unice.polytech.cf.connectors.TooGoodToGoProxy;
import fr.unice.polytech.cf.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The class OrderComponent, as its name refers to, manages all what is related to an order entity
 * Starting with its creation process, modification or finding and cancelling if needed then status
 * updating and notification management depending on its status
 */
@Component
public class OrderComponent implements OrderCreator, OrderModifier, OrderFinder, CustomerNotifier, SurpriseBasketModifier, OrderFinalizer {

    private final CookAssigner cookAssigner;
    private final Payment paymentProxy;
    private final OrderRepository orderRepository;
    private final OrderInvoker orderInvoker;
    private final IngredientStockReserver ingredientStockReserver;
    private final StoreOrdersModifier storeOrdersModifier;


    @Autowired
    public OrderComponent(Payment paymentProxy,IngredientStockModifier ingredientStockModifier, IngredientStockReserver ingredientReserver, OrderRepository orderRepository,
                          OrderInvoker orderInvoker,StoreOrdersModifier storeOrdersModifier) {
        this.cookAssigner = new OrderScheduler();
        this.paymentProxy = paymentProxy;
        this.ingredientStockReserver = ingredientReserver;
        this.storeOrdersModifier = storeOrdersModifier;
        this.orderRepository = orderRepository;
        this.orderInvoker = orderInvoker;
    }

    /**
     * Initializing an order and save it on the repository
     * @return the created order
     */
    @Override
    public Order startOrder() {
        Order order = new Order();
        orderRepository.save(order, order.getId());
        return order;
    }

    public Order startOrder(String id){
        Order order = new Order();
        orderRepository.save(order, UUID.fromString(id));
        return order;
    }

    /**
     * Add a cookie to a specific order among a list of orders
     * @param order to order which the cookie will be added to
     * @param cookies the other order which belongs the concerned order to
     * @return the updated order
     */
    @Override
    public Order addCookies(Order order, Map<IPastry, Integer> cookies) {
        for (Map.Entry<IPastry, Integer> cookie : cookies.entrySet()) {
            order.addItemWithQuantity(cookie.getKey(), cookie.getValue());
        }
        return order;
    }

    /**
     * Full in the client coordinates to its order
     * @param contact client coordinates
     * @param order the order with contact information
     */
    @Override
    public void attachContactCoordinates(ContactCoordinates contact, Order order) {
        if (contact.getEmail().isEmpty() && contact.getPhoneNumber().isEmpty())
            throw new InvalidContactCoordinatesException("Insufficient contact coordinates");
        order.setContact(contact);
    }

    /**
     * The checkout process to pay the order
     * @param customerAccount the client personal account
     * @param cardNumber the credit card number to debit
     * @param order the payed order
     * @return a receipt confirming that order is successfully paid
     */
    @Override
    public Receipt makePayment(CustomerAccount customerAccount, String cardNumber, Order order) {
        customerAccount.applyDiscount(order);
        ContactCoordinates contactCoordinates = customerAccount.getContactCoordinates();
        Receipt receipt = makePayment(contactCoordinates, cardNumber, order);
        customerAccount.addOrder(order);
        return receipt;
    }

    /**
     * The checkout process to pay the order
     * NB: Almost same as the previous one. Difference only in the process of client
     * getting contact information
     * @param contact the client personal contact
     * @param cardNumber the credit card number to debit
     * @param order the paid order
     * @return a receipt confirming that order is successfully paid
     */
    @Override
    public Receipt makePayment(ContactCoordinates contact, String cardNumber, Order order) {
        Receipt receipt ;
        try {
            attachContactCoordinates(contact, order);
            cookAssigner.assignOrderToACook(order);
            ingredientStockReserver.reserveStock(order.getStore(), order.getNecessaryIngredients());
            receipt = paymentProxy.makePayment(cardNumber, order);

            orderInvoker.setCommand(new MarkOrderAsPaid());
            orderInvoker.invoke(order);

        } catch (TransactionFailureException ex) {
            ingredientStockReserver.cancelReservation(order.getStore(), order.getNecessaryIngredients());
            storeOrdersModifier.remove(order, order.getStore());
            order.setCook(null);
            throw ex;
        }
        if(receipt!=null) {
            return receipt;
        }else{
            return new Receipt();
        }
    }

    /**
     * this method allow getting all the contained orders in the repository
     * @return the orders list
     */
    @Override
    public List<Order> getOrders() {
        return (List<Order>) orderRepository.findAll();
    }

    /**
     * this method allow getting all the contained orders in the repository
     * filtered by status
     * @return the orders list
     */
    public List<Order> getOrders(EOrderStatus status) {
        List<Order> orders = new ArrayList<>();
        orderRepository.findAll().forEach(order -> {
            if (order.getStatus() == status)
                orders.add(order);
        });
        return orders;
    }

    /**
     * Updating the orders status based on the duration from which it's been marked as READY to be retrieved
     * @param currentDate the actual local date & time
     */
    @Override
    public void updateOrdersStatus(LocalDateTime currentDate) {
        Iterable<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            if (order.getStatus() != EOrderStatus.PREPARED) continue;
            LocalDateTime orderDate = order.getRetrievalDateTime();
            System.out.println("current date " + currentDate.toString() + " vs " + orderDate.toString());
            Duration duration = Duration.between(orderDate, currentDate);
            Duration limit = Duration.ofHours(2);
            notifyCustomer(duration, order);
            if (duration.compareTo(limit) > 0) {
                orderInvoker.setCommand(new MarkOrderAsObsolete());
                orderInvoker.invoke(order);
            }
        }
    }

    /**
     * This method is responsible for notifying the client based on the rules described in the context
     * In this case we notify him that his order is been already more than 2 hours and 5 minutes
     * @param duration the duration which the order is marked as ready to be picked up
     * @param order the concerned order
     */
    @Override
    public void notifyCustomer(Duration duration, Order order) {
        Duration firstNotification = Duration.ofMinutes(5);
        Duration firstNotificationLimit = Duration.ofMinutes(7);
        Duration secondNotification = Duration.ofHours(1);
        Duration secondNotificationLimit = Duration.ofMinutes(65);
        if (duration.compareTo(firstNotification) > 0 && duration.compareTo(firstNotificationLimit) < 0) {
            notifyCustomer(order);
        }
        if (duration.compareTo(secondNotification) > 0 && duration.compareTo(secondNotificationLimit) < 0) {
            notifyCustomer(order);
        }
    }

    /**
     * This method is responsible for notifying the client based on the rules described in the context
     * In this case we just notify him that his order is ready to be retrieved
     * @param order the concerned order
     */
    @Override
    public void notifyCustomer(Order order) {
        ContactCoordinates contact = order.getContact();
        contact.addNotifications("Your order is ready");
        contact.notifyCustomer();
    }

    /**
     * Creating the TooGoodToGo chart
     * @param currentDate actual date
     * @param toGoodToGo service
     */
    @Override
    public void updateSurpriseBaskets(LocalDateTime currentDate, TooGoodToGoProxy toGoodToGo) {
        updateOrdersStatus(currentDate);

        List<Order> obsoleteOrders = new ArrayList<>();
        orderRepository.findAll().forEach(order -> {
            if (order.getStatus() == EOrderStatus.OBSOLETE) {
                obsoleteOrders.add(order);
            }
        });
        for(Order o: obsoleteOrders) {
            orderRepository.deleteById(o.getId());
        }

        toGoodToGo.makeSurpriseBasket(obsoleteOrders, currentDate);
    }

    /**
     * The client decides to cancel an order. The verification of the order status is mandatory in this case
     * i.e if it's already in the making process the cancelling cannot be done
     * @param order to cancel
     */
    @Override
    public void cancelAnOrder(Order order) {
        if (order.getStatus().equals(EOrderStatus.IN_PREPARATION)) {
            throw new ImpossibleOrderCancelingException("Can't cancel order in preparation.");
        } else {
            try {
                orderRepository.deleteById(order.getId());
                for (OrderItem orderItem : order.getOrderItems()) {
                    for (Map.Entry<Ingredient, Integer> cookie : orderItem.getIngredients().entrySet()) {
                        order.getStore().getIngredientsStock().liberate(cookie.getKey(), cookie.getValue());
                    }
                }
                order.setCook(null);
                order.setStatus(EOrderStatus.CANCELLED);
            } catch (OrderNotFoundException e) {
                e.getMessage();
            }
        }
    }


    public CookAssigner getCookAssigner() {
        return cookAssigner;
    }

}
