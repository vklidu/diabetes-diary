package org.diabetesdiary.commons.swing.calendar.datemania;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TimeDisplayPanel extends JPanel
{
  public TimeDisplayPanel(Locale locale, Calendar cal)
  {
    _locale = locale;
    _cal = cal;
    _init();
  }
  
  private void _init()
  {
    _timeFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM, _locale);
    _timeLabel = new JLabel();
    _timeZoneLabel = new JLabel();
    _timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
    _timeLabel.setAlignmentX(.5f);
    _timeZoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
    _timeZoneLabel.setAlignmentX(.5f);
    setOpaque(false);
    
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    add(_timeLabel);
    add(_timeZoneLabel);
    updateDisplay();
  }
  
  public void updateDisplay()
  {
    _timeFormat.setTimeZone(_cal.getTimeZone());
    _timeLabel.setText(_timeFormat.format(_cal.getTime()));
    _timeZoneLabel.setText((_cal.getTimeZone().getDisplayName(true, TimeZone.LONG, _locale)));
  }
  
  private Locale     _locale;
  private DateFormat _timeFormat;
  private JLabel     _timeLabel;
  private JLabel     _timeZoneLabel;
  private Calendar   _cal;
}
