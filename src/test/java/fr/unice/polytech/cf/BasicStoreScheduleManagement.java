package fr.unice.polytech.cf;

import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;
import fr.unice.polytech.cf.OrderService.Exceptions.OrderNotFoundException;
import fr.unice.polytech.cf.StoreService.Entities.OneDaySlot;
import fr.unice.polytech.cf.StoreService.Entities.Schedule;
import fr.unice.polytech.cf.StoreService.Entities.Store;
import fr.unice.polytech.cf.StoreService.Entities.TimeSlot;
import fr.unice.polytech.cf.StoreService.Exceptions.InvalidHourException;
import fr.unice.polytech.cf.StoreService.Exceptions.InvalidMinuteException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BasicStoreScheduleManagement {

    Store myStore;
    Schedule currentSchedule;

    @Given("a store")
    public void aStore() {
        myStore = new Store();
    }


    @Given("a current store schedule")
    public void aCurrentStoreSchedule() {
        currentSchedule = new Schedule();
    }

    @When("the store manager tries to update the schedule with valid data from the new one")
    public void theStoreManagerTriesToUpdateTheScheduleWithValidDataFromTheNewOne() throws InvalidMinuteException, InvalidHourException {
        TimeSlot timeSlot = new TimeSlot(12, 45, 23, 59);
        OneDaySlot monday = new OneDaySlot(0, timeSlot);
        assertEquals(currentSchedule.getADayFromTheSchedule(0).getTimeSlot().getStartHour(), 8);
        assertEquals(currentSchedule.getADayFromTheSchedule(0).getTimeSlot().getStartMinute(), 30);
        currentSchedule.setADayInTheSchedule(monday);
    }

    @Then("the store schedule is correctly updated")
    public void theStoreScheduleIsCorrectlyUpdated() {
        assertEquals(currentSchedule.getADayFromTheSchedule(0).getTimeSlot().getStartHour(), 12);
        assertEquals(currentSchedule.getADayFromTheSchedule(0).getTimeSlot().getStartMinute(), 45);
    }

    @When("the store manager tries to update the schedule with invalid data from the new one")
    public void theStoreManagerTriesToUpdateTheScheduleWithInvalidDataFromTheNewOne() {
        TimeSlot timeSlot = new TimeSlot(26, 45, 23, 59);
        OneDaySlot wednesday = new OneDaySlot(2, timeSlot);
        assertThrows(InvalidHourException.class, () -> currentSchedule.setADayInTheSchedule(wednesday));
    }

    @Then("the previous schedule is kept")
    public void thePreviousScheduleIsKept() {
        TimeSlot timeSlot = new TimeSlot(26, 45, 23, 59);
        OneDaySlot wednesday = new OneDaySlot(2, timeSlot);
        assertThrows(InvalidHourException.class, () -> currentSchedule.setADayInTheSchedule(wednesday));
        assertEquals(currentSchedule.getADayFromTheSchedule(0).getTimeSlot().getStartHour(), 8);
        assertEquals(currentSchedule.getADayFromTheSchedule(0).getTimeSlot().getStartMinute(), 30);
    }

    @Then("a exception is thrown")
    public void aExceptionIsThrown() {
    }
}
