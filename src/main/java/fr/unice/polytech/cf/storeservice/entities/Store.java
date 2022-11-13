package fr.unice.polytech.cf.storeservice.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Store {
    private Stock ingredientsStock;
    private List<Cook> cooks = new ArrayList<>();
    private String name;
    private String address;
    private double taxes;
    private int id;
    private static int ID = 0;
    private Schedule schedule;

    public Store() {

        schedule = new Schedule();
        Cook cook = new Cook();
        cook.setId(getCooks().size() + 1);
        cook.getSchedule().seedSchedule();
        cooks.add(cook);
        ingredientsStock = new Stock();

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
    public void setId(int id) {
        this.id = id;
    }

    public List<Cook> getCooks() {
        return cooks;
    }

    public void setCooks(List<Cook> cooks) {
        this.cooks = cooks;
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

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
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

    public Stock getIngredientsStock() {
        return ingredientsStock;
    }
}
