package fr.unice.polytech.cf.storeservice.entities;

import fr.unice.polytech.cf.orderservice.entities.Order;
import fr.unice.polytech.cf.orderservice.entities.OrderItem;
import fr.unice.polytech.cf.orderservice.exceptions.OrderNotFoundException;
import fr.unice.polytech.cf.partycookieservice.Requirement;
import fr.unice.polytech.cf.partycookieservice.enums.Occasion;
import fr.unice.polytech.cf.partycookieservice.enums.Theme;
import fr.unice.polytech.cf.storeservice.exceptions.TimeslotUnavailableException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


public class Cook {

    private int id;
    private String name;
    private Schedule schedule;
    private SortedMap<LocalDate, List<Order>> assignments;

    private List<Occasion> occasions;
    private List<Theme> themes;

    public Cook(){
        schedule = new Schedule();
        assignments = new TreeMap<>();
        occasions = new ArrayList<>();
        themes = new ArrayList<>();
    }

    public Cook(int id, String name, Schedule schedule, SortedMap<LocalDate, List<Order>> assignments) {
        this.id = id;
        this.name = name;
        this.schedule = schedule;
        this.assignments = assignments;
    }

    public Cook(int id, String name, Schedule schedule,
                SortedMap<LocalDate, List<Order>> assignments, List<Occasion> occasions, List<Theme> themes) {
        this(id, name, schedule, assignments);
        this.occasions = occasions;
        this.themes = themes;
    }


    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name){this.name = name;}

    public List<Order> getWorkLoadOfTheDay(LocalDate date){
        return assignments.get(date);
    }

    public Map<Order,TimeSlot> getWorkOfTheDay(LocalDate date){
        Map<Order,TimeSlot> workOfTheDay = new HashMap<>();
        List<Order> orders = assignments.get(date);
        if(orders != null) {
            for (Order order : orders) {
                LocalTime startTime = LocalTime.from(order.getRetrievalDateTime().
                        minus(order.getPreparationDuration()));
                LocalTime endTime = LocalTime.from(order.getRetrievalDateTime());
                workOfTheDay.put(order, new TimeSlot(startTime, endTime));
            }
        }
        return workOfTheDay;
    }

    public List<TimeSlot> getOccupiedTimeSlotsOfTheDay(LocalDate date){
        Map<Order,TimeSlot> workOfTheDay = getWorkOfTheDay(date);
        List<TimeSlot> occupiedHours = new ArrayList<>();
        for(Map.Entry<Order, TimeSlot> workUnit : workOfTheDay.entrySet()){
            occupiedHours.add(workUnit.getValue());
        }
        return occupiedHours;
    }

    public List<TimeSlot> getWorkingHoursOfTheDay(LocalDate date){
        return schedule.getScheduledHours(date.getDayOfWeek());
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public List<TimeSlot> setSchedule(List<TimeSlot> workinghours,DayOfWeek day){
        return this.schedule.setHoursOfDay(workinghours,day);
    }

    public void setSchedule(Schedule schedule) {
        for(Map.Entry<DayOfWeek,List<TimeSlot>> dailySchedule : schedule.getHours().entrySet()){
            this.setSchedule(dailySchedule.getValue(),dailySchedule.getKey());
        }
    }

    public void assignOrder(Order order){
        LocalDate date = LocalDate.from(order.getRetrievalDateTime());
        List<Order> orders;
        if(getWorkLoadOfTheDay(date) == null) {
            orders = new ArrayList<>();
            orders.add(order);
            assignments.put(date,orders);
        }else{
            orders = getWorkLoadOfTheDay(date);
            if(orders.stream().anyMatch(o -> o.getRetrievalDateTime().
                    equals(order.getRetrievalDateTime())))
                throw new TimeslotUnavailableException("An order is already " +
                        "assigned for at this hour");
            orders.add(order);
            assignments.put(date,orders);
        }
    }

    public void removeOrder(Order order){
        LocalDate date = LocalDate.from(order.getRetrievalDateTime());
        if(getWorkLoadOfTheDay(date) == null) {
            throw new OrderNotFoundException("Order to be removed not found");
        }else{
            List<Order> orders = getWorkLoadOfTheDay(date);
            orders.remove(order);
            assignments.put(date,orders);
        }
    }

    public boolean hasOrder(Order order){
        if(order != null) {
            LocalDate date = LocalDate.from(order.getRetrievalDateTime());
            return getWorkLoadOfTheDay(date).contains(order);
        }
        return false;
    }

    public boolean canPrepare(Order order){

        for(OrderItem oi : order.getOrderItems()){
            for(Requirement req : oi.getCookie().getRequirements()){
                if(!occasions.contains(req) && !themes.contains(req)) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public SortedMap<LocalDate, List<Order>> getAssignments() {
        return assignments;
    }

    public void setAssignments(SortedMap<LocalDate, List<Order>> assignments) {
        this.assignments = assignments;
    }

    public List<Occasion> getOccasions() {
        return occasions;
    }

    public void setOccasions(List<Occasion> occasions) {
        this.occasions = occasions;
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }
}
