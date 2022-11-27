package fr.unice.polytech.cf;
import static org.junit.Assert.*;

import fr.unice.polytech.cf.cookieservice.entities.Cookie;
import fr.unice.polytech.cf.cookieservice.entities.ingredients.Ingredient;
import fr.unice.polytech.cf.cookieservice.enums.EIngredientType;
import fr.unice.polytech.cf.orderservice.OrderService;
import fr.unice.polytech.cf.orderservice.PaymentService;
import fr.unice.polytech.cf.orderservice.entities.Order;
import fr.unice.polytech.cf.orderservice.entities.OrderItem;
import fr.unice.polytech.cf.orderservice.enums.EOrderStatus;
import fr.unice.polytech.cf.orderservice.exceptions.ImpossibleOrderCancelingException;
import fr.unice.polytech.cf.storeservice.StoreService;
import fr.unice.polytech.cf.storeservice.entities.Store;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class CancelAnOrder {
    OrderService orderService = new OrderService(new PaymentService(),new StoreService());
    Order orderInPrepa;
    Order anotherOrder;
    Ingredient ingredient1 = new Ingredient("DryDough",2, EIngredientType.DOUGH);
    Ingredient ingredient2 = new Ingredient("SweetTopping",3, EIngredientType.TOPPING);
    Store store = new Store();
    Cookie cookie1;
    Cookie cookie2;
    ArrayList<OrderItem> orderItemList = new ArrayList<OrderItem>();




    @Given("the cook it was assigned to an order")
    public void theCookAndTheScheduleItWasAssignedTo() {
        store.getIngredientsStock().add(ingredient1,5);
        store.getIngredientsStock().add(ingredient2,5);
        cookie1 = new Cookie("cookieTest",5,store.getIngredientsStock().getAvailableIngredients());
        cookie1.setPreparationDuration(Duration.of(5, ChronoUnit.MINUTES));
        OrderItem orderitem = new OrderItem(cookie1);
        orderItemList.add(orderitem);
        orderInPrepa = new Order(orderItemList,EOrderStatus.IN_PREPARATION);
        orderInPrepa.setStore(store);
        orderInPrepa.setRetrievalDateTime(LocalDateTime.of(2022, Calendar.DECEMBER, 6, 14, 15));
        orderService.getScheduler().assignOrderToACook(orderInPrepa);
    }

    @When("the customer tries to cancel the chosen order")
    public void theCustomerTriesToCancelTheChosenOrder() {
    }

    @Then("an error is then thrown with message {string}")
    public void anErrorIsThrownWithMessage(String arg0) {
        Exception exception = assertThrows(ImpossibleOrderCancelingException.class,()-> orderService.cancelAnOrder(orderInPrepa));
        assertEquals("Can't cancel order in preparation.",exception.getMessage());
    }

    @Then("the cook is still preparing it")
    public void theCookIsStillPreparingIt() {
       assertEquals(orderInPrepa.getCook(),store.getCooks().get(0));
       assertEquals(orderInPrepa.getStatus(),EOrderStatus.IN_PREPARATION);
    }

    @Then("the ingredients are still removed from the stock")
    public void theIngredientsAreStillRemovedFromTheStock() {
    store.getIngredientsStock().reserve(ingredient1,1);
    store.getIngredientsStock().reserve(ingredient2,1);
    assertTrue(store.getIngredientsStock().getAvailableIngredients().get(ingredient1)==4);
    assertTrue(store.getIngredientsStock().getAvailableIngredients().get(ingredient2)==4);
    assertTrue(store.getIngredientsStock().getReservedIngredients().get(ingredient1)==1);
    assertTrue(store.getIngredientsStock().getReservedIngredients().get(ingredient2)==1);

    }
    @Given("the cook it was assigned to an another order")
    public void cookToAnAnotherOrder(){
        store.getIngredientsStock().add(ingredient1,10);
        store.getIngredientsStock().add(ingredient2,10);
        HashMap<Ingredient,Integer> recetteDuCookie = new HashMap<>();
        recetteDuCookie.put(ingredient1,2);
        recetteDuCookie.put(ingredient2,2);
        ArrayList<OrderItem> orderItemList2 = new ArrayList<OrderItem>();
        cookie2 = new Cookie("CookieCancelled",10,recetteDuCookie);
        cookie2.setPreparationDuration(Duration.of(5, ChronoUnit.MINUTES));
        OrderItem orderItem2 = new OrderItem(cookie2);
        orderItemList2.add(orderItem2);
        anotherOrder = new Order(orderItemList2,EOrderStatus.PENDING);
        anotherOrder.setStore(store);
        anotherOrder.setRetrievalDateTime(LocalDateTime.of(2022, Calendar.DECEMBER, 6, 14, 15));
        orderService.getScheduler().assignOrderToACook(anotherOrder);
    }
    @Then("the customer tries to cancel an another order")
    public void cancelAnotherOrder(){
        store.getIngredientsStock().reserve(ingredient1,3);
        store.getIngredientsStock().reserve(ingredient2,3);
        assertTrue(store.getIngredientsStock().getAvailableIngredients().get(ingredient1)==7);
        assertTrue(store.getIngredientsStock().getAvailableIngredients().get(ingredient2)==7);
        assertTrue(store.getIngredientsStock().getReservedIngredients().get(ingredient1)==3);
        assertTrue(store.getIngredientsStock().getReservedIngredients().get(ingredient2)==3);

    }
    @Then("the cook is no longer preparing it")
    public void theCookIsNoLongerPreparingIt() {
        orderService.cancelAnOrder(anotherOrder);
        assertTrue(anotherOrder.getStatus().equals(EOrderStatus.CANCELLED));
        assertTrue(anotherOrder.getCook()==null);
    }

    @Then("the ingredients are back to the stock")
    public void theIngredientsAreBackToTheStock() {
        assertTrue(store.getIngredientsStock().getAvailableIngredients().get(ingredient1)==9);
        assertTrue(store.getIngredientsStock().getAvailableIngredients().get(ingredient2)==9);
        assertTrue(store.getIngredientsStock().getReservedIngredients().get(ingredient1)==1);
        assertTrue(store.getIngredientsStock().getReservedIngredients().get(ingredient2)==1);

    }

    @Then("the order is marked as CANCELLED")
    public void theOrderIsMarkedAsCANCELLED() {
        assertEquals(anotherOrder.getStatus(),EOrderStatus.CANCELLED);
    }


}
