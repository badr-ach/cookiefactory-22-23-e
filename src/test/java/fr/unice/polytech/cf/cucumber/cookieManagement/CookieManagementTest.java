package fr.unice.polytech.cf.cucumber.cookieManagement;

import static org.junit.Assert.*;

import fr.unice.polytech.cf.entities.Account;
import fr.unice.polytech.cf.entities.enums.EAccountType;
import fr.unice.polytech.cf.entities.Cookie;
import fr.unice.polytech.cf.entities.enums.ECookieStatus;
import fr.unice.polytech.cf.exceptions.CookieNotSubmittedException;
import fr.unice.polytech.cf.exceptions.CookieNotValidatedException;
import fr.unice.polytech.cf.exceptions.UnauthorizedRoleException;
import fr.unice.polytech.cf.interfaces.CookieRecipeCreator;
import fr.unice.polytech.cf.interfaces.CookieRecipeModifier;
import fr.unice.polytech.cf.interfaces.CookieRecipeValidator;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.*;

import java.util.HashMap;

public class CookieManagementTest {
    @Autowired
    private CookieRecipeValidator cookieRecipeValidator;
    @Autowired
    private CookieRecipeCreator cookieRecipeCreator;
    @Autowired
    private CookieRecipeModifier cookieRecipeModifier;
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
        brandManagerAccount = new Account("Manager", "admin", EAccountType.BRAND_MANAGER);
    }

    @And("an Account with the KitchenChef role")
    public void anAccountWithTheKitchenChefRole() {
        kitchenChefAccount = new Account("Chef", "chef", EAccountType.CHEF);
    }

    @When("the cookie is validated with an account with the BrandManager role")
    public void theCookieIsValidatedWithAnAccountWithTheBrandManagerRole() {
        cookieRecipeValidator.validateCookie(cookieSubmitted, brandManagerAccount);
    }

    @Then("the cookie status is VALIDATED")
    public void theCookieStatusIsVALIDATED() {
        assertEquals(cookieSubmitted.getStatus(), ECookieStatus.VALIDATED);
    }

    @When("the cookie is validated with an account with the KitchenChef role")
    public void theCookieIsValidatedWithAnAccountWithTheKitchenChefRole() {
        Exception exception = assertThrows(UnauthorizedRoleException.class, () -> cookieRecipeValidator.validateCookie(cookieSubmitted, kitchenChefAccount));
        assertEquals("Your account can't validate a cookie", exception.getMessage());
    }

    @Then("the cookie status stay SUBMITTED")
    public void theCookieStatusStaySUBMITTED() {
        assertEquals(cookieSubmitted.getStatus(), ECookieStatus.SUBMITTED);
    }

    @When("the SUBMITTED cookie is set to AVAILABLE with an account with the KitchenChef role")
    public void theSUBMITTEDCookieIsSetToAVAILABLEWithAnAccountWithTheKitchenChefRole() {
        Exception exception = assertThrows(CookieNotValidatedException.class, () -> cookieRecipeModifier.activateCookie(cookieSubmitted, kitchenChefAccount));
        assertEquals("This cookie isn't validate by the brand manager", exception.getMessage());
    }

    @Then("the recipe status stay SUBMITTED")
    public void theRecipeStatusStaySUBMITTED() {
        assertEquals(cookieSubmitted.getStatus(), ECookieStatus.SUBMITTED);
    }

    @Given("a VALIDATED cookie")
    public void aVALIDATEDCookie() {
        cookieValidated = new Cookie("validatedCookie", 3, new HashMap<>());
        cookieValidated.setStatus(ECookieStatus.VALIDATED);
    }

    @When("the VALIDATED cookie is set to AVAILABLE with an account with the KitchenChef role")
    public void theVALIDATEDCookieIsSetToAVAILABLEWithAnAccountWithTheKitchenChefRole() {
        cookieRecipeModifier.activateCookie(cookieValidated, kitchenChefAccount);
    }

    @Then("the recipe status becomes AVAILABLE")
    public void theRecipeStatusBecomesAVAILABLE() {
        assertEquals(cookieValidated.getStatus(), ECookieStatus.ACTIVE);
    }

    @Then("an error message is thrown with {string}")
    public void anErrorMessageIsThrownWith(String args) {
    }

    @Given("a DEFAULT cookie")
    public void aDefaultCookie() {
        cookieDefault = new Cookie("DefaultCookie", 5, new HashMap<>());
        cookieDefault.setStatus(ECookieStatus.DRAFT);
    }

    @Then("The SUBMITTED cookie is submitted again")
    public void submitAsubmittedCookie() {
        Exception exception = assertThrows(CookieNotSubmittedException.class, () -> cookieRecipeCreator.submitCookie(cookieSubmitted, kitchenChefAccount));
        assertEquals("This cookie can't be submitted to the brand manager", exception.getMessage());
    }

    @When("The DEFAULT cookie is SUBMITTED by a kitchenChef")
    public void submissionOfADefaultCookie() {
        cookieRecipeCreator.submitCookie(cookieDefault, kitchenChefAccount);
    }

    @Then("the cookie status change to SUBMITTED")
    public void changeStatutsToSubmitted() {
        assertEquals(cookieDefault.getStatus(), ECookieStatus.SUBMITTED);
    }
}
