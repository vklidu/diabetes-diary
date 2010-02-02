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

import java.util.Calendar;
import java.util.Collection;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import org.diabetesdiary.calendar.ColumnGroup;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class DayModel implements TableSubModel, Comparable<TableSubModel>{

    private int baseIndex;
    private Calendar month;
    
    /** Creates a new instance of DayModel */
    public DayModel(Calendar month) {
        this.month = month;
    }
    
    public int getColumnCount() {
        return 1;
    }
    
    public ColumnGroup getColumnHeader(TableColumnModel cm) {
        cm.getColumn(baseIndex).setPreferredWidth(20);        
        return null;
    }
    
    public Object getValueAt(int row, int col) {
        month.set(Calendar.DAY_OF_MONTH,row+1);
        return new CalendarDay(month.getTime());
    }
    
    public Object getNewRecordValueAt(int rowIndex, int columnIndex) {
        return null;
    }
    
    public void setValueAt(Object value, int row, int col) {
        //nothing
    }
    
    public Class<?> getColumnClass(int columnIndex) {
        return CalendarDay.class;
    }
    
    public String getColumnName(int col) {
        return NbBundle.getMessage(DayModel.class, "TableModel.Day");
    }
    
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    
    public void setData(Collection<?> data) {
        //nothing to do
    }
    
    public int getBaseIndex() {
        return baseIndex;
    }
    
    public int compareTo(TableSubModel o) {
        return Integer.valueOf(getBaseIndex()).compareTo(o.getBaseIndex());
    }
    
    public void setBaseIndex(int baseIndex) {
        this.baseIndex = baseIndex;
    }

    public int getRowCount() {
        throw new IllegalStateException("Don't use it.");
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }
    
}
