package fr.unice.polytech.cf.CookieService.Entities;

import fr.unice.polytech.cf.CookieService.Enums.EIngredientType;

public class Ingredient {

  private int id;
  private String name;
  private double price;
  private EIngredientType type;
  private static int ID = 0;

  public Ingredient(String name, double price, EIngredientType type) {
    this.name = name;
    this.price = price;
    this.type = type;
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

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public EIngredientType getType() {
    return type;
  }

  public void setType(EIngredientType type) {
    this.type = type;
  }

  public static int getID() {
    return ID;
  }

  public static void setID(int iD) {
    ID = iD;
  }
}
