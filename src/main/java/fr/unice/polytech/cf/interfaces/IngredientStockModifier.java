package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Ingredient;
import fr.unice.polytech.cf.entities.Store;

import java.util.Map;

public interface IngredientStockModifier {
    Map<Ingredient, Integer> addToStock(Store store, Map<Ingredient, Integer> ingredients);

    Map<Ingredient, Integer> removeFromStock(Store store, Map<Ingredient, Integer> ingredients);
}
