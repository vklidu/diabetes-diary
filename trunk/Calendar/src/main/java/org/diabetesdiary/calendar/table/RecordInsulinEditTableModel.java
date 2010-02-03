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
package org.diabetesdiary.calendar.table;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import org.diabetesdiary.calendar.utils.DbLookUp;
import org.diabetesdiary.datamodel.api.DiaryRepository;
import org.diabetesdiary.datamodel.pojo.InsulinSeason;
import org.diabetesdiary.datamodel.pojo.RecordInsulinDO;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 *
 * @author Jiri Majer
 */
public class RecordInsulinEditTableModel extends AbstractTableModel {

    private static NumberFormat format = NumberFormat.getInstance();
    private static DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    private List<RecordInsulinDO> recs;
    private static final String DELETE_ICO = "org/diabetesdiary/calendar/resources/delete16.png";
    private Date dateTo;
    private DiaryRepository diary;
    private Date dateFrom;

    /** Creates a new instance of CalendarTableModel */
    public RecordInsulinEditTableModel(Date date) {
        diary = DbLookUp.getDiaryRepo();
        setDate(date);
    }

    public int getRowCount() {
        return recs == null ? 0 : recs.size();
    }

    public int getColumnCount() {
        return 5;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        RecordInsulinDO rec = recs.get(rowIndex);
        if (columnIndex == getColumnCount() - 1) {
            if (rec != null && rec.getAmount() != null) {
                return new ImageIcon(Utilities.loadImage(DELETE_ICO, true));
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
                return dateFormat.format(rec.getId().getDate());
            case 3:
                return rec.getSeason() != null ? InsulinSeason.valueOf(rec.getSeason()).toString() : "";
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

    public RecordInsulinDO getRecord(int rowIndex, int columnIndex) {
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

    public void setDate(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date.getTime());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            this.dateFrom = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            this.dateTo = cal.getTime();
        } else {
            this.dateFrom = null;
            this.dateTo = null;
        }
        fillData();
    }

    public void fillData() {
        //no data => end
        if (diary.getCurrentPatient() == null || dateFrom == null || dateTo == null) {
            recs = null;
            return;
        }
        recs = diary.getRecordInsulins(dateFrom, dateTo, diary.getCurrentPatient().getIdPatient());
    }
}
