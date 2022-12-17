package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.ContactCoordinates;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.interfaces.IPastry;

import java.time.LocalDateTime;
import java.util.Map;

public interface OrderModifier {
    Order addCookies(Order order, Map<IPastry, Integer> cookies);

    void attachContactCoordinates(ContactCoordinates contact, Order order);

    void updateOrdersStatus(LocalDateTime currentDate);
    void cancelAnOrder(Order order);
}
