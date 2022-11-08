package fr.unice.polytech.cf;

import fr.unice.polytech.cf.AccountService.Entities.ContactCoordinates;
import fr.unice.polytech.cf.CookieService.Entities.Cookie;
import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Entities.Receipt;
import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;
import fr.unice.polytech.cf.OrderService.OrderScheduler;
import fr.unice.polytech.cf.StoreService.Entities.Cook;
import fr.unice.polytech.cf.StoreService.Entities.Store;
import fr.unice.polytech.cf.StoreService.StoreService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderValidation {
    private CustomerSystem customerSystem;
    private OrderScheduler orderScheduler;
    private ContactCoordinates contactCoordinates;
    private Order order;
    private Exception exception;
    private Receipt receipt;


    @Given("a customer {string} with email {string} and phone number {string} and address {string}")
    public void aCustomerWithEmailAndPhoneNumber(String name, String email, String phone, String address) {
        contactCoordinates = new ContactCoordinates(name, email, phone, address);
    }


    @And("our customer system")
    public void ourCustomerSystem() {
        customerSystem = new CustomerSystem();
    }

    @And("a scheduler")
    public void aScheduler() {
        orderScheduler = new OrderScheduler();
    }

    @Given("an order with missing store information")
    public void anOrderWithMissingStoreInformation() {
        order = customerSystem.createOrder(new Cookie("Chocolala", 10, new ArrayList<>()));
    }

    @When("the customer proceeds to pay his order with {string}")
    public void theCustomerProceedsToPayHisOrderWith(String creditCard) {
        try {
            receipt = customerSystem.payOrder(contactCoordinates, creditCard);
        } catch (Exception ex) {
            exception = ex;
        }
    }

    @Then("order validation fails with an error message {string}")
    public void orderValidationFailsWithAnErrorMessageStoreNotSpecified(String exceptionMsg) {
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Given("an order with missing retrieval date and time")
    public void anOrderWithMissingRetrievalDateAndTime() {
        order = customerSystem.createOrder(new Cookie("Chocolala", 10, new ArrayList<>()));
        customerSystem.selectStore(customerSystem.getStores().get(0));
    }

    @Given("a proper order")
    public void aProperOrder() {
        order = customerSystem.createOrder(new Cookie("Chocolala", 10, new ArrayList<>()));
        customerSystem.selectStore(customerSystem.getStores().get(0));
        customerSystem.selectPickUpDate(new Date(2022, Calendar.DECEMBER, 6, 14, 15));
    }

    @And("and the store it was assigned having gotten an order for the same retrieval time")
    public void andTheStoreItWasAssignedGettingAnOrderForTheSameRetrievalTime() {
        Order concurrentOrder = new Order();
        concurrentOrder.setRetrievalDateTime(order.getRetrievalDateTime());
        concurrentOrder.setStore(order.getStore());
        orderScheduler.assignCook(concurrentOrder);
    }

    @Then("no cook is assigned to the order")
    public void noCookIsAssignedToTheOrder() {
        assertTrue(order.getStore().getAssignedCook(order).isEmpty());
    }


    @And("the order status is pending")
    public void theOrderStatusIsPending() {
        assertEquals(order.getStatus(), EOrderStatus.PENDING);
    }

    @Then("the assigned retrieval time is still available")
    public void theAssignedRetrievalTimeIsStillAvailable() {
        assertTrue(orderScheduler.hasTimeSlotAvailable(order));
    }

    @Then("the available cook is assigned the order")
    public void theAvailableCookIsAssignedTheOrder() {
        assertTrue(order.getStore().getCooks().get(0).hasOrder(order));
    }

    @Then("the order has a cook")
    public void theOrderHasACook() {
        assertTrue(order.getStore().getAssignedCook(order).isPresent());
    }

    @Then("the order status is PAID")
    public void theOrderStatusIsPAID() {
        assertEquals(order.getStatus(), EOrderStatus.PAID);
    }

    @Then("correct receipt is generated")
    public void correctReceiptIsGenerated() {
        assertEquals(receipt.getCustomerName(), contactCoordinates.getName());
        assertTrue(receipt.getPrice() == (order.getPrice()));
        assertEquals(receipt.getOrderId(), order.getId());
    }
}
