package fr.unice.polytech.cf;

import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;
import fr.unice.polytech.cf.OrderService.Exceptions.OrderNotFoundException;
import fr.unice.polytech.cf.OrderService.OrderService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BasicOrderRetrievaldefs {

    OrderService orderService = new OrderService();
    List<Order> preparedOrders = new ArrayList<>();
    String orderId;
    Optional<Order> maybeOrder;
    Order order;

    @Given("a list of prepared orders")
    public void aListOfPreparedOrders() {
        Order one = orderService.startOrder();
        Order two = orderService.startOrder();
        one.setStatus(EOrderStatus.PREPARED);
        one.setStatus(EOrderStatus.PREPARED);
        preparedOrders = orderService.getOrders(EOrderStatus.PREPARED);
    }

    @Given("an {string} to be retrieved")
    public void anToBeRetrieved(String id) {
        this.orderId = id;
    }

    @When("the order is looked up in the prepared order list")
    public void theOrderIsLookedUpInThePreparedOrderList() {
        this.maybeOrder = preparedOrders.stream().filter(o -> o.getId() == Integer.parseInt(orderId)).findFirst();
    }

    @Then("the prepared order exists")
    public void thePreparedOrderExists() {
        assert(maybeOrder.isPresent());
        this.order = maybeOrder.get();
    }

    @Then("the order is not found")
    public void theOrderIsNotFound() {
        assert(maybeOrder.isEmpty());
    }

    @Then("an error is thrown")
    public void anErrorIsDisplayedSayingThatTheOrderIsNotFoundOrNotPreparedYet() {
        assertThrows(OrderNotFoundException.class,()->orderService.updateStatus(order,EOrderStatus.FULFILLED));
    }

    @Given("a valid {string}")
    public void aValid(String id) {
        Optional<Order> order = preparedOrders.stream().filter(o -> o.getId() == Integer.parseInt(id)).findFirst();
        assert(order.isPresent());
        this.order = order.get();
    }

    @When("the store employee marks the order as fulfilled")
    public void theStoreEmployeeMarksTheOrderAsFulfilled() {
        assertDoesNotThrow(() -> orderService.updateStatus(this.order,EOrderStatus.FULFILLED));
    }

    @Then("the status of the order is changed to fulfilled")
    public void theStatusOfTheOrderIsChangedToFulfilled() {
        Optional<Order> order = preparedOrders.stream().filter(o -> o.equals(this.order)).findFirst();
        assert(order.isPresent() && order.get().getStatus().equals(EOrderStatus.FULFILLED));
    }


    @Then("the order is removed from the prepared orders list")
    public void theOrderIsRemovedFromThePreparedOrdersList() {
        preparedOrders.remove(this.order);
    }

    @Then("the orders list no longer contains it")
    public void theOrdersListNoLongerContainsIt(){
        assert(preparedOrders.stream().noneMatch(o -> o.equals(this.order)));
    }
}
