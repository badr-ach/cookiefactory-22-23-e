package fr.unice.polytech.cf.entities;

public class Mix extends CookingManoeuvre implements Cloneable {
    public Mix(String type, double price) {
        super(type, price);
    }

    @Override
    public Mix clone() {
        return (Mix) super.clone();
    }
}
