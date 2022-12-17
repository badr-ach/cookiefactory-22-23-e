package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Order;
import org.aspectj.weaver.ast.Or;

import java.util.UUID;

public interface OrderCreator {
    Order startOrder();
    Order startOrder(String id);
}
