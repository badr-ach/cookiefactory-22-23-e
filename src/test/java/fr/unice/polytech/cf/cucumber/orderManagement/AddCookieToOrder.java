package fr.unice.polytech.cf.cucumber.orderManagement;

import fr.unice.polytech.cf.components.CookieComponent;
import fr.unice.polytech.cf.components.OrderComponent;
import fr.unice.polytech.cf.entities.Cookie;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.OrderItem;
import fr.unice.polytech.cf.entities.SurpriseBasket;
import fr.unice.polytech.cf.interfaces.*;
import fr.unice.polytech.cf.repositories.OrderRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AddCookieToOrder {

    private Order order;
    @Autowired
    private OrderComponent orderComponent;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CookieComponent cookieComponent;
    @Autowired
    private OrderCreator orderCreator;
    @Autowired
    private OrderModifier orderModifier;
    @Autowired
    private OrderModifier orderCanceller;
    @Autowired
    private OrderFinder orderFinder;
    @Autowired
    private CustomerNotifier customerNotifier;
    @Autowired
    private SurpriseBasketModifier surpriseBasketModifier;
    @Autowired
    private OrderFinalizer orderFinalizer;

    List<Cookie> cookies = new ArrayList<Cookie>();

    @Given("an empty order")
    public void anEmptyOrder() {
        order = orderCreator.startOrder();
    }

    @Given("a cookie named {string} priced {double} is in the order")
    public void aCookieNamedPricedCookiePriceIsInTheOrder(String name, double price) {
        Cookie theNamedAndPricedCookie = cookieComponent.initializeACookie();
        theNamedAndPricedCookie.setName(name);
        theNamedAndPricedCookie.setPrice(price);
        order.addItem(theNamedAndPricedCookie);
        cookies.add(theNamedAndPricedCookie);
    }

    @And("a cookie named {string} priced {double}")
    public void aCookieNamedPriced(String name, double price) {
        Cookie theNamedCookie = cookieComponent.initializeACookie();
        theNamedCookie.setName(name);
        theNamedCookie.setPrice(price);
        cookies.add(theNamedCookie);
    }

    @Then("the order item quantity is {int}")
    public void theOrderItemQuantityIs(int quantity) {
        assertEquals(order.getOrderItems().get(0).getQuantity(), quantity);
    }

    @Then("the calculated price is equal to {double}")
    public void theCalculatedPriceIsEqualTo(double price) {
        assertEquals(price, order.getPrice(), 0.01);
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
        Cookie cookie = null;
        for ( Cookie c : cookies ) {
            if ( c.getName().equals(name) ) cookie = c;
        }
        for (int i = 0; i < numberOfTimes; i++) {
            order.addItem(cookie);
        }
    }

    @Then("the cookie named {string} quantity is {int}")
    public void theCookieNamedQuantityIs(String cookieName, int quantity) {
        OrderItem foundOrderItem = order.getOrderItems().stream().filter(orderItem -> orderItem.getCookie().getName().equals(cookieName)).findFirst().orElse(null);
        if(foundOrderItem != null){
            assertEquals(foundOrderItem.getQuantity(), quantity);
        }else {
            assertEquals(0, quantity);
        }
    }

    @When("a cookie named {string} is removed {int} time from the order")
    public void aCookieNamedIsRemovedTimeToTheOrder(String cookieName, int numberOfTimes) {
        Cookie theNamedCookie = cookieComponent.initializeACookie();
        theNamedCookie.setName(cookieName);
        for (int i = 0; i < numberOfTimes; i++) {
            order.removeItem(theNamedCookie);
        }
    }

    @When("a cookie named {string} quantity is set to {int}")
    public void aCookieNamedQuantityIsSetTo(String cookieName, int quantity) {
        Cookie theNamedCookie = cookieComponent.initializeACookie();
        theNamedCookie.setName(cookieName);
        order.setItemQuantity(theNamedCookie, quantity);
    }
}
