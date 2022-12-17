package fr.unice.polytech.cf.entities;

import java.util.ArrayList;
import java.util.UUID;

public class Receipt {
    private UUID id;
    private double total;
    private UUID orderId;
    private ArrayList<OrderItem> orderItems;
    private ContactCoordinates customerContacts;
    private double taxes;
    private static int ID = 0;

    public Receipt(Order order) {
        this.customerContacts = order.getContact();
        this.orderItems = order.getOrderItems();
        this.total = order.getTTCPrice();
        this.orderId = order.getId();
        this.taxes = order.getStore().getTaxes();
        this.id = UUID.randomUUID();

    }
    public Receipt() {
        this.id = UUID.randomUUID();

    }

    public Receipt(ContactCoordinates customerContacts, Order order){
        this(order);
        this.customerContacts = customerContacts;
    }



    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double amount) {
        this.total = amount;
    }

    public double getTaxes() {
        return taxes;
    }

    public void setTaxes(double taxes) {
        this.taxes = taxes;
    }

    public ContactCoordinates getCustomerContacts() {
        return customerContacts;
    }

    public void setCustomerContacts(ContactCoordinates customerContacts) {
        this.customerContacts = customerContacts;
    }
}
