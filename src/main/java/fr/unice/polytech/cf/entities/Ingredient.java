package fr.unice.polytech.cf.entities;

import fr.unice.polytech.cf.entities.enums.EIngredientType;

import java.util.UUID;

public class Ingredient implements Cloneable {


    private UUID id;
    private String name;
    private double price;
    private EIngredientType type;


    public Ingredient(String name, double price, EIngredientType type) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.id = UUID.randomUUID();

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

    public UUID getId() {
          return id;
      }

    public void setId(UUID id) {
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

    @Override
    public Ingredient clone() {
        try {
            Ingredient clone = (Ingredient) super.clone();
            clone.setType(type);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
