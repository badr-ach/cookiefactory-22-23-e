package fr.unice.polytech.cf.cucumber.toGoodToGo;

import fr.unice.polytech.cf.components.OrderComponent;
import fr.unice.polytech.cf.services.CustomerService;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.enums.EOrderStatus;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class ObsoleteOrders {
@Autowired
    private CustomerService customerSystem;
@Autowired
private OrderComponent orderComponent;
    private Order testedOrder;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Given("an order with a {string}")
    public void anOrderWithA(String retrievalDateText) throws ParseException {
        LocalDateTime retrievalDate = LocalDateTime.parse(retrievalDateText, formatter);
        testedOrder = orderComponent.startOrder();
        testedOrder.setStatus(EOrderStatus.PREPARED);
        testedOrder.setRetrievalDateTime(retrievalDate);
    }

    @Then("the order status is set as OBSOLETE")
    public void theOrderStatusIsSetAsOBSOLETE() {
        assertEquals(EOrderStatus.OBSOLETE, testedOrder.getStatus());
    }

    @Then("the order status is still PREPARED")
    public void theOrderStatusIsStillPREPARED() {
        assertEquals(EOrderStatus.PREPARED, testedOrder.getStatus());
    }

    @When("the order status is updated with {string}")
    public void theOrderStatusIsUpdatedWith(String currentDateText) {
        LocalDateTime currentDate = LocalDateTime.parse(currentDateText, formatter);
        orderComponent.updateOrdersStatus(currentDate);
    }
}
