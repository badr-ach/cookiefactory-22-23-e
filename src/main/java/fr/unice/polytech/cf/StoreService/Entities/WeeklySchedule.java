package fr.unice.polytech.cf.StoreService.Entities;

import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Exceptions.OrderNotFoundException;

import java.time.DayOfWeek;
import java.util.LinkedHashMap;

public class WeeklySchedule {

  private LinkedHashMap<DayOfWeek, DailySchedule> schedule =
      new LinkedHashMap<DayOfWeek, DailySchedule>();

  public WeeklySchedule() {
    setSameScheduleAllWeek("08:00", "22:00");
  }

  public WeeklySchedule(String openingHour, String closingHour) {
    setSameScheduleAllWeek(openingHour, closingHour);
  }

  public void setSameScheduleAllWeek(String openingHour, String closingHour) {
    schedule.put(DayOfWeek.MONDAY, new DailySchedule(openingHour, closingHour));
    schedule.put(DayOfWeek.TUESDAY, new DailySchedule(openingHour, closingHour));
    schedule.put(DayOfWeek.WEDNESDAY, new DailySchedule(openingHour, closingHour));
    schedule.put(DayOfWeek.THURSDAY, new DailySchedule(openingHour, closingHour));
    schedule.put(DayOfWeek.FRIDAY, new DailySchedule(openingHour, closingHour));
    schedule.put(DayOfWeek.SATURDAY, new DailySchedule(openingHour, closingHour));
    schedule.put(DayOfWeek.SUNDAY, new DailySchedule(openingHour, closingHour));
  }

  public void setDayInSchedule(DayOfWeek day, DailySchedule dailySchedule) {
    schedule.put(day, dailySchedule);
  }

  public void setDayInSchedule(DayOfWeek day, String startTime, String endTime) {
    DailySchedule timeslot = new DailySchedule(startTime, endTime);
    schedule.put(day, timeslot);
  }

  public DailySchedule getDayFromSchedule(DayOfWeek day) {
    return schedule.get(day);
  }

  public boolean hasOrder(Order order) {
    for (int i = 0; i < schedule.size(); i++) {
      DailySchedule dailySchedule = schedule.get(i);
      if (dailySchedule != null && dailySchedule.hasOrder(order)) {
        return true;
      }
    }
    return false;
  }

  public void removeOrder(Order order) throws OrderNotFoundException {
    for (int i = 0; i < schedule.size(); i++) {
      DailySchedule dailySchedule = schedule.get(i);
      if (dailySchedule.hasOrder(order)) {
        dailySchedule.removeOrder(order);
        return;
      }
    }
    throw new OrderNotFoundException("The order was not found");
  }

  public WeeklySchedule getEmptyCopy() {
    WeeklySchedule weeklySchedule = new WeeklySchedule();
    weeklySchedule.setDayInSchedule(
        DayOfWeek.MONDAY, schedule.get(DayOfWeek.MONDAY).getEmptyCopy());
    weeklySchedule.setDayInSchedule(
        DayOfWeek.TUESDAY, schedule.get(DayOfWeek.TUESDAY).getEmptyCopy());
    weeklySchedule.setDayInSchedule(
        DayOfWeek.WEDNESDAY, schedule.get(DayOfWeek.WEDNESDAY).getEmptyCopy());
    weeklySchedule.setDayInSchedule(
        DayOfWeek.THURSDAY, schedule.get(DayOfWeek.THURSDAY).getEmptyCopy());
    weeklySchedule.setDayInSchedule(
        DayOfWeek.FRIDAY, schedule.get(DayOfWeek.FRIDAY).getEmptyCopy());
    weeklySchedule.setDayInSchedule(
        DayOfWeek.SATURDAY, schedule.get(DayOfWeek.SATURDAY).getEmptyCopy());
    weeklySchedule.setDayInSchedule(
        DayOfWeek.SUNDAY, schedule.get(DayOfWeek.SUNDAY).getEmptyCopy());
    return weeklySchedule;
  }

  public String toString() {
    return "Monday: "
        + schedule.get(DayOfWeek.MONDAY).getWorkingHours()
        + "\n"
        + "Tuesday: "
        + schedule.get(DayOfWeek.TUESDAY).getWorkingHours()
        + "\n"
        + "Wednesday: "
        + schedule.get(DayOfWeek.WEDNESDAY).getWorkingHours()
        + "\n"
        + "Thursday: "
        + schedule.get(DayOfWeek.THURSDAY).getWorkingHours()
        + "\n"
        + "Friday: "
        + schedule.get(DayOfWeek.FRIDAY).getWorkingHours()
        + "\n"
        + "Saturday: "
        + schedule.get(DayOfWeek.SATURDAY).getWorkingHours()
        + "\n"
        + "Sunday: "
        + schedule.get(DayOfWeek.SUNDAY).getWorkingHours();
  }
}
