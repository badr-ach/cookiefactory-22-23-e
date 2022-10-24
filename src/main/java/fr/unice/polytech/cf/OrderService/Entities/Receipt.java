package fr.unice.polytech.cf.OrderService.Entities;
import java.util.ArrayList;

public class Receipt {
  private int id;
  private double price;
  private int orderId;
  private ArrayList<OrderItem> orderItems;
  private String customerName;
  private String customerAddress;
  private double taxes;
  private static int ID = 0;

  public Receipt(Order order){
    this.customerName = order.getContact().getName();
    this.customerAddress = order.getContact().getAdress();
    this.orderItems = order.getOrderItems();
    this.price = order.getPrice();
    this.orderId = order.getId();
    this.id = ID;
    ID++;
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

  public double getPrice() {
    return price;
  }

  public void setPrice(double amount) {
    this.price = amount;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getCustomerAddress() {
    return customerAddress;
  }

  public void setCustomerAddress(String customerAddress) {
    this.customerAddress = customerAddress;
  }

  public double getTaxes() {
    return taxes;
  }

  public void setTaxes(double taxes) {
    this.taxes = taxes;
  }
}
