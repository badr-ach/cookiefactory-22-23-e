package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.enums.EOrderStatus;

import java.util.List;

public interface OrderFinder {

    List<Order> getOrders();
    List<Order> getOrders(EOrderStatus status);

}
