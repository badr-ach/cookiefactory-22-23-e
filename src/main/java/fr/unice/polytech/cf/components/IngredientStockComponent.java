package fr.unice.polytech.cf.components;

import fr.unice.polytech.cf.entities.Ingredient;
import fr.unice.polytech.cf.entities.Stock;
import fr.unice.polytech.cf.entities.Store;
import fr.unice.polytech.cf.exceptions.InvalidStoreException;
import fr.unice.polytech.cf.interfaces.IngredientStockModifier;
import fr.unice.polytech.cf.interfaces.IngredientStockReserver;
import fr.unice.polytech.cf.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * This class is the implementation of the IngredientStockModifier interface used to modify the stock of ingredients
 * And the implementation of the IngredientStockReserver interface used to reserve ingredients
 */
@Component
public class IngredientStockComponent implements IngredientStockReserver, IngredientStockModifier {
    /**
     * The store repository used to Acess to the store
     */
    private StoreRepository storeRepository;

    /**
     * Constructor of the IngredientStockComponent
     * @param storeRepository for retrieving the store
     */
    @Autowired
    public IngredientStockComponent(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    /**
     * This method is used to reserve ingredients and put them in the reserved stock of the store
     * @param store our store
     * @param ingredients an hasmap of ingredients and their quantity to reserve
     * @return true if the reservation is done, false if not
     */
    @Override
    public Map<Ingredient, Integer> reserveStock(Store store, Map<Ingredient, Integer> ingredients) {
        if (storeRepository.findById(store.getId()).isEmpty()) throw new InvalidStoreException("Store Specified does not exist");
        Stock stock = store.getIngredientsStock();
        return stock.reserve(ingredients);
    }

    /**
     * this is used to Cancel a reservation of ingredients and put them back in the stock of the store
     * @param store our store
     * @param ingredients an hasmap of ingredients and their quantity to put back in the stock
     * @return true if the cancelation is done, false if not
     */
    @Override
    public Map<Ingredient, Integer> cancelReservation(Store store, Map<Ingredient, Integer> ingredients){
        if (storeRepository.findById(store.getId()).isEmpty()) throw new InvalidStoreException("Store Specified does not exist");
        Stock stock = store.getIngredientsStock();
        return stock.cancelReservation(ingredients);
    }

    /**
     * This method is used to consume ingredients from the reserved stock of the store for a recipe
     * @param store our store
     * @param ingredients an hasmap of ingredients and their quantity to consume
     * @return true if the consumption is done, false if not
     */
    @Override
    public Map<Ingredient, Integer> consumeFromStock(Store store, Map<Ingredient, Integer> ingredients){
        if (storeRepository.findById(store.getId()).isEmpty()) throw new InvalidStoreException("Store Specified does not exist");
        Stock stock = store.getIngredientsStock();
        return stock.removeFromReserve(ingredients);
    }

    /**
     * This method is used to add ingredients to the stock of the store
     * @param store our store
     * @param ingredients an hasmap of ingredients and their quantity to add to the store stock
     * @return true if the addition is done, false if not
     */
    @Override
    public Map<Ingredient, Integer> addToStock(Store store, Map<Ingredient, Integer> ingredients){
        if (storeRepository.findById(store.getId()).isEmpty()) throw new InvalidStoreException("Store Specified does not exist");
        Stock stock = store.getIngredientsStock();
        return stock.add(ingredients);
    }

    /**
     * This method is used to remove ingredients from the stock of the store
     * @param store our store
     * @param ingredients an hasmap of ingredients and their quantity to remove from the store stock
     * @return true if the removal is done, false if not
     */
    @Override
    public Map<Ingredient, Integer> removeFromStock(Store store, Map<Ingredient, Integer> ingredients){
        if (storeRepository.findById(store.getId()).isEmpty()) throw new InvalidStoreException("Store Specified does not exist");
        Stock stock = store.getIngredientsStock();
        return stock.remove(ingredients);
    }

}
