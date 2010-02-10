/**
 * Copyright (c) Oracle Corporation 2002. All Rights Reserved.
 *
 * NAME
 * MonthPanel.java
 *
 * FUNCTION
 * Creation of the month panel.
 *
 * OWNER
 * @author   Neil.Cochrane@oracle.com
 * @since    Discoverer 10i
 *
 * MODIFIED
 * ncochran 15-Jun-04 - 3693439 - allow to tab out in Java 1.4
 * ncochran 29-Mar-04 - 3541396 - respect NLS parameters correctly
 * rthiruva 10-Feb-2004 Bug 3359652. Calendar shuold respect NLSDateLanguage
 * rthiruva 26-Jan-2003 Incorporating code review comments.
 * rthiruva 30-Dec-2002 Modified for integration with conditions, parameters
 *                      and scheduling.
 * ncochran 01-Aug-2002 Creation.
 */

package org.diabetesdiary.commons.swing.calendar.datemania;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.UIManager;

class MonthComponent extends JComboBox implements ItemListener
{
  /**
   * Constructor
   * <p>
   */
  public MonthComponent(Calendar cal, DayComponent dp, Locale locale)
  {
    _cal = cal;
    _dp  = dp;

    _dfSymbols  = new DateFormatSymbols(locale);
    
    Integer[] months = new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11};
    setModel(new DefaultComboBoxModel(months));
    setRenderer(new MonthRenderer());
    addItemListener(this);
    
    int month = _cal.get(Calendar.MONTH);
    setSelectedItem(month);
    setBackground(UIManager.getColor("TextArea.background"));

    setOpaque(false);
  }
  
  public void updateDisplay()
  {
    setSelectedIndex(_cal.get(Calendar.MONTH));
  }
  
  public void itemStateChanged(ItemEvent e)
  {
    if (e.getStateChange() != ItemEvent.SELECTED)
      return;
    
    _cal.set(Calendar.MONTH, (Integer)getSelectedItem()); 
    _dp.rebuild();
  }

  private class MonthRenderer extends DefaultListCellRenderer
  {
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
      JLabel monthLabel = (JLabel)super.getListCellRendererComponent(list, value,
          index, isSelected, cellHasFocus);
      
      if (value instanceof Integer)
        monthLabel.setText(_dfSymbols.getMonths()[(Integer)value]);
      return monthLabel;
    }
    
  }
    
  void cleanUp()
  {
    _dp         = null;
    _cal        = null;
  }
  
  private DayComponent _dp;
  private Calendar _cal;
  
  private DateFormatSymbols _dfSymbols;
}