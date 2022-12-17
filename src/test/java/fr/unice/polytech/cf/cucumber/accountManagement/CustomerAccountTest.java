package fr.unice.polytech.cf.cucumber.accountManagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import fr.unice.polytech.cf.components.AccountRegistry;
import fr.unice.polytech.cf.entities.*;
import fr.unice.polytech.cf.repositories.AccountRepository;
import fr.unice.polytech.cf.repositories.OrderRepository;
import fr.unice.polytech.cf.repositories.StoreRepository;
import fr.unice.polytech.cf.services.CustomerService;
import fr.unice.polytech.cf.entities.enums.EOrderStatus;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class CustomerAccountTest {

    @Autowired
    private CustomerService customerSystem;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private StoreRepository storeRepository;

    private Order order;
    private Receipt receipt;
    private Exception caughtException;
    private CustomerAccount loggedInAccount;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Before
    public void setContext(){
        Iterable<Store> stores = storeRepository.findAll();
        for(Store store : stores){
            List<Cook> cookList = store.getCooks();
            for(Cook cook : cookList){
                cook.setAssignments(new TreeMap<>());
            }
        }
        orderRepository.deleteAll();
        accountRepository.deleteAll();
        storeRepository.deleteAll();
        Store store = new Store();
        storeRepository.save(store,store.getId());
    }

    @Given(
            "an existing account with username: {string}, password: {string} and customer name: {string}")
    public void anExistingAccountWithUsernamePasswordAndCustomerName(
            String username, String password, String customerName) {
        aNewAccountIsCreatedWithUsernamePasswordAndCustomerName(username, password, customerName);
    }

    @When("A new account is created with username: {string}, password: {string} and customer name:"
            + " {string}")
    public void aNewAccountIsCreatedWithUsernamePasswordAndCustomerName(
            String username, String password, String customerName) {
        ContactCoordinates contactCoordinates = new ContactCoordinates(customerName, customerName, customerName, customerName);
        try {
            customerSystem.signup(username, password, contactCoordinates);
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Then("the account with username: {string} is present {int} times")
    public void theAccountWithUsernameIsPresentNumberOfExistingAccountsTimes(
            String username, int expectedNumberOfAccounts) {

        Iterable<Account> accounts = accountRepository.findAll();
        int numberOfAccounts = 0;

        for (Account account : accounts) {
            if (account.getUsername().equals(username)) {
                numberOfAccounts++;
            }
        }

        assertEquals(expectedNumberOfAccounts, numberOfAccounts);
    }

    @And("the customer is not logged in")
    public void theCustomerIsNotLoggedIn() {
        assertNull(loggedInAccount);
    }

    @When("a customer logs in with username: {string}, password: {string}")
    public void aCustomerLogsInWithUsernamePassword(String username, String password) {
        try {
            loggedInAccount = customerSystem.login(username, password);
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Then("the customer is logged in with {string} {string}")
    public void theCustomerIsLoggedInWith(String username, String password) {
        assertEquals(username, loggedInAccount.getUsername());
        assertEquals(password, loggedInAccount.getPassword());
    }

    @Then("an runtime exception is thrown with message {string}")
    public void anRuntimeExceptionIsThrownWithMessage(String message) {
        assertEquals(message, caughtException.getMessage());
    }

    @Then("the order succeeds")
    public void theOrderSucceeds() {
        Order orderInSystem = orderRepository.findById(order.getId()).orElse(null);
        assertNotNull(orderInSystem);
        assertEquals(EOrderStatus.PAID, orderInSystem.getStatus());
    }

    @When("a logged in customer pays his order with {string}")
    public void aLoggedInCustomerPaysHisOrderWith(String creditCard) {
        try {
            receipt = customerSystem.payOrder(loggedInAccount, creditCard,order);

        } catch (Exception e) {
            caughtException = e;
        }
    }

    @And("an order awaiting payment")
    public void anOrderAwaitingPayment() {
        Store store = customerSystem.getStores().get(0);
        order = customerSystem.startOrder();

        LocalDateTime pickupDate = LocalDateTime.of(2022, Calendar.DECEMBER, 6, 14, 15);

        customerSystem.addCookie(new Cookie("Chocolala", 10, new HashMap<>(), Duration.of(10, ChronoUnit.MINUTES)),order);
        customerSystem.selectPickUpDate(pickupDate,order);
        customerSystem.selectStore(store,order);
    }

    @And("a logged in customer with username: {string}, password: {string}")
    public void aLoggedInCustomerWithUsernamePassword(String username, String password) {
        aCustomerLogsInWithUsernamePassword(username, password);
    }

    @And("no logged in account")
    public void noLoggedInAccount() {
        loggedInAccount = null;
    }

    @Then("the order is in the account order history")
    public void theOrderIsInTheAccountOrderHistory() {
        System.out.println("Order history: " + loggedInAccount.getHistory().size());
        assertTrue(loggedInAccount.getHistory().contains(order));
    }

    @Then("the order is not in the account order history")
    public void theOrderIsNotInTheAccountOrderHistory() {
        assertFalse(loggedInAccount.getHistory().contains(order));
    }

    @And("an order of {int} cookies priced {double}")
    public void anOrderOfCookieNumberCookiesPricedPrice(int cookieNumber, double price) {
        Store store = customerSystem.getStores().get(0);
        order = customerSystem.startOrder();
        LocalDateTime pickupDate = LocalDateTime.of(2022, Calendar.DECEMBER, 6, 14, 15);
        Cookie chocolalala = new Cookie("Chocolala", price, new HashMap<>(), Duration.of(10, ChronoUnit.MINUTES));
        for (int i = 0; i < cookieNumber; i++) {
            customerSystem.addCookie(chocolalala,order);
        }
        customerSystem.selectPickUpDate(pickupDate,order);
        customerSystem.selectStore(store,order);
    }

    @Then("The order price is {double}")
    public void theOrderPriceIsDiscountedPrice(double discountedPrice) {
        double paidAmount = receipt.getTotal();
        assertEquals(discountedPrice, paidAmount, 0.0);
    }

    @And("the customer made a previous order of {int} cookies for the {string}")
    public void theCustomerMadeAPreviousOrderOfCookies(int numberCookies, String currentDateText) {
        Store store = customerSystem.getStores().get(0);
        order = customerSystem.startOrder();

        LocalDateTime pickupDate = LocalDateTime.parse(currentDateText, formatter);
        Cookie chocolalala = new Cookie("Chocolala", 10, new HashMap<>(), Duration.of(10, ChronoUnit.MINUTES));
        for (int i = 0; i < numberCookies; i++) {
            customerSystem.addCookie(chocolalala,order);
        }
        customerSystem.selectPickUpDate(pickupDate,order);
        customerSystem.selectStore(store,order);
        aLoggedInCustomerPaysHisOrderWith("123456789");
    }

    @Given("an existing account with username: {string}, password: {string} and customer name: {string} subscribed to the loyalty program")
    public void anExistingAccountWithUsernamePasswordAndCustomerNameAndLoyaltyProgram(String username, String password, String name) {
        anExistingAccountWithUsernamePasswordAndCustomerName(username, password, name);
        customerSystem.login(username, password).setHasLoyaltyProgram(true);
    }
}
