package fr.unice.polytech.cf;

import fr.unice.polytech.cf.accountservice.entities.ContactCoordinates;
import fr.unice.polytech.cf.accountservice.entities.CustomerAccount;
import fr.unice.polytech.cf.cookieservice.entities.Cookie;
import fr.unice.polytech.cf.orderservice.entities.Order;
import fr.unice.polytech.cf.orderservice.enums.EOrderStatus;
import fr.unice.polytech.cf.storeservice.entities.Schedule;
import fr.unice.polytech.cf.storeservice.entities.TimeSlot;
import fr.unice.polytech.cf.toogoodtogo.TooGoodToGo;
import fr.unice.polytech.cf.toogoodtogo.entities.SurpriseBasket;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class TooGoodToGoTest {

    private Order order;
    private TooGoodToGo tooGoodToGo;
    private CustomerAccount customerAccount;

    private CustomerSystem customerSystem;
    private Cookie cookie;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Given("{int} orders with {int} cookies and a retrieval date {string}")
    public void ordersWithCookieQuantityCookiesAndARetrievalDate(int orderNumber, int numberCookies, String retrievalDateText) {
        customerSystem = new CustomerSystem();
        for (int i = 0; i < orderNumber; i++) {
            newOrder(numberCookies, "Chocolala", 5.0, retrievalDateText);
        }
    }
    @Given("{int} orders with {int} cookies priced {double} and a retrieval date {string}")
    public void ordersWithCookieQuantityCookiesPricedCookiePriceAndARetrievalDate(int orderNumber, int numberCookies, double cookiePrice, String retrievalDateText) {
        customerSystem = new CustomerSystem();
        for (int i = 0; i < orderNumber; i++) {
            newOrder(numberCookies, "Chocolala", cookiePrice, retrievalDateText);
        }
    }

    private void newOrder(int cookieQuantity, String cookieName, double cookiePrice, String retrievalDateText){
        LocalDateTime retrievalDate = LocalDateTime.parse(retrievalDateText, formatter);
        order = customerSystem.startOrder();
        customerSystem.selectStore(customerSystem.storeService.getStores().get(0));
        cookie = new Cookie(cookieName, cookiePrice, new HashMap<>());
        customerSystem.addCookie(cookie, cookieQuantity);
        order.setStatus(EOrderStatus.PREPARED);
        order.setRetrievalDateTime(retrievalDate);
    }

    @When("the order status is updated with on the {string}")
    public void theOrderStatusIsUpdatedWithOnThe(String currentDateText) {
        LocalDateTime currentDate = LocalDateTime.parse(currentDateText, formatter);
        customerSystem.orderService.updateOrdersStatus(currentDate);
        customerSystem.orderService.updateSurpriseBaskets(currentDate, tooGoodToGo);
    }


    @Then("{int} baskets of {int} cookies are available for too good to go pickup")
    public void basketsOfNumberOfCookiesToGoCookiesAreAvailableForTooGoodToGoPickup(int numberOfBaskets, int numberOfCookiesPerBasket) {
        List<SurpriseBasket> surpriseBaskets = tooGoodToGo.getSurpriseBaskets();
        assertEquals(numberOfBaskets, surpriseBaskets.size());
        for (SurpriseBasket surpriseBasket : surpriseBaskets) {
            assertEquals(numberOfCookiesPerBasket, surpriseBasket.getQuantity());
        }
    }

    @Given("a TooGooToGoService")
    public void aTooGooToGoService() {
        tooGoodToGo = new TooGoodToGo();
    }

    @Then("SurpriseBaskets have an associated store")
    public void surprisebasketsHaveAnAssociatedStore() {
        List<SurpriseBasket> surpriseBaskets = tooGoodToGo.getSurpriseBaskets();
        for (SurpriseBasket surpriseBasket : surpriseBaskets) {
            assertNotNull(surpriseBasket.getStore());
        }
    }

    @Then("SurpriseBaskets have an associated price")
    public void surprisebasketsHaveAnAssociatedPrice() {
        List<SurpriseBasket> surpriseBaskets = tooGoodToGo.getSurpriseBaskets();
        for (SurpriseBasket surpriseBasket : surpriseBaskets) {
            assertNotNull(surpriseBasket.getPrice());
        }
    }

    @And("an account with username {string}, password {string}, name {string} and email {string}")
    public void anAccountWithUsernamePasswordNameAndEmail(String username, String password, String name, String email) {
        ContactCoordinates contactCoordinates = new ContactCoordinates(name, email);
        customerAccount = customerSystem.signup(username, password, contactCoordinates);
    }

    @And("the account is subscribed for ToGoodToGo notifications")
    public void theAccountIsSubscribedForToGoodToGoNotifications() {
        Schedule schedule = new Schedule();
        schedule.seedSchedule();
        customerAccount.startTooGoodToGoNotifications(tooGoodToGo, schedule);
    }

    @Then("the customer is notified with {string}")
    public void theCustomerIsNotifiedWith(String message) {
        List<String> sentNotifications = customerAccount.getContactCoordinates().getSentNotifications();
        String lastNotification =sentNotifications.get(0);
        assertEquals(message, lastNotification);
    }

    @And("the account is subscribed for ToGoodToGo notifications between {string} and {string}")
    public void theAccountIsSubscribedForToGoodToGoNotificationsBetweenAnd(String startTime, String endTime) {
        Schedule schedule = new Schedule();
        for (DayOfWeek day: DayOfWeek.values()) {
            TimeSlot timeSlot = new TimeSlot(startTime, endTime);
            List<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
            timeSlots.add(timeSlot);
            schedule.setHoursOfDay(timeSlots, day);
        }
        customerAccount.startTooGoodToGoNotifications(tooGoodToGo, schedule);
    }

    @Then("the number of notification the customer received is {int}")
    public void theNumberOfNotificationTheCustomerReceivedIsNumberOfNotifications(int numberOfNotifications) {
        List<String> sentNotifications = customerAccount.getContactCoordinates().getSentNotifications();
        assertEquals(numberOfNotifications, sentNotifications.size());
    }
}
