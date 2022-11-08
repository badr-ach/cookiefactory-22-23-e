package fr.unice.polytech.cf;

import fr.unice.polytech.cf.CookieService.Entities.Ingredient;
import fr.unice.polytech.cf.CookieService.Enums.EIngredientType;
import fr.unice.polytech.cf.StoreService.Entities.Store;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import static org.junit.Assert.*;

public class AddIngredientToStock {

    Store store;
    Store anotherStore;
    Ingredient ingredient = new Ingredient("Swift Chocolate", 1, EIngredientType.TOPPING);
   @Given("a store")
    public void newStore() {
        store = new Store();
    }

    @And("an Ingredient")
    public void anIngredient() {
        ingredient = new Ingredient("Swift Chocolate", 1, EIngredientType.TOPPING);
    }

    @Given("the Ingredient Stock does not contains the ingredient to be added")
    public void checkNotContains() {
        assertFalse(store.getIngredientStock().is_in(ingredient));
    }


    @When("the ingredient is added to the stock")
        public void addIngredient(){
            store.getIngredientStock().add(ingredient);
        }

    @Then("a new ingredient is added to the stock")
    public void ingredientIsAdded() {
        assertTrue(store.getIngredientStock().is_in(ingredient));
    }
    @Then("the stock ingredient quantity is 1")
    public void quantity() {
        assertTrue(store.getIngredientStock().getValue(ingredient).equals(1));
    }
    @Given("the Ingredient Stock already contains the ingredient to be added")
    public void alreadyin(){
    store.getIngredientStock().add(ingredient);
    }
    @Then("the stock ingredient quantity is 2")
    public void newQuantity(){
    assertTrue(store.getIngredientStock().getValue(ingredient).equals(2));
    }


    @Given("an other Store")
    public void anotherStore(){
        anotherStore = new Store();
    }


    @Then("the ingredient should not be in the stock of anotherStore")
    public void notAddedInAnotherStore(){
    store.getIngredientStock().add(ingredient);
    assertFalse(anotherStore.getIngredientStock().is_in(ingredient));
    }






}



