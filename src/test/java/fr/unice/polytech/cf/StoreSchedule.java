package fr.unice.polytech.cf;

import fr.unice.polytech.cf.components.StoreService;
import fr.unice.polytech.cf.entities.Schedule;
import fr.unice.polytech.cf.entities.Store;
import fr.unice.polytech.cf.entities.TimeSlot;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StoreSchedule {

    StoreService storeService = new StoreService();
    Exception caughtException;
    Store store;

    @Given("a store with {string} on {string}")
    public void aStoreWithOn(String openinghours, String day) {
        store = storeService.getStores().get(0);
        store.setSchedule(convertStringsToSchedule(openinghours,day));
    }

    @When("the store schedule is set to {string} on day {string}")
    public void theStoreScheduleIsSetToOnDay(String openinghours, String day) {
        try {
            storeService.updateStoreSchedule(store, convertStringsToSchedule(openinghours,day));
        }catch(Exception ex){
            caughtException = ex;
        }
    }

    @Then("the store opening hours are {string} on day {string}")
    public void theStoreOpeningHoursAreOnDay(String openinghours, String day) {
        assertEquals(store.getSchedule(DayOfWeek.valueOf(day)), convertStringsToSchedule(openinghours,day).getScheduledHours(DayOfWeek.valueOf(day)));
    }

    @Then("an error message is then thrown with message {string}")
    public void anErrorMessageIsThenThrownWithMessage(String arg0) {
        assertEquals(caughtException.getMessage(),arg0);
    }

    @When("the store schedule is set to {string} on {string}")
    public void theStoreScheduleIsSetToOn(String openinghours, String day) {
        try {
            storeService.updateStoreSchedule(store, convertStringsToSchedule(openinghours,day));
        }catch(Exception ex){
            caughtException = ex;
        }
    }

    @Then("the store opening hours are {string} on {string}")
    public void theStoreOpeningHoursAreOn(String openinghours, String day) {
        Schedule expected = convertStringsToSchedule(openinghours,day);
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
            String[] openingAndclosingTimes = openingHoursString[i].split(";");
            for(String s1 : openingAndclosingTimes){
                String[] openingANdCLosingTime = s1.split("-");
                TimeSlot workingSlot = new TimeSlot(openingANdCLosingTime[0], openingANdCLosingTime[1]);
                openingHours.add(workingSlot);
            }
            schedule.setHoursOfDay(openingHours,DayOfWeek.valueOf(days[i]));
        }

        return schedule;
    }
}
