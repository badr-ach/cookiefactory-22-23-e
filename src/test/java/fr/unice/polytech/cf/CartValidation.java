package fr.unice.polytech.cf;

import fr.unice.polytech.cf.AccountService.Entities.ContactCoordinates;
import fr.unice.polytech.cf.CookieService.Entities.Cookie;
import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Entities.Receipt;
import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;
import fr.unice.polytech.cf.OrderService.PaymentService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;

public class CartValidation {
  private Order order;
  private Cookie cookie;
  private PaymentService paymentService;
  private ContactCoordinates contactCoordinates;
  private Receipt receipt;

  @Given("a payment service")
  public void aPaymentService() {
    paymentService = new PaymentService();
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
    order = new Order();
    order.setContact(contactCoordinates);
    for (int i = 0; i < numberCookie; i++) {
      order.addItem(cookie);
    }
  }

  @When("the customer pays the order with credit card number {string}")
  public void theCustomerPaysTheOrderWithCreditCardNumber(String cardNumber) {
    receipt = paymentService.makePayment(cardNumber, order);
  }
  @Then("the customer receives the receipt")
  public void theCustomerReceivesTheReceipt() {
    assert receipt.getCustomerName().equals(contactCoordinates.getName());
  }

  @Then("the order status is payed")
  public void theOrderStatusIsPayed() {
    assert order.getStatus() == EOrderStatus.PAYED;
  }


}
