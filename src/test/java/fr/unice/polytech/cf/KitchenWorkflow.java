package fr.unice.polytech.cf;

import fr.unice.polytech.cf.accountservice.entities.ContactCoordinates;
import fr.unice.polytech.cf.cookieservice.entities.Cookie;
import fr.unice.polytech.cf.cookieservice.entities.ingredients.Ingredient;
import fr.unice.polytech.cf.cookieservice.enums.EIngredientType;
import fr.unice.polytech.cf.orderservice.entities.Order;
import fr.unice.polytech.cf.orderservice.enums.EOrderStatus;
import fr.unice.polytech.cf.storeservice.entities.Cook;
import fr.unice.polytech.cf.storeservice.entities.Store;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class KitchenWorkflow {
    CustomerSystem customerSystem = new CustomerSystem();

    Ingredient ingredient = new Ingredient("Swift Chocolate", 1, EIngredientType.TOPPING);

    LocalDateTime retrievalDate;
    Store selectedStore;
    Order order;
    Cook cook;

    @Given("a paid order")
    public void aPaidOrder() {
        String creditCard = "123456789";
        ContactCoordinates contactCoordinates = new ContactCoordinates("name", "email@gmail.com",
                "0710234547", "Rue des Colines, Valbonnes");
        retrievalDate = LocalDateTime.of(2022, Calendar.DECEMBER, 6, 14, 15);
        order = customerSystem.startOrder();
        selectedStore = customerSystem.getStores().get(0);
        customerSystem.storeService.addToStock(selectedStore,new HashMap<>(Collections.singletonMap(ingredient,4)));
        customerSystem.addCookie(new Cookie("Chocolala", 10, new HashMap<>(Collections.singletonMap(ingredient,2)),
                Duration.of(10, ChronoUnit.MINUTES)));
        customerSystem.selectStore(selectedStore);
        customerSystem.selectPickUpDate(retrievalDate);
        customerSystem.payOrder(contactCoordinates, creditCard);
    }

    @Given("the assigned cook to the order")
    public void theAssignedCookToTheOrder() {
        cook = customerSystem.storeService.hasOrder(order,selectedStore);
    }
    @When("the cook starts preparing the order")
    public void theCookStartsPreparingTheOrder() {
        customerSystem.orderService.markOrderAsInPreparation(order);
    }


    @Then("the reserved stock for the order is removed")
    public void theReservedStockForTheOrderIsRemoved() {
        assertEquals(selectedStore.getIngredientsStock().getAvailableReserveQuantity(ingredient),0);
    }

    @And("the order status is {string}")
    public void theOrderStatusIs(String status) {
        assertEquals(order.getStatus(), EOrderStatus.valueOf(status));
    }

    @When("the cook marks an order as prepared")
    public void theCookMarksAnOrderAsPrepared() {
        customerSystem.orderService.markOrderAsInPreparation(order);
        customerSystem.orderService.markOrderAsPrepared(order);
    }

    @Then("he no longer has it")
    public void heNoLongerHasIt() {
        assertEquals(cook.getWorkLoadOfTheDay(retrievalDate.toLocalDate()).contains(order),false);
    }

}
