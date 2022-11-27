package fr.unice.polytech.cf.storeservice.entities;

import fr.unice.polytech.cf.storeservice.exceptions.InvalidStoreException;
import fr.unice.polytech.cf.orderservice.entities.Order;
import fr.unice.polytech.cf.orderservice.exceptions.InvalidRetrievalDateException;
import fr.unice.polytech.cf.storeservice.exceptions.TimeslotUnavailableException;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class Scheduler {

    public Cook assignOrderToACook(Order order) {
        if (order.getStore() == null) throw new InvalidStoreException("Store not specified");
        if (order.getRetrievalDateTime() == null)
            throw new InvalidRetrievalDateException("Retrieval time not specified");
        List<Cook> storeCooks = order.getStore().getCooks();

        TimeSlot preparationTimeSlot = new TimeSlot(LocalTime.from(order.getRetrievalDateTime()).
                minus(order.getPreparationDuration()),
                LocalTime.from(order.getRetrievalDateTime()));
        LocalDateTime retrievalDateTime = order.getRetrievalDateTime();
        List<Cook> availableCooks = getAvailableCooksForRetrievalTime(retrievalDateTime, preparationTimeSlot, storeCooks);
        if (availableCooks.size() != 0) {
            for(Cook ableCook : availableCooks){
                if(ableCook.canPrepare(order)){
                    ableCook.assignOrder(order);
                    order.setCook(ableCook);
                    return ableCook;
                }
            }
            throw new TimeslotUnavailableException("No able cook available for this order");
        } else {
            throw new TimeslotUnavailableException("The assigned retrieval time is not available");
        }
    }

    public List<Cook> getAvailableCooksForRetrievalTime(LocalDateTime date, TimeSlot timeSlot, List<Cook> cooks) {
        List<Cook> availableCooks = new ArrayList<>();
        for (Cook cook : cooks) {
            List<TimeSlot> availableTimeSlotsOfEmployee = getAvailableTimeSlotsOfEmployee(date.toLocalDate(), timeSlot.duration(), cook);
            if (availableTimeSlotsOfEmployee.size() > 0) {
                for(TimeSlot availableTimeSlot : availableTimeSlotsOfEmployee){
                    if(availableTimeSlot.contains(timeSlot)) {
                        availableCooks.add(cook);
                        break;
                    }
                }
            }
        }
        return availableCooks;
    }

    public Set<LocalDateTime> getAvailablePickUpTimes(Order order, LocalDateTime startingDate, LocalDateTime endingDate) {
        SortedSet<LocalDateTime> availablePickUpTimes = new TreeSet<>();
        for (LocalDate date : getDaysBetween(startingDate, endingDate)) {
            Set<LocalDateTime> pickUpTimes = getAvailableTimeSlots(date,
                    order.getPreparationDuration(),
                    order.getStore().getCooks()).stream().
                    map((o) ->LocalDateTime.of(date, LocalTime.from(o.getEndTime()))).collect(Collectors.toSet());
            availablePickUpTimes.addAll(pickUpTimes);
        }
        return availablePickUpTimes;
    }


    public Set<TimeSlot> getAvailableTimeSlots(LocalDate date, Duration duration, List<Cook> cooks) {
        Set<TimeSlot> availableTimeSlots = new HashSet<>();
        for (Cook cook : cooks) {
            availableTimeSlots.addAll(getAvailableTimeSlotsOfEmployee(date, duration, cook));
        }
        return availableTimeSlots;
    }

    public List<TimeSlot> getAvailableTimeSlotsOfEmployee(LocalDate date, Duration duration, Cook cook) {
        List<TimeSlot> occupiedTime = cook.getOccupiedTimeSlotsOfTheDay(date);
        List<TimeSlot> workingHours = cook.getWorkingHoursOfTheDay(date);
        List<TimeSlot> timeSlotsLeft = computeTimeSlotsLeft(workingHours, occupiedTime);
        return computePossibleTimeSlotsAssignments(duration, fuseTimeSlots(timeSlotsLeft));
    }

    public List<TimeSlot> computePossibleTimeSlotsAssignments(Duration duration, List<TimeSlot> availableTimeSlots) {
        List<TimeSlot> possibleTimeSlotAssignments = new ArrayList<>();
        for (TimeSlot timeSlot : availableTimeSlots) {
            if (timeSlot.duration().equals(duration) || timeSlot.duration().compareTo(duration) > 0)
                possibleTimeSlotAssignments.add(timeSlot);
        }
        return possibleTimeSlotAssignments;
    }

    public List<TimeSlot> computeTimeSlotsLeft(List<TimeSlot> workingHours, List<TimeSlot> occupiedTime) {
        List<TimeSlot> timeSlotsLeft = new ArrayList<>(workingHours);
        for (TimeSlot busySlot : occupiedTime) {
            for (TimeSlot slot : timeSlotsLeft) {
                if (slot.contains(busySlot)) {
                    timeSlotsLeft.addAll(slot.extract(busySlot));
                    timeSlotsLeft.remove(slot);
                    break;
                }
            }
        }
        Collections.sort(timeSlotsLeft);
        return timeSlotsLeft;
    }

    private List<TimeSlot> fuseTimeSlots(List<TimeSlot> timeSlots) {
        List<TimeSlot> fusedTimeSlots = new ArrayList<>();
        for (TimeSlot timeSlot : timeSlots) {
            if (fusedTimeSlots.isEmpty()) {
                fusedTimeSlots.add(timeSlot);
            } else {
                TimeSlot latestSlot = fusedTimeSlots.get(fusedTimeSlots.size() - 1);
                TimeSlot fusionSlot = latestSlot.fuse(timeSlot);
                if (fusionSlot != null) {
                    fusedTimeSlots.remove(fusedTimeSlots.size() - 1);
                    fusedTimeSlots.add(fusionSlot);
                } else {
                    fusedTimeSlots.add(timeSlot);
                }
            }
        }
        return fusedTimeSlots;
    }

    private List<LocalDate> getDaysBetween(LocalDateTime startingDate, LocalDateTime endingDate) {
        LocalDate start = startingDate.toLocalDate();
        LocalDate end = endingDate.toLocalDate();
        List<LocalDate> totalDates = new ArrayList<>();
        while (!start.isAfter(end)) {
            totalDates.add(start);
            start = start.plusDays(1);
        }
        return totalDates;
    }
}
