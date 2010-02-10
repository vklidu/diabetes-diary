/**
 * Copyright (c) Oracle Corporation 2002. All Rights Reserved.
 *
 * NAME
 * Day.java
 *
 * FUNCTION
 *  Wrapper of a Date object. Can be used as a comparator for just the day part
 *  of that date.
 *
 * OWNER
 * @author   Neil.Cochrane@oracle.com
 * @since    Discoverer 10i
 *
 * MODIFIED
 * ncochran 11-Jun-04 Creation.
 */

package org.diabetesdiary.commons.swing.calendar.datemania;
import java.util.Date;

public class Day implements Comparable
{
  public Day(Date date)
  {
    _date = (Date)date.clone();
    _day  = _date.getTime() / MILLI_TO_DAY_DIVIDER;
  }
  
  public Date getDate()
  {
    return (Date)_date.clone();
  }
  
  public long getDay()
  {
    return _day;
  }
  
  public boolean equals(Object obj)
  {
    if (obj instanceof Date)
      obj = new Day((Date)obj);
    
    if (!(obj instanceof Day))
      return false;
    
    return (((Day)obj).getDay() == _day);
  }
  
  public int compareTo(Object obj)
  {
    if (obj instanceof Date)
      obj = new Day((Date)obj);
    
    if (!(obj instanceof Day))
      return 0;
    
    Day day = (Day)obj;
    if (day.getDay() == _day)
      return 0;
      
    if (_day > day.getDay())
      return 1;
    
    return -1;
  }
  
  public int hashCode()
  {
    return (int)_day;
  }
  
  // Convert a Date's time in milliseconds into a integer representing just a day
  // by dividing by this.
  static final long MILLI_TO_DAY_DIVIDER = 24*60*60*1000;
  
  private long _day;
  private Date _date;
}