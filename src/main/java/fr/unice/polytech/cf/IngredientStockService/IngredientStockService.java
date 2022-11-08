package fr.unice.polytech.cf.IngredientStockService;
import fr.unice.polytech.cf.CookieService.Entities.*;

import java.util.HashMap;
public class IngredientStockService {

    HashMap<Ingredient, Integer> ingredients;
public IngredientStockService(){
    ingredients = new HashMap<Ingredient,Integer>();

}
    public void add(Ingredient ingredient){
        if(ingredients.containsKey(ingredient)){
            ingredients.put(ingredient,ingredients.get(ingredient)+1);
            }else{
            ingredients.put(ingredient,1);
        }

    }
    public HashMap<Ingredient, Integer> getIngredients() {
        return ingredients;
    }
    public boolean is_in(Ingredient ingredient){
    if(ingredients.containsKey(ingredient)) return true;
    return false;
    }
    public Integer getValue(Ingredient ingredient){
    return ingredients.get(ingredient);
    }

}
