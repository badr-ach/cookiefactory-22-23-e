package fr.unice.polytech.cf.entities;

import fr.unice.polytech.cf.exceptions.InsufficientStockException;
import fr.unice.polytech.cf.exceptions.InvalidQuantityException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Stock {
    Map<Ingredient, Integer> availableIngredients;
    Map<Ingredient, Integer> reservedIngredients;

    public Stock() {
        availableIngredients = new HashMap<Ingredient, Integer>();
        reservedIngredients = new HashMap<Ingredient, Integer>();
    }

    public void add(Ingredient ingredient) {
        add(ingredient, 1);
    }

    public void remove(Ingredient ingredient) {
        remove(ingredient, 1);
    }

    public int remove(Ingredient ingredient, int quantity) {
        if (getAvailableQuantity(ingredient) >= quantity) {
            availableIngredients.put(ingredient, getAvailableQuantity(ingredient) - quantity);
        } else {
            throw new InvalidQuantityException("Attempting to remove more ingredients than what exists quantity");
        }
        return availableIngredients.get(ingredient);
    }

    public Map<Ingredient, Integer> remove(Map<Ingredient, Integer> supplies) {
        Map<Ingredient, Integer> res = new HashMap<>();
        for (Map.Entry<Ingredient, Integer> supply : supplies.entrySet()) {
            res.put(supply.getKey(), remove(supply.getKey(), supply.getValue()));
        }
        return res;
    }

    public int removeFromReserve(Ingredient ingredient, int quantity) {
        if (getAvailableReserveQuantity(ingredient) >= quantity) {
            reservedIngredients.put(ingredient, getAvailableReserveQuantity(ingredient) - quantity);
        } else {
            throw new InvalidQuantityException("Attempting to remove more ingredients than what exists quantity");
        }
        return reservedIngredients.get(ingredient);
    }

    public Map<Ingredient, Integer> removeFromReserve(Map<Ingredient, Integer> supplies) {
        Map<Ingredient, Integer> res = new HashMap<>();
        for (Map.Entry<Ingredient, Integer> supply : supplies.entrySet()) {
            res.put(supply.getKey(), removeFromReserve(supply.getKey(), supply.getValue()));
        }
        return res;
    }

    public int add(Ingredient ingredient, int quantity) {
        if (quantity > 0) {
            availableIngredients.put(ingredient, getAvailableQuantity(ingredient) + quantity);
        } else {
            throw new InvalidQuantityException("Invalid ingredient addition quantity");
        }
        return availableIngredients.get(ingredient);
    }

    public Map<Ingredient, Integer> add(Map<Ingredient, Integer> supplies) {
        Map<Ingredient, Integer> res = new HashMap<>();
        for (Map.Entry<Ingredient, Integer> supply : supplies.entrySet()) {
            res.put(supply.getKey(), add(supply.getKey(), supply.getValue()));
        }
        return res;
    }

    public int reserve(Ingredient ingredient, int quantity) {
        if (quantity > 0) {
            if (getAvailableQuantity(ingredient) >= quantity) {
                availableIngredients.put(ingredient, getAvailableQuantity(ingredient) - quantity);
                reservedIngredients.put(ingredient, getAvailableReserveQuantity(ingredient) + quantity);
                return reservedIngredients.get(ingredient);
            } else {
                throw new InsufficientStockException("Ingredient is not available in the stock");
            }
        } else {
            throw new InvalidQuantityException("Invalid ingredient reservation quantity");
        }
    }

    public int liberate(Ingredient ingredient, int quantity) {
        if (quantity > 0) {
            if (getAvailableReserveQuantity(ingredient) >= quantity) {
                availableIngredients.put(ingredient, getAvailableQuantity(ingredient) + quantity);
                reservedIngredients.put(ingredient, getAvailableReserveQuantity(ingredient) - quantity);
                return availableIngredients.get(ingredient);
            } else {
                throw new InsufficientStockException("Ingredient is not available in the reserved stock stock");
            }
        } else {
            throw new InvalidQuantityException("Invalid ingredient liberation quantity");
        }
    }



    public boolean available(Map<Ingredient, Integer> supplies){
        for (Map.Entry<Ingredient, Integer> supply : supplies.entrySet()) {
            if(getAvailableQuantity(supply.getKey()) < supply.getValue())
                return false;
        }
        return true;
    }
    public Map<Ingredient, Integer> reserve(Map<Ingredient, Integer> supplies) {
        Map<Ingredient, Integer> res = new HashMap<>();
        if(!available(supplies))
            throw new InsufficientStockException("Ingredient is not available in the stock");
        for (Map.Entry<Ingredient, Integer> supply : supplies.entrySet()) {
            reserve(supply.getKey(), supply.getValue());
        }
        return res;
    }

    public boolean reserveContains(Ingredient ingredient, int quantity) {
        return (reservedIngredients.get(ingredient) != null) &&
                (reservedIngredients.get(ingredient) >= quantity);
    }

    public boolean reserveContains(Map<Ingredient, Integer> ingredients) {
        for (Map.Entry<Ingredient, Integer> ingredient : ingredients.entrySet()) {
            if (!reserveContains(ingredient.getKey(), ingredient.getValue())) return false;
        }
        return true;
    }

    public Map<Ingredient, Integer> cancelReservation(Map<Ingredient, Integer> supplies) {
        Map<Ingredient, Integer> res = new HashMap<>();
        if (!reserveContains(supplies))
            throw new InvalidQuantityException("Ingredient quantity is not available in the reserve");

        for (Map.Entry<Ingredient, Integer> supply : supplies.entrySet()) {
            this.add(supply.getKey(), supply.getValue());
            reservedIngredients.put(supply.getKey(), reservedIngredients.get(supply.getKey()) - supply.getValue());
        }
        return res;
    }

    public int getAvailableQuantity(Ingredient ingredient) {
        if (availableIngredients.containsKey(ingredient)) {
            return availableIngredients.get(ingredient);
        }
        return 0;
    }

    public int getAvailableReserveQuantity(Ingredient ingredient) {
        if (reservedIngredients.containsKey(ingredient)) {
            return reservedIngredients.get(ingredient);
        }
        return 0;
    }

    public boolean contains(Ingredient ingredient) {
        return availableIngredients.containsKey(ingredient);
    }

    public Integer getValue(Ingredient ingredient) {
        return availableIngredients.get(ingredient);
    }

    public Map<Ingredient, Integer> getAvailableIngredients() {
        return availableIngredients;
    }

    public Map<Ingredient, Integer> getReservedIngredients() {
        return reservedIngredients;
    }
}
