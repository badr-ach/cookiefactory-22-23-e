package fr.unice.polytech.cf;
import static org.junit.Assert.*;
import fr.unice.polytech.cf.accountservice.entities.Account;
import fr.unice.polytech.cf.accountservice.enums.EAccountType;
import fr.unice.polytech.cf.cookieservice.CookieService;
import fr.unice.polytech.cf.cookieservice.entities.Cookie;
import fr.unice.polytech.cf.cookieservice.enums.ECookieStatus;
import fr.unice.polytech.cf.orderservice.exceptions.ImpossibleOrderCancelingException;
import fr.unice.polytech.cf.storeservice.exceptions.CookieNotSubmittedException;
import fr.unice.polytech.cf.storeservice.exceptions.CookieNotValidatedException;
import fr.unice.polytech.cf.storeservice.exceptions.UnhautorizedRoleException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashMap;

public class CookieManagement {
  private CookieService cookieService = new CookieService();
  private Account brandManagerAccount;
  private Account kitchenChefAccount;
  private Cookie cookieSubmitted;
  private Cookie cookieValidated;
  private Cookie cookieDefault;

  @Given("a SUBMITTED cookie")
  public void aSUBMITTEDCookie() {
    cookieSubmitted = new Cookie("submittedCookie", 5, new HashMap<>());
    cookieSubmitted.setStatus(ECookieStatus.SUBMITTED);
  }

  @And("an Account with the BrandManager role")
  public void anAccountWithTheBrandManagerRole() {
    brandManagerAccount = new Account("Manager","admin", EAccountType.BRAND_MANAGER);
  }

  @And("an Account with the KitchenChef role")
  public void anAccountWithTheKitchenChefRole() {
    kitchenChefAccount = new Account("Chef","chef", EAccountType.CHEF);
  }

  @When("the cookie is validated with an account with the BrandManager role")
  public void theCookieIsValidatedWithAnAccountWithTheBrandManagerRole() {
    cookieService.validateCookie(cookieSubmitted, brandManagerAccount);
  }

  @Then("the cookie status is VALIDATED")
  public void theCookieStatusIsVALIDATED() {
    assertEquals(cookieSubmitted.getStatus(),ECookieStatus.VALIDATED);
  }

  @When("the cookie is validated with an account with the KitchenChef role")
  public void theCookieIsValidatedWithAnAccountWithTheKitchenChefRole() {
    Exception exception = assertThrows(UnhautorizedRoleException.class,()-> cookieService.validateCookie(cookieSubmitted,kitchenChefAccount));
    assertEquals("Your account can't validate a cookie",exception.getMessage());
  }

  @Then("the cookie status stay SUBMITTED")
  public void theCookieStatusStaySUBMITTED() {
    assertEquals(cookieSubmitted.getStatus(),ECookieStatus.SUBMITTED);
  }

  @When("the SUBMITTED cookie is set to AVAILABLE with an account with the KitchenChef role")
  public void theSUBMITTEDCookieIsSetToAVAILABLEWithAnAccountWithTheKitchenChefRole() {
    Exception exception = assertThrows(CookieNotValidatedException.class,()-> cookieService.activateCookie(cookieSubmitted,kitchenChefAccount));
    assertEquals("This cookie isn't validate by the brand manager",exception.getMessage());
  }

  @Then("the recipe status stay SUBMITTED")
  public void theRecipeStatusStaySUBMITTED() {
    assertEquals(cookieSubmitted.getStatus(),ECookieStatus.SUBMITTED);
  }


  @Given("a VALIDATED cookie")
  public void aVALIDATEDCookie() {
    cookieValidated = new Cookie("validatedCookie", 3, new HashMap<>());
    cookieValidated.setStatus(ECookieStatus.VALIDATED);
  }

  @When("the VALIDATED cookie is set to AVAILABLE with an account with the KitchenChef role")
  public void theVALIDATEDCookieIsSetToAVAILABLEWithAnAccountWithTheKitchenChefRole() {
    cookieService.activateCookie(cookieValidated, kitchenChefAccount);
  }

  @Then("the recipe status becomes AVAILABLE")
  public void theRecipeStatusBecomesAVAILABLE() {
    assertEquals(cookieValidated.getStatus(),ECookieStatus.ACTIVE);
  }

  @Then("an error message is thrown with {string}")
  public void anErrorMessageIsThrownWith(String args) {
  }
  @Given("a DEFAULT cookie")
  public void aDefaultCookie(){
    cookieDefault = new Cookie("DefaultCookie",5,new HashMap<>());
    cookieDefault.setStatus(ECookieStatus.DEFAULT);
  }
  @Then("The SUBMITTED cookie is submitted again")
  public void submitAsubmittedCookie(){
    Exception exception = assertThrows(CookieNotSubmittedException.class,()-> cookieService.submitCookie(cookieSubmitted,kitchenChefAccount));
    assertEquals("This cookie can't be submitted to the brand manager",exception.getMessage());
  }
  @When("The DEFAULT cookie is SUBMITTED by a kitchenChef")
    public void submissionOfADefaultCookie(){
    cookieService.submitCookie(cookieDefault,kitchenChefAccount);
}
  @Then("the cookie status change to SUBMITTED")
    public void changeStatutsToSubmitted(){
    assertEquals(cookieDefault.getStatus(),ECookieStatus.SUBMITTED);
}
}
