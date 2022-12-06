package fr.unice.polytech.cf.entities;

import fr.unice.polytech.cf.interfaces.IPastry;
import fr.unice.polytech.cf.exceptions.InvalidQuantityException;
import fr.unice.polytech.cf.entities.enums.EOrderStatus;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Order {
    private int id;
    private Cook cook;
    private Store store;
    private EOrderStatus status;
    private ContactCoordinates contact;
    private ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
    private LocalDateTime retrievalDateTime;
    private double discountedPrice; 
    private static int ID = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Order() {
        status = EOrderStatus.PENDING;
        this.id = ID;
        ID++;
    }

    public Order(ArrayList<OrderItem> orderItems, EOrderStatus status) {
        this();
        this.orderItems = orderItems;
        this.status = status;
    }

    public Map<Ingredient, Integer> getNecessaryIngredients() {
        Map<Ingredient, Integer> ingredients = new HashMap<>();
        for (OrderItem orderItem : orderItems) {
            for (Map.Entry<Ingredient, Integer> ingredient : orderItem.getIngredients().entrySet()) {
                ingredients.merge(ingredient.getKey(), ingredient.getValue(), Integer::sum);
            }
        }
        return ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrderItem addItem(IPastry cookie) {
        OrderItem foundItem = contains(cookie);
        if (foundItem != null) {
            foundItem.increase();
            return foundItem;
        } else {
            OrderItem orderItem = new OrderItem(cookie);
            orderItems.add(orderItem);
            return orderItem;
        }
    }

    public void removeItem(Cookie cookie) {
        OrderItem foundItem = contains(cookie);
        if (foundItem == null) return;
        foundItem.decrease();
        if (foundItem.getQuantity() == 0) orderItems.remove(foundItem);
    }

    public void setItemQuantity(Cookie cookie, int quantity) {
        if (quantity < 0) throw new InvalidQuantityException("Quantity can't be set to a negative value.");
        OrderItem foundItem = contains(cookie);
        if (quantity == 0) orderItems.remove(foundItem);
        if (foundItem == null) foundItem = addItem(cookie);
        foundItem.setQuantity(quantity);
    }

    public void addItemWithQuantity(IPastry cookie, int quantity) {
        if (quantity <= 0) throw new InvalidQuantityException("Quantity can't be set to a negative value.");
        OrderItem foundItem = contains(cookie);
        if (foundItem == null) {
            foundItem = addItem(cookie);
            foundItem.setQuantity(quantity);
        }else{
            foundItem.setQuantity(quantity + foundItem.getQuantity());
        }
    }

    public OrderItem contains(IPastry cookie){
        return orderItems.stream()
                .filter(orderItem -> orderItem.getCookie() == cookie)
                .findFirst()
                .orElse(null);
    }

    public double getPrice() {
        if(discountedPrice != 0) return discountedPrice;
        return orderItems.stream()
                .reduce(0.0, (subtotal, orderItem) -> subtotal + orderItem.getPrice(), Double::sum);
    }

    public int getTotalItems() {
        return orderItems.stream()
                .reduce(0, (subtotal, orderItem) -> subtotal + orderItem.getQuantity(), Integer::sum);
    }

    public double getTTCPrice() {
        double taxes = store.getTaxes();
        return getPrice() * (100+taxes) / 100;
    }

    public Duration getPreparationDuration() {
        Duration totalDuration = Duration.ZERO;
        for (int i = 0; i < orderItems.size(); i++) {
            Duration orderItemDuration = orderItems.get(i).getPreparationDuration();
            totalDuration = totalDuration.plus(orderItemDuration);
        }
        if(totalDuration.equals(Duration.ZERO))
            return totalDuration;
        return effectiveDuration(totalDuration);
    }

    private Duration effectiveDuration(Duration duration) {
        return Duration.of((int) Math.ceil(duration.toMinutes() / 15.0) + 1, ChronoUnit.MINUTES);
    }

    public EOrderStatus getStatus() {
        return status;
    }

    public void setStatus(EOrderStatus status) {
        this.status = status;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Store getStore() {
        return store;
    }

    public ContactCoordinates getContact() {
        return contact;
    }

    public void setContact(ContactCoordinates contact) {
        this.contact = contact;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public LocalDateTime getRetrievalDateTime() {
        return retrievalDateTime;
    }

    public void setRetrievalDateTime(LocalDateTime retrievalDateTime) {
        this.retrievalDateTime = retrievalDateTime;
    }

    public Cook getCook() {
        return cook;
    }

    public void setCook(Cook cook) {
        this.cook = cook;
    }

    public void setDiscountedPrice(double price){
      this.discountedPrice = price;
    }

}
