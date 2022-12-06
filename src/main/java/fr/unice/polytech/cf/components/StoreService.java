package fr.unice.polytech.cf.components;

import fr.unice.polytech.cf.entities.Ingredient;
import fr.unice.polytech.cf.entities.*;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.exceptions.InvalidScheduleException;
import fr.unice.polytech.cf.exceptions.InvalidStoreException;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoreService {
    private List<Store> stores = new ArrayList<Store>();

    public StoreService() {
        Store store = new Store();
        store.setId(getStores().size() + 1);
        store.getSchedule().seedSchedule();
        stores.add(store);
    }

    public boolean addStore(Store store) {
        return stores.add(store);
    }

    public List<Store> getStores() {
        return stores;
    }

    public Cook hasOrder(Order order, Store store){
        for(Cook cook : store.getCooks()){
            if(cook.hasOrder(order)) return cook;
        }
        return null;
    }

    public boolean remove(Order order, Store store){
        for(Cook cook: store.getCooks()){
            if(cook.hasOrder(order)) {
                cook.removeOrder(order);
                return true;
            }
        }
        return false;
    }

    public Store getStore(int id) {
        Store store = stores.stream().filter(o -> o.getId() == id).findFirst().orElse(null);
        if (store == null) throw new InvalidStoreException("Store Specified does not exist");
        return store;
    }

    public List<TimeSlot> updateStoreSchedule(Store store, List<TimeSlot> timeSlots, DayOfWeek day){
        if (!stores.contains(store)) throw new InvalidStoreException("Store Specified does not exist");
        return store.setSchedule(timeSlots,day);
    }

    public void updateStoreSchedule(Store store, Schedule schedule){
        if (!stores.contains(store)) throw new InvalidStoreException("Store Specified does not exist");
        store.setSchedule(schedule);
    }

    public void updateCookSchedule(Cook cook, Schedule schedule){
        Store store = stores.stream().filter(o->o.getCooks().contains(cook)).findFirst().orElse(null);
        if (store == null) throw new InvalidStoreException("Store Specified does not exist");
        if (!store.getSchedule().hasWithinIt(schedule)) throw new InvalidScheduleException("Incompatible schedule");
        cook.setSchedule(schedule);
    }

    public Map<Ingredient, Integer> reserveStock(Store store, Map<Ingredient, Integer> ingredients) {
        if (!stores.contains(store)) throw new InvalidStoreException("Store Specified does not exist");
        Stock stock = store.getIngredientsStock();
        return stock.reserve(ingredients);
    }

    public Map<Ingredient, Integer> cancelReservation(Store store, Map<Ingredient, Integer> ingredients){
        if (!stores.contains(store)) throw new InvalidStoreException("Store Specified does not exist");
        Stock stock = store.getIngredientsStock();
        return stock.cancelReservation(ingredients);
    }

    public Map<Ingredient, Integer> removeFromStock(Store store, Map<Ingredient, Integer> ingredients){
        if (!stores.contains(store)) throw new InvalidStoreException("Store Specified does not exist");
        Stock stock = store.getIngredientsStock();
        return stock.remove(ingredients);
    }

    public Map<Ingredient, Integer> consumeFromStock(Store store, Map<Ingredient, Integer> ingredients){
        if (!stores.contains(store)) throw new InvalidStoreException("Store Specified does not exist");
        Stock stock = store.getIngredientsStock();
        return stock.removeFromReserve(ingredients);
    }

    public Map<Ingredient, Integer> addToStock(Store store, Map<Ingredient, Integer> ingredients){
        if (!stores.contains(store)) throw new InvalidStoreException("Store Specified does not exist");
        Stock stock = store.getIngredientsStock();
        return stock.add(ingredients);
    }
}
