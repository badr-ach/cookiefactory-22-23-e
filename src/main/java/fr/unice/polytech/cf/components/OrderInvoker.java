package fr.unice.polytech.cf.components;

import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.interfaces.OrderCommand;
import org.springframework.stereotype.Component;

@Component
public class OrderInvoker {
    private OrderCommand command;

    public OrderInvoker() {
    }
    
    public OrderInvoker(OrderCommand command) {
        this.command = command;
    }

    public void invoke(Order order) {
        command.execute(order);
    }

    public void setCommand(OrderCommand command) {
        this.command = command;
    }
}
