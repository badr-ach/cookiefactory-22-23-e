package fr.unice.polytech.cf.entities;

import java.time.DayOfWeek;
import java.util.*;

public class Store {

    private UUID id;
    private Stock ingredientsStock;
    private List<Cook> cooks = new ArrayList<>();
    private String name;
    private String address;
    private double taxes;
    private Schedule schedule;

    public Store() {

        schedule = new Schedule();
        Cook cook = new Cook();
        cook.getSchedule().seedSchedule();
        cooks.add(cook);
        ingredientsStock = new Stock();
        this.id = UUID.randomUUID();
    }

    public Store(String name) {
        this();
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public List<TimeSlot> getSchedule(DayOfWeek day){
        return this.schedule.getScheduledHours(day);
    }

    public void setSchedule(Schedule schedule) {
        for(Map.Entry<DayOfWeek,List<TimeSlot>> dailySchedule : schedule.getHours().entrySet()){
            this.setSchedule(dailySchedule.getValue(),dailySchedule.getKey());
        }
    }

    public List<TimeSlot> setSchedule(List<TimeSlot> openinghours, DayOfWeek day){
        return this.schedule.setHoursOfDay(openinghours,day);
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
