package fr.unice.polytech.cf.StoreService.Entities;

import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Exceptions.OrderNotFoundException;
import fr.unice.polytech.cf.StoreService.Exceptions.InsufficientTimeslotDurationException;
import fr.unice.polytech.cf.StoreService.Exceptions.TimeslotUnavailableException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;

public class DailySchedule {

  private ArrayList<TimeSlot> availableTimeslots = new ArrayList<TimeSlot>();
  private ArrayList<TimeSlot> takenTimeslots = new ArrayList<TimeSlot>();
  private TimeSlot workingHours;
  private Duration TIME_SLOT_INCREMENT = Duration.parse("PT15M");

  public DailySchedule(LocalTime startTime, LocalTime endTime) {
    workingHours = new TimeSlot(startTime, endTime);
    this.initializeTimeSlots();
  }

  public DailySchedule(String startTime, String endTime) {
    workingHours = new TimeSlot(startTime, endTime);
    this.initializeTimeSlots();
  }

  public TimeSlot getWorkingHours() {
    return workingHours;
  }

  public void initializeTimeSlots() {
    int numberOfTimeSlots =
        (int) Math.floor(workingHours.duration().dividedBy(TIME_SLOT_INCREMENT));
    LocalTime timeSlotStartingTime = workingHours.getStartTime();
    for (int i = 0; i < numberOfTimeSlots; i++) {
      TimeSlot timeslot = new TimeSlot(timeSlotStartingTime, TIME_SLOT_INCREMENT);
      availableTimeslots.add(timeslot);
      takenTimeslots.add(null);
      timeSlotStartingTime.plus(TIME_SLOT_INCREMENT);
    }
  }

  public HashSet<TimeSlot> getAvailableTimeSlots(Duration duration) {
    int numberOfTimeSlotsRequired =
        (int) Math.ceil(1.0 * duration.toSeconds() / TIME_SLOT_INCREMENT.toSeconds());

    HashSet<TimeSlot> availableTimeSlotsForOrder = new HashSet<TimeSlot>();

    int numberAvailableTimeSlotsInARow = 0;

    for (int i = 0; i < availableTimeslots.size(); i++) {
      TimeSlot timeslot = this.availableTimeslots.get(i);
      if (timeslot == null) {
        numberAvailableTimeSlotsInARow = 0;
      } else if (numberAvailableTimeSlotsInARow >= numberOfTimeSlotsRequired) {
        LocalTime startingHour =
            workingHours
                .getStartTime()
                .plus(TIME_SLOT_INCREMENT.multipliedBy(i - numberOfTimeSlotsRequired));
        TimeSlot orderTimeslot =
            new TimeSlot(startingHour, TIME_SLOT_INCREMENT.multipliedBy(numberOfTimeSlotsRequired));
        availableTimeSlotsForOrder.add(orderTimeslot);
      }
      numberAvailableTimeSlotsInARow++;
    }

    return availableTimeSlotsForOrder;
  }

  public void addOrder(Order order, TimeSlot timeSlot)
      throws TimeslotUnavailableException, InsufficientTimeslotDurationException {
    if (order.getPreparationDuration().compareTo(timeSlot.duration()) > 0) {
      throw new InsufficientTimeslotDurationException(
          "The order preparation duration is greater than the timeslot allocated");
    }

    int timeSlotIndex =
        (int)
            Duration.between(workingHours.getStartTime(), timeSlot.getStartTime())
                .dividedBy(TIME_SLOT_INCREMENT);

    if (timeSlot.getStartTime().compareTo(workingHours.getStartTime()) < 0) {
      throw new TimeslotUnavailableException("Timeslot outside of opening hours");
    }

    if (timeSlot.getEndTime().compareTo(workingHours.getEndTime()) > 0) {
      throw new TimeslotUnavailableException("Timeslot outside of opening hours");
    }

    int numberOfTimeSlotsRequired = (int) timeSlot.duration().dividedBy(TIME_SLOT_INCREMENT);

    for (int i = timeSlotIndex; i < timeSlotIndex + numberOfTimeSlotsRequired; i++) {
      if (takenTimeslots.get(i) != null)
        throw new TimeslotUnavailableException("The timeslot is already allocated");
      if (availableTimeslots.get(i) == null)
        throw new TimeslotUnavailableException("The timeslot is already allocated");
    }

    for (int i = timeSlotIndex; i < timeSlotIndex + numberOfTimeSlotsRequired; i++) {
      takenTimeslots.set(i, timeSlot);
      availableTimeslots.set(i, null);
    }
  }

  public void removeOrder(Order order) throws OrderNotFoundException {
    boolean wasOrderFound = false;

    for (int i = 0; i < takenTimeslots.size(); i++) {
      TimeSlot timeslot = takenTimeslots.get(i);
      if (timeslot.getOrder().getId() == order.getId()) {
        wasOrderFound = true;
        LocalTime startingTime = workingHours.getStartTime().plus(TIME_SLOT_INCREMENT.multipliedBy(i));
        takenTimeslots.set(i, null);
        TimeSlot availableTimeslot = new TimeSlot(startingTime, TIME_SLOT_INCREMENT);
        availableTimeslots.set(i, availableTimeslot);
      }
    }

    if (!wasOrderFound) {
      throw new OrderNotFoundException("The order was not found");
    }
  }

  public boolean hasOrder(Order order) {
    for (int i = 0; i < takenTimeslots.size(); i++) {
      TimeSlot timeslot = takenTimeslots.get(i);
      if (timeslot.getOrder().getId() == order.getId()) return true;
    }
    return false;
  }

  public DailySchedule getEmptyCopy() {
    return new DailySchedule(workingHours.getStartTime(), workingHours.getEndTime());
  }
}
