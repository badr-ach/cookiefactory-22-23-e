package fr.unice.polytech.cf.cookieservice;

import fr.unice.polytech.cf.cookieservice.entities.Cookie;
import fr.unice.polytech.cf.cookieservice.interfaces.IPastry;

public class CookieBuilder extends APastryBuilder {
    protected Double margin;

    public void withMargin(double margin) {
        this.margin = margin;
    }

    public void reset() {
        dough = null;
        flavor = null;
        toppings = null;
        cooking = null;
        mix = null;
        ingredients = null;
    }

    public IPastry getResult() {
        bundleCookie();
        return new Cookie(name, preparationDuration, ingredients, mix, cooking, margin);
    }

}
