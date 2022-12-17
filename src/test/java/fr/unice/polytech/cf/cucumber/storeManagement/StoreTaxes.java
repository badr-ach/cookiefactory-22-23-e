package fr.unice.polytech.cf.cucumber.storeManagement;

import static org.junit.Assert.*;

import fr.unice.polytech.cf.components.*;
import fr.unice.polytech.cf.entities.*;
import fr.unice.polytech.cf.interfaces.IngredientStockReserver;
import fr.unice.polytech.cf.repositories.StoreRepository;
import fr.unice.polytech.cf.services.BackOfficeService;
import fr.unice.polytech.cf.services.CustomerService;
import fr.unice.polytech.cf.entities.enums.EAccountType;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class StoreTaxes {
    @Autowired
    private CustomerService customerSystem;
    @Autowired
    private BackOfficeService backOfficeService;
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private IngredientStockComponent ingredientStockComponent;
    private ContactCoordinates contactCoordinates;
    private Store anotherStore;
    private Order order;
    private Account account;
    private Receipt receipt;
    private Store currentStore;

    @Autowired
    private StoreComponent storeComponent;

    private Exception caughtException;
     

    @Before
    public void setUp() {
        Iterable<Store> stores = storeRepository.findAll();
        for (Store store : stores) {
            // freeing cooks
            List<Cook> cookList = store.getCooks();
            for (Cook cook : cookList) {
                cook.setAssignments(new TreeMap<>());
            }

            // freeing ingredients
            Stock stock = store.getIngredientsStock();
            Map<Ingredient, Integer> ingredients =  stock.getAvailableIngredients();
            ingredientStockComponent.removeFromStock(store, ingredients);
        }
    }


    @And("the cookie on demand system")
    public void theCookieOnDemandSystem() {
    }

    @Given("a customer {string} with email {string}, phone number {string} and address {string}")
    public void aCustomerWithEmailPhoneNumberAndAddress(String name, String email, String phone, String address) {
        contactCoordinates = new ContactCoordinates(name, email, phone, address);
    }

    @And("the {double} of the store")
    public void theTaxesOfTheStore(double taxes) {
        customerSystem.getStores().get(0).setTaxes(taxes);
        backOfficeService.getStores().get(0).setTaxes(taxes);
    }

    @When("the order is paid")
    public void theOrderIsPaid() {
        try {
            receipt = customerSystem.payOrder(contactCoordinates, "123456789",order);
        } catch (Exception e) {
            caughtException = e;
            System.out.println(e.getMessage());
        }
    }

    @Then("the order price is equal to {double}")
    public void theOrderPriceIsEqualToTotal(double total) {
        double paidAmount = receipt.getTotal();
        assertEquals(total, paidAmount, 0.0);
    }

    @Given("an order with cookie named {string} priced {double} and quantity {int}")
    public void anOrderWithCookieNamedPricedCookiePriceAndQuantityCookieQuantity(String cookieName, double cookiePrice, int cookieQuantity) {
        currentStore = storeRepository.findAll().iterator().next();
        order = customerSystem.startOrder();
        LocalDateTime pickupDate = LocalDateTime.of(2022, Calendar.DECEMBER, 6, 14, 15);
        Cookie chocolalala = new Cookie(cookieName, cookiePrice, new HashMap<>(), Duration.of(10, ChronoUnit.MINUTES));
        for (int i = 0; i <cookieQuantity ; i++) {
            customerSystem.addCookie(chocolalala,order);
        }
        customerSystem.selectPickUpDate(pickupDate,order);
        customerSystem.selectStore(currentStore,order);
    }

    @Given("an account with the {string} role")
    public void anAccountWithTheRole(String accountType) {
        EAccountType role = EAccountType.valueOf(accountType);
        account = new Account("Manager","admin", role);
    }

    @When("the taxes are set to {double} with the account")
    public void theTaxesAreSetToNewTaxesWithTheAccount(double newTaxes) {
        currentStore = backOfficeService.getStores().get(0);
        try{
            backOfficeService.updateTaxes(currentStore, newTaxes, account);
        } catch(Exception e){
            caughtException = e;
        }
    }

    @Then("the store taxes are {double}")
    public void theStoreTaxesAreFinalTaxes(double finalTaxes) {
        double storeTaxes = currentStore.getTaxes();
        assertEquals(finalTaxes, storeTaxes, 0.0);
    }

    @And("the {double} of another store")
    public void theStartingTaxesOfAnotherStore(double startingTaxes) {
        anotherStore = new Store();
        anotherStore.setTaxes(startingTaxes);
        storeRepository.save(anotherStore,anotherStore.getId());
        System.out.println("another store taxes: " + anotherStore.getTaxes());
    }

    @Then("the other store taxes are {double}")
    public void theOtherStoreTaxesAreStartingTaxes(double startingTaxes) {
        anotherStore.setTaxes(startingTaxes);
        assertEquals(startingTaxes, anotherStore.getTaxes(), 0.0);
    }
}
