package fr.unice.polytech.cf.cucumber.kitchenManagement;

import fr.unice.polytech.cf.components.*;
import fr.unice.polytech.cf.entities.commands.MarkOrderAsInPreparation;
import fr.unice.polytech.cf.entities.commands.MarkOrderAsPrepared;
import fr.unice.polytech.cf.repositories.StoreRepository;
import fr.unice.polytech.cf.services.CustomerService;
import fr.unice.polytech.cf.entities.ContactCoordinates;
import fr.unice.polytech.cf.entities.Cookie;
import fr.unice.polytech.cf.entities.Ingredient;
import fr.unice.polytech.cf.entities.enums.EIngredientType;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.enums.EOrderStatus;
import fr.unice.polytech.cf.entities.Cook;
import fr.unice.polytech.cf.entities.Store;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class KitchenWorkflow {
    @Autowired
    CustomerService customerSystem;
    @Autowired
    IngredientStockComponent ingredientStockComponent;
    @Autowired
    StoreComponent storeComponent;

    @Autowired
    StoreRepository storeRepository;
    @Autowired
    OrderInvoker invoker;



    LocalDateTime retrievalDate;
    Ingredient ingredient;
    Store selectedStore;
    Order order;
    Cook cook;

    @Before
    public void setContext(){
        Iterable<Store> stores = storeRepository.findAll();
        for(Store store : stores){
            List<Cook> cookList = store.getCooks();
            for(Cook cook : cookList){
                cook.setAssignments(new TreeMap<>());
            }
        }
        ingredient = new Ingredient("Swift Chocolate", 1, EIngredientType.TOPPING);
    }

    @Given("a paid order")
    public void aPaidOrder() {
        String creditCard = "123456789";
        ContactCoordinates contactCoordinates = new ContactCoordinates("name", "email@gmail.com",
                "0710234547", "Rue des Colines, Valbonnes");
        retrievalDate = LocalDateTime.of(2022, Calendar.DECEMBER, 6, 14, 15);
        order = customerSystem.startOrder();
        selectedStore = customerSystem.getStores().get(0);
        ingredientStockComponent.addToStock(selectedStore,new HashMap<>(Collections.singletonMap(ingredient,4)));
        customerSystem.addCookie(new Cookie("Chocolala", 10, new HashMap<>(Collections.singletonMap(ingredient,2)),
                Duration.of(10, ChronoUnit.MINUTES)));
        customerSystem.selectStore(selectedStore);
        customerSystem.selectPickUpDate(retrievalDate);
        customerSystem.payOrder(contactCoordinates, creditCard);
    }

    @Given("the assigned cook to the order")
    public void theAssignedCookToTheOrder() {
        cook = storeComponent.hasOrder(order,selectedStore);
    }
    @When("the cook starts preparing the order")
    public void theCookStartsPreparingTheOrder() {
        invoker.setCommand(new MarkOrderAsInPreparation(ingredientStockComponent));
        invoker.invoke(order);
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
        invoker.setCommand(new MarkOrderAsInPreparation(ingredientStockComponent));
        invoker.invoke(order);
        invoker.setCommand(new MarkOrderAsPrepared(storeComponent));
        invoker.invoke(order);
    }

    @Then("he no longer has it")
    public void heNoLongerHasIt() {
        assertFalse(cook.getWorkLoadOfTheDay(retrievalDate.toLocalDate()).contains(order));
    }

}


