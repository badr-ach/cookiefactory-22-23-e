package fr.unice.polytech.cf;

import fr.unice.polytech.cf.orderservice.entities.Order;
import fr.unice.polytech.cf.orderservice.enums.EOrderStatus;
import fr.unice.polytech.cf.orderservice.OrderService;
import fr.unice.polytech.cf.orderservice.PaymentService;
import fr.unice.polytech.cf.storeservice.StoreService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderRetrieval {

    OrderService orderService;
    List<Order> filteredOrders = new ArrayList<>();
    Optional<Order> maybeOrder;

    Exception caughtException;

    @Given("an order service")
    public void anOrderService() {
        orderService = new OrderService(new PaymentService(),new StoreService());
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
        assertEquals(filteredById.count(), numberOrders);
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

    @When("the order with order id {int} status is marked as FULFILLED")
    public void theOrderWithOrderIdStatusIsSetTo(int orderId) {
        Order order = orderService.getOrder(orderId).orElseThrow();
        try {
            orderService.markOrderAsFulfilled(order);
        }catch(Exception ex){
            caughtException = ex;
        }
    }

    @Then("the order with order id {int} status is {string}")
    public void theOrderWithOrderIdOrderIdStatusIs(int orderId, String status) {
        Order order = orderService.getOrder(orderId).orElseThrow();
        EOrderStatus orderStatus = EOrderStatus.valueOf(status);
        assertSame(order.getStatus(), orderStatus);
    }

    @Then("an error message is thrown with message {string}")
    public void anErrorMessageIsThrownWithMessage(String errMsg) {
        assertEquals(errMsg, caughtException.getMessage());
    }
}
