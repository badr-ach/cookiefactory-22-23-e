package fr.unice.polytech.cf.entities.ingredients;

import fr.unice.polytech.cf.entities.Ingredient;
import fr.unice.polytech.cf.entities.enums.EIngredientType;

public class Topping extends Ingredient implements Cloneable {
    public Topping(String name, double price) {
        super(name, price, EIngredientType.TOPPING);
    }

    @Override
    public Topping clone() {
        return (Topping) super.clone();
    }
}
