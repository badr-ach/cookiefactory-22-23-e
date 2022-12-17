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


@Component
public class IngredientStockComponent implements IngredientStockReserver, IngredientStockModifier {

    private StoreRepository storeRepository;

    @Autowired
    public IngredientStockComponent(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public Map<Ingredient, Integer> reserveStock(Store store, Map<Ingredient, Integer> ingredients) {
        if (storeRepository.findById(store.getId()).isEmpty()) throw new InvalidStoreException("Store Specified does not exist");
        Stock stock = store.getIngredientsStock();
        return stock.reserve(ingredients);
    }

    @Override
    public Map<Ingredient, Integer> cancelReservation(Store store, Map<Ingredient, Integer> ingredients){
        if (storeRepository.findById(store.getId()).isEmpty()) throw new InvalidStoreException("Store Specified does not exist");
        Stock stock = store.getIngredientsStock();
        return stock.cancelReservation(ingredients);
    }

    @Override
    public Map<Ingredient, Integer> consumeFromStock(Store store, Map<Ingredient, Integer> ingredients){
        if (storeRepository.findById(store.getId()).isEmpty()) throw new InvalidStoreException("Store Specified does not exist");
        Stock stock = store.getIngredientsStock();
        return stock.removeFromReserve(ingredients);
    }

    @Override
    public Map<Ingredient, Integer> addToStock(Store store, Map<Ingredient, Integer> ingredients){
        if (storeRepository.findById(store.getId()).isEmpty()) throw new InvalidStoreException("Store Specified does not exist");
        Stock stock = store.getIngredientsStock();
        return stock.add(ingredients);
    }

    @Override
    public Map<Ingredient, Integer> removeFromStock(Store store, Map<Ingredient, Integer> ingredients){
        if (storeRepository.findById(store.getId()).isEmpty()) throw new InvalidStoreException("Store Specified does not exist");
        Stock stock = store.getIngredientsStock();
        return stock.remove(ingredients);
    }

}
