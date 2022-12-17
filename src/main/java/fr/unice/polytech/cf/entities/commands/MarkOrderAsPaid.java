package fr.unice.polytech.cf.entities.commands;

import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.enums.EOrderStatus;
import fr.unice.polytech.cf.exceptions.InvalidOrderStatusUpdateException;
import fr.unice.polytech.cf.interfaces.OrderCommand;

public class MarkOrderAsPaid implements OrderCommand {

    /**
     * Mark an order as paid
     *
     * @param order
     */
    public void execute(Order order) {
        if (order.getStatus().equals(EOrderStatus.PENDING))
            order.setStatus(EOrderStatus.PAID);
        else
            throw new InvalidOrderStatusUpdateException("Invalid Operation of Order Status Update");
    }
}