package fr.unice.polytech.cf.storeservice.entities;

import java.time.DayOfWeek;
import java.util.*;

public class Schedule {

    private SortedMap<DayOfWeek, List<TimeSlot>> workingHours;

    public Schedule(){
        workingHours = new TreeMap<>();
    }

    public Schedule(SortedMap<DayOfWeek, List<TimeSlot>> workingHours) {
        this.workingHours = workingHours;
    }

    public List<TimeSlot> getWorkingHours(DayOfWeek day){
        return workingHours.get(day);
    }

    public void seedSchedule(){
        workingHours.put(DayOfWeek.MONDAY, new ArrayList<>(
                Collections.singleton(new TimeSlot("08:00", "22:00"))));
        workingHours.put(DayOfWeek.TUESDAY, new ArrayList<>(
                Collections.singleton(new TimeSlot("08:00", "22:00"))));
        workingHours.put(DayOfWeek.WEDNESDAY, new ArrayList<>(
                Collections.singleton(new TimeSlot("08:00", "22:00"))));
        workingHours.put(DayOfWeek.THURSDAY, new ArrayList<>(
                Collections.singleton(new TimeSlot("08:00", "22:00"))));
        workingHours.put(DayOfWeek.FRIDAY, new ArrayList<>(
                Collections.singleton(new TimeSlot("08:00", "22:00"))));
        workingHours.put(DayOfWeek.SATURDAY, new ArrayList<>(
                Collections.singleton(new TimeSlot("08:00", "22:00"))));
        workingHours.put(DayOfWeek.SUNDAY, new ArrayList<>(
                Collections.singleton(new TimeSlot("08:00", "22:00"))));
    }
}
