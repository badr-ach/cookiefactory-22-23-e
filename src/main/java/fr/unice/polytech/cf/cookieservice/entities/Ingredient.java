package fr.unice.polytech.cf.cookieservice.entities;

import fr.unice.polytech.cf.cookieservice.enums.EIngredientType;

public class Ingredient {

    private static int ID = 0;
    private int id;
    private String name;
    private double price;
    private EIngredientType type;


    public Ingredient(String name, double price, EIngredientType type) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.id = ID;
        ID++;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Ingredient that)) return false;
      return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString(){
        return "name: " + name + " price: " + price;
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
}