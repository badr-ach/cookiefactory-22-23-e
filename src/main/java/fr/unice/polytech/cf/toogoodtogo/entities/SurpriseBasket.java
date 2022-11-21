package fr.unice.polytech.cf.toogoodtogo.entities;

import fr.unice.polytech.cf.storeservice.entities.Store;

public class SurpriseBasket {

  private double price;
  private Store store;
  private int quantity;

  public SurpriseBasket(Store store, double price, int quantity) {
    this.store = store;
    this.price = price;
    this.quantity = quantity;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Store getStore() {
    return store;
  }

  public void setStore(Store store) {
    this.store = store;
  }

  public String getDescription() {
    return "Surprise basket with " + quantity + " items for " + price;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
