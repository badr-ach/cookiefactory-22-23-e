package fr.unice.polytech.cf.cookieservice.entities.cooking;

public class CookingManoeuvre implements Cloneable {
    private String type;
    private double price;
    public CookingManoeuvre(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public CookingManoeuvre clone() {
        try {
            CookingManoeuvre clone = (CookingManoeuvre) super.clone();

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
