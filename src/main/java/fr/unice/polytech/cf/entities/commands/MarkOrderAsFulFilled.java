package fr.unice.polytech.cf.entities.commands;

import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.enums.EOrderStatus;
import fr.unice.polytech.cf.exceptions.InvalidOrderStatusUpdateException;
import fr.unice.polytech.cf.interfaces.OrderCommand;

public class MarkOrderAsFulFilled implements OrderCommand {

    public MarkOrderAsFulFilled() {
    }

    public void execute(Order order) {
        if (order.getStatus().equals(EOrderStatus.PREPARED))
            order.setStatus(EOrderStatus.FULFILLED);
        else
            throw new InvalidOrderStatusUpdateException("Invalid Operation of Order Status Update");
    }
}