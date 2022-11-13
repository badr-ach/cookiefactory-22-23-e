package fr.unice.polytech.cf;

import fr.unice.polytech.cf.cookieservice.entities.Cookie;
import fr.unice.polytech.cf.orderservice.entities.Order;
import fr.unice.polytech.cf.orderservice.entities.OrderItem;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AddCookieToOrder {

    private Order order;

    private HashMap<String, Cookie> cookies = new HashMap<String, Cookie>();

    @Given("an empty order")
    public void anEmptyOrder() {
        order = new Order();
    }

    @And("a cookie named {string} priced {double}")
    public void aCookieNamedPriced(String name, double price) {
        Cookie cookie = new Cookie(name,price,new HashMap<>());
        cookies.putIfAbsent(name, cookie);
    }

    @Then("the order item quantity is {int}")
    public void theOrderItemQuantityIs(int quantity) {
        assertEquals( order.getOrderItems().get(0).getQuantity(), quantity);
    }

    @Then("the calculated price is equal to {double}")
    public void theCalculatedPriceIsEqualTo(double price) {
        assertEquals(price, order.getPrice(), 0.01);
    }

    @Given("a cookie named {string} priced {double} is in the order")
    public void aCookieNamedPricedCookiePriceIsInTheOrder(String name, double price) {
        Cookie cookie = new Cookie(name,price,new HashMap<>());
        cookies.putIfAbsent(name, cookie);
        order.addItem(cookie);
    }

    @Then("a cookie named {string} is in the order")
    public void aCookieNamedIsInTheOrder(String cookieName) {
       boolean hasCookie = order.getOrderItems().stream().anyMatch(orderItem -> orderItem.getCookie().getName().equals(cookieName));
       assertTrue(hasCookie);
    }

    @Then("the number of order items is {int}")
    public void theOrderSizeIs(int number) {
        assertEquals(order.getOrderItems().size(),number);
    }

    @When("a cookie named {string} is added {int} time to the order")
    public void aCookieNamedIsAddedTimeToTheOrder(String name, int numberOfTimes) {
        Cookie cookie = cookies.get(name);
        for (int i = 0; i < numberOfTimes; i++) {
            order.addItem(cookie);
        }
    }

    @Then("the cookie named {string} quantity is {int}")
    public void theCookieNamedQuantityIs(String cookieName, int quantity) {
        OrderItem foundOrderItem = order.getOrderItems().stream().filter(orderItem -> orderItem.getCookie().getName().equals(cookieName)).findFirst().orElse(null);
        if(foundOrderItem != null){
            assertEquals( foundOrderItem.getQuantity(), quantity);
        }else {
            assertEquals( 0, quantity);
        }
    }

    @When("a cookie named {string} is removed {int} time from the order")
    public void aCookieNamedIsRemovedTimeToTheOrder(String cookieName, int numberOfTimes) {
        Cookie cookie = cookies.get(cookieName);
        for (int i = 0; i < numberOfTimes; i++) {
            order.removeItem(cookie);
        }
    }

    @When("a cookie named {string} quantity is set to {int}")
    public void aCookieNamedQuantityIsSetTo(String cookieName, int quantity) {
        Cookie cookie = cookies.get(cookieName);
        order.setItemQuantity(cookie, quantity);
    }
}
