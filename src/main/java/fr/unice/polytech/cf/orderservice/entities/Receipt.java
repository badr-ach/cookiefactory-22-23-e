package fr.unice.polytech.cf.orderservice.entities;

import fr.unice.polytech.cf.accountservice.entities.ContactCoordinates;

import java.util.ArrayList;

public class Receipt {
    private int id;
    private double total;
    private int orderId;
    private ArrayList<OrderItem> orderItems;
    private ContactCoordinates customerContacts;
    private double taxes;
    private static int ID = 0;

    public Receipt(Order order, double total) {
        this.customerContacts = order.getContact();
        this.orderItems = order.getOrderItems();
        this.total = total;
        this.orderId = order.getId();
        this.id = ID;
        ID++;
    }

    public Receipt(ContactCoordinates customerContacts, double total, Order order){
        this(order,total);
        this.customerContacts = customerContacts;
    }



    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setId(int id) {
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
