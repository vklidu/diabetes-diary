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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import org.diabetesdiary.diary.utils.MyLookup;
import org.diabetesdiary.diary.api.DiaryRepository;
import org.diabetesdiary.diary.domain.RecordInvest;
import org.joda.time.DateTime;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class RecordInvestEditTableModel extends AbstractTableModel {

    private static NumberFormat format = NumberFormat.getInstance();
    private static DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    private List<RecordInvest> recs;
    private static final String DELETE_ICO = "org/diabetesdiary/calendar/resources/delete16.png";
    private Date dateTo;
    private DiaryRepository diary;
    private Date dateFrom;

    /** Creates a new instance of CalendarTableModel */
    public RecordInvestEditTableModel(Date date) {
        diary = MyLookup.getDiaryRepo();
        setDate(date);
    }

    @Override
    public int getRowCount() {
        return recs == null ? 0 : recs.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RecordInvest rec = recs.get(rowIndex);
        if (columnIndex == getColumnCount() - 1) {
            if (rec != null && rec.getValue() != null) {
                return new ImageIcon(ImageUtilities.loadImage(DELETE_ICO, true));
            } else {
                return null;
            }
        }

        switch (columnIndex) {
            case 0:
                return rec.getInvest().getName();
            case 1:
                return rec.getValue() != null ? format.format(rec.getValue()) + " " + rec.getInvest().getUnit() : "";
            case 2:
                return dateFormat.format(rec.getDatetime().toDate());
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

    public RecordInvest getRecord(int rowIndex, int columnIndex) {
        return recs == null ? null : recs.get(rowIndex);
    }

    @Override
    public String getColumnName(int column) {
        if (column == getColumnCount() - 1) {
            return "";
        }

        switch (column) {
            case 0:
                return NbBundle.getMessage(RecordInvestEditTableModel.class, "Type");
            case 1:
                return NbBundle.getMessage(RecordInvestEditTableModel.class, "Value");
            case 2:
                return NbBundle.getMessage(RecordInvestEditTableModel.class, "Time");
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
        if(date != null){
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date.getTime());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            this.dateFrom = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            this.dateTo = cal.getTime();
        }else{
            this.dateFrom = null;
            this.dateTo = null;
        }
        fillData();
    }

    public void fillData() {
        //no data => end
        if (MyLookup.getCurrentPatient() == null || dateFrom == null || dateTo == null) {
            recs = null;
            return;
        }
        recs = MyLookup.getCurrentPatient().getRecordInvests(new DateTime(dateFrom), new DateTime(dateTo));
    }
}