package fr.unice.polytech.cf.CookieService.Entities;

import java.time.Duration;
import java.util.ArrayList;

public class Cookie {
  private int id;
  private String name;
  private double price;
  private Duration preparationDuration;
  private ArrayList<Ingredient> ingredients;
  private static int ID = 0;

  public Cookie(String name, double price, ArrayList<Ingredient> ingredients) {
    this.name = name;
    this.price = price;
    this.ingredients = ingredients;

    this.id = ID;
    ID++;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getPrice() {
    return price;
  }

  public void setPreparationDuration(Duration preparationDuration) {
    this.preparationDuration = preparationDuration;
  }

  public Duration getPreparationDuration() {
    return preparationDuration;
  }

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof Cookie)) return false;
    if( (Cookie)obj == this) return true;
    return this.id == ((Cookie) obj).id;
  }
}
