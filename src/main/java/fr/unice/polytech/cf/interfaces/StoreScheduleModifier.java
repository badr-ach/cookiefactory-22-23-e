package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Cook;
import fr.unice.polytech.cf.entities.Schedule;
import fr.unice.polytech.cf.entities.Store;
import fr.unice.polytech.cf.entities.TimeSlot;

import java.time.DayOfWeek;
import java.util.List;

public interface StoreScheduleModifier {
    List<TimeSlot> updateStoreSchedule(Store store, List<TimeSlot> timeSlots, DayOfWeek day);

    void updateStoreSchedule(Store store, Schedule schedule);
}
