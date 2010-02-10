package org.diabetesdiary.commons.swing.calendar.datemania.demo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;

import oracle.bali.ewt.dateEditor.DateEditor;
import oracle.bali.ewt.layout.PreferredGridLayout;
import org.diabetesdiary.commons.swing.calendar.datemania.CalendarPanel;
import org.diabetesdiary.commons.swing.calendar.datemania.ReflectiveCellRenderer;


public class DateMania
{
  public DateMania()
  {
      _f = new JFrame("Date/Time Picker");
      _f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      JPanel contPanel = new JPanel();
      contPanel.setLayout(new PreferredGridLayout(contPanel, 1, 5, 10, 2, 0, 1));
      contPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
      _f.getContentPane().add(contPanel);
      JComponent datePanel = _createDatePicker();

      contPanel.add(Box.createHorizontalStrut(10));
      contPanel.add(datePanel, BorderLayout.NORTH);
      contPanel.add(Box.createHorizontalStrut(10));
      contPanel.add(new JSeparator(JSeparator.VERTICAL));
      contPanel.add(_createControlPanel());
      
      _f.pack();
      _f.setLocationRelativeTo(null);
      _f.setVisible(true);
  }

  private JComponent _createDatePicker()
  { 
    JPanel compPanel = new JPanel(new BorderLayout());
    _datePanel = new JPanel();
    _cp = new CalendarPanel(false, Locale.ENGLISH);
    _datePanel.add(_cp);
    
    compPanel.add(_datePanel, BorderLayout.CENTER);
    return compPanel;
  }
  
  private JComponent _createControlPanel()
  {
    JPanel controlPanel = new JPanel();
    GridBagLayout gridbag = new GridBagLayout();
    controlPanel.setLayout(gridbag);
    GridBagConstraints c = new GridBagConstraints();

    _createLocaleCombo();
    _createTimeZoneCombo();
    _createMultiSelect();
    _createShowTime();
    _createEditTime();
    _createVisibleDate();
    _createMinDate();
    _createMaxDate();
    _createDateSelect();
    _createDateProvider();
    _createSelectDump();

    c.insets = new Insets(2, 2, 0, 2);
    c.anchor = GridBagConstraints.WEST;

    c.gridwidth = GridBagConstraints.REMAINDER;
    JLabel controlLabel = new JLabel("A date/time picker with test controls that invoke");
    JLabel controlLabel2 = new JLabel("methods available on the Calendar Picker API");
    gridbag.setConstraints(controlLabel, c);
    controlPanel.add(controlLabel);
    c.insets = new Insets(0, 2, 15, 2);
    gridbag.setConstraints(controlLabel2, c);
    controlPanel.add(controlLabel2);


    c.insets = new Insets(10, 2, 0, 2);
    JLabel interLabel = new JLabel("Internationalization:");
    gridbag.setConstraints(interLabel, c);
    controlPanel.add(interLabel);
    c.insets = new Insets(2, 2, 0, 2);
    c.fill = GridBagConstraints.HORIZONTAL;
    JSeparator interSep = new JSeparator(JSeparator.HORIZONTAL);
    gridbag.setConstraints(interSep, c);
    controlPanel.add(interSep);
    c.fill = GridBagConstraints.NONE;
    
    c.insets = new Insets(2, 2, 0, 2);
    c.gridwidth = 1;
    c.fill = GridBagConstraints.HORIZONTAL;
    JLabel localeLabel = new JLabel("Locale: ");
    gridbag.setConstraints(localeLabel, c);
    controlPanel.add(localeLabel);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(_localeCombo, c);
    controlPanel.add(_localeCombo);

    c.insets = new Insets(2, 2, 0, 2);
    c.gridwidth = 1;
    JLabel tzLabel = new JLabel("Time zone: ");
    gridbag.setConstraints(tzLabel, c);
    controlPanel.add(tzLabel);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(_tzCombo, c);
    controlPanel.add(_tzCombo);


    c.insets = new Insets(10, 2, 0, 2);
    JLabel displayLabel = new JLabel("Display options");
    gridbag.setConstraints(displayLabel, c);
    controlPanel.add(displayLabel);
    c.insets = new Insets(2, 2, 0, 2);
    c.fill = GridBagConstraints.HORIZONTAL;
    JSeparator displaySep = new JSeparator(JSeparator.HORIZONTAL);
    gridbag.setConstraints(displaySep, c);
    controlPanel.add(displaySep);
    c.fill = GridBagConstraints.NONE;
    
    gridbag.setConstraints(_multiCheckbox, c);
    controlPanel.add(_multiCheckbox);

    gridbag.setConstraints(_showTimeCheckbox, c);
    controlPanel.add(_showTimeCheckbox);
    
    gridbag.setConstraints(_editTimeCheckbox, c);
    controlPanel.add(_editTimeCheckbox);

    c.gridwidth = GridBagConstraints.RELATIVE;
    gridbag.setConstraints(_showButton, c);
    controlPanel.add(_showButton);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(_showDate, c);
    controlPanel.add(_showDate);


    c.insets = new Insets(10, 2, 0, 2);
    JLabel validLabel = new JLabel("Valid Dates");
    gridbag.setConstraints(validLabel, c);
    controlPanel.add(validLabel);
    c.insets = new Insets(2, 2, 0, 2);
    c.fill = GridBagConstraints.HORIZONTAL;
    JSeparator validSep = new JSeparator(JSeparator.HORIZONTAL);
    gridbag.setConstraints(validSep, c);
    controlPanel.add(validSep);
    c.fill = GridBagConstraints.NONE;
    
    gridbag.setConstraints(_providerCheckbox, c);
    controlPanel.add(_providerCheckbox);

    c.gridwidth = 3;
    gridbag.setConstraints(_minCheckbox, c);
    controlPanel.add(_minCheckbox);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(_minDate, c);
    controlPanel.add(_minDate);

    c.gridwidth = 3;
    gridbag.setConstraints(_maxCheckbox, c);
    controlPanel.add(_maxCheckbox);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(_maxDate, c);
    controlPanel.add(_maxDate);


    c.insets = new Insets(10, 2, 0, 2);
    JLabel selLabel = new JLabel("Date Selection");
    gridbag.setConstraints(selLabel, c);
    controlPanel.add(selLabel);
    c.insets = new Insets(2, 2, 0, 2);
    c.fill = GridBagConstraints.HORIZONTAL;
    JSeparator selSep = new JSeparator(JSeparator.HORIZONTAL);
    gridbag.setConstraints(selSep, c);
    controlPanel.add(selSep);
    c.fill = GridBagConstraints.NONE;
 
    c.gridwidth = 1;
    gridbag.setConstraints(_selectDate, c);
    controlPanel.add(_selectDate);
    gridbag.setConstraints(_addDate, c);
    controlPanel.add(_addDate);
    gridbag.setConstraints(_removeDate, c);
    controlPanel.add(_removeDate);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(_selDate, c);
    controlPanel.add(_selDate);

    c.gridwidth = 3;
    c.insets = new Insets(2, 2, 20, 2);
    JLabel dumpLabel = new JLabel("Dump selected dates to console: ");
    gridbag.setConstraints(dumpLabel, c);
    controlPanel.add(dumpLabel);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(_dumpDates, c);
    controlPanel.add(_dumpDates);
            
    return controlPanel;
  }
  
  private JComponent _createLocaleCombo()
  {
    _localeCombo = new JComboBox(Locale.getAvailableLocales());
    _localeCombo.setSelectedItem(Locale.ENGLISH);
    _localeCombo.setRenderer(new ReflectiveCellRenderer(Locale.class, "getDisplayName"));
    _localeCombo.addItemListener(new ItemListener(){
      public void itemStateChanged(ItemEvent e)
      {
        if (e.getStateChange() != ItemEvent.SELECTED)
          return;
        _reloadCalendar();
      }
    });   
    return _localeCombo;
  }  
  
  private JComponent _createTimeZoneCombo()
  {
    _tzCombo = new JComboBox(_getTimeZoneIDs());
    _tzCombo.setPrototypeDisplayValue("WWWWWWWWWWWWWWW");
    _tzCombo.setSelectedItem(_cp.getTimeZone());
    _tzCombo.setRenderer(new TimeZoneRenderer());
    _tzCombo.addItemListener(new ItemListener(){
      public void itemStateChanged(ItemEvent e)
      {
        if (e.getStateChange() != ItemEvent.SELECTED)
          return;
        TimeZone tz = (TimeZone)_tzCombo.getSelectedItem();
        _cp.setTimeZone(tz);
      }
    });   
    return _tzCombo;
  }  
  
  private TimeZone[] _getTimeZoneIDs()
  {
    LinkedList<String> zoneNames = new LinkedList<String>();
    LinkedList<TimeZone> zones = new LinkedList<TimeZone>();
    
    for (String zoneID : TimeZone.getAvailableIDs())
    {
      try
      {
        TimeZone tz = TimeZone.getTimeZone(zoneID);
        String displayName = tz.getDisplayName(true, TimeZone.LONG, (Locale)_localeCombo.getSelectedItem());
        if (!zoneNames.contains(displayName))
        {
          zoneNames.add(displayName);
          zones.add(tz);
        }
      }
      catch(Exception e){}
    }
    
    return zones.toArray(new TimeZone[0]);
  }

  private JComponent _createMultiSelect()
  {
    _multiCheckbox = new JCheckBox("Multi-select");
    _multiCheckbox.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        _reloadCalendar();
      }
    });   
    return _multiCheckbox;
  }  
  
  private JComponent _createShowTime()
  {
    _showTimeCheckbox = new JCheckBox("Display Time");
    _showTimeCheckbox.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        _cp.setShowTime(_showTimeCheckbox.isSelected());
      }
    });   
    return _showTimeCheckbox;
  }  
  
  private JComponent _createEditTime()
  {
    _editTimeCheckbox = new JCheckBox("Edit Time");
    _editTimeCheckbox.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        _cp.setEditTime(_editTimeCheckbox.isSelected());
      }
    });   
    return _editTimeCheckbox;
  }  
  
  private JComponent _createDateProvider()
  {
    _providerCheckbox = new JCheckBox("Restrict selection to predefined dates in Sep 2005");
    _providerCheckbox.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        _cp.setDateProvider(_providerCheckbox.isSelected()
            ? new ExampleDateProvider()
            : null);
      }
    });   
    return _providerCheckbox;
  }

  private void _createVisibleDate()
  {
    _showButton = new JButton("Show");
    _showButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        _cp.makeDateVisible(_showDate.getDate());
      }
    });
    
    _showDate = new DateEditor();
    _showDate.setDateFormat(new SimpleDateFormat("dd.MMM.yyyy"));
  }  
  
  private void _createMinDate()
  {
    _minCheckbox = new JCheckBox("Minimum date: ");
    _minCheckbox.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        _cp.setMinimumDate(_minCheckbox.isSelected() ? _minDate.getDate() : null);
      }
    }); 
    _minDate = new DateEditor();
    _minDate.setDateFormat(new SimpleDateFormat("dd.MMM.yyyy"));
    _minDate.addPropertyChangeListener(new PropertyChangeListener(){
      public void propertyChange(PropertyChangeEvent evt)
      {
        if ((evt.getPropertyName().equals(DateEditor.PROPERTY_DATE)) &&
            _minCheckbox.isSelected())
        {
          _cp.setMinimumDate(_minDate.getDate());
        }
      }
    });
  }
  
  private void _createMaxDate()
  {
    _maxCheckbox = new JCheckBox("Maximum date: ");
    _maxCheckbox.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        _cp.setMaximumDate(_maxCheckbox.isSelected() ? _maxDate.getDate() : null);
      }
    });   
    _maxDate = new DateEditor();
    _maxDate.setDateFormat(new SimpleDateFormat("dd.MMM.yyyy"));
    _maxDate.addPropertyChangeListener(new PropertyChangeListener(){
      public void propertyChange(PropertyChangeEvent evt)
      {
        if ((evt.getPropertyName().equals(DateEditor.PROPERTY_DATE)) &&
            _maxCheckbox.isSelected())
        {
          _cp.setMaximumDate(_maxDate.getDate());
        }
      }
    });
  }  
  
  private void _createDateSelect()
  {
    _selectDate = new JButton("Select");
    _selectDate.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        _cp.setSelectedDate(_selDate.getDate());
      }
    });
    _addDate = new JButton("Add");
    _addDate.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        _cp.addSelectedDates(new Date[]{_selDate.getDate()});
      }
    });
    _removeDate = new JButton("Remove");
    _removeDate.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        _cp.removeSelectedDates(new Date[]{_selDate.getDate()});
      }
    });   
    
    _selDate = new DateEditor();
    _selDate.setDateFormat(new SimpleDateFormat("dd.MMM.yyyy"));
  }  
  
  private void _createSelectDump()
  {
    _dumpDates = new JButton("Dump");
    _dumpDates.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        System.out.println("-------------------");
        DateFormat sdf = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.LONG, SimpleDateFormat.LONG, (Locale)_localeCombo.getSelectedItem());
        Date[] dates = _cp.getSelectedDates();
        for (Date date : dates)
          System.out.println("Date/Time: " + sdf.format(date) + ", TimeZone: " + _cp.getTimeZone().getDisplayName());
      }
    });
  }
  
  private void _reloadCalendar()
  {
    boolean multi = _multiCheckbox.isSelected();
    Locale locale = (Locale)_localeCombo.getSelectedItem();
    _datePanel.removeAll();
    _cp = new CalendarPanel(multi, locale);
    _cp.setMinimumDate(_minCheckbox.isSelected() ? _minDate.getDate() : null);
    _cp.setMaximumDate(_maxCheckbox.isSelected() ? _maxDate.getDate() : null);
    _cp.setShowTime(_showTimeCheckbox.isSelected());
    _cp.setEditTime(_editTimeCheckbox.isSelected());
    
    if (_providerCheckbox.isSelected())
      _cp.setDateProvider(new ExampleDateProvider());

    TimeZone tz = (TimeZone)_tzCombo.getSelectedItem();
    _cp.setTimeZone(tz);
    
    _datePanel.add(_cp);
    //_f.pack();
    _f.repaint();    
  }
  
  
  private class TimeZoneRenderer extends DefaultListCellRenderer
  {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
        boolean isSelected, boolean cellHasFocus)
    {
      JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index,
          isSelected, cellHasFocus);
      
      if (value instanceof TimeZone)
      {
        TimeZone tz = (TimeZone)value;
        label.setText(tz.getDisplayName(true, TimeZone.LONG, (Locale)_localeCombo.getSelectedItem()));
      }
      else if (value instanceof String)
        label.setText((String)value);

      
      return label;
    }
  }
  
  private JPanel _datePanel;
  private JComboBox _localeCombo;
  private JComboBox _tzCombo;
  private JCheckBox _multiCheckbox;
  private JCheckBox _showTimeCheckbox;
  private JCheckBox _editTimeCheckbox;
  private DateEditor _minDate;
  private DateEditor _maxDate;
  private DateEditor _selDate;
  private DateEditor _showDate;
  private JCheckBox _minCheckbox;
  private JCheckBox _maxCheckbox;
  private JCheckBox _providerCheckbox;
  private JButton   _addDate;
  private JButton   _selectDate;
  private JButton   _showButton;
  private JButton   _removeDate;
  private JButton   _dumpDates;
  private CalendarPanel _cp;
  private JFrame _f;

  
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    try{
      //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      //UIManager.setLookAndFeel("oracle.bali.ewt.olaf.BrowserLookAndFeel");
      //UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
      UIManager.setLookAndFeel("oracle.bali.ewt.olaf2.OracleLookAndFeel");
    } catch (Throwable t){}
    new DateMania();
  }
}
