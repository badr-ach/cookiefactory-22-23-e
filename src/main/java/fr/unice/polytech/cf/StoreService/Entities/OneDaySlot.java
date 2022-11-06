package fr.unice.polytech.cf.StoreService.Entities;

public class OneDaySlot {

    public static final int
            MONDAY  = 0,
            TUESDAY = 1,
            WEDNESDAY = 2,
            THURSDAY = 3,
            FRIDAY = 4,
            SATURDAY = 5,
            SUNDAY = 6;

    private int day;
    private TimeSlot timeSlot;

    public OneDaySlot(int day, TimeSlot timeSlot) {
        this.day = day;
        this.timeSlot = timeSlot;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String toString() {
        return day + " : " + timeSlot.startHour + ":" + timeSlot.startMinute + " - " + timeSlot.endHour + ":" +
                timeSlot.endMinute;
    }

}
