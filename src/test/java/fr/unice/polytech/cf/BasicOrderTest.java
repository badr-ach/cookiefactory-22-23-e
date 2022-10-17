package fr.unice.polytech.cf;

import fr.unice.polytech.cf.AccountService.Entities.ContactCoordinates;
import fr.unice.polytech.cf.CookieService.Entities.Cookie;
import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Entities.OrderItem;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;

public class BasicOrderTest {

    private Order order;
    private ContactCoordinates contactCoordinates;

    private Cookie cookie;


    @Given("a newly created order")
    public void aNewlyCreatedOrder() {
        order = new Order();
    }

    @And("a customer named {string}")
    public void aCustomerNamed(String name) {
        contactCoordinates = new ContactCoordinates(name);
    }

    @And("a cookie named {string} priced {double}")
    public void aCookieNamedPriced(String name, double price) {
        cookie = new Cookie(name,price,new ArrayList<>());
    }

    @When("the customer adds the cookie")
    public void theCustomerAddsTheCookie() {
        order.addItem(cookie);
    }

    @Then("a new order item is added to the order")
    public void aNewOrderItemIsAddedToTheOrder() {
        assert order.getOrderItems().contains(new OrderItem(cookie));
    }

    @Then("the order item quantity is {int}")
    public void theOrderItemQuantityIs(int quantity) {
        assert order.getOrderItems().get(0).getQuantity() == quantity;
    }

    @Given("the order already contains the cookie to be added")
    public void theOrderAlreadyContainsTheCookieToBeAdded() {
        order.addItem(cookie);
    }

    @When("the customer adds the same cookie")
    public void theCustomerAddsTheSameCookie() {
        order.addItem(cookie);
    }

    @Then("a new order item is not added to the order")
    public void aNewOrderItemIsNotAddedToTheOrder() {
        assert order.getOrderItems().size() == 1;
    }

    @Given("the order contains two cookies")
    public void theOrderContainsTwoCookies() {
        order.addItem(cookie);
        order.addItem(cookie);
    }

    @When("the customer verifies his cart")
    public void theCustomerVerifiesHisCart() {

    }

    @Then("the calculated price is equal to {double}")
    public void theCalculatedPriceIsEqualTo(double price) {
        assert order.getPrice() == price;
    }

}
