package fr.unice.polytech.cf.entities.ingredients;

import fr.unice.polytech.cf.entities.CookingManoeuvre;

public class Mix extends CookingManoeuvre implements Cloneable {
    public Mix(String type, double price) {
        super(type, price);
    }

    @Override
    public Mix clone() {
        return (Mix) super.clone();
    }
}
