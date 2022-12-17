package fr.unice.polytech.cf.entities.commands;

import fr.unice.polytech.cf.components.StoreComponent;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.Store;
import fr.unice.polytech.cf.entities.enums.EOrderStatus;
import fr.unice.polytech.cf.exceptions.InvalidOrderStatusUpdateException;
import fr.unice.polytech.cf.interfaces.OrderCommand;
import fr.unice.polytech.cf.interfaces.StoreOrdersModifier;

public class MarkOrderAsPrepared implements OrderCommand {
    private final StoreOrdersModifier storeOrdersModifier;

    public MarkOrderAsPrepared(StoreOrdersModifier storeOrdersModifier) {
        this.storeOrdersModifier = storeOrdersModifier;
    }

    public void execute(Order order) {
        if (order.getStatus().equals(EOrderStatus.IN_PREPARATION)) {
            storeOrdersModifier.remove(order, order.getStore());
            order.setStatus(EOrderStatus.PREPARED);
        } else throw new InvalidOrderStatusUpdateException("Invalid Operation of Order Status Update");
    }
}