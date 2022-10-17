package fr.unice.polytech.cf;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BasicCookAssignment {
    @Given("an order has been submitted to be cooked")
    public void anOrderHasBeenSubmittedToBeCooked() {
    }

    @Given("the list of free cooks that are working in the store")
    public void theListOfFreeCooksThatAreWorkingInTheStore() {
    }

    @And("the list is not empty")
    public void theListIsNotEmpty() {
    }

    @Then("the order is assigned to one and one only cook of the free cooks")
    public void theOrderIsAssignedToOneAndOneOnlyCookOfTheFreeCooks() {
    }

    @When("A cook wants to retrieve the orders that he has to prepare")
    public void aCookWantsToRetrieveTheOrdersThatHeHasToPrepare() {
    }

    @Then("the orders that were assigned to this cook are returned")
    public void theOrdersThatWereAssignedToThisCookAreReturned() {
    }
}
