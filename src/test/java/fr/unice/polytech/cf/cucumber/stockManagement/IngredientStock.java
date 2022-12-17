package fr.unice.polytech.cf.cucumber.stockManagement;

import fr.unice.polytech.cf.components.IngredientStockComponent;
import fr.unice.polytech.cf.entities.Ingredient;
import fr.unice.polytech.cf.entities.enums.EIngredientType;
import fr.unice.polytech.cf.entities.Store;
import fr.unice.polytech.cf.interfaces.IngredientStockModifier;
import fr.unice.polytech.cf.repositories.CookieRepository;
import fr.unice.polytech.cf.repositories.StoreRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.hu.Ha;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.HashMap;

import static org.junit.Assert.*;

public class IngredientStock {
    @Autowired
    IngredientStockModifier ingredientStockModifier;
    @Autowired
    StoreRepository storeRepository;
    Store store;
    Store anotherStore;
    Ingredient ingredient = new Ingredient("Swift Chocolate", 1, EIngredientType.TOPPING);
   @Given("a store")
    public void newStore() {
        store = new Store();
        storeRepository.save(store,store.getId());
    }

    @And("an Ingredient")
    public void anIngredient() {
        ingredient = new Ingredient("Swift Chocolate", 1, EIngredientType.TOPPING);
    }

    @Given("the Ingredient Stock does not contains the ingredient to be added")
    public void checkNotContains() {
        assertFalse(storeRepository.findById(store.getId()).get().getIngredientsStock().contains(ingredient));
    }


    @When("the ingredient is added to the stock")
        public void addIngredient(){
        HashMap<Ingredient, Integer> ingredients = new HashMap<>();
        ingredients.put(ingredient, 1);
            ingredientStockModifier.addToStock(store,ingredients);
        }

    @Then("a new ingredient is added to the stock")
    public void ingredientIsAdded() {
        assertTrue(storeRepository.findById(store.getId()).get().getIngredientsStock().contains(ingredient));
    }
    @Then("the stock ingredient quantity is 1")
    public void quantity() {
        assertEquals(1, (int) storeRepository.findById(store.getId()).get().getIngredientsStock().getValue(ingredient));
    }
    @Given("the Ingredient Stock already contains the ingredient to be added")
    public void alreadyin(){
    HashMap<Ingredient, Integer> ingredients = new HashMap<>();
    ingredients.put(ingredient, 1);
       ingredientStockModifier.addToStock(store,ingredients);
    }
    @Then("the stock ingredient quantity is 2")
    public void newQuantity(){
        assertEquals(2, (int) storeRepository.findById(store.getId()).get().getIngredientsStock().getValue(ingredient));
    }


    @Given("an other Store")
    public void anotherStore(){

       anotherStore = new Store();
         storeRepository.save(anotherStore,anotherStore.getId());
    }


    @Then("the ingredient should not be in the stock of anotherStore")
    public void notAddedInAnotherStore(){
       HashMap<Ingredient, Integer> ingredients = new HashMap<>();
         ingredients.put(ingredient, 1);
    ingredientStockModifier.addToStock(store,ingredients);
    assertFalse(storeRepository.findById(anotherStore.getId()).get().getIngredientsStock().contains(ingredient));
    }


    @Given("the ingredient Stock already contains the ingredient to be removed with a quantity of {int}")
    public void theIngredientStockAlreadyContainsTheIngredientToBeRemovedWithAQuantityOf(int quantity) {
        HashMap<Ingredient, Integer> ingredients = new HashMap<>();
        ingredients.put(ingredient, quantity);
        ingredientStockModifier.addToStock(store,ingredients);
    }

    @When("{int} units of ingredient are removed from the stock")
    public void unitsOfIngredientAreRemovedFromTheStock(int quantity) {
       HashMap<Ingredient, Integer> ingredients = new HashMap<>();
        ingredients.put(ingredient, quantity);
        ingredientStockModifier.removeFromStock(store,ingredients);
    }

    @Then("the quantity of the ingredient in stock is {int}")
    public void theQuantityOfTheIngredientInStockIs(int quantity) {
       assertEquals(storeRepository.findById(store.getId()).get().getIngredientsStock().getAvailableQuantity(ingredient),quantity);
    }
}



