package fr.unice.polytech.cf.components;

import fr.unice.polytech.cf.entities.Cook;
import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.TimeSlot;
import fr.unice.polytech.cf.exceptions.InvalidStoreException;
import fr.unice.polytech.cf.exceptions.InvalidRetrievalDateException;
import fr.unice.polytech.cf.exceptions.TimeslotUnavailableException;
import fr.unice.polytech.cf.interfaces.CookAssigner;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class OrderScheduler
 * It manages all what concern the distribution line of orders on the cooks, retrieving available
 * timeslots of available cooks and other computations to optimize the precess of orders preparation
 */
public class OrderScheduler implements CookAssigner {

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
        if (!availableCooks.isEmpty()) {
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

    /**
     * Allows getting the available cooks for a given date in a given timeslot
     * @param date matches with the order retrieval date
     * @param timeSlot which the date is included in
     * @param cooks the list of all cooks
     * @return the available cooks
     */
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

    /**
     * Allows getting the available cooks for a given date with a given duration
     * @param date matches with the order retrieval date
     * @param duration the order preparation duration
     * @param cooks the list of all cooks
     * @return the available cooks
     */
    public Set<TimeSlot> getAvailableTimeSlots(LocalDate date, Duration duration, List<Cook> cooks) {
        Set<TimeSlot> availableTimeSlots = new HashSet<>();
        for (Cook cook : cooks) {
            availableTimeSlots.addAll(getAvailableTimeSlotsOfEmployee(date, duration, cook));
        }
        return availableTimeSlots;
    }

    /**
     * Allow getting the available slots of cooks
     * @param date the specific date
     * @param duration for how long they are available
     * @param cook
     * @return
     */
    public List<TimeSlot> getAvailableTimeSlotsOfEmployee(LocalDate date, Duration duration, Cook cook) {
        List<TimeSlot> occupiedTime = cook.getOccupiedTimeSlotsOfTheDay(date);
        List<TimeSlot> workingHours = cook.getWorkingHoursOfTheDay(date);
        List<TimeSlot> timeSlotsLeft = computeTimeSlotsLeft(workingHours, occupiedTime);
        return computePossibleTimeSlotsAssignments(duration, fuseTimeSlots(timeSlotsLeft));
    }

    /**
     * Compute all the possible assignments to allow choosing the optimal one in the end
     * @param duration
     * @param availableTimeSlots
     * @return
     */
    public List<TimeSlot> computePossibleTimeSlotsAssignments(Duration duration, List<TimeSlot> availableTimeSlots) {
        List<TimeSlot> possibleTimeSlotAssignments = new ArrayList<>();
        for (TimeSlot timeSlot : availableTimeSlots) {
            if (timeSlot.duration().equals(duration) || timeSlot.duration().compareTo(duration) > 0)
                possibleTimeSlotAssignments.add(timeSlot);
        }
        return possibleTimeSlotAssignments;
    }

    /**
     * Retrieve all the remaining empty timeslots after assigning an order to a cook
     * @param workingHours all the working hours of the store
     * @param occupiedTime the non available timeslots (taken by other cooks)
     * @return a list of the available timeslots left
     */
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

    /**
     * This methods take separate timeslots and does all the possible connections to make
     * others timeslots with bigger lengths and intervals
     * @param timeSlots
     * @return
     */
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

    /**
     * Retrieve the working days included in a given interval time
     * @param startingDate the start of the interval
     * @param endingDate the end of the interval
     * @return
     */
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
