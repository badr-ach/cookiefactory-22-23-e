package fr.unice.polytech.cf;

import fr.unice.polytech.cf.storeservice.StoreService;
import fr.unice.polytech.cf.storeservice.entities.Cook;
import fr.unice.polytech.cf.storeservice.entities.Schedule;
import fr.unice.polytech.cf.storeservice.entities.Store;
import fr.unice.polytech.cf.storeservice.entities.TimeSlot;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CookSchedule {

    StoreService storeService = new StoreService();
    Exception caughtException;
    Cook cook;
    Store store;


    @Given("a cook with {string} on {string}")
    public void aCookWithOn(String workinghours, String days) {
        cook = storeService.getStores().get(0).getCooks().get(0);
        cook.setSchedule(convertStringsToSchedule(workinghours,days));
    }

    @When("the cook's schedule is set to {string} on {string}")
    public void theCookSScheduleIsSetToOn(String workinghours, String days) {
        try {
            storeService.updateCookSchedule(cook, convertStringsToSchedule(workinghours, days));
        }catch(Exception ex){
            caughtException = ex;
        }
    }

    @Then("the cook's working hours are {string} on {string}")
    public void theCookSWorkingHoursAreOn(String workinghours, String days) {
        Schedule expected = convertStringsToSchedule(workinghours,days);
        Schedule current = cook.getSchedule();
        for(Map.Entry<DayOfWeek,List<TimeSlot>> hours : expected.getHours().entrySet()){
            assertEquals(hours.getValue(),current.getWorkingHours(hours.getKey()));
        }
    }

    @And("a Cook that works in that store with {string} on {string}")
    public void aCookThatWorksInThatStoreWithOn(String workinghours, String days) {
        cook = storeService.getStores().get(0).getCooks().get(0);
        cook.setSchedule(convertStringsToSchedule(workinghours,days));
    }

    @Then("an error is thrown with message {string}")
    public void anErrorIsThrownWithMessage(String errmsg) {
        assertEquals(errmsg,caughtException.getMessage());
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
