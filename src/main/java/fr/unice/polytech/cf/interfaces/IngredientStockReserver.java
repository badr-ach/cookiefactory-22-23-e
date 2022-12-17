package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Ingredient;
import fr.unice.polytech.cf.entities.Store;

import java.util.Map;

public interface IngredientStockReserver {
    Map<Ingredient, Integer> reserveStock(Store store, Map<Ingredient, Integer> ingredients);

    Map<Ingredient, Integer> cancelReservation(Store store, Map<Ingredient, Integer> ingredients);

    Map<Ingredient, Integer> consumeFromStock(Store store, Map<Ingredient, Integer> ingredients);
}
