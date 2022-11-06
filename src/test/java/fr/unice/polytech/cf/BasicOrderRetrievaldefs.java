package fr.unice.polytech.cf;

import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;
import fr.unice.polytech.cf.OrderService.Exceptions.InvalidOrderStatusUpdateException;
import fr.unice.polytech.cf.OrderService.Exceptions.OrderNotFoundException;
import fr.unice.polytech.cf.OrderService.OrderService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BasicOrderRetrievaldefs {

    OrderService orderService;
    List<Order> preparedOrders = new ArrayList<>();

    List<Order> filteredOrders = new ArrayList<>();
    String orderId;
    Optional<Order> maybeOrder;
    Order order;

    Exception caughtException;

    @Given("an order service")
    public void anOrderService() {
        orderService = new OrderService();
    }

    @Given("an order with order id {int} and status {string}")
    public void anOrderWithOrderIdOrderIdAndStatus(int orderId, String status) {
        Order order = orderService.startOrder();
        order.setId(orderId);
        order.setStatus(EOrderStatus.valueOf(status));
    }

    @When("the orders are filtered by status {string}")
    public void theOrdersAreFilteredByStatus(String status) {
        EOrderStatus orderStatus = EOrderStatus.valueOf(status);
        this.filteredOrders = orderService.getOrders(orderStatus);
    }

    @Then("{int} orders are found with order id: {int}")
    public void ordersAreFoundWithOrderId(int numberOrders, int orderId) {
        Stream<Order> filteredById = this.filteredOrders.stream().filter(order -> order.getId() == orderId);
        assertTrue(filteredById.count() == numberOrders);
    }

    @When("the order id: {int} is searched")
    public void theOrderIdIsSearched(int orderId) {
        maybeOrder = orderService.getOrder(orderId);
    }

    @Then("{int} orders are found")
    public void OrdersAreFound(int numberOrdersFound) {
        if(numberOrdersFound == 1){
            assertTrue(maybeOrder.isPresent());
        } else {
            assertTrue(maybeOrder.isEmpty());
        }
    }

    @When("the order with order id {int} status is set to {string}")
    public void theOrderWithOrderIdStatusIsSetTo(int orderId, String status) {
        Order order = orderService.getOrder(orderId).orElseThrow();
        EOrderStatus orderStatus = EOrderStatus.valueOf(status);
        try {
            orderService.updateStatus(order, orderStatus);
        } catch (OrderNotFoundException e) {
            this.caughtException = e;
        } catch (InvalidOrderStatusUpdateException e) {
            this.caughtException = e;
        }
    }

    @Then("the order with order id {int} status is {string}")
    public void theOrderWithOrderIdOrderIdStatusIs(int orderId, String status) {
        Order order = orderService.getOrder(orderId).orElseThrow();
        EOrderStatus orderStatus = EOrderStatus.valueOf(status);
        assertTrue(order.getStatus() == orderStatus);
    }
}
