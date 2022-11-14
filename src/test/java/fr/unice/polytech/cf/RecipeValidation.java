package fr.unice.polytech.cf;
import static org.junit.Assert.*;
import fr.unice.polytech.cf.accountservice.entities.Account;
import fr.unice.polytech.cf.accountservice.enums.EAccountType;
import fr.unice.polytech.cf.cookieservice.CookieService;
import fr.unice.polytech.cf.cookieservice.entities.Cookie;
import fr.unice.polytech.cf.cookieservice.enums.ECookieStatus;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashMap;

public class RecipeValidation {
  private CookieService cookieService = new CookieService();
  private Account brandManagerAccount;
  private Account kitchenChefAccount;
  private Cookie cookieSubmitted;
  private Cookie cookieValidated;

  @Given("a SUBMITTED Cookie")
  public void aSUBMITTEDCookie() {
    cookieSubmitted = new Cookie("submittedCookie", 5, new HashMap<>());
  }

  @And("an Account with the BrandManager role")
  public void anAccountWithTheBrandManagerRole() {
    brandManagerAccount = new Account("Manager","admin", EAccountType.BRAND_MANAGER);
  }

  @And("an Account with the KitchenChef role")
  public void anAccountWithTheKitchenChefRole() {
    kitchenChefAccount = new Account("Chef","chef", EAccountType.COOK);
  }

  @When("the cookie is validated with an account with the BrandManager role")
  public void theCookieIsValidatedWithAnAccountWithTheBrandManagerRole() {
    cookieService.validateCookie(cookieSubmitted, brandManagerAccount);
  }

  @Then("the cookie status is VALIDATED")
  public void theCookieStatusIsVALIDATED() {
    assert cookieSubmitted.getStatus() == ECookieStatus.VALIDATED;
  }

  @When("the cookie is validated with an account with the KitchenChef role")
  public void theCookieIsValidatedWithAnAccountWithTheKitchenChefRole() {
    cookieService.validateCookie(cookieSubmitted, kitchenChefAccount);
  }

  @Then("the cookie status stay SUBMITTED")
  public void theCookieStatusStaySUBMITTED() {
    assert cookieSubmitted.getStatus() == ECookieStatus.SUBMITTED;
  }

  @When("the SUBMITTED cookie is set to AVAILABLE with an account with the KitchenChef role")
  public void theSUBMITTEDCookieIsSetToAVAILABLEWithAnAccountWithTheKitchenChefRole() {
    cookieService.activateCookie(cookieSubmitted, kitchenChefAccount);
  }

  @Then("the recipe status stay SUBMITTED")
  public void theRecipeStatusStaySUBMITTED() {
    assert cookieSubmitted.getStatus() == ECookieStatus.SUBMITTED;
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
}
