package fr.unice.polytech.cf.storeservice.entities;

import fr.unice.polytech.cf.storeservice.exceptions.InvalidScheduleException;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.*;

public class Schedule {

    private SortedMap<DayOfWeek, List<TimeSlot>> hours;

    public Schedule(){
        hours = new TreeMap<>();
    }

    public Schedule(SortedMap<DayOfWeek, List<TimeSlot>> workingHours) {
        this.hours = workingHours;
    }

    public List<TimeSlot> getScheduledHours(DayOfWeek day){
        return hours.get(day);
    }

    public List<TimeSlot> setHoursOfDay(List<TimeSlot> hours, DayOfWeek day){
        for(int i = 0 ;i  < hours.size() ;i++){
            for (TimeSlot hour : hours) {
                if (hours.get(i).isBetween(hour.getEndTime()) ||
                        hours.get(i).isBetween(hour.getStartTime()))
                    throw new InvalidScheduleException("Attempt to assign incoherent schedule");
            }
        }
        return this.hours.put(day,hours);
    }


    public boolean hasWithinIt(DayOfWeek day, List<TimeSlot> slots){
        List<TimeSlot> worktimesOfDay = getScheduledHours(day);
        int i =0,j=0;
        while (j < slots.size() && i < worktimesOfDay.size()) {
            if(worktimesOfDay.get(i).contains(slots.get(j))){
                j++;
            }else{
                i++;
            }
        }
        return j == slots.size();
    }
    public boolean hasWithinIt(Schedule schedule){
        for(Map.Entry<DayOfWeek,List<TimeSlot>> daySchedule : schedule.getHours().entrySet()){
            if(!hasWithinIt(daySchedule.getKey(),daySchedule.getValue())) return false;
        }
        return true;
    }

    public SortedMap<DayOfWeek, List<TimeSlot>> getHours() {
        return hours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Schedule schedule)) return false;
        return hours.equals(schedule.hours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours);
    }

    public void seedSchedule(){
        hours.put(DayOfWeek.MONDAY, new ArrayList<>(
                Collections.singleton(new TimeSlot("08:00", "22:00"))));
        hours.put(DayOfWeek.TUESDAY, new ArrayList<>(
                Collections.singleton(new TimeSlot("08:00", "22:00"))));
        hours.put(DayOfWeek.WEDNESDAY, new ArrayList<>(
                Collections.singleton(new TimeSlot("08:00", "22:00"))));
        hours.put(DayOfWeek.THURSDAY, new ArrayList<>(
                Collections.singleton(new TimeSlot("08:00", "22:00"))));
        hours.put(DayOfWeek.FRIDAY, new ArrayList<>(
                Collections.singleton(new TimeSlot("08:00", "22:00"))));
        hours.put(DayOfWeek.SATURDAY, new ArrayList<>(
                Collections.singleton(new TimeSlot("08:00", "22:00"))));
        hours.put(DayOfWeek.SUNDAY, new ArrayList<>(
                Collections.singleton(new TimeSlot("08:00", "22:00"))));
    }
}
