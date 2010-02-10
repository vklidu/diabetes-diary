package org.diabetesdiary.commons.swing.calendar.datemania.demo;

import java.util.Date;
import java.util.HashSet;
import org.diabetesdiary.commons.swing.calendar.datemania.CalendarDateProvider;
import org.diabetesdiary.commons.swing.calendar.datemania.Day;


public class ExampleDateProvider implements CalendarDateProvider
{

  public ExampleDateProvider()
  {
    _validDates = new HashSet<Day>();

    _validDates.add(new Day(new Date(105, 8, 1)));
    _validDates.add(new Day(new Date(105, 8, 2)));
    _validDates.add(new Day(new Date(105, 8, 5)));
    _validDates.add(new Day(new Date(105, 8, 6)));
    _validDates.add(new Day(new Date(105, 8, 7)));
    _validDates.add(new Day(new Date(105, 8, 10)));
    _validDates.add(new Day(new Date(105, 8, 11)));
    _validDates.add(new Day(new Date(105, 8, 21)));
    _validDates.add(new Day(new Date(105, 8, 22)));
    _validDates.add(new Day(new Date(105, 8, 23)));
    _validDates.add(new Day(new Date(105, 8, 27)));
    _validDates.add(new Day(new Date(105, 8, 28)));
  }

  public boolean isDateValid(Date date)
  {
    return _validDates.contains(new Day(date));
  }

  private HashSet<Day> _validDates;
}
