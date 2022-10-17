package fr.unice.polytech.cf.OrderService.Entities;

public class Receipt {
  private int id;
  private double price;
  private String customerName;
  private String customerAddress;
  private double taxes;
  private static int ID = 0;

  public Receipt(String customerName, String customerAddress, double price, double taxes){
    this.customerName = customerName;
    this.customerAddress = customerAddress;
    this.id = ID;
    ID++;
  }

  public int getId() {
    return id;
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
