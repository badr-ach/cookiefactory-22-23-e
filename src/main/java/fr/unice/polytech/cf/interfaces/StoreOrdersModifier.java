package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.Store;

public interface StoreOrdersModifier {
    public  boolean remove(Order order, Store store);
}
