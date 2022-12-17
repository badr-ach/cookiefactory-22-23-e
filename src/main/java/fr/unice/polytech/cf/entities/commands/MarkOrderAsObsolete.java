package fr.unice.polytech.cf.entities.commands;

import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.enums.EOrderStatus;
import fr.unice.polytech.cf.exceptions.InvalidOrderStatusUpdateException;
import fr.unice.polytech.cf.interfaces.OrderCommand;


public class MarkOrderAsObsolete implements OrderCommand {

    public MarkOrderAsObsolete() {
    }

    public void execute(Order order) {
        if (order.getStatus().equals(EOrderStatus.PREPARED))
            order.setStatus(EOrderStatus.OBSOLETE);
        else
            throw new InvalidOrderStatusUpdateException("Invalid Operation of Order Status Update");
    }
}