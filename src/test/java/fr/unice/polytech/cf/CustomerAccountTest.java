package fr.unice.polytech.cf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import fr.unice.polytech.cf.accountservice.entities.ContactCoordinates;
import fr.unice.polytech.cf.accountservice.entities.CustomerAccount;
import fr.unice.polytech.cf.cookieservice.entities.Cookie;
import fr.unice.polytech.cf.orderservice.entities.Order;
import fr.unice.polytech.cf.orderservice.entities.Receipt;
import fr.unice.polytech.cf.orderservice.enums.EOrderStatus;
import fr.unice.polytech.cf.storeservice.entities.Store;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;

public class CustomerAccountTest {

  Exception caughtException;
  private CustomerAccount loggedInAccount;
  private CustomerSystem customerSystem;
  private Order order;
  private Receipt receipt;

  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  @Given("a customer system")
  public void aCustomerSystem() {
    customerSystem = new CustomerSystem();
  }

  @Given(
      "an existing account with username: {string}, password: {string} and customer name: {string}")
  public void anExistingAccountWithUsernamePasswordAndCustomerName(
      String username, String password, String customerName) {
    aNewAccountIsCreatedWithUsernamePasswordAndCustomerName(username, password, customerName);
  }

  @When(
      "A new account is created with username: {string}, password: {string} and customer name:"
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
    int numberOfAccounts =
        (int)
            customerSystem.accountService.getAccounts().stream()
                .filter(account -> account.getUsername().equals(username))
                .count();
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
    Order orderInSystem = customerSystem.orderService.getOrder(order.getId()).orElse(null);
    assertNotNull(orderInSystem);
    assertEquals(EOrderStatus.PAID, orderInSystem.getStatus());
  }

  @When("a logged in customer pays his order with {string}")
  public void aLoggedInCustomerPaysHisOrderWith(String creditCard) {
    try {
      receipt = customerSystem.payOrder(loggedInAccount, creditCard);

    } catch (Exception e) {
      caughtException = e;
      System.out.println(e.getMessage());
    }
  }

  @And("an order awaiting payment")
  public void anOrderAwaitingPayment() {
    Store store = customerSystem.getStores().get(0);
    order = customerSystem.startOrder();

    LocalDateTime pickupDate = LocalDateTime.of(2022, Calendar.DECEMBER, 6, 14, 15);

    customerSystem.addCookie(
        new Cookie("Chocolala", 10, new HashMap<>(), Duration.of(10, ChronoUnit.MINUTES)));
    customerSystem.selectPickUpDate(pickupDate);
    customerSystem.selectStore(store);
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
    for (int i = 0; i <cookieNumber ; i++) {
      customerSystem.addCookie(chocolalala);
    }
    customerSystem.selectPickUpDate(pickupDate);
    customerSystem.selectStore(store);
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
    for (int i = 0; i <numberCookies ; i++) {
      customerSystem.addCookie(chocolalala);
    }
    customerSystem.selectPickUpDate(pickupDate);
    customerSystem.selectStore(store);
    aLoggedInCustomerPaysHisOrderWith("123456789");
  }

  @Given("an existing account with username: {string}, password: {string} and customer name: {string} subscribed to the loyalty program")
  public void anExistingAccountWithUsernamePasswordAndCustomerNameAndLoyaltyProgram(String username, String password, String name) {
    anExistingAccountWithUsernamePasswordAndCustomerName(username, password, name);
    customerSystem.login(username, password).setHasLoyaltyProgram(true);
  }
}
