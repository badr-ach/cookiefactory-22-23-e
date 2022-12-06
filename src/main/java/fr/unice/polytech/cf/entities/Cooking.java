package fr.unice.polytech.cf.entities;

public class Cooking extends CookingManoeuvre implements Cloneable {
    public Cooking(String type, double price) {
        super(type, price);
    }

    @Override
    public Cooking clone() {
        return (Cooking) super.clone();
    }
}
