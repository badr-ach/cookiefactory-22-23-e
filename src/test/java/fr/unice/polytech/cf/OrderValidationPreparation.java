package fr.unice.polytech.cf;

import fr.unice.polytech.cf.orderservice.entities.Order;
import fr.unice.polytech.cf.orderservice.enums.EOrderStatus;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;

public class OrderValidationPreparation {
    private Order order;
    @Given("an order")
    public void anOrder() {
        order = new Order();
    }

    @When("the order is marked as prepared")
    public void theOrderIsMarkedAsPrepared() {
        order.setStatus(EOrderStatus.PREPARED);
    }

    @Then("the order status is PREPARED")
    public void theOrderStatusIsPREPARED() {
        assertEquals(order.getStatus(),EOrderStatus.PREPARED);
    }
}
