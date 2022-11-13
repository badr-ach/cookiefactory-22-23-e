package fr.unice.polytech.cf.storeservice.entities;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeSlot implements Comparable<TimeSlot> {

    private LocalTime startTime;
    private LocalTime endTime;

    public TimeSlot(String startTime, String endTime) {
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
    }

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeSlot(String startTime, Duration duration) {
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(startTime).plus(duration);
    }

    public TimeSlot(LocalTime startTime, Duration duration) {
        this.startTime = startTime;
        this.endTime = startTime.plus(duration);
    }

    public boolean isBetween(LocalTime timeToTest) {
        return startTime.isAfter(timeToTest) && endTime.isBefore(timeToTest);
    }

    public boolean contains(LocalTime startTime, LocalTime endTime){
        if(startTime.equals(this.startTime) && endTime.equals(this.endTime)) return  true;
        return startTime.isAfter(this.startTime) && endTime.isBefore(this.endTime);
    }

    public boolean contains(TimeSlot timeSlot){
        return contains(timeSlot.startTime,timeSlot.endTime);
    }

    public List<TimeSlot> extract(TimeSlot timeSlot){
        if(timeSlot.equals(this)) return new ArrayList<>();
        if(timeSlot.contains(timeSlot)){
            List<TimeSlot> timeSlots = new ArrayList<>();
            timeSlots.add(new TimeSlot(this.startTime,timeSlot.startTime));
            timeSlots.add(new TimeSlot(timeSlot.endTime,this.startTime));
            return timeSlots;
        }
        return null;
    }

    public TimeSlot fuse(TimeSlot timeSlot){
        if(timeSlot.startTime.equals(this.endTime)){
            return new TimeSlot(this.startTime,timeSlot.endTime);
        }else if(timeSlot.endTime.equals(this.startTime)){
            return new TimeSlot(timeSlot.startTime,this.endTime);
        }
        return null;
    }

    public Duration duration() {
        return Duration.between(startTime, endTime);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }


    public String toString() {
        return "TimeSlot: " + startTime + " - " + endTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TimeSlot)) return false;
        if ((TimeSlot) obj == this) return true;
        if (this.getStartTime().toNanoOfDay() == ((TimeSlot) obj).getStartTime().toNanoOfDay()
                && this.getEndTime().toNanoOfDay() == ((TimeSlot) obj).getEndTime().toNanoOfDay()) return true;
        return false;
    }

    @Override
    public int compareTo(TimeSlot o) {
        if(this.startTime.isBefore(o.startTime)) return 1;
        if(this.startTime.isAfter(o.startTime)) return -1;
        return 0;
    }
}
