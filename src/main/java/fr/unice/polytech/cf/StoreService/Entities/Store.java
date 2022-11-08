package fr.unice.polytech.cf.StoreService.Entities;

import fr.unice.polytech.cf.CookieService.Entities.Ingredient;
import fr.unice.polytech.cf.IngredientStockService.IngredientStockService;
import fr.unice.polytech.cf.OrderService.Entities.Order;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class Store {
  private IngredientStockService ingredientStock;
  private ArrayList<Cook> cooks = new ArrayList<Cook>();
  private String name;
  private String address;
  private double taxes;
  private int id;
  private static int ID = 0;
  private WeeklySchedule weeklySchedule;

  public Store() {

    weeklySchedule = new WeeklySchedule();
    Cook cook = new Cook(weeklySchedule);
    cook.setId(getCooks().size()+1);
    cooks.add(cook);
    ingredientStock = new IngredientStockService();

    this.id = ID;
    ID++;
  }

  public Store(String name) {
    this();
    this.name = name;
  }

  public int getId() {
    return id;
  }

  // should probably be not allowed in production
  public void setId(int id) {
    this .id = id;
  }

  public ArrayList<Cook> getCooks() {
    return cooks;
  }

  public void setCooks(ArrayList<Cook> cooks) {
    this.cooks = cooks;
  }

  public WeeklySchedule getWeeklySchedule() {
    return weeklySchedule;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public double getTaxes() {
    return taxes;
  }

  public void setTaxes(double taxes) {
    this.taxes = taxes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Store store)) return false;
    return id == store.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public Optional<Cook> getAssignedCook(Order order) {
    return cooks.stream().filter(cook -> cook.hasOrder(order)).findFirst();
  }
  public IngredientStockService getIngredientStock(){
    return ingredientStock;
  }
}
