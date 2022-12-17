package fr.unice.polytech.cf.components;

import fr.unice.polytech.cf.entities.APastryBuilder;
import fr.unice.polytech.cf.entities.Cookie;
import fr.unice.polytech.cf.entities.ingredients.Dough;
import fr.unice.polytech.cf.entities.ingredients.Flavor;
import fr.unice.polytech.cf.entities.Ingredient;
import fr.unice.polytech.cf.entities.ingredients.Topping;
import fr.unice.polytech.cf.entities.enums.EIngredientType;
import fr.unice.polytech.cf.exceptions.IllegalBuildStepException;
import fr.unice.polytech.cf.interfaces.IPastry;
import fr.unice.polytech.cf.entities.BasePartyCookie;
import fr.unice.polytech.cf.entities.PersonalizedPartyCookie;
import fr.unice.polytech.cf.entities.enums.Occasion;
import fr.unice.polytech.cf.entities.enums.Size;
import fr.unice.polytech.cf.entities.enums.Theme;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PartyPastryBuilder extends APastryBuilder {
    private Cookie cookie;
    private Size size;
    private Theme theme;
    private Occasion occasion;

    public void withBaseCookie(Cookie cookie){
        this.cookie = cookie.clone();
    }

    public void withoutTopping(Topping topping){
        if(cookie == null){
            throw new IllegalBuildStepException("You must specify a base cookie before removing a topping");
        }
        Map<Ingredient,Integer> ingredients = this.cookie.getIngredients();
        ingredients.remove(topping);
    }

    public void withSize(Size size){
        if(cookie == null){
            throw new IllegalBuildStepException("You must specify a base cookie before specifying a size");
        }
        this.size = size;
        for(Map.Entry<Ingredient,Integer> ingredientIntegerSimpleEntry : cookie.getIngredients().entrySet()){
            ingredientIntegerSimpleEntry.setValue(ingredientIntegerSimpleEntry.getValue() * size.getMultiplier());
        }
        cookie.setPreparationDuration(cookie.getPreparationDuration().multipliedBy(size.getMultiplier()));
        cookie.setPrice(cookie.getPrice() * size.getMultiplier());
    }

    @Override
    public void withDough(Dough dough, Integer quantity) {
        if(cookie == null){
            super.withDough(dough, quantity);
        }else {
            for (Map.Entry<Ingredient, Integer> ingredientIntegerSimpleEntry : cookie.getIngredients().entrySet()) {
                if (ingredientIntegerSimpleEntry.getKey().getType().equals(EIngredientType.DOUGH)) {
                    ingredients.remove(ingredientIntegerSimpleEntry.getKey());
                    ingredients.put(dough, quantity);
                }
            }
        }
    }

    @Override
    public void withFlavor(Flavor flavor, Integer quantity) {
        if(cookie == null){
            super.withFlavor(flavor, quantity);
        }else{
            for (Map.Entry<Ingredient, Integer> ingredientIntegerSimpleEntry : cookie.getIngredients().entrySet()) {
                if (ingredientIntegerSimpleEntry.getKey().getType().equals(EIngredientType.FLAVOR)) {
                    ingredients.remove(ingredientIntegerSimpleEntry.getKey());
                    ingredients.put(flavor, quantity);
                }
            }
        }
    }

    public void withOccasion(Occasion occasion){
        this.occasion = occasion;
    }

    public void withTheme(Theme theme){
        this.theme = theme;
    }

    @Override
    public void withTopping(Topping topping, Integer quantity) {
        if(toppings == null){
            toppings = new ArrayList<>();
            toppings.add(new AbstractMap.SimpleEntry<>(topping, quantity));
        }
        List<AbstractMap.SimpleEntry<Topping, Integer>> toppingList = toppings;
        if (toppingList.size() < 6) {
            toppingList.add(new AbstractMap.SimpleEntry<>(topping, quantity));
        }else{
            throw new IllegalBuildStepException("You can't add more than 6 toppings");
        }
    }


    @Override
    public void reset() {
        dough = null;
        flavor = null;
        toppings = null;
        cooking = null;
        mix = null;
        cookie = null;
        size = null;
        theme = null;
        occasion = null;
    }

    @Override
    public IPastry getResult() {
        if(cookie == null) {
            cookie = new Cookie(name, preparationDuration,ingredients,mix,cooking);
            return new PersonalizedPartyCookie(cookie, theme, occasion);
        }
        return new BasePartyCookie(cookie, size, theme, occasion);
    }
}
