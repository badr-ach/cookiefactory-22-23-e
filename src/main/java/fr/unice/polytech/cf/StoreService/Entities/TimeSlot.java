package fr.unice.polytech.cf.StoreService.Entities;

import fr.unice.polytech.cf.StoreService.Exceptions.InvalidHourException;
import fr.unice.polytech.cf.StoreService.Exceptions.InvalidMinuteException;

public class TimeSlot {

    public int dayOfWeek, startHour, startMinute, endHour, endMinute;

    public TimeSlot(int startHour, int startMinute, int endHour, int endMinute) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    public TimeSlot() {
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) throws InvalidHourException {
        if ( startHour >= 0 && startHour <= 23 ) {
            this.startHour = startHour;
        }
        else throw new InvalidHourException("Invalid Hour Input");
    }

    public void setStartMinute(int startMinute) throws InvalidMinuteException {
        if ( startMinute >= 0 && startMinute <= 59 ) {
            this.startMinute = startMinute;
        }
        else throw new InvalidMinuteException("Invalid Minute Input");
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) throws InvalidHourException {
        if ( endHour >= 0 && endHour <= 23 ) {
            this.endHour = endHour;
        }
        else throw new InvalidHourException("Invalid Hour Input");
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) throws InvalidMinuteException {
        if ( endMinute >= 0 && endMinute <= 59 ) {
            this.endMinute = endMinute;
        }
        else throw new InvalidMinuteException("Invalid Minute Input");
    }

}
