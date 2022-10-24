package fr.unice.polytech.cf;

import fr.unice.polytech.cf.AccountService.Entities.ContactCoordinates;
import fr.unice.polytech.cf.CookieService.Entities.Cookie;
import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Entities.Receipt;
import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;
import fr.unice.polytech.cf.OrderService.PaymentService;
import fr.unice.polytech.cf.StoreService.Entities.Cook;
import fr.unice.polytech.cf.StoreService.Entities.Store;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;

public class OrderValidation {
  private Order order;
  private Cookie cookie;
  private CustomerSystem customerSystem;
  private ContactCoordinates contactCoordinates;
  private Receipt receipt;


  @Given("a CustomerSystem")
  public void aCustomerSystem() {
    customerSystem = new CustomerSystem();
  }

  @And("a customer with name {string}")
  public void aCustomerWithName(String name) {
    contactCoordinates = new ContactCoordinates(name);
  }
  @And("a {string} Cookie priced {double}")
  public void aCookiePriced(String name, double price) {
    cookie = new Cookie(name, price, new ArrayList<>());
  }

  @And("an Order with {int} Cookie")
  public void anOrderWithCookie(int numberCookie) {
    ArrayList<Cookie> cookies = new ArrayList<Cookie>();
    for (int i = 0; i < numberCookie; i++) {
      cookies.add(cookie);
    }
    System.out.println("test");
    order = customerSystem.createOrder(contactCoordinates, cookies);
    ArrayList<Store> stores = customerSystem.getStores();
    Store store = stores.get(0);
    customerSystem.selectStore(contactCoordinates, store);
  }

  @When("the customer pays the order with credit card number {string}")
  public void theCustomerPaysTheOrderWithCreditCardNumber(String cardNumber) {
    receipt = customerSystem.payOrder(contactCoordinates, cardNumber);
  }

  @Then("the order status is payed")
  public void theOrderStatusIsPayed() {
    assert order.getStatus() == EOrderStatus.PAYED;
  }

  @Then("the order is assigned to a Cook")
  public void theOrderIsAssignedToACook() {
    Store store = order.getStore();
    Cook cook = store.getAssignedCook(order);
    assert cook != null;
  }

  @Then("the order status is PENDING")
  public void theOrderStatusIsPENDING() {
    assert order.getStatus() == EOrderStatus.PENDING;
  }

  @Then("the receipt is correct")
  public void theReceiptIsCorrect() {
    assert receipt.getCustomerName().equals(contactCoordinates.getName());
    assert receipt.getPrice() == order.getPrice();
    assert receipt.getOrderId() == order.getId();
  }
}
