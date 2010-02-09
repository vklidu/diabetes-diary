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


package org.diabetesdiary.calendar.table.model;

import javax.swing.table.TableColumnModel;
import org.diabetesdiary.calendar.table.header.ColumnGroup;
import org.diabetesdiary.calendar.table.CalendarDay;
import org.joda.time.DateTime;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class DayModel extends AbstractRecordSubModel {

    public DayModel(DateTime month) {
        super(month);
    }
    
    @Override
    public int getColumnCount() {
        return 1;
    }
    
    @Override
    public ColumnGroup getColumnHeader(TableColumnModel cm, int baseIndex) {
        cm.getColumn(baseIndex).setPreferredWidth(20);        
        return null;
    }
    
    @Override
    public CalendarDay getValueAt(int row, int col) {
        return new CalendarDay(dateTime.withDayOfMonth(row+1));
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return CalendarDay.class;
    }
    
    @Override
    public String getColumnName(int col) {
        return NbBundle.getMessage(DayModel.class, "TableModel.Day");
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void invalidateData() {
    }

}
