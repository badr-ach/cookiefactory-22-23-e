package fr.unice.polytech.cf.storeservice;

import fr.unice.polytech.cf.cookieservice.entities.ingredients.Ingredient;
import fr.unice.polytech.cf.cookieservice.enums.EIngredientType;

import java.util.*;

public class PartnerCatalogService {

    private Map<String,List<Ingredient>> catalog;

    private List<String> partners;

    public PartnerCatalogService(){
        catalog = fetchAvailableIngredients();
    }

    /**
     * fetches the latest version of catalog
     * @return Key Pair of partners and ingredients they offer
     */
    public Map<String,List<Ingredient>> fetchAvailableIngredients(){
        return new HashMap<String,List<Ingredient>>();
    }


    /**
     * Cache of catalog
     * @return Key Pair of partners and ingredients they offer
     */
    public Map<String,List<Ingredient>> getAvailableIngredients(){
        return catalog;
    }

    /**
     * Cache of ingredients
     * @param partner
     * @return List of ingredients offered by this partner
     */
    public List<Ingredient> getAvailableIngredients(String partner){
        return catalog.get(partner);
    }

    public void seedCatalog(){
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("DryDough",2, EIngredientType.DOUGH));
        ingredients.add(new Ingredient("SweetTopping",3, EIngredientType.TOPPING));
        ingredients.add(new Ingredient("Oreo",2, EIngredientType.FLAVOR));
        ingredients.add(new Ingredient("Strawberry",2, EIngredientType.FLAVOR));
        ingredients.add(new Ingredient("ChocolateTopping",2, EIngredientType.TOPPING));
        catalog.put("Partner1",ingredients);
    }
}
