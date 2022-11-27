package fr.unice.polytech.cf.cookieservice.entities.ingredients;

import fr.unice.polytech.cf.cookieservice.enums.EIngredientType;

public class Dough extends Ingredient implements Cloneable {
    public Dough(String name, double price) {
        super(name, price, EIngredientType.DOUGH);
    }

    @Override
    public Dough clone() {
        return (Dough) super.clone();
    }
}
