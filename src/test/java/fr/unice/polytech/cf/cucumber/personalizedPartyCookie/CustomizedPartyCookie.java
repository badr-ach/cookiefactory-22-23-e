package fr.unice.polytech.cf.cucumber.personalizedPartyCookie;

import static org.junit.Assert.*;

import fr.unice.polytech.cf.repositories.StoreRepository;
import fr.unice.polytech.cf.services.CustomerService;
import fr.unice.polytech.cf.entities.ContactCoordinates;
import fr.unice.polytech.cf.entities.Cookie;
import fr.unice.polytech.cf.entities.ingredients.Dough;
import fr.unice.polytech.cf.entities.ingredients.Flavor;
import fr.unice.polytech.cf.entities.Ingredient;
import fr.unice.polytech.cf.entities.ingredients.Topping;
import fr.unice.polytech.cf.entities.enums.EIngredientType;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.enums.EOrderStatus;
import fr.unice.polytech.cf.entities.BasePartyCookie;
import fr.unice.polytech.cf.entities.enums.Occasion;
import fr.unice.polytech.cf.entities.enums.Size;
import fr.unice.polytech.cf.entities.enums.Theme;
import fr.unice.polytech.cf.entities.Cook;
import fr.unice.polytech.cf.entities.Schedule;
import fr.unice.polytech.cf.entities.Stock;
import fr.unice.polytech.cf.entities.Store;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class CustomizedPartyCookie {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private CustomerService customerSystem;


    private Cook cook;
    private Order order;
    private Store store;
    private Cookie cookie;

    private BasePartyCookie customizedPartyCookie;
    private UUID idrandom = UUID.randomUUID();
    private Exception caughException;


    @Given("a store with a cook that can make Wedding cookies with Music theme")
    public void aStoreWithACookThatCanMakeWeddingCookiesWithMusicTheme() {
        store = new Store();
        List<Occasion> occasionList = new ArrayList<>();
        occasionList.add(Occasion.WEDDING);
        List<Theme> themeList = new ArrayList<>();
        themeList.add(Theme.MUSIC);
        Schedule schedule = new Schedule();
        schedule.seedSchedule();
        store.setSchedule(schedule);
        cook = new Cook(idrandom,"cook added",schedule,new TreeMap<>(),occasionList,themeList);
        store.getCooks().add(cook);
        storeRepository.save(store,store.getId());
    }

    @And("a Basic recipe {string} with {int} minutes cooking duration")
    public void aBasicRecipeWithMinutesCookingDuration(String arg0, int arg1) {
        cookie = new Cookie();
        cookie.setPreparationDuration(Duration.ofMinutes(arg1));
        cookie.setName(arg0);
    }

    @And("that needs A Dough {string} of {int} g that costs {int} euros per unit {int} g")
    public void thatNeedsADoughOfGThatCostsEurosPerUnitG(String arg0, int arg1, int arg2, int arg3) {
        cookie.getIngredients().put(new Dough(arg0,arg2),arg1/arg3);
    }

    @And("Flavor of {string} of {int} g that costs {int} euros per unit {int} g")
    public void flavorOfOfGThatCostsEurosPerUnitG(String arg0, int arg1, int arg2, int arg3) {
        cookie.getIngredients().put(new Flavor(arg0,arg2),arg1/arg3);
    }

    @And("one Topping {string} {int} g that costs {int} euros per unit {int} g")
    public void oneToppingGThatCostsEurosPerUnitG(String arg0, int arg1, int arg2, int arg3) {
        cookie.getIngredients().put(new Topping(arg0,arg2),arg1/arg3);
    }

    @And("a margin of {int}")
    public void aPriceOfEuros(int arg0) {
        cookie.setMargin((double) arg0);
    }

    @Given("an order of a party cookie of {string} and {string} for {string} with {string}")
    public void anOrderOfAPartyCookieOfAndForWith(String arg0, String arg1, String arg2, String arg3) {
        customizedPartyCookie = new BasePartyCookie(cookie, Size.valueOf(arg1),Theme.valueOf(arg3),Occasion.valueOf(arg2));
        customizedPartyCookie.setOccasion(Occasion.valueOf(arg0));
        customizedPartyCookie.setTheme(Theme.valueOf(arg1));
        order = customerSystem.startOrder();
        customerSystem.selectStore(store,order);
        customerSystem.addCookie(customizedPartyCookie,order);
        customerSystem.selectPickUpDate(LocalDateTime.of(2022, Calendar.DECEMBER, 12, 14, 15),order);
    }

    @Given("an order of a party cookie of {string} based on Chocolala recipe for {string} with {string}")
    public void anOrderOfAPartyCookieOfBasedOnChocolalaRecipeForWith(String arg0, String arg1, String arg2) {
        customizedPartyCookie = new BasePartyCookie(cookie, Size.valueOf(arg0),Theme.valueOf(arg2),Occasion.valueOf(arg1));
        order = customerSystem.startOrder();
        customerSystem.selectStore(store,order);
        customerSystem.addCookie(customizedPartyCookie,order);
        customerSystem.selectPickUpDate(LocalDateTime.of(2022, Calendar.DECEMBER, 10, 14, 15),order);
    }

    @And("{string} added")
    public void added(String ingredients) {
        String[] ingred = ingredients.split(";");
        Map<Ingredient, Integer> addedIngredients = new HashMap<>();
        for(String ingredToken : ingred){
            String[] ingredInfo = ingredToken.split(",");
            Ingredient ingredient = new Ingredient(ingredInfo[1], Double.parseDouble(ingredInfo[2]),EIngredientType.valueOf(ingredInfo[0]));
            addedIngredients.put(ingredient, Integer.parseInt(ingredInfo[3])/100);
        }
        for(Map.Entry<Ingredient, Integer> entry : cookie.getIngredients().entrySet()){
            if(addedIngredients.containsKey(entry.getKey())){
                entry.setValue(entry.getValue() + addedIngredients.get(entry.getKey()));
            }
        }
    }

    @And("the order status is PENDING")
    public void theOrderStatusIsPENDING() {
        assertEquals(EOrderStatus.PENDING, order.getStatus());
    }

    @And("the order price is {string}")
    public void theOrderPriceIs(String arg0) {
        assertEquals(Double.parseDouble(arg0),order.getPrice(),0.1);
    }

    @And("store stock has {string}")
    public void storeStockHas(String ingredients) {
        Stock stock = store.getIngredientsStock();
        String[] ingred = ingredients.split(";");
        Map<Ingredient, Integer> currentIngredients = new HashMap<>();
        for(String ingredToken : ingred){
            String[] ingredInfo = ingredToken.split(",");
            Ingredient ingredient = new Ingredient(ingredInfo[1], Double.parseDouble(ingredInfo[2]),EIngredientType.valueOf(ingredInfo[0]));
            currentIngredients.put(ingredient, Integer.parseInt(ingredInfo[3]));
        }
        stock.add(currentIngredients);
    }

    @When("the customer proceeds to pay his party cookie order order with {string}")
    public void theCustomerProceedsToPayHisPartyCookieOrderOrderWith(String arg0) {
        try{
            ContactCoordinates contactCoordinates = new ContactCoordinates("test", "test", "test", "test");
            customerSystem.payOrder(contactCoordinates, arg0,order);
        }catch(Exception ex){
            caughException = ex;
        }
    }

    @And("an error is then thrown with the message {string}")
    public void anErrorIsThenThrownWithTheMessage(String arg0) {
        assertEquals(arg0, caughException.getMessage());
    }

    @Then("the order status becomes PAID")
    public void theOrderStatusBecomesPAID() {
        assertEquals(EOrderStatus.PAID,order.getStatus());
    }
}
