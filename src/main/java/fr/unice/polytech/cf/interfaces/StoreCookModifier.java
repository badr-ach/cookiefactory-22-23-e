package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Cook;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.Schedule;
import fr.unice.polytech.cf.entities.Store;

public interface StoreCookModifier {
    void updateCookSchedule(Cook cook, Schedule schedule);
    public Cook hasOrder(Order order, Store store);
}
