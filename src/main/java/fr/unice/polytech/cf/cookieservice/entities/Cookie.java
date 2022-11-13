package fr.unice.polytech.cf.cookieservice.entities;


import fr.unice.polytech.cf.cookieservice.enums.ECookieStatus;

import java.time.Duration;

import java.util.Map;

public class Cookie {
    private int id;
    private String name;
    private double price;
    private Duration preparationDuration;
    private Map<Ingredient,Integer> ingredients;
    private ECookieStatus status;
    private static int ID = 0;

    public Cookie(String name, double price, Map<Ingredient,Integer> ingredients) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.status = ECookieStatus.SUBMITTED;
        this.id = ID;
        ID++;
    }

    public Cookie(String name, double price, Map<Ingredient,Integer> ingredients, Duration preparationDuration){
        this(name,price,ingredients);
        this.preparationDuration = preparationDuration;
    }

    public Map<Ingredient,Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<Ingredient,Integer> ingredients) {
        this.ingredients = ingredients;
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

    public ECookieStatus getStatus() {
        return status;
    }

    public void setStatus(ECookieStatus status) {
        this.status = status;
    }

    public void setPreparationDuration(Duration preparationDuration) {
        this.preparationDuration = preparationDuration;
    }

    public Duration getPreparationDuration() {
        return preparationDuration;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cookie)) return false;
        if ((Cookie) obj == this) return true;
        return this.id == ((Cookie) obj).id;
    }
}
