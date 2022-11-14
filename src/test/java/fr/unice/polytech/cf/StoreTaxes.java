package fr.unice.polytech.cf;

import static org.junit.Assert.*;

import fr.unice.polytech.cf.accountservice.entities.Account;
import fr.unice.polytech.cf.accountservice.entities.ContactCoordinates;
import fr.unice.polytech.cf.accountservice.enums.EAccountType;
import fr.unice.polytech.cf.cookieservice.entities.Cookie;
import fr.unice.polytech.cf.cookieservice.entities.Ingredient;
import fr.unice.polytech.cf.orderservice.entities.Order;
import fr.unice.polytech.cf.orderservice.entities.Receipt;
import fr.unice.polytech.cf.storeservice.entities.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class StoreTaxes {
    private CustomerSystem customerSystem;
    private BackOfficeSystem backOfficeSystem;
    private Scheduler orderScheduler;
    private ContactCoordinates contactCoordinates;
    private LocalDateTime retrievalDate;
    private Store anotherStore;
    private Order order;
    private Exception exception;
    private Account account;
    private Receipt receipt;
    private Cookie cookie;
    private Map<Ingredient, Integer> initialStock;
    private Store currentStore;
    private Exception caughtException;

    @And("the cookie on demand system")
    public void theCookieOnDemandSystem() {
        customerSystem = new CustomerSystem();
        backOfficeSystem = new BackOfficeSystem();
    }

    @Given("a customer {string} with email {string}, phone number {string} and address {string}")
    public void aCustomerWithEmailPhoneNumberAndAddress(String name, String email, String phone, String address) {
        contactCoordinates = new ContactCoordinates(name, email, phone, address);
    }

    @And("the {double} of the store")
    public void theTaxesOfTheStore(double taxes) {
        customerSystem.getStores().get(0).setTaxes(taxes);
        backOfficeSystem.getStores().get(0).setTaxes(taxes);
    }

    @When("the order is paid")
    public void theOrderIsPaid() {
        try {
            receipt = customerSystem.payOrder(contactCoordinates, "123456789");
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
        currentStore = customerSystem.getStores().get(0);
        order = customerSystem.startOrder();
        LocalDateTime pickupDate = LocalDateTime.of(2022, Calendar.DECEMBER, 6, 14, 15);
        Cookie chocolalala = new Cookie(cookieName, cookiePrice, new HashMap<>(), Duration.of(10, ChronoUnit.MINUTES));
        for (int i = 0; i <cookieQuantity ; i++) {
            customerSystem.addCookie(chocolalala);
        }
        customerSystem.selectPickUpDate(pickupDate);
        customerSystem.selectStore(currentStore);
    }

    @Given("an account with the {string} role")
    public void anAccountWithTheRole(String accountType) {
        EAccountType role = EAccountType.valueOf(accountType);
        account = new Account("Manager","admin", role);
    }

    @When("the taxes are set to {double} with the account")
    public void theTaxesAreSetToNewTaxesWithTheAccount(double newTaxes) {
        currentStore = backOfficeSystem.getStores().get(0);
        try{
            backOfficeSystem.updateTaxes(currentStore, newTaxes, account);
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
        backOfficeSystem.storeService.addStore(anotherStore);
    }

    @Then("the other store taxes are {double}")
    public void theOtherStoreTaxesAreStartingTaxes(double startingTaxes) {
        double storeTaxes = anotherStore.getTaxes();
        assertEquals(startingTaxes, storeTaxes, 0.0);
    }
}
