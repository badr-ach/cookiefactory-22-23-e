package fr.unice.polytech.cf.cucumber.orderManagement;

import static org.junit.Assert.*;

import fr.unice.polytech.cf.components.IngredientStockComponent;
import fr.unice.polytech.cf.components.OrderComponent;
import fr.unice.polytech.cf.components.OrderScheduler;
import fr.unice.polytech.cf.interfaces.StoreCookModifier;
import fr.unice.polytech.cf.repositories.OrderRepository;
import fr.unice.polytech.cf.repositories.StoreRepository;
import fr.unice.polytech.cf.services.CustomerService;
import fr.unice.polytech.cf.entities.ContactCoordinates;
import fr.unice.polytech.cf.entities.Cookie;
import fr.unice.polytech.cf.entities.Ingredient;
import fr.unice.polytech.cf.entities.enums.EIngredientType;
import fr.unice.polytech.cf.entities.*;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.Receipt;
import fr.unice.polytech.cf.entities.enums.EOrderStatus;
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

public class OrderValidation {
    @Autowired
    private IngredientStockComponent ingredientStockComponent;
    @Autowired
    private CustomerService customerSystem;
    @Autowired
    private OrderComponent orderComponent;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private StoreCookModifier storeCookModifier;

    private OrderScheduler orderScheduler;
    private ContactCoordinates contactCoordinates;
    private LocalDateTime retrievalDate;
    private Order order;
    private Exception exception;
    private Receipt receipt;
    private Cookie cookie;
    private Map<Ingredient, Integer> initialStock;
    private Store selectedStore;

    @Before
    public void setContext() {
        storeRepository.deleteAll();
        Store store = new Store();
        storeRepository.save(store,store.getId());
    }

    @Given("a customer {string} with email {string} and phone number {string} and address {string}")
    public void aCustomerWithEmailAndPhoneNumber(String name, String email, String phone, String address) {
        contactCoordinates = new ContactCoordinates(name, email, phone, address);
    }

    @And("our customer system")
    public void ourCustomerSystem() {
    }

    @And("a scheduler")
    public void aScheduler() {
        orderScheduler = new OrderScheduler();
    }

    @Given("an order with missing store information")
    public void anOrderWithMissingStoreInformation() {
        order = customerSystem.startOrder();
        customerSystem.addCookie(
                new Cookie("Chocolala", 10, new HashMap<>(), Duration.of(10, ChronoUnit.MINUTES)),order);

    }


    @When("the customer proceeds to pay his order with {string}")
    public void theCustomerProceedsToPayHisOrderWith(String creditCard) {
        try {
            receipt = customerSystem.payOrder(contactCoordinates, creditCard,order);
        } catch (Exception ex) {
            exception = ex;
        }
    }

    @Then("order validation fails with an error message {string}")
    public void orderValidationFailsWithAnErrorMessageStoreNotSpecified(String exceptionMsg) {
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Given("an order with missing retrieval date and time")
    public void anOrderWithMissingRetrievalDateAndTime() {
        order = customerSystem.startOrder();
        selectedStore = customerSystem.getStores().get(0);
        customerSystem.addCookie(
                new Cookie("Chocolala", 10, new HashMap<>(), Duration.of(10, ChronoUnit.MINUTES)),order);
        customerSystem.selectStore(selectedStore,order);

    }

    @Given("a proper order")
    public void aProperOrder() {
        retrievalDate = LocalDateTime.of(2022, Calendar.DECEMBER, 6, 14, 15);
        order = customerSystem.startOrder();
        selectedStore = customerSystem.getStores().get(0);
        customerSystem.addCookie(
                new Cookie("Chocolala", 10, new HashMap<>(), Duration.of(10, ChronoUnit.MINUTES)),order);
        customerSystem.selectStore(selectedStore,order);
        customerSystem.selectPickUpDate(retrievalDate,order);

    }

    @And("and the store it was assigned having gotten an order for the same retrieval time")
    public void andTheStoreItWasAssignedGettingAnOrderForTheSameRetrievalTime() {
        Order concurrentOrder = orderComponent.startOrder();
        concurrentOrder.setRetrievalDateTime(order.getRetrievalDateTime());
        concurrentOrder.setStore(order.getStore());
        orderScheduler.assignOrderToACook(concurrentOrder);
        orderRepository.save(concurrentOrder, concurrentOrder.getId());
    }

    @Then("no cook is assigned to the order")
    public void noCookIsAssignedToTheOrder() {
        assertNull(order.getCook());
        assertNull(storeCookModifier.hasOrder(order, selectedStore));
    }

    @And("the order status is pending")
    public void theOrderStatusIsPending() {
        assertEquals(order.getStatus(), EOrderStatus.PENDING);
    }

    private List<Cook> getAvailableCooks() {
        TimeSlot timeslot =
                new TimeSlot(
                        retrievalDate.toLocalTime().minus(order.getPreparationDuration()),
                        retrievalDate.toLocalTime());

        return orderScheduler.getAvailableCooksForRetrievalTime(
                retrievalDate, timeslot, selectedStore.getCooks());
    }

    @Then("the assigned retrieval time is still available")
    public void theAssignedRetrievalTimeIsStillAvailable() {
        List<Cook> availableCooks = getAvailableCooks();
        System.out.println(availableCooks.size());
        assertTrue(availableCooks.size() > 0);
    }

    @Then("the assigned retrieval time is not available")
    public void theAssignedRetrievalTimeIsNotAvailable() {
        List<Cook> availableCooks = getAvailableCooks();
        assertEquals(availableCooks.size(), 0);
    }

    @Then("the available cook is assigned the order")
    public void theAvailableCookIsAssignedTheOrder() {

        assertTrue(order.getStore().getCooks().get(0).hasOrder(order));
    }

    @Then("the order has a cook")
    public void theOrderHasACook() {
        assertNotNull(order.getCook());
        assertNotNull(storeCookModifier.hasOrder(order, selectedStore));
    }

    @Then("the order status is PAID")
    public void theOrderStatusIsPAID() {
        assertEquals(order.getStatus(), EOrderStatus.PAID);
    }

    @Then("correct receipt is generated")
    public void correctReceiptIsGenerated() {
        assertEquals(receipt.getCustomerContacts(), contactCoordinates);
        assertEquals((order.getPrice() * (100 - order.getStore().getTaxes()) / 100), receipt.getTotal(), 0.0);
        assertEquals(receipt.getOrderId(), order.getId());
    }

    @Given("The {string} and their {string} in the stock of the store")
    public void theAndTheirInTheStockOfTheStore(String ingredients, String quantities) {
        String[] ingredientNames = ingredients.split(",");
        String[] stockQuantities = quantities.split(",");
        Map<Ingredient, Integer> currentIngredients = new HashMap<>();
        for (int i = 0; i < ingredientNames.length; i++) {
            currentIngredients.put(
                    new Ingredient(ingredientNames[i], 5, EIngredientType.DOUGH),
                    Integer.parseInt(stockQuantities[i]));
        }
        initialStock = currentIngredients;
        order = customerSystem.startOrder();
        selectedStore = customerSystem.getStores().get(0);
        ingredientStockComponent.addToStock(selectedStore, initialStock);
    }

    @And("a {string} needing {string} with {string}")
    public void aNeedingWith(String name, String ingredients, String quantities) {
        String[] ingredientNames = ingredients.split(",");
        String[] stockQuantities = quantities.split(",");
        Map<Ingredient, Integer> neededIngredients = new HashMap<>();
        for (int i = 0; i < ingredientNames.length; i++) {
            neededIngredients.put(
                    new Ingredient(ingredientNames[i], 5, EIngredientType.DOUGH),
                    Integer.parseInt(stockQuantities[i]));
        }
        cookie = new Cookie(name, 20.0, neededIngredients, Duration.of(10, ChronoUnit.MINUTES));
    }

    @And("an order to that same store with {string} cookies of that recipe")
    public void anOrderWithCookiesOf(String amount) {
        retrievalDate = LocalDateTime.of(2022, Calendar.DECEMBER, 6, 14, 15);
        customerSystem.addCookie(cookie, Integer.parseInt(amount),order);
        customerSystem.selectStore(selectedStore,order);
        customerSystem.selectPickUpDate(retrievalDate,order);
    }

    @Then("the final state of the stock of the store is {string} for {string}")
    public void theFinalStateOfTheStockOfTheStoreIsFor(String expected_state, String ingredients) {

        String[] ingredientNames = ingredients.split(",");
        String[] stockQuantities = expected_state.split(",");
        Map<Ingredient, Integer> expectedIngredients = new HashMap<>();
        for (int i = 0; i < ingredientNames.length; i++) {
            expectedIngredients.put(
                    new Ingredient(ingredientNames[i], 5, EIngredientType.DOUGH),
                    Integer.parseInt(stockQuantities[i]));
        }
        Stock stock = selectedStore.getIngredientsStock();
        for (Ingredient ingredient : expectedIngredients.keySet()) {
            int expected = expectedIngredients.get(ingredient);
            assertEquals(expected, stock.getAvailableQuantity(ingredient));
        }
    }

}
