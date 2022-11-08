package fr.unice.polytech.cf.OrderService.Entities;

import java.time.Duration;

import fr.unice.polytech.cf.CookieService.Entities.Cookie;

public class OrderItem {
  private int quantity;
  private Cookie cookie;

  public OrderItem(Cookie cookie){
    this.cookie = cookie;
    this.quantity = 1;
  }

  public void increase() {
    quantity++;
  }

  public void decrease() {
    if (quantity >= 0) {
      quantity--;
    }
  }

  public int getQuantity() {
    return quantity;
  }

  public double getPrice() {
    return quantity * cookie.getPrice();
  }

  public Duration getPreparationDuration() {
    return cookie.getPreparationDuration().multipliedBy(quantity);
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public Cookie getCookie(){
    return cookie;
  }

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof OrderItem)) return false;
    if( (OrderItem)obj == this) return true;
    if (quantity == ((OrderItem)obj).quantity && cookie == ((OrderItem)obj).cookie) return true;
    return false;
  }
}
