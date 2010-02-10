package org.diabetesdiary.commons.swing.calendar.datemania;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JSpinner.DateEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

public class TimeEditPanel extends JPanel {

    public TimeEditPanel() {
        this(Locale.getDefault(), Calendar.getInstance());
    }

    public TimeEditPanel(Locale locale, Calendar cal) {
        _locale = locale;
        _cal = cal;
        _init();
    }

    private void _init() {
        _timeSpinner = new JSpinner(
                new SpinnerDateModel(_cal.getTime(), null, null, Calendar.HOUR_OF_DAY));

        _updateTimeSpinnerFormat();

        _timeZoneCombo = new JComboBox(_getTimeZoneIDs());
        _timeZoneCombo.setRenderer(new TimeZoneRenderer());
        _timeZoneCombo.setBackground(UIManager.getColor("TextArea.background"));
        _timeZoneCombo.setFont(UIManager.getFont("Label.font"));
        _timeZoneCombo.setPrototypeDisplayValue("WWWWWWWWWWWWWWW");
        _timeZoneCombo.setBorder(null);

        _timeSpinner.setAlignmentX(.5f);
        _timeZoneCombo.setAlignmentX(.5f);
        setOpaque(false);

        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 2, 0);
        gbl.setConstraints(_timeSpinner, c);
        gbl.setConstraints(_timeZoneCombo, c);
        add(_timeSpinner);
        add(_timeZoneCombo);
        updateTimeDisplay();
        updateTimeZoneDisplay();

        TimeChangeListener timeListener = new TimeChangeListener();
        _timeSpinner.addChangeListener(timeListener);
        _timeZoneCombo.addItemListener(timeListener);
    }

    public void updateDisplay() {
        updateTimeDisplay();
        updateTimeZoneDisplay();
    }

    public void updateTimeDisplay() {
        _timeSpinner.setValue(_cal.getTime());
        _updateTimeSpinnerFormat();
    }

    public void updateTimeZoneDisplay() {
        TimeZone tz = _cal.getTimeZone();
        tz = getNearestTimeZoneMatch(tz);
        _timeZoneCombo.setSelectedItem(tz);
    }

    /**
     * Find the time zone in the combo list that has the same name as the given one
     * @param tz
     * @return the first timezone that matches the TimeZone.LONG name of the given one
     */
    public TimeZone getNearestTimeZoneMatch(TimeZone tz) {
        String tzName = tz.getDisplayName(true, TimeZone.LONG, _locale);

        for (int i = 0; i < _timeZoneCombo.getItemCount(); i++) {
            TimeZone aTz = (TimeZone) _timeZoneCombo.getItemAt(i);
            if (aTz.getDisplayName(true, TimeZone.LONG, _locale).equals(tzName)) {
                return aTz;
            }
        }
        return null;
    }

    private void _updateTimeSpinnerFormat() {
        DateEditor de = (DateEditor) _timeSpinner.getEditor();
        de.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
        DateFormat df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM, _locale);
        df.setTimeZone(_cal.getTimeZone());
        DateFormatter dfer = new DateFormatter(df);
        DefaultFormatterFactory ff = new DefaultFormatterFactory(dfer);
        de.getTextField().setFormatterFactory(ff);
    }

    /**
     * To standard list of supported timezones is much too large for display
     * to users, and contains many duplicates (There are time zones for
     * America/Los Angeles and America/San Diego which both display as Pacific
     * Daylight time. So this method strips out any duplicate long names and
     * just returns a list containing the first.
     * @return
     */
    private TimeZone[] _getTimeZoneIDs() {
        LinkedList<String> zoneNames = new LinkedList<String>();
        LinkedList<TimeZone> zones = new LinkedList<TimeZone>();

        for (String zoneID : TimeZone.getAvailableIDs()) {
            try {
                TimeZone tz = TimeZone.getTimeZone(zoneID);
                String displayName = tz.getDisplayName(true, TimeZone.LONG, _locale);
                if (!zoneNames.contains(displayName)) {
                    zoneNames.add(displayName);
                    zones.add(tz);
                }
            } catch (Exception e) {
            }
        }

        return zones.toArray(new TimeZone[0]);
    }

    private class TimeZoneRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);

            if (value instanceof TimeZone) {
                TimeZone tz = (TimeZone) value;
                label.setText(tz.getDisplayName(true, TimeZone.LONG, _locale));
            } else if (value instanceof String) {
                label.setText((String) value);
            }

            label.setHorizontalAlignment((index == -1)
                    ? SwingConstants.CENTER
                    : SwingConstants.LEADING);
            label.setAlignmentX((index == -1) ? 0.5f : 0f);

            return label;
        }
    }

    private class TimeChangeListener implements ChangeListener, ItemListener {

        public void stateChanged(ChangeEvent e) {
            Date time = (Date) _timeSpinner.getValue();
            Calendar timeCal = (Calendar) _cal.clone();
            timeCal.setTime(time);
            _cal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
            _cal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
            _cal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
        }

        public void itemStateChanged(ItemEvent e) {
            _cal.setTimeZone((TimeZone) _timeZoneCombo.getSelectedItem());
            updateTimeDisplay();
        }
    }
    private Locale _locale;
    private JSpinner _timeSpinner;
    private JComboBox _timeZoneCombo;
    private Calendar _cal;
}
