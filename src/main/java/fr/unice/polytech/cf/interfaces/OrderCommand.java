package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Order;

public interface OrderCommand {
    void execute(Order order);
}
