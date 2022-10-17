package fr.unice.polytech.cf;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BasicOrderRetrieval {
    @Given("a store employee named {string}")
    public void aStoreEmployeeNamed(String arg0) {
    }

    @When("John marks the {string} as fulfilled")
    public void johnMarksTheAsFulfilled(String arg0) {
    }

    @Then("the order is not removed from the pending {string} list")
    public void theOrderIsNotRemovedFromThePendingList(String arg0) {
    }
}
