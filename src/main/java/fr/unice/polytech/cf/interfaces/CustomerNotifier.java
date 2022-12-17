package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Order;

import java.time.Duration;

public interface CustomerNotifier {
    void notifyCustomer(Duration duration, Order order);

    void notifyCustomer(Order order);
}
