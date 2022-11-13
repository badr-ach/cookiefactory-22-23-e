//package fr.unice.polytech.cf;
//
//import static org.junit.Assert.assertTrue;
//
//import fr.unice.polytech.cf.OrderService.Entities.Order;
//import fr.unice.polytech.cf.OrderService.OrderScheduler;
//import fr.unice.polytech.cf.StoreService.Entities.*;
//import fr.unice.polytech.cf.StoreService.Exceptions.InsufficientTimeslotDurationException;
//import fr.unice.polytech.cf.StoreService.Exceptions.TimeslotUnavailableException;
//import io.cucumber.java.en.And;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import java.time.DayOfWeek;
//import java.util.ArrayList;
//
//public class StoreSchedule {
//  private Order order;
//  private Store store;
//  private TimeSlot timeslot;
//  private Throwable exception = null;
//
//  @Given("a store opened from {string} to {string}")
//  public void aStoreOpenedFromTo(String openingHour, String closingHour) {
//    store = new Store();
//    store.getWeeklySchedule().setSameScheduleAllWeek(openingHour, closingHour);
//  }
//
//  @Given("a store opened from {string} to {string} with a Cook available from {string} to {string}")
//  public void aStoreOpenedFromToWithACookAvailableFromTo(String openingHour, String closingHour, String startAvailableHour, String endAvailableHour) {
//    store = new Store();
//    store.getWeeklySchedule().setSameScheduleAllWeek(openingHour, closingHour);
//    Cook cook = new Cook();
//    WeeklySchedule weeklySchedule = new WeeklySchedule(startAvailableHour, endAvailableHour);
//    cook.setSchedule(weeklySchedule);
//    ArrayList<Cook> cooks = new ArrayList<Cook>();
//    cooks.add(cook);
//    store.setCooks(cooks);
//  }
//
//  @And("a timeslot from {string} to {string}")
//  public void aTimeslotFromTo(String preparationStart, String preparationEnd) {
//    timeslot = new TimeSlot(preparationStart, preparationEnd);
//  }
//
//  @When("the order is set for pick up")
//  public void theOrderIsSetForPickUp() {
//    exception = null;
//    OrderScheduler orderScheduler = new OrderScheduler();
//    try {
//      orderScheduler.selectTimeSlot(timeslot, order, DayOfWeek.MONDAY);
//    } catch (TimeslotUnavailableException e) {
//      exception = e;
//    } catch (InsufficientTimeslotDurationException e) {
//      exception = e;
//    }
//  }
//
//  @Then("an exception is thrown")
//  public void anExceptionIsThrown() {
//    assertTrue(exception != null);
//  }
//
//  @Then("the order is correctly added")
//  public void theOrderIsCorrectlyAdded() {
//    assertTrue(exception == null);
//  }
//
//  @When("the store schedule is set to open at {string} and close at {string} on day {string}")
//  public void theStoreScheduleIsSetToOpenAtAndCloseAtOnDay(
//      String openingHour, String closingHour, String day) {
//    DayOfWeek dayOfWeek = DayOfWeek.valueOf(day);
//    store.getWeeklySchedule().setDayInSchedule(dayOfWeek, openingHour, closingHour);
//  }
//
//  @Then("the store opening hour is {string} and its closing hour is {string} on day {string}")
//  public void theStoreOpeningHourIsAndItsClosingHourIsOnDay(
//      String openingHour, String closingHour, String day) {
//    DayOfWeek dayOfWeek = DayOfWeek.valueOf(day);
//    DailySchedule dailySchedule = store.getWeeklySchedule().getDayFromSchedule(dayOfWeek);
//    TimeSlot timeslot = new TimeSlot(openingHour, closingHour);
//    assertTrue(dailySchedule.getWorkingHours().equals(timeslot));
//  }
//
//  @And("an order associated to the store")
//  public void anOrderAssociatedToTheStore() {
//    order = new Order();
//    order.setStore(store);
//  }
//
//}
