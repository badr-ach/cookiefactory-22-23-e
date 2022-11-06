package fr.unice.polytech.cf.StoreService.Entities;

import fr.unice.polytech.cf.StoreService.Exceptions.InvalidHourException;
import fr.unice.polytech.cf.StoreService.Exceptions.InvalidMinuteException;

public class Schedule {

    private OneDaySlot[] days;

    public Schedule() {
        days = new OneDaySlot[7];
        for ( int i = 0; i < 7; i++ ) {
            days[i] = new OneDaySlot(i, new TimeSlot(8, 30, 21, 30));
        }
    }

    public void setADayInTheSchedule(OneDaySlot oneDaySlot) throws InvalidHourException, InvalidMinuteException {
        days[oneDaySlot.getDay()].getTimeSlot().setStartHour(oneDaySlot.getTimeSlot().startHour);
        days[oneDaySlot.getDay()].getTimeSlot().setStartMinute(oneDaySlot.getTimeSlot().startMinute);
        days[oneDaySlot.getDay()].getTimeSlot().setEndHour(oneDaySlot.getTimeSlot().endHour);
        days[oneDaySlot.getDay()].getTimeSlot().setEndMinute(oneDaySlot.getTimeSlot().endMinute);
    }

    public OneDaySlot getADayFromTheSchedule(int day) {
        return days[day];
    }

    public OneDaySlot getTimeSlotOfDay(int day) {
        return days[day - 1];
    }

    public String toString() {
        String schedule = "";
        for ( int i = 0; i < 7; i++ ) {
            schedule += days[i].toString();
        }
        return schedule;
    }

    public OneDaySlot[] getDays() {
        return days;
    }

    public void setDays(OneDaySlot[] days) {
        this.days = days;
    }

}
