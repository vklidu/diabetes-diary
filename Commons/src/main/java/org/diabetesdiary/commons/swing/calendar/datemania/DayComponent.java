/**
 * Copyright (c) Oracle Corporation 2002. All Rights Reserved.
 *
 * NAME
 * DayPanel.java
 *
 * FUNCTION
 * Creation of the day panel.
 *
 * OWNER
 * @author   Neil.Cochrane@oracle.com
 * @since    Discoverer 10i
 *
 * MODIFIED
 * ncochran 15-Jun-2004 - 3664957 - fix problem w/ different first days of week 
 * ncochran 11-Jun-2004 - 3511248 - use Day class and allow minimum day
 * rthiruva 10-Feb-2004 Bug 3359652. Calendar shuold respect NLSDateLanguage
 * rthiruva 24-Jul-2003 Rewrote using Bali SpreadTable.
 * rthiruva 26-Jan-2003 Incorporating code review comments.
 * rthiruva 31-Dec-2002 Making day initials in the day table locale complaint.
 * rthiruva 30-Dec-2002 Modified for integration with conditions, parameters
 *                      and scheduling.
 * ncochran 01-Aug-2002 Creation.
 */
/**
 * Assumptions:
 * Bali always uses (column, row)
 * Normal usage is always (row, column)
 *
 * This class has been modified to adhere to the bali standard.
 * so day - represents column
 *    week - represents row
 */
package org.diabetesdiary.commons.swing.calendar.datemania;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import oracle.bali.ewt.graphics.Appearance;
import oracle.bali.ewt.grid.ComplexAppearanceManager;
import oracle.bali.ewt.grid.GeneralGridSelectionManager;
import oracle.bali.ewt.grid.Grid;
import oracle.bali.ewt.grid.StandardGridKeyHandler;
import oracle.bali.ewt.header.Header;
import oracle.bali.ewt.model.ArrayOneDModel;
import oracle.bali.ewt.model.ArrayTwoDModel;
import oracle.bali.ewt.model.OneDModel;
import oracle.bali.ewt.model.TwoDModel;
import oracle.bali.ewt.painter.FormatPainter;
import oracle.bali.ewt.painter.NullPainter;
import oracle.bali.ewt.selection.Cell;
import oracle.bali.ewt.selection.CellRange;
import oracle.bali.ewt.selection.TwoDSelection;
import oracle.bali.ewt.table.SpreadTable;
import oracle.bali.ewt.util.ImmInsets;

class DayComponent extends JPanel {

    /**
     * Constructor
     * <p>
     *
     * @param cal Calendar instance.
     */
    public DayComponent(Calendar cal,
            boolean isMultiSelect,
            TreeSet<Day> selectedDays,
            Locale locale) {
        _cal = cal;
        _isMultiSelect = isMultiSelect;
        _selectedDays = selectedDays;
        _minDay = null;
        _dfSymbols = new DateFormatSymbols(locale);

        _buildTableModel();
        _buildColHeaderModel();

        _dayTable = new CustomSpreadTable(_tableModel, _headerModel, null);
        _customizeDayTable();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(_dayTable);
    }

    /**
     * rebuilds the day table.
     */
    public void rebuild() {
        /*if(_dateProvider != null)
        {
        _dateProvider.setCurrentDate(_cal.getTime());
        _dateProvider.performLOVFetch();
        }*/

        _buildTableModel();
        _dayTable.setModels(_tableModel, _headerModel, null);

        _updateSelection();
        _updateAppearance();
    }

    /**
     * refresh the day panel to select any days (from the current month) in the
     * given collection.
     */
    private void _updateSelection() {
        _isSelectionAdjusting = true;
        TwoDSelection newSelection = new TwoDSelection();

        for (int week = 0; week <= 5; week++) {
            for (int day = 0; day <= 6; day++) {
                Date date = getDateAt(day, week);

                if ((date == null)
                        || !isInCurrentMonth(date)
                        || !_dateInRange(date)) {
                    continue;
                }

                if (_selectedDays.contains(new Day(date))) {
                    if ((_dateProvider == null) || (_dateProvider.isDateValid(date))) {
                        TwoDSelection cellSelection = new TwoDSelection(day, week);
                        newSelection = newSelection.add(cellSelection);
                    }
                }
            }
        }

        try {
            _dayTable.getGrid().setSelection(newSelection);
            _dayTable.getGrid().repaint();
        } catch (PropertyVetoException veto) {
            System.out.println("ui.calendar.DayPanel._updateSelection: VetoExeception");
        }

        _isSelectionAdjusting = false;
    }

    private void _updateAppearance() {
        ComplexAppearanceManager appMan =
                (ComplexAppearanceManager) _dayTable.getGrid().getAppearanceManager();

        for (int week = 0; week < 6; week++) {
            for (int day = 0; day < 7; day++) {
                Appearance app = appMan.getCellAppearance(day, week);
                Date date = getDateAt(day, week);

                if ((date == null) || !isInCurrentMonth(date)) {
                    app.setBackground(UIManager.getColor("TextArea.background"));
                    app.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
                    app.setFont(_boldFont);
                    app.setInsets(_cellInsets);
                } else if ((_dateProvider != null)
                        && (!_dateProvider.isDateValid(date))) {
                    app.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
                    app.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
                } else if (!_dateInRange(date)) {
                    app.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
                    app.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
                    app.setFont(_boldFont);
                    app.setInsets(_cellInsets);
                } else {
                    app.setBackground(UIManager.getColor("Table.background"));
                    app.setForeground(UIManager.getColor("TextArea.foreground"));
                    app.setFont(_boldFont);
                    app.setInsets(_cellInsets);
                }

                appMan.setCellAppearance(day, week, app);
            }
        }
    }

    /**
     * retruns the day table.
     * <p>
     *
     * @param none.
     * @return Day table.
     */
    public SpreadTable getTable() {
        return _dayTable;
    }

    /**
     * retruns the date at the given location.
     * <p>
     *
     * @param int day - The column of the table.
     * @param int week - The row of the table.
     * @return Date.
     */
    public Date getDateAt(int day, int week) {
        Object object = _tableModel.getData(day, week);
        return (object == null) ? null : (Date) object;
    }

    /**
     * Returns whether the given date is in the current month.
     * <p>
     *
     * @param date The given date.
     * @return boolean true if the given date is in the current month,
     *                 false otherwise.
     */
    public boolean isInCurrentMonth(Date date) {
        boolean bRet = false;

        int month = _cal.get(Calendar.MONTH);
        Date origDate = _cal.getTime();
        _cal.setTime(date);

        if (_cal.get(Calendar.MONTH) != month) {
            bRet = false;
        } else {
            bRet = true;
        }
        _cal.setTime(origDate);

        return bRet;
    }

    /**
     * Will disable all dates less than the given date
     * @param minimumDate
     */
    public void setMinimumDate(Date minimumDate) {
        _minDay = (minimumDate == null) ? null : new Day(minimumDate);
        _refreshSelectedDates();
        rebuild();
    }

    public Date getMinimumDate() {
        return _minDay.getDate();
    }

    public void setMaximumDate(Date maximumDate) {
        _maxDay = (maximumDate == null) ? null : new Day(maximumDate);
        _refreshSelectedDates();
        rebuild();
    }

    public Date getMaximumDate() {
        return _maxDay.getDate();
    }

    public void setDateProvider(CalendarDateProvider calDateProvider) {
        _dateProvider = calDateProvider;
        _refreshSelectedDates();
        rebuild();
    }

    public CalendarDateProvider getDateProvider() {
        return _dateProvider;
    }

    private boolean _dateInRange(Date date) {
        if ((_minDay == null) && (_maxDay == null)) {
            return true;
        }

        if (date == null) {
            return false;
        }

        Day dateDay = new Day(date);

        if ((_minDay != null) && (dateDay.compareTo(_minDay) < 0)) {
            return false;
        }

        if ((_maxDay != null) && (dateDay.compareTo(_maxDay) > 0)) {
            return false;
        }

        return true;
    }

    /**
     * rebuild the selected dates list, filtering out any that don't make enabled
     * restrictions such as minDate, MaxDate, CalendarDateProvider.isDateValid()
     *
     */
    private void _refreshSelectedDates() {
        for (Day day : (Day[]) _selectedDays.toArray(new Day[0])) {
            if (!_dateInRange(day.getDate())) {
                _selectedDays.remove(day);
            }

            if ((_dateProvider != null) && (!_dateProvider.isDateValid(day.getDate()))) {
                _selectedDays.remove(day);
            }
        }
    }

    /**
     * Build the table model of dates.
     *
     * As different countries start their weeks on different days, we can't assume
     * that the first column in the table is always Sunday (or Monday).
     * We use the Calendar.getFirstDayOfWeek() to help us wrap the dates to fit.
     */
    private void _buildTableModel() {
        Date date = _cal.getTime();

        int firstDayOfWeek = _cal.getFirstDayOfWeek() - 1;

        _cal.set(Calendar.DAY_OF_MONTH, 1);
        _cal.add(Calendar.DAY_OF_MONTH, 1 - _cal.get(Calendar.DAY_OF_WEEK) + firstDayOfWeek);  // plus firstDayOfWeek?

        int firstDay = _cal.get(Calendar.DAY_OF_MONTH);
        if ((firstDay > 1) && (firstDay < 10)) {
            _cal.add(Calendar.DAY_OF_MONTH, -7);
        }

        for (int w = 0; w < 6; w++) {
            for (int day = 0; day < 7; day++) {
                _tableModel.setData(day, w, _cal.getTime());
                _cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        _cal.setTime(date);
    }

    /**
     * Build the column header. Internally two loops are needed to account for the
     * fact that calendars start on different days of the week depening
     * on locale.
     */
    private void _buildColHeaderModel() {
        _headerModel = new ArrayOneDModel(7);
        int firstDay = _cal.getFirstDayOfWeek();
        for (int i = firstDay; i <= 7; i++) {
            _headerModel.setData(i - firstDay, _dfSymbols.getShortWeekdays()[i]);
        }

        for (int i = 1; i < firstDay; i++) {
            _headerModel.setData(7 - firstDay + i, _dfSymbols.getShortWeekdays()[i]);
        }
    }

    private void _customizeDayTable() {
        Font baseFont = UIManager.getFont("Label.font");
        _boldFont = baseFont.deriveFont(Font.BOLD).deriveFont((float) baseFont.getSize() - 1f);

        //modify the appearance
        Appearance appearance = _dayTable.getColumnHeader().getAppearance();
        appearance.setHorizontalJustify(Appearance.HORIZONTAL_JUSTIFY_CENTER);
        appearance.setForeground(UIManager.getColor("TitledBorder.titleColor"));
        appearance.setInsets(_cellInsets);
        Font smallerFont = baseFont.deriveFont((float) baseFont.getSize() - 1f);
        appearance.setFont(smallerFont);
        _dayTable.getColumnHeader().setAppearance(appearance);
        _dayTable.setColumnHeaderHeight(16);
        _dayTable.setFont(_boldFont);

        //remove the border in header
        NullPainter np = new NullPainter();
        _dayTable.getColumnHeader().setFirstItemBorderPainter(np);
        _dayTable.getColumnHeader().setLastItemBorderPainter(np);
        _dayTable.getColumnHeader().setHeaderItemBorderPainter(np);
        _dayTable.getColumnHeader().setFont(_boldFont);

        ComplexAppearanceManager appMan = new ComplexAppearanceManager();
        _dayTable.getGrid().setAppearanceManager(appMan);
        appearance = appMan.getAppearance();
        appearance.setHorizontalJustify(Appearance.HORIZONTAL_JUSTIFY_CENTER);
        appearance.setVerticalJustify(Appearance.VERTICAL_JUSTIFY_MIDDLE);
        appMan.setAppearance(appearance);

        if (_isMultiSelect) {
            _dayTable.getGrid().setGridSelectionManager(
                    new DayGridSelectionManager(GeneralGridSelectionManager.NONE,
                    GeneralGridSelectionManager.NONE,
                    GeneralGridSelectionManager.MULTIPLE));
        } else {
            _dayTable.getGrid().setGridSelectionManager(
                    new DayGridSelectionManager(GeneralGridSelectionManager.NONE,
                    GeneralGridSelectionManager.NONE,
                    GeneralGridSelectionManager.SINGLE));
        }

        _dayTable.setHorizontalSeparatorsVisible(false);
        _dayTable.setVerticalSeparatorsVisible(false);
        _dayTable.getColumnHeader().setCanResizeItems(false);

        FormatPainter datePainter = new FormatPainter(_dayFormat);
        _dayTable.getGrid().setColumnPainter(0, datePainter);
        _dayTable.getGrid().setColumnPainter(1, datePainter);
        _dayTable.getGrid().setColumnPainter(2, datePainter);
        _dayTable.getGrid().setColumnPainter(3, datePainter);
        _dayTable.getGrid().setColumnPainter(4, datePainter);
        _dayTable.getGrid().setColumnPainter(5, datePainter);
        _dayTable.getGrid().setColumnPainter(6, datePainter);

        _autoSetWidth();
    }

    private void _autoSetWidth() {
        int colWidth = _getMinColSize();
        _dayTable.getGrid().setColumnWidth(0, colWidth);
        _dayTable.getGrid().setColumnWidth(1, colWidth);
        _dayTable.getGrid().setColumnWidth(2, colWidth);
        _dayTable.getGrid().setColumnWidth(3, colWidth);
        _dayTable.getGrid().setColumnWidth(4, colWidth);
        _dayTable.getGrid().setColumnWidth(5, colWidth);
        _dayTable.getGrid().setColumnWidth(6, colWidth);

        _dayTable.getColumnHeader().setPreferredHeaderSize(colWidth * 7);
    }

    /*
     * the minimum size is the smallest size that allows strings to render
     * without clipping.
     */
    private int _getMinColSize() {
        int minSize = 0;
        FontMetrics fmetrics = getFontMetrics(_boldFont);

        for (String name : _dfSymbols.getShortWeekdays()) {
            int width = SwingUtilities.computeStringWidth(fmetrics, name);
            width += _cellInsets.left + _cellInsets.right;
            minSize = Math.max(minSize, width);
        }

        for (int i = 1; i <= 31; i++) {
            int width = SwingUtilities.computeStringWidth(fmetrics, (new Integer(i)).toString());
            minSize = Math.max(minSize, width);
        }

        int sepSize = _dayTable.getColumnGeometryManager().getSeparatorSize();
        minSize += sepSize;

        return minSize;
    }

    //private inner class that customizes the SpreadTable
    private class CustomSpreadTable extends SpreadTable {

        public CustomSpreadTable(
                TwoDModel gridModel,
                OneDModel columnHeaderModel,
                OneDModel rowHeaderModel) {
            super(gridModel, columnHeaderModel, rowHeaderModel);
            setBorder(null);
        }

        protected Grid createGrid(TwoDModel ds) {
            return new CustomGrid(ds);
        }

        protected Header createColumnHeader(OneDModel ds) {
            return new CustomColumnHeader(ds);
        }

        /**
         * A custom grid so disable dates not available
         */
        private class CustomGrid extends Grid {

            public CustomGrid(TwoDModel ds) {
                super(ds);
                setGridKeyHandler(new CustomKeyHandler());
            }
        }

        private class CustomColumnHeader extends Header {

            public CustomColumnHeader(OneDModel ds) {
                super(ds);
            }

            public boolean isFocusable() {
                return false;
            }

            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                // if the final UI design requires a line between header and grid...
        /*g.setColor(Color.BLACK);

                int y = getColumnHeaderHeight() - 3;

                g.drawLine(2, y, getWidth() - 2, y);*/
            }

            public Header.AccessibleHeader createAccessibleHeader() {
                return new AccessibleDayHeader();
            }

            private class AccessibleDayHeader extends Header.AccessibleHeader {

                public String getAccessibleName(int index) {
                    int mod = _cal.getFirstDayOfWeek();
                    int day = mod + index;
                    day = (day > 7) ? day - 7 : day;
                    return _dfSymbols.getWeekdays()[day];
                }
            }
        }

        private class CustomKeyHandler extends StandardGridKeyHandler {

            protected Cell enter(Grid grid, Cell focusCell, KeyEvent e, boolean shiftDown) {
                //_okAction.actionPerformed(
                //  new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "ok"));

                return focusCell;
            }
        }
    }

    /**
     * Selection manager class that maintains the master list of selected day
     * The _selectedDay set can contain day not in the month currently
     * set in the calendar.
     */
    private class DayGridSelectionManager extends GeneralGridSelectionManager {

        public DayGridSelectionManager(
                int columnConstraint, int rowConstraint, int cellConstraint) {
            super(columnConstraint, rowConstraint, cellConstraint);
        }

        public TwoDSelection modifySelection(
                TwoDSelection oldSelection, TwoDSelection newSelection, int how) {
            if (!_isSelectionAdjusting) {
                switch (how) {
                    case GeneralGridSelectionManager.REPLACE:
                        _selectedDays.clear();
                    case GeneralGridSelectionManager.ADD:
                        newSelection = (!_isMultiSelect) ? _addSingleSelection(newSelection) : _addMultiSelection(newSelection);
                        break;
                    case GeneralGridSelectionManager.SUBTRACT:
                        _removeSelection(newSelection);
                }
            }

            if (newSelection == null) {
                _updateSelection();
                newSelection = getSelection();
                how = GeneralGridSelectionManager.REPLACE;
            }

            return super.modifySelection(oldSelection, newSelection, how);
        }

        /**
         * Check whether the given date is within the current month for _cal
         * @param date date to check
         * @return true if date is in month
         */
        private boolean _isDateInCurrentMonth(Date date) {
            Date origTime = _cal.getTime();
            int thisMonth = _cal.get(Calendar.MONTH);
            _cal.setTime(date);
            int givenMonth = _cal.get(Calendar.MONTH);
            _cal.setTime(origTime);

            return thisMonth == givenMonth;
        }

        /**
         * Add a tables selection in single select mode
         * @param newValue The new selection of the day table
         */
        private TwoDSelection _addSingleSelection(TwoDSelection newValue) {
            _selectedDays.clear();
            CellRange[] cellRanges = newValue.getCellRanges();
            if (cellRanges != null) {
                Date newDate = getDateAt(newValue.getSingleCell().column,
                        newValue.getSingleCell().row);

                if ((newDate != null)
                        && _isDateInCurrentMonth(newDate)
                        && ((_dateProvider == null) || _dateProvider.isDateValid(newDate))
                        && _dateInRange(newDate)) {
                    _selectedDays.add(new Day(newDate));
                } else {
                    newValue = newValue.subtract(newValue);
                }
            }
            return newValue;
        }

        /**
         * update the selection in multi selection mode with the new day table selection
         * @param newValue the new selection on the day table
         */
        private TwoDSelection _addMultiSelection(TwoDSelection newValue) {
            CellRange[] cellRanges = newValue.getCellRanges();
            if (cellRanges != null) {
                for (int rangeIndex = 0; rangeIndex < cellRanges.length; rangeIndex++) {
                    CellRange range = cellRanges[rangeIndex];
                    for (int x = range.getLowerLimit().column; x <= range.getUpperLimit().column; x++) {
                        for (int y = range.getLowerLimit().row; y <= range.getUpperLimit().row; y++) {
                            Date newDate = getDateAt(x, y);

                            if ((newDate != null)
                                    && _isDateInCurrentMonth(newDate)
                                    && ((_dateProvider == null) || _dateProvider.isDateValid(newDate))
                                    && _dateInRange(newDate)) {
                                _selectedDays.add(new Day(newDate));
                            } else {
                                TwoDSelection removeCell = new TwoDSelection(new Cell(x, y));
                                newValue = newValue.subtract(removeCell);
                            }
                        }
                    }
                }
            }

            return newValue;
        }

        /**
         * remove the selection from the selected dates
         * @param newValue the selection to remove
         */
        private void _removeSelection(TwoDSelection newValue) {
            CellRange[] cellRanges = newValue.getCellRanges();
            if (cellRanges != null) {
                for (int rangeIndex = 0; rangeIndex < cellRanges.length; rangeIndex++) {
                    CellRange range = cellRanges[rangeIndex];
                    for (int x = range.getLowerLimit().column; x <= range.getUpperLimit().column; x++) {
                        for (int y = range.getLowerLimit().row; y <= range.getUpperLimit().row; y++) {
                            Date newDate = getDateAt(x, y);
                            if ((newDate != null) && _isDateInCurrentMonth(newDate)) {
                                _selectedDays.remove(new Day(newDate));
                            }
                        }
                    }
                }
            }
        }
    }

    void cleanUp() {
        _dayTable = null;
        _tableModel = null;
        _headerModel = null;
        _cal = null;
        if (_selectedDays != null) {
            _selectedDays.clear();
        }
        _selectedDays = null;
        _dateProvider = null;
    }
    ////////////////////////////////////////////////////////////////////////
    // end of private inner class
    ////////////////////////////////////////////////////////////////////////
    private boolean _isSelectionAdjusting = false;
    private SpreadTable _dayTable;
    private ArrayTwoDModel _tableModel = new ArrayTwoDModel(_DAYS_IN_WEEK, _WEEKS_IN_MONTH);
    private ArrayOneDModel _headerModel = new ArrayOneDModel(_DAYS_IN_WEEK);
    private static final int _DAYS_IN_WEEK = 7;
    private static final int _WEEKS_IN_MONTH = 6;
    private DateFormatSymbols _dfSymbols;
    private Calendar _cal;
    private boolean _isMultiSelect;
    private SimpleDateFormat _dayFormat = new SimpleDateFormat("d");
    private CalendarDateProvider _dateProvider;
    private Font _boldFont;
    private ImmInsets _cellInsets = new ImmInsets(0, 0, 0, 0);
    private TreeSet<Day> _selectedDays;
    private Day _minDay;
    private Day _maxDay;
}
