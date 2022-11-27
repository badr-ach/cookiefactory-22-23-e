package fr.unice.polytech.cf.cookieservice;

import fr.unice.polytech.cf.cookieservice.entities.cooking.Cooking;
import fr.unice.polytech.cf.cookieservice.entities.cooking.Mix;
import fr.unice.polytech.cf.cookieservice.entities.ingredients.Dough;
import fr.unice.polytech.cf.cookieservice.entities.ingredients.Flavor;
import fr.unice.polytech.cf.cookieservice.entities.ingredients.Ingredient;
import fr.unice.polytech.cf.cookieservice.entities.ingredients.Topping;
import fr.unice.polytech.cf.cookieservice.exceptions.IllegalBuildStepException;
import fr.unice.polytech.cf.cookieservice.interfaces.IPastry;

import java.time.Duration;
import java.util.*;

public abstract class APastryBuilder {

    protected AbstractMap.SimpleEntry<Dough, Integer> dough;
    protected AbstractMap.SimpleEntry<Flavor, Integer> flavor;
    protected List<AbstractMap.SimpleEntry<Topping, Integer>> toppings;
    protected Mix mix;
    protected Cooking cooking;
    protected String name;
    protected Duration preparationDuration;
    protected Map<Ingredient, Integer> ingredients;

    public void withName(String name) {
        this.name = name;
    }

    public void withPreparationDuration(Duration preparationDuration) {
        this.preparationDuration = preparationDuration;
    }

    public void withDough(Dough dough, Integer quantity) {
        this.dough = new AbstractMap.SimpleEntry<>(dough, quantity);
    }

    public void withFlavor(Flavor flavor, Integer quantity) {
        this.flavor = new AbstractMap.SimpleEntry<>(flavor, quantity);
    }

    public void withTopping(Topping topping, Integer quantity) {
        if (toppings == null) {
            toppings = new ArrayList<>();
            toppings.add(new AbstractMap.SimpleEntry<>(topping, quantity));
        }
        List<AbstractMap.SimpleEntry<Topping, Integer>> toppingList = toppings;
        if (toppingList.size() < 3) {
            toppingList.add(new AbstractMap.SimpleEntry<>(topping, quantity));
        } else {
            throw new IllegalBuildStepException("You can't add more than 3 toppings");
        }
    }

    public void withMix(Mix mix) {
        this.mix = mix;
    }

    public void withCooking(Cooking cooking) {
        this.cooking = cooking;
    }

    protected void bundleCookie() {
        if (name == null) {
            throw new IllegalArgumentException("You must add a name");
        }
        if (preparationDuration == null) {
            throw new IllegalArgumentException("You must add a preparation duration");
        }
        if (cooking == null) {
            throw new IllegalArgumentException("You must add a cooking");
        }
        if (mix == null) {
            throw new IllegalArgumentException("You must add a mix");
        }
        if (dough == null) {
            throw new IllegalArgumentException("You must add a dough");
        }
        ingredients = new HashMap<>();
        ingredients.put(dough.getKey(), dough.getValue());
        ingredients.put(flavor.getKey(), flavor.getValue());
        for (AbstractMap.SimpleEntry<Topping, Integer> topping : toppings) {
            ingredients.put(topping.getKey(), topping.getValue());
        }
    }

    public abstract void reset();

    public abstract IPastry getResult();
}
