/*
 *   Copyright (C) 2006-2007 Jiri Majer. All Rights Reserved.
 *   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *   This code is free software; you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License version 2 only, as
 *   published by the Free Software Foundation.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.diabetesdiary.calendar.table.model.recordeditor;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import org.diabetesdiary.calendar.utils.DataChangeEvent;
import org.diabetesdiary.calendar.utils.DataChangeListener;
import org.diabetesdiary.diary.utils.MyLookup;
import org.diabetesdiary.diary.domain.RecordInsulin;
import org.joda.time.DateTime;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class RecordInsulinEditTableModel extends AbstractTableModel implements DataChangeListener {

    private static NumberFormat format = NumberFormat.getInstance();
    private static DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    private List<RecordInsulin> recs;
    private static final String DELETE_ICO = "org/diabetesdiary/calendar/resources/delete16.png";
    private DateTime date;

    /** Creates a new instance of CalendarTableModel */
    public RecordInsulinEditTableModel(DateTime date) {
        setDate(date);
    }

    @Override
    public int getRowCount() {
        return recs == null ? 0 : recs.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RecordInsulin rec = recs.get(rowIndex);
        if (columnIndex == getColumnCount() - 1) {
            if (rec != null && rec.getAmount() != null) {
                return new ImageIcon(ImageUtilities.loadImage(DELETE_ICO, true));
            } else {
                return null;
            }
        }

        switch (columnIndex) {
            case 0:
                return rec.getInsulin().getName();
            case 1:
                return rec.getAmount() != null ? format.format(rec.getAmount()) + " U" : "";
            case 2:
                return dateFormat.format(rec.getDatetime().toDate());
            case 3:
                return rec.getSeason() != null ? rec.getSeason().toString() : "";
        }

        return "";
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
    }

    public RecordInsulin getRecord(int rowIndex, int columnIndex) {
        return recs == null ? null : recs.get(rowIndex);
    }

    @Override
    public String getColumnName(int column) {
        if (column == getColumnCount() - 1) {
            return "";
        }

        switch (column) {
            case 0:
                return NbBundle.getMessage(RecordInsulinEditTableModel.class, "Type");
            case 1:
                return NbBundle.getMessage(RecordInsulinEditTableModel.class, "Value");
            case 2:
                return NbBundle.getMessage(RecordInsulinEditTableModel.class, "Time");
            case 3:
                return NbBundle.getMessage(RecordInsulinEditTableModel.class, "Season");
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == getColumnCount() - 1) {
            return ImageIcon.class;
        }

        return super.getColumnClass(columnIndex);
    }

    public void setDate(DateTime date) {
        this.date = date;
        reloadData();
    }

    private void reloadData() {
        //no data => end
        if (MyLookup.getCurrentPatient() == null || date == null) {
            recs = null;
            return;
        }
        recs = MyLookup.getCurrentPatient().getRecordInsulins(date.toDateMidnight().toDateTime(), date.toDateMidnight().plusDays(1).toDateTime());
        fireTableDataChanged();
    }

    @Override
    public void onDataChange(DataChangeEvent evt) {
        if (evt.getDataChangedClazz() == null || evt.getDataChangedClazz().equals(RecordInsulin.class)) {
            reloadData();
        }
    }
}
