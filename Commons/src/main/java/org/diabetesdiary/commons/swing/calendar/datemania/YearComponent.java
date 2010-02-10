/**
 * Copyright (c) Oracle Corporation 2002. All Rights Reserved.
 *
 * NAME
 * YearPanel.java
 *
 * FUNCTION
 * Creation of the year panel.
 *
 * OWNER
 * @author   Neil.Cochrane@oracle.com
 * @since    Discoverer 10i
 *
 */

package org.diabetesdiary.commons.swing.calendar.datemania;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class YearComponent extends JSpinner implements ChangeListener
{
  public YearComponent(Calendar cal, DayComponent dp)
  {
    _cal = cal;
    _dp  = dp;
    _init();
  }
  
  private void _init()
  {
    SpinnerDateModel dateModel = new SpinnerDateModel();
    dateModel.setCalendarField(Calendar.YEAR);
    setModel(dateModel);
    
    Calendar cloneCal = (Calendar)_cal.clone();
    cloneCal.set(_MIN_ALLOWED_YEAR, 0, 1);
    dateModel.setStart(cloneCal.getTime());
    cloneCal.set(_MAX_ALLOWED_YEAR, 1, 1);
    dateModel.setEnd(cloneCal.getTime());
    
    setEditor(new DateEditor(this, "yyyy"));
    setValue(_cal.getTime());
    setOpaque(false);
    addChangeListener(this);
  }
  
  public void updateDisplay()
  {
    setValue(_cal.getTime());
  }
  
  public void stateChanged(ChangeEvent e)
  {
    Date date = (Date)getValue();
    Calendar cal = (Calendar)_cal.clone();
    cal.setTime(date);
    int year = cal.get(Calendar.YEAR);
    
    // To avoid changing to invalid date like: 31-Feb-01
    _cal.set(Calendar.DAY_OF_MONTH, 1); 
    _cal.set(Calendar.YEAR, year); 
    _dp.rebuild();
  }

  void cleanUp()
  {
    _cal       = null;
    _dp        = null;
  }

  private Calendar _cal;
  private DayComponent _dp;
  
  private final int _MAX_ALLOWED_YEAR = 9999;
  private final int _MIN_ALLOWED_YEAR = 1;
  
}