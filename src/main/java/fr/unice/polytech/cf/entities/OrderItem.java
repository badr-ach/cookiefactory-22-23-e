package fr.unice.polytech.cf.entities;

import java.time.Duration;
import java.util.Map;

import fr.unice.polytech.cf.interfaces.IPastry;

public class OrderItem {
    private int quantity;
    private IPastry cookie;

    public OrderItem(IPastry cookie) {
        this.cookie = cookie;
        this.quantity = 1;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public void increase() {
        quantity++;
    }

    public void decrease() {
        if (quantity >= 0) {
            quantity--;
        }
    }

    public Map<Ingredient,Integer> getIngredients(){
        Map<Ingredient,Integer> ingredients = cookie.getIngredients();
        ingredients.replaceAll((k, v) -> v * quantity);
        return ingredients;
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

    public IPastry getCookie() {
        return cookie;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OrderItem)) return false;
        if ((OrderItem) obj == this) return true;
        if (quantity == ((OrderItem) obj).quantity && cookie == ((OrderItem) obj).cookie) return true;
        return false;
    }
}
