package fr.unice.polytech.cf;

import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;
import fr.unice.polytech.cf.OrderService.Exceptions.InvalidOrderStatusUpdateException;
import fr.unice.polytech.cf.OrderService.Exceptions.OrderNotFoundException;
import fr.unice.polytech.cf.OrderService.OrderService;
import fr.unice.polytech.cf.StoreService.Entities.OrdersStatusUpdater;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class ObsoleteOrders {

    private CustomerSystem customerSystem1;

    private Order orderTwo;
    private Order orderThree;

    private OrdersStatusUpdater ordersStatusUpdater;

    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM hh:mm:ss", Locale.ENGLISH);

    @Given("a prepared order")
    public void aPreparedOrder() throws OrderNotFoundException, InvalidOrderStatusUpdateException {
        customerSystem1 = new CustomerSystem();
        Order orderOne = customerSystem1.startOrder();
        orderTwo = customerSystem1.startOrder();
        customerSystem1.orderService.updateStatus(orderOne, EOrderStatus.PREPARED);
        customerSystem1.orderService.updateStatus(orderTwo, EOrderStatus.PREPARED);
        assertEquals(orderOne.getStatus(), EOrderStatus.PREPARED);
    }

    @Given("an order with a {string}")
    public void anOrderWithA(String retrievalDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM hh:mm:ss", Locale.ENGLISH);
        Date date = formatter.parse(retrievalDate);
        customerSystem1.orderService.getOrders(EOrderStatus.PREPARED).get(0).setRetrievalDateTime(date);
        customerSystem1.orderService.getOrders(EOrderStatus.PREPARED).get(1).setRetrievalDateTime(date);
    }

    @When("the scheduler filters out the prepared orders at the {string} that exceeded the maximum time to be withdrawn which is two hours")
    public void theSchedulerFiltersOutThePreparedOrdersAtTheThatExceededTheMaximumTimeToBeWithdrawnWhichIsTwoHours(String currentDate) throws ParseException {
        Date date = formatter.parse(currentDate);
        assertEquals(customerSystem1.orderService.getOrders(EOrderStatus.PREPARED).size(), 2);
        ordersStatusUpdater = new OrdersStatusUpdater(customerSystem1.orderService.getOrders(EOrderStatus.PREPARED));
        ordersStatusUpdater.updatePreparedOrdersStatus(date);
    }

    @Then("the order status is set as OBSOLETE")
    public void theOrderStatusIsSetAsOBSOLETE() {
        assertEquals(customerSystem1.orderService.getOrders(EOrderStatus.PREPARED).size(), 0);
        assertEquals(customerSystem1.orderService.getOrders(EOrderStatus.OBSOLETE).size(), 2);
    }

    @When("the scheduler filters out the prepared orders at the {string} that have not yet exceeded the maximum time to be withdrawn which is two hours")
    public void theSchedulerFiltersOutThePreparedOrdersAtTheThatHaveNotYetExceededTheMaximumTimeToBeWithdrawnWhichIsTwoHours(String currentDate) throws ParseException {
        Date date = formatter.parse(currentDate);
        assertEquals(customerSystem1.orderService.getOrders(EOrderStatus.PREPARED).size(), 2);
        ordersStatusUpdater = new OrdersStatusUpdater(customerSystem1.orderService.getOrders(EOrderStatus.PREPARED));
        ordersStatusUpdater.updatePreparedOrdersStatus(date);
    }

    @Then("the order status is still PREPARED")
    public void theOrderStatusIsStillPREPARED() {
        assertEquals(customerSystem1.orderService.getOrders(EOrderStatus.PREPARED).size(), 2);
        assertEquals(customerSystem1.orderService.getOrders(EOrderStatus.OBSOLETE).size(), 0);
    }
}
