package fr.unice.polytech.cf.components;

import fr.unice.polytech.cf.entities.APastryBuilder;
import fr.unice.polytech.cf.entities.Cookie;
import fr.unice.polytech.cf.interfaces.IPastry;
import org.springframework.stereotype.Component;

@Component
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
