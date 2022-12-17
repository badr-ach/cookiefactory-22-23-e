package fr.unice.polytech.cf.entities.commands;

import fr.unice.polytech.cf.components.IngredientStockComponent;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.enums.EOrderStatus;
import fr.unice.polytech.cf.exceptions.InvalidOrderStatusUpdateException;
import fr.unice.polytech.cf.interfaces.IngredientStockModifier;
import fr.unice.polytech.cf.interfaces.IngredientStockReserver;
import fr.unice.polytech.cf.interfaces.OrderCommand;

public class MarkOrderAsInPreparation implements OrderCommand {
    private final IngredientStockReserver ingredientStockReserver;

    public MarkOrderAsInPreparation(IngredientStockReserver ingredientStockReserver) {
        this.ingredientStockReserver = ingredientStockReserver;
    }

    public void execute(Order order) {
        if (order.getStatus().equals(EOrderStatus.PAID)) {
            ingredientStockReserver.consumeFromStock(order.getStore(), order.getNecessaryIngredients());
            order.setStatus(EOrderStatus.IN_PREPARATION);
        } else throw new InvalidOrderStatusUpdateException("Invalid Operation of Order Status Update");
    }
}