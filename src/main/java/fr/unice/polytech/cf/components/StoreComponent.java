package fr.unice.polytech.cf.components;


import fr.unice.polytech.cf.entities.*;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.exceptions.InvalidScheduleException;
import fr.unice.polytech.cf.exceptions.InvalidStoreException;
import fr.unice.polytech.cf.interfaces.StoreCookModifier;
import fr.unice.polytech.cf.interfaces.StoreOrdersModifier;
import fr.unice.polytech.cf.interfaces.StoreScheduleFinder;
import fr.unice.polytech.cf.interfaces.StoreScheduleModifier;
import fr.unice.polytech.cf.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

/**
 * The store component which is based on a schedule manager and all what is related to
 * the cook and its schedule updating too
 */
@Component
public class StoreComponent implements StoreScheduleFinder, StoreCookModifier, StoreScheduleModifier, StoreOrdersModifier {
    private StoreRepository storeRepository;

    @Autowired
    public StoreComponent(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
        Store store = new Store();
        store.setId(UUID.randomUUID());
        store.getSchedule().seedSchedule();
        storeRepository.save(store,store.getId());
    }

    /**
     * For a specific order, in a given store, look for the cook who's responsible for its preparation
     * @param order the order we're looking for its cook
     * @param store the store which the cook and the order are existing in
     * @return the cook
     */
    public Cook hasOrder(Order order, Store store){
        if(store==null){
            throw new InvalidStoreException("Store is null");
        }
        for(Cook cook : store.getCooks()){
            if(cook.hasOrder(order)) return cook;
        }
        return null;
    }

    /**
     * This method allows retrieving all stores in the repo
     * @return
     */
    public List<Store> getStores(){
        return (List<Store>) storeRepository.findAll();
    }

    /**
     * This method allows retrieving all stores in the repo selected by id
     * @return
     */
    public Store getStore(UUID id){
        return storeRepository.findById(id).get();
    }

    public boolean addStore(Store store){
            storeRepository.save(store,store.getId());
            return true;
    }

    /**
     * Remove the given order from the preparation list of a particular store
     * @param order to remove
     * @param store in which store
     * @return true if the deleting is succeeded
     */
    public boolean remove(Order order, Store store){
        for(Cook cook: store.getCooks()){
            if(cook.hasOrder(order)) {
                cook.removeOrder(order);
                return true;
            }
        }
        return false;
    }

    /**
     * This method allows the store schedule update process per day given a :
     * @param store the store to update its schedule
     * @param timeSlots the new timeslots to put
     * @param day the day of the week to update
     * @return the new timeslot if the process succeeded
     */
    @Override
    public List<TimeSlot> updateStoreSchedule(Store store, List<TimeSlot> timeSlots, DayOfWeek day){
        if (storeRepository.findById(store.getId()).isEmpty()) throw new InvalidStoreException("Store Specified does not exist");
        return store.setSchedule(timeSlots,day);
    }

    /**
     * This method allows updating a store schedule by changing the whole schedule by a new one
     * @param store the store to update
     * @param schedule the new schedule to put in
     */
    @Override
    public void updateStoreSchedule(Store store, Schedule schedule){
        if (storeRepository.findById(store.getId()).isEmpty()) throw new InvalidStoreException("Store Specified does not exist");
        store.setSchedule(schedule);
    }

    /**
     * Update the cook schedule by a new one
     * @param cook the cook which schedule we'll be updated
     * @param schedule the new schedule to put in
     */
    @Override
    public void updateCookSchedule(Cook cook, Schedule schedule){
        Iterable<Store> stores = storeRepository.findAll();
        Store store = null;
        for(Store st : stores){
            if(st.getCooks().contains(cook)){
                store = st;
                break;
            }
        }
        if (store == null) throw new InvalidStoreException("Store Specified does not exist");
        if (!store.getSchedule().hasWithinIt(schedule)) throw new InvalidScheduleException("Incompatible schedule");
        cook.setSchedule(schedule);
    }
}
