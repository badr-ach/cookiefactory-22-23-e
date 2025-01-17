package fr.unice.polytech.cf.entities.ingredients;

import fr.unice.polytech.cf.entities.Ingredient;
import fr.unice.polytech.cf.entities.enums.EIngredientType;

public class Flavor extends Ingredient implements Cloneable {
    public Flavor(String name, double price) {
        super(name, price, EIngredientType.FLAVOR);
    }

    @Override
    public Flavor clone() {
        return (Flavor) super.clone();
    }
}
