package org.diabetesdiary.commons.swing.calendar.datemania;

import java.util.Date;

/**
 * Interface for CalendarPanel to allow the disabling of invalid dates.
 * @author ncochran
 *
 */
public interface CalendarDateProvider
{
  /**
   * Method to indic ate whether the calendar should mark the date as valid or invalid.
   * Invalid dates are coloured differently and are not able to be selected.
   * @param date
   * @return true if date is valid
   */
  public boolean isDateValid(Date date);
}
