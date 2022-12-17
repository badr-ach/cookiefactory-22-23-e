package fr.unice.polytech.cf.cucumber.storeManagement;


import fr.unice.polytech.cf.entities.Schedule;
import fr.unice.polytech.cf.entities.Store;
import fr.unice.polytech.cf.entities.TimeSlot;
import fr.unice.polytech.cf.interfaces.StoreScheduleModifier;
import fr.unice.polytech.cf.services.BackOfficeService;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StoreScheduling {

    @Autowired
    private BackOfficeService backOfficeService;
    @Autowired
    private StoreScheduleModifier storeScheduleModifier;


    Exception caughtException;
    Store store;

    @ParameterType(value = ".+")
    public String paramType1(String... s) {
        if (s.length > 0) {
            return s[0];
        }
        return null;
    }

    @Given("a store with {string} on {string}")
    public void aStoreWithOn(String openingHours, String day) {
        store = backOfficeService.getStores().get(0);
        store.setSchedule(convertStringsToSchedule(openingHours,day));
    }

    @When("the store schedule is set to {string} on day {string}")
    public void theStoreScheduleIsSetToOnDay(String openinghours, String day) {
        try {
            storeScheduleModifier.updateStoreSchedule(store, convertStringsToSchedule(openinghours,day));
        }catch(Exception ex){
            caughtException = ex;
        }
    }

    @Then("the store opening hours are {string} on day {string}")
    public void theStoreOpeningHoursAreOnDay(String openingHours, String day) {
        assertEquals(store.getSchedule(DayOfWeek.valueOf(day)), convertStringsToSchedule(openingHours,day).getScheduledHours(DayOfWeek.valueOf(day)));
    }

    @Then("an error message is then thrown with message {string}")
    public void anErrorMessageIsThenThrownWithMessage(String arg0) {
        assertEquals(caughtException.getMessage(),arg0);
    }

    @When("the store schedule is set to {string} on {string}")
    public void theStoreScheduleIsSetToOn(String openingHours, String day) {
        try {
            storeScheduleModifier.updateStoreSchedule(store, convertStringsToSchedule(openingHours,day));
        }catch(Exception ex){
            caughtException = ex;
        }
    }

    @Then("the store opening hours are {string} on {string}")
    public void theStoreOpeningHoursAreOn(String openingHours, String day) {
        Schedule expected = convertStringsToSchedule(openingHours,day);
        Schedule current = store.getSchedule();
        for(Map.Entry<DayOfWeek,List<TimeSlot>> hours : expected.getHours().entrySet()){
            assertEquals(hours.getValue(),current.getScheduledHours(hours.getKey()));
        }
    }


    public Schedule convertStringsToSchedule(String string, String string2){
        String[] openingHoursString = string.split(", ");
        String[] days = string2.split(",");
        Schedule schedule = new Schedule();
        for (int i = 0 ; i < openingHoursString.length ; i++) {
            List<TimeSlot> openingHours = new ArrayList<>();
            String[] openingAndClosingTimes = openingHoursString[i].split(";");
            for(String s1 : openingAndClosingTimes){
                String[] openingAndCLosingTime = s1.split("-");
                TimeSlot workingSlot = new TimeSlot(openingAndCLosingTime[0], openingAndCLosingTime[1]);
                openingHours.add(workingSlot);
            }
            schedule.setHoursOfDay(openingHours,DayOfWeek.valueOf(days[i]));
        }
        return schedule;
    }
}
