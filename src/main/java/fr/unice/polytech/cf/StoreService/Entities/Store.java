package fr.unice.polytech.cf.StoreService.Entities;

import fr.unice.polytech.cf.OrderService.Entities.Order;
import java.util.ArrayList;

public class Store {

  private ArrayList<Cook> cooks = new ArrayList<Cook>();
  private String name;
  private String address;
  private double taxes;
  private int id;
  private static int ID = 0;

  public Store() {
    for (int i = 0; i < 10; i++) {
      Cook cook = new Cook();
      cooks.add(cook);
    }
    this.id = ID;
    ID++;
  }

  public int getId() {
    return id;
  }

  public ArrayList<Cook> getCooks() {
    return cooks;
  }

  public void setCooks(ArrayList<Cook> cooks) {
    this.cooks = cooks;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public double getTaxes() {
    return taxes;
  }

  public void setTaxes(double taxes) {
    this.taxes = taxes;
  }

  public Cook getAssignedCook(Order order) {
    return cooks.stream().filter(cook -> cook.hasOrder(order)).findFirst().orElse(null);
  }
}
