package fr.unice.polytech.cf.cucumber.orderManagement;

import static org.junit.Assert.*;

import fr.unice.polytech.cf.components.IngredientStockComponent;
import fr.unice.polytech.cf.entities.Cookie;
import fr.unice.polytech.cf.entities.Ingredient;
import fr.unice.polytech.cf.entities.enums.EIngredientType;
import fr.unice.polytech.cf.components.OrderComponent;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.OrderItem;
import fr.unice.polytech.cf.entities.enums.EOrderStatus;
import fr.unice.polytech.cf.exceptions.ImpossibleOrderCancelingException;
import fr.unice.polytech.cf.components.StoreComponent;
import fr.unice.polytech.cf.entities.Store;
import fr.unice.polytech.cf.interfaces.IngredientStockModifier;
import fr.unice.polytech.cf.interfaces.IngredientStockReserver;
import fr.unice.polytech.cf.interfaces.OrderFinder;
import fr.unice.polytech.cf.interfaces.OrderModifier;
import fr.unice.polytech.cf.repositories.StoreRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class CancelAnOrder {

    @Autowired
    OrderFinder orderFinder;
    @Autowired
    OrderModifier orderCanceller;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    StoreComponent storeComponent;
    @Autowired
    IngredientStockReserver ingredientStockReserver;
    @Autowired
    IngredientStockModifier ingredientStockModifier;
    @Autowired
    IngredientStockComponent ingredientStockComponent;
    @Autowired
    OrderComponent orderComponent;

    Order orderInPreparation;
    Order anotherOrder;
    Ingredient ingredient1;
    Ingredient ingredient2;
    Store store;
    Cookie cookie1;
    Cookie cookie2;


    Exception caughtException;
    HashMap <Ingredient, Integer> ingredients = new HashMap<>();
    ArrayList<OrderItem> orderItemList = new ArrayList<OrderItem>();

    @Before
    public void setContext(){
        store = new Store("Store1");
        ingredient1 = new Ingredient("DryDough",2, EIngredientType.DOUGH);
        ingredient2 = new Ingredient("SweetTopping",3, EIngredientType.TOPPING);
    }


    @Given("the cook it was assigned to an order")
    public void theCookAndTheScheduleItWasAssignedTo() {
        storeRepository.save(store,store.getId());
        ingredients.put(ingredient1, 5);
        ingredients.put(ingredient2, 5);
        ingredientStockComponent.addToStock(store,ingredients);
        cookie1 = new Cookie("cookieTest",5,storeRepository.findById(store.getId()).get().getIngredientsStock().getAvailableIngredients());
        cookie1.setPreparationDuration(Duration.of(5, ChronoUnit.MINUTES));
        OrderItem orderitem = new OrderItem(cookie1);
        orderItemList.add(orderitem);
        orderInPreparation = new Order(orderItemList,EOrderStatus.IN_PREPARATION);
        orderInPreparation.setStore(store);
        orderInPreparation.setRetrievalDateTime(LocalDateTime.of(2022, Calendar.DECEMBER, 6, 14, 15));
        orderComponent.getCookAssigner().assignOrderToACook(orderInPreparation);
    }

    @When("the customer tries to cancel the chosen order")
    public void theCustomerTriesToCancelTheChosenOrder() {
        try{
            orderCanceller.cancelAnOrder(orderInPreparation);
        }catch(Exception ex){
            caughtException = ex;
        }
    }

    @Then("an error is then thrown with message {string}")
    public void anErrorIsThrownWithMessage(String arg0) {
        assertThrows("Can't cancel order in preparation.",ImpossibleOrderCancelingException.class,()-> {
            throw caughtException;
        } );

    }

    @Then("the cook is still preparing it")
    public void theCookIsStillPreparingIt() {
       assertEquals(orderInPreparation.getCook(),store.getCooks().get(0));
       assertEquals(orderInPreparation.getStatus(),EOrderStatus.IN_PREPARATION);
    }

    @Then("the ingredients are still removed from the stock")
    public void theIngredientsAreStillRemovedFromTheStock() {
    HashMap<Ingredient, Integer> ingredients1 = new HashMap<>();
    HashMap<Ingredient, Integer> ingredients2 = new HashMap<>();
    ingredients1.put(ingredient1, 2);
    ingredients2.put(ingredient2, 2);
    ingredientStockComponent.reserveStock(store,ingredients1);
    ingredientStockComponent.reserveStock(store,ingredients2);
        assertEquals(3, (int) storeRepository.findById(store.getId()).get().getIngredientsStock().getAvailableIngredients().get(ingredient1));
        assertEquals(3, (int) storeRepository.findById(store.getId()).get().getIngredientsStock().getAvailableIngredients().get(ingredient2));
        assertEquals(2, (int) storeRepository.findById(store.getId()).get().getIngredientsStock().getReservedIngredients().get(ingredient1));
        assertEquals(2, (int) storeRepository.findById(store.getId()).get().getIngredientsStock().getReservedIngredients().get(ingredient1));

    }

    @Given("the cook it was assigned to an another order")
    public void cookToAnAnotherOrder(){
        storeRepository.save(store,store.getId());
        HashMap <Ingredient, Integer> ingredients = new HashMap<>();
        ingredients.put(ingredient1, 10);
        ingredients.put(ingredient2, 10);
        ingredientStockComponent.addToStock(store,ingredients);
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
        orderComponent.getCookAssigner().assignOrderToACook(anotherOrder);
    }

    @Then("the customer tries to cancel an another order")
    public void cancelAnotherOrder(){
    HashMap <Ingredient, Integer> ingredients = new HashMap<>();
    ingredients.put(ingredient1, 3);
    ingredients.put(ingredient2, 3);
            ingredientStockComponent.reserveStock(store,ingredients);
        assertEquals(7, (int) store.getIngredientsStock().getAvailableIngredients().get(ingredient1));
        assertEquals(7, (int) store.getIngredientsStock().getAvailableIngredients().get(ingredient2));
        assertEquals(3, (int) store.getIngredientsStock().getReservedIngredients().get(ingredient1));
        assertEquals(3, (int) store.getIngredientsStock().getReservedIngredients().get(ingredient2));
        orderComponent.cancelAnOrder(anotherOrder);
    }

    @Then("the cook is no longer preparing it")
    public void theCookIsNoLongerPreparingIt() {
        assertEquals(anotherOrder.getStatus(), EOrderStatus.CANCELLED);
        assertNull(anotherOrder.getCook());
    }

    @Then("the ingredients are back to the stock")
    public void theIngredientsAreBackToTheStock() {
        assertEquals(9, (int) store.getIngredientsStock().getAvailableIngredients().get(ingredient1));
        assertEquals(9, (int) store.getIngredientsStock().getAvailableIngredients().get(ingredient2));
        assertEquals(1, (int) store.getIngredientsStock().getReservedIngredients().get(ingredient1));
        assertEquals(1, (int) store.getIngredientsStock().getReservedIngredients().get(ingredient2));
    }

    @Then("the order is marked as CANCELLED")
    public void theOrderIsMarkedAsCANCELLED() {
        assertEquals(anotherOrder.getStatus(),EOrderStatus.CANCELLED);
    }

}
