package fr.unice.polytech.cf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import fr.unice.polytech.cf.accountservice.entities.Account;
import fr.unice.polytech.cf.accountservice.entities.ContactCoordinates;
import fr.unice.polytech.cf.orderservice.entities.Order;
import fr.unice.polytech.cf.orderservice.enums.EOrderStatus;
import fr.unice.polytech.cf.storeservice.entities.Store;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.util.Calendar;

public class CustomerAccountTest {

  Exception caughtException;
  private Account loggedInAccount;
  private CustomerSystem customerSystem;
  private Order order;

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
    ContactCoordinates contactCoordinates = new ContactCoordinates(customerName,customerName+"@gmail.com","0710234874","address");
    try {
      customerSystem.signup(username, password, contactCoordinates);
    } catch (Exception e) {
      caughtException = e;
    }
  }

  @Then("the account with username: {string} is present {int} times")
  public void theAccountWithUsernameIsPresentNumberOfExistingAccountsTimes(String username, int expectedNumberOfAccounts) {
    int numberOfAccounts = (int) customerSystem.accountService.getAccounts().stream().filter(account -> account.getUsername().equals(username)).count();
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
      customerSystem.payOrder(loggedInAccount, creditCard);
    } catch (Exception e) {
      caughtException = e;
      System.out.println(e.getMessage());
    }
  }

  @And("an order awaiting payment")
  public void anOrderAwaitingPayment() {
    Store store = customerSystem.getStores().get(0);
    order = customerSystem.startOrder();
    customerSystem.selectPickUpDate(LocalDateTime.of(2022, Calendar.DECEMBER, 6, 14, 15));
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
}
