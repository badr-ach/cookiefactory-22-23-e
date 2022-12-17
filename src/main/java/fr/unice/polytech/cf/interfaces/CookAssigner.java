package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Cook;
import fr.unice.polytech.cf.entities.Order;

public interface CookAssigner {
    public Cook assignOrderToACook(Order order);
}

