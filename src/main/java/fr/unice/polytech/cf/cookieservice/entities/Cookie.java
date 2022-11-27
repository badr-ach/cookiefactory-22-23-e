package fr.unice.polytech.cf.cookieservice.entities;


import fr.unice.polytech.cf.cookieservice.entities.cooking.Cooking;
import fr.unice.polytech.cf.cookieservice.entities.cooking.CookingManoeuvre;
import fr.unice.polytech.cf.cookieservice.entities.cooking.Mix;
import fr.unice.polytech.cf.cookieservice.entities.ingredients.Ingredient;
import fr.unice.polytech.cf.cookieservice.enums.ECookieStatus;
import fr.unice.polytech.cf.cookieservice.interfaces.IPastry;
import fr.unice.polytech.cf.partycookieservice.Requirement;

import java.time.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cookie implements IPastry, Cloneable {
    private int id;
    private String name;
    private Double price;
    private Duration preparationDuration;
    private Map<Ingredient, Integer> ingredients;
    private ECookieStatus status;
    private static int ID = 0;
    private Double margin;
    private Mix mix;
    private Cooking cooking;

    public Cookie() {

        this.status = ECookieStatus.SUBMITTED;
        this.ingredients = new HashMap<>();
    }

    public Cookie(String name, Duration preparationDuration, Map<Ingredient, Integer> ingredients,
                  Mix mix, Cooking cooking, double margin) {
        this(name, preparationDuration, ingredients, mix, cooking);
        this.margin = margin;
    }

    public Cookie(String name, Duration preparationDuration, Map<Ingredient, Integer> ingredients,
                  Mix mix, Cooking cooking) {
        this.name = name;
        this.preparationDuration = preparationDuration;
        this.ingredients = ingredients;
        this.mix = mix;
        this.cooking = cooking;
        this.status = ECookieStatus.SUBMITTED;

    }

    public Cookie(String name, double price, Map<Ingredient, Integer> ingredients) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.status = ECookieStatus.DEFAULT;
        this.id = ID;
        ID++;
    }

    public Cookie(String name, double price, Map<Ingredient, Integer> ingredients, Duration preparationDuration) {
        this(name, price, ingredients);
        this.preparationDuration = preparationDuration;
    }


    public Map<Ingredient, Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<Ingredient, Integer> ingredients) {
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

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        if (price == null) {
            double price = 0;
            for (Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()) {
                price += entry.getKey().getPrice() * entry.getValue();
            }
            return price * (1 + margin);
        } else {
            return price;
        }
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

    public Double getMargin() {
        return margin;
    }

    public void setMargin(Double margin) {
        this.margin = margin;
    }

    public Mix getMix() {
        return mix;
    }

    public void setMix(Mix mix) {
        this.mix = mix;
    }

    public Cooking getCooking() {
        return cooking;
    }

    public void setCooking(Cooking cooking) {
        this.cooking = cooking;
    }

    public List<Requirement> getRequirements() {
        return new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cookie)) return false;
        if ((Cookie) obj == this) return true;
        return this.id == ((Cookie) obj).id;
    }

    @Override
    public Cookie clone() {
        try {
            Cookie clone = (Cookie) super.clone();
            clone.setMix(this.mix.clone());
            clone.setCooking(this.cooking.clone());
            HashMap<Ingredient, Integer> cloneIngredients = new HashMap<>();
            for (Map.Entry<Ingredient, Integer> entry : this.ingredients.entrySet()) {
                cloneIngredients.put(entry.getKey().clone(), entry.getValue());
            }
            clone.setIngredients(cloneIngredients);
            clone.setPreparationDuration(Duration.ofSeconds(this.preparationDuration.getSeconds()));
            clone.setStatus(ECookieStatus.CUSTOM);
            clone.setMargin(this.margin);
            clone.setPrice(this.price);
            clone.setPrice(this.price);
            clone.setId(Cookie.ID++);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
