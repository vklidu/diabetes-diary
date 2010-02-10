/**
 * Copyright (c) Oracle Corporation 2002. All Rights Reserved.
 *
 * NAME
 * CalendarPanel.java
 *
 * FUNCTION
 * Creation of the calendar panel.
 *
 * OWNER
 * @author   Neil.Cochrane@oracle.com
 *           Raja.Thiruvasagam@oracle.com
 * @since    Discoverer 10i
 *
 * MODIFIED
 * rthiruva 07-Oct-2004 Bug 3933444. Escape doesn't cancel when selection is on.
 * ncochran 11-Jun-2004 - 3511248 - use Day class and allow minimum day
 * rthiruva 10-Feb-2004 Bug 3359652. Calendar shuold respect NLSDateLanguage
 * rthiruva 08-May-2003 Always use Gregorian Calendar.
 * rthiruva 26-Jan-2003 Incorporating code review comments.
 * rthiruva 03-Jan-2003 Moved ok/cancel listener to this.
 * rthiruva 02-Jan-2003 Hooked up Enter key with calendar.
 * rthiruva 30-Dec-2002 Modified for integration with conditions, parameters
 *                      and scheduling.
 * ncochran 01-Aug-2002 Creation.
 */
package org.diabetesdiary.commons.swing.calendar.datemania;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;

public class CalendarPanel extends JPanel {

    public CalendarPanel() {
        this(false, Locale.getDefault());
    }

    public CalendarPanel(boolean isMultiSelect, Locale locale) {
        _isMultiSelect = isMultiSelect;
        _selectedDays = new TreeSet<Day>();
        _locale = locale;
        _init();
        setSelectedDate(new Date());
    }

    private void _init() {
        _cal = Calendar.getInstance(_locale);
        _cal.setTime(new Date());

        _cal.setMinimalDaysInFirstWeek(1);
        _createSubcomponents();
        setTimeZone(_cal.getTimeZone());

        _layoutComponents();

        setBorder(BorderFactory.createCompoundBorder(
                UIManager.getBorder("TextField.border"),
                BorderFactory.createEmptyBorder(2, 4, 8, 4)));
        setBackground(UIManager.getColor("TextArea.background"));
    }

    private void _createSubcomponents() {
        _dayPanel = new DayComponent(_cal, _isMultiSelect, _selectedDays, _locale);
        _yearPanel = new YearComponent(_cal, _dayPanel);
        _monthPanel = new MonthComponent(_cal, _dayPanel, _locale);
        _timeDisplay = new TimeDisplayPanel(_locale, _cal);
        _timeEditor = new TimeEditPanel(_locale, _cal);
        _previousButton = new BasicArrowButton(SwingConstants.WEST);
        _nextButton = new BasicArrowButton(SwingConstants.EAST);
        _previousButton.setBackground(UIManager.getColor("TextArea.background"));
        _nextButton.setBackground(UIManager.getColor("TextArea.background"));
        _previousButton.setOpaque(false);
        _nextButton.setOpaque(false);

        _previousButton.setBorderPainted(false);
        _previousButton.setAction(null);
        _nextButton.setBorderPainted(false);

        ArrowButtonListener abl = new ArrowButtonListener();
        _previousButton.addActionListener(abl);
        _nextButton.addActionListener(abl);

        ClickListener dcl = new ClickListener();
        _dayPanel.getTable().getGrid().addMouseListener(dcl);
    }

    private void _layoutComponents() {
        removeAll();
        GridBagLayout gridbag = new GridBagLayout();
        setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();

        gridbag.setConstraints(_previousButton, c);
        c.insets = new Insets(4, 2, 4, 2);
        c.ipadx = 2;
        c.anchor = GridBagConstraints.EAST;
        gridbag.setConstraints(_monthPanel, c);
        c.anchor = GridBagConstraints.WEST;
        gridbag.setConstraints(_yearPanel, c);
        c.insets = new Insets(4, 0, 4, 2);
        c.anchor = GridBagConstraints.CENTER;
        c.ipadx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(_nextButton, c);
        add(_previousButton);
        add(_monthPanel);
        add(_yearPanel);
        add(_nextButton);

        gridbag.setConstraints(_dayPanel, c);
        add(_dayPanel);
        _dayPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIManager.getColor("ComboBox.disabledBackground")));

        if (_showTime) {
            if (_editTime) {
                c.insets = new Insets(0, 2, 0, 2);
                gridbag.setConstraints(_timeEditor, c);
                add(_timeEditor);
            } else {
                c.insets = new Insets(0, 2, 4, 2);
                gridbag.setConstraints(_timeDisplay, c);
                add(_timeDisplay);
            }
        }
    }

    /**
     * returns an array of dates choosen in the calendar.
     * <p>
     * If the calendar is displaying the time section (in either edit or display mode)
     * then the time portion (hours/minutes/seconds) for all returned dates
     * will match the displayed time. Note:  This means that any date set using
     * setSelectedDate() or setSelectedDates() may not exactly match the return
     * values for the same day.
     * Calendar time
     *
     * @param none.
     * @return an array of selected dates as java Date object.
     */
    public Date[] getSelectedDates() {
        Date[] dates = new Date[_selectedDays.size()];
        Calendar cal = (Calendar) _cal.clone();
        int i = 0;
        for (Day day : _selectedDays) {
            Date date = day.getDate();
            if (_showTime) {
                // Adjust all selected dates to the ensure the time syncs up with the
                // currently selected time (set on _cal, see TimeEditPanel)
                cal.setTime(date);
                cal.set(Calendar.HOUR_OF_DAY, _cal.get(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE, _cal.get(Calendar.MINUTE));
                cal.set(Calendar.SECOND, _cal.get(Calendar.SECOND));
                cal.setTimeZone(_cal.getTimeZone());
                date = cal.getTime();
            }

            dates[i] = date;
            i++;
        }
        return dates;
    }

    /**
     * Change the selected date/time to the given one.
     * @param date
     */
    public void setSelectedDate(Date date) {
        _selectedDays.clear();
        _selectedDays.add(new Day(date));

        // now adjust time
        Calendar cal = (Calendar) _cal.clone();
        cal.setTime(date);
        _cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
        _cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
        _cal.set(Calendar.SECOND, cal.get(Calendar.SECOND));

        _refreshCalendar();
    }

    /**
     * Change the timzone.
     * @param date
     */
    public void setTimeZone(TimeZone timeZone) {
        TimeZone nearestMatchZone = _timeEditor.getNearestTimeZoneMatch(timeZone);
        _cal.setTimeZone(nearestMatchZone);

        _refreshCalendar();
    }

    public TimeZone getTimeZone() {
        return _cal.getTimeZone();
    }

    /**
     * Change the current selected dates to be the given array.
     * If the calendar is single select then the current selection changes to the
     * first date of the given date array.
     *
     * As the calendar can have many selected dates but only one selected time,
     * the calendar time is adjusted to that of the first date in the given array
     * @param dates
     */
    public void setSelectedDates(Date[] dates) {
        _selectedDays.clear();
        if (!_isMultiSelect) {
            _selectedDays.add(new Day(dates[0]));
        } else {
            for (Date date : dates) {
                _selectedDays.add(new Day(date));
            }
        }

        // now adjust time
        Calendar cal = (Calendar) _cal.clone();
        cal.setTime(dates[0]);
        _cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
        _cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
        _cal.set(Calendar.SECOND, cal.get(Calendar.SECOND));

        _dayPanel.rebuild();
    }

    /**
     * add the given dates to the current selection.
     * If the calendar is single select then the current selection changes to the
     * first date of the given date array.
     *
     * The time portion of the calendar is not changed by this call
     * @param dates
     */
    public void addSelectedDates(Date[] dates) {
        if (!_isMultiSelect) {
            _selectedDays.clear();
            _selectedDays.add(new Day(dates[0]));
        } else {
            for (Date date : dates) {
                _selectedDays.add(new Day(date));
            }
        }

        _dayPanel.rebuild();
    }

    /**
     * remove the given dates from the current selection
     * @param dates
     */
    public void removeSelectedDates(Date[] dates) {
        for (Date date : dates) {
            _selectedDays.remove(new Day(date));
        }

        _dayPanel.rebuild();
    }

    /**
     * will return a date that is currently visible in the calendar. i.e. one of the days
     * of the currently selected month and year. There's no guarentee of which day in the month
     * will be returned.
     * @return
     */
    public Date getVisibleDate() {
        return _cal.getTime();
    }

    /**
     * Will adjust the calendar so that the given date is visible
     * @param date
     */
    public void makeDateVisible(Date date) {
        // ensure only the date portion is changed, not the time portion.
        // so reset time fields to pre-method-call values.
        Calendar cal = (Calendar) _cal.clone();
        _cal.setTime(date);
        _cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
        _cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
        _cal.set(Calendar.SECOND, cal.get(Calendar.SECOND));

        _refreshCalendar();
    }

    private void _refreshCalendar() {
        _monthPanel.updateDisplay();
        _yearPanel.updateDisplay();
        _timeDisplay.updateDisplay();
        _timeEditor.updateDisplay();
        _dayPanel.rebuild();
    }

    /**
     * Will disable all dates less than the given date
     * @param minimumDate
     */
    public void setMinimumDate(Date minimumDate) {
        _dayPanel.setMinimumDate(minimumDate);
    }

    public Date getMinimumDate() {
        return _dayPanel.getMinimumDate();
    }

    public void setMaximumDate(Date maximumDate) {
        _dayPanel.setMaximumDate(maximumDate);
    }

    public Date getMaximumDate() {
        return _dayPanel.getMaximumDate();
    }

    public void setDateProvider(CalendarDateProvider calDateProvider) {
        _dayPanel.setDateProvider(calDateProvider);
    }

    public CalendarDateProvider getDateProvider() {
        return _dayPanel.getDateProvider();
    }

    @Override
    public Locale getLocale() {
        return _locale;
    }

    public void setShowTime(boolean showTime) {
        _showTime = showTime;
        _timeDisplay.updateDisplay();
        _timeEditor.updateDisplay();
        _layoutComponents();
    }

    public boolean isShowingTime() {
        return _showTime;
    }

    public void setEditTime(boolean editTime) {
        _editTime = editTime;
        _timeDisplay.updateDisplay();
        _timeEditor.updateDisplay();
        _layoutComponents();
    }

    public boolean isEditingTime() {
        return _editTime;
    }

    /**
     * Cleanup code
     * <p>
     *
     * @param none.
     * @return none.
     */
    public void cleanUp() {
        _dayPanel.cleanUp();
        _monthPanel.cleanUp();
        _yearPanel.cleanUp();
        _dayPanel = null;
        _monthPanel = null;
        _yearPanel = null;
        _cal = null;
        _selectedDays.clear();
        _selectedDays = null;
    }

    protected void onDaySelected(Date[] selectedDates) {

    }

    private class ClickListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent me) {
            if ((me.getClickCount() == 1) && SwingUtilities.isLeftMouseButton(me)) {
                onDaySelected(getSelectedDates());
            }
        }

    }

    private class ArrowButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == _previousButton) {
                _cal.add(Calendar.MONTH, -1);
                _refreshCalendar();
            } else if (e.getSource() == _nextButton) {
                _cal.add(Calendar.MONTH, 1);
                _refreshCalendar();
            }
        }
    }
    private Calendar _cal;
    private Locale _locale;
    private boolean _isMultiSelect;
    private boolean _showTime;
    private boolean _editTime;
    private DayComponent _dayPanel;
    private MonthComponent _monthPanel;
    private YearComponent _yearPanel;
    private TimeDisplayPanel _timeDisplay;
    private TimeEditPanel _timeEditor;
    private JButton _previousButton;
    private JButton _nextButton;
    private TreeSet<Day> _selectedDays;
}
