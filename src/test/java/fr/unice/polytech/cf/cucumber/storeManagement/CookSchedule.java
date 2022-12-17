package fr.unice.polytech.cf.cucumber.storeManagement;

import fr.unice.polytech.cf.components.StoreComponent;
import fr.unice.polytech.cf.entities.Cook;
import fr.unice.polytech.cf.entities.Schedule;
import fr.unice.polytech.cf.entities.Store;
import fr.unice.polytech.cf.entities.TimeSlot;
import fr.unice.polytech.cf.interfaces.StoreCookModifier;
import fr.unice.polytech.cf.interfaces.StoreScheduleFinder;
import fr.unice.polytech.cf.interfaces.StoreScheduleModifier;
import fr.unice.polytech.cf.services.BackOfficeService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CookSchedule {

    @Autowired
    private BackOfficeService backOfficeService;
    @Autowired
    private StoreCookModifier storeCookModifier;
    Exception caughtException;
    Cook cook;
    Store store;

    @Given("a cook with {string} on {string}")
    public void aCookWithOn(String workingHours, String days) {
        cook = backOfficeService.getStores().get(0).getCooks().get(0);
        cook.setSchedule(convertStringsToSchedule(workingHours,days));
    }

    @When("the cook's schedule is set to {string} on {string}")
    public void theCookSScheduleIsSetToOn(String workingHours, String days) {
        try {
            storeCookModifier.updateCookSchedule(cook, convertStringsToSchedule(workingHours, days));
        }catch(Exception ex){
            caughtException = ex;
        }
    }

    @Then("the cook's working hours are {string} on {string}")
    public void theCookSWorkingHoursAreOn(String workingHours, String days) {
        Schedule expected = convertStringsToSchedule(workingHours,days);
        Schedule current = cook.getSchedule();
        for(Map.Entry<DayOfWeek,List<TimeSlot>> hours : expected.getHours().entrySet()){
            assertEquals(hours.getValue(),current.getScheduledHours(hours.getKey()));
        }
    }

    @And("a Cook that works in that store with {string} on {string}")
    public void aCookThatWorksInThatStoreWithOn(String workingHours, String days) {
        cook = backOfficeService.getStores().get(0).getCooks().get(0);
        cook.setSchedule(convertStringsToSchedule(workingHours,days));
    }

    @Then("an error is thrown with message {string}")
    public void anErrorIsThrownWithMessage(String errorMessage) {
        assertEquals(errorMessage,caughtException.getMessage());
    }

    private Schedule convertStringsToSchedule(String string, String string2){
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
