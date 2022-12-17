package fr.unice.polytech.cf.cucumber.orderManagement;

import fr.unice.polytech.cf.components.*;
import fr.unice.polytech.cf.entities.commands.MarkOrderAsFulFilled;
import fr.unice.polytech.cf.interfaces.*;
import fr.unice.polytech.cf.repositories.OrderRepository;
import fr.unice.polytech.cf.repositories.StoreRepository;
import fr.unice.polytech.cf.services.CustomerService;
import fr.unice.polytech.cf.entities.ContactCoordinates;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.enums.EOrderStatus;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class OrderRetrieval {

    @Autowired
    private OrderModifier orderModifier;
    @Autowired
    private OrderComponent orderComponent;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderInvoker orderInvoker;

    private Order order;
    private Optional<Order> maybeOrder;
    private Exception caughtException;
    private ContactCoordinates contactCoordinates;
    private List<Order> ordersFilteredByStatus = new ArrayList<>();

    @Autowired
    private CustomerService customerSystem;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @Before
    public void setContext() {
        orderRepository.deleteAll();
    }
    @Given("an order service")
    public void anOrderService() {

    }


    @Given("an order with order id {string} and status {string}")
    public void anOrderWithOrderIdOrderIdAndStatus(String orderId, String status) {
        Order order = orderComponent.startOrder(orderId);

        order.setId(UUID.fromString(orderId));
        order.setStatus(EOrderStatus.valueOf(status));
    }

    @When("the orders are filtered by status {string}")
    public void theOrdersAreFilteredByStatus(String status) {
        EOrderStatus orderStatus = EOrderStatus.valueOf(status);
        this.ordersFilteredByStatus = orderComponent.getOrders(orderStatus);
    }

    @Then("{int} orders are found with order id: {string}")
    public void ordersAreFoundWithOrderId(int numberOrders, String orderId) {
        Stream<Order> filteredById = this.ordersFilteredByStatus.stream().filter(order -> order.getId().equals(UUID.fromString(orderId)));
        assertEquals(filteredById.count(), numberOrders);
    }

    @When("the order id: {string} is searched")
    public void theOrderIdIsSearched(String orderId) {
        maybeOrder = orderRepository.findById(UUID.fromString(orderId));
    }

    @Then("{int} orders are found")
    public void OrdersAreFound(int numberOrdersFound) {
        if(numberOrdersFound == 1){
            assertTrue(maybeOrder.isPresent());
        } else {
            assertTrue(maybeOrder.isEmpty());
        }
    }

    @When("the order with order id {string} status is marked as FULFILLED")
    public void theOrderWithOrderIdStatusIsSetTo(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId)).orElseThrow();
        try {
            orderInvoker.setCommand(new MarkOrderAsFulFilled());
            orderInvoker.invoke(order);
        }catch(Exception ex){
            caughtException = ex;
        }
    }

    @Then("the order with order id {string} status is {string}")
    public void theOrderWithOrderIdOrderIdStatusIs(String orderId, String status) {
        Order order = orderRepository.findById(UUID.fromString(orderId)).orElseThrow();
        EOrderStatus orderStatus = EOrderStatus.valueOf(status);
        assertSame(order.getStatus(), orderStatus);
    }

    @Then("an error message is thrown with message {string}")
    public void anErrorMessageIsThrownWithMessage(String errMsg) {
        assertEquals(errMsg, caughtException.getMessage());
    }

    @And("An order scheduled for pick up at {string} with status {string}")
    public void anOrderScheduledForPickUpAtWithStatus(String retrievalDateText, String currentOrderStatus) {
        LocalDateTime retrievalDate = LocalDateTime.parse(retrievalDateText, formatter);
        order = customerSystem.startOrder();
        if(contactCoordinates != null) order.setContact(contactCoordinates);
        customerSystem.selectStore(customerSystem.getStores().get(0),order);
        EOrderStatus orderStatus = EOrderStatus.valueOf(currentOrderStatus);
        order.setStatus(orderStatus);
        order.setRetrievalDateTime(retrievalDate);
    }

    @Then("the customer received {int} notifications")
    public void theCustomerReceivedNumberOfNotificationsNotifications(int numberNotifications) {
        List<String> sentNotifications = order.getContact().getSentNotifications();
        assertEquals(numberNotifications, sentNotifications.size());
    }

    @When("the order status is updated on the {string}")
    public void theOrderStatusIsUpdatedOnThe(String currentDateText) {
        LocalDateTime currentDate = LocalDateTime.parse(currentDateText, formatter);
        orderModifier.updateOrdersStatus(currentDate);
    }

    @Given("contact coordinates name {string} and email {string}")
    public void contactCoordinatesNameAndEmail(String name, String email) {
        contactCoordinates = new ContactCoordinates(name, email);
    }
}
