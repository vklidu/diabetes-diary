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

import java.util.List;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.diabetesdiary.calendar.table.header.ColumnGroup;
import org.joda.time.DateTime;

/**
 *
 * @author Jiri Majer
 */
public interface TableSubModel {
     
    public ColumnGroup getColumnHeader(List<TableColumn> cols);
     
    public int getColumnCount();

    public String getColumnName(int columnIndex);

    public Class<?> getColumnClass(int columnIndex);

    public boolean isCellEditable(int rowIndex, int columnIndex);

    public Object getValueAt(int rowIndex, int columnIndex);

    public void setValueAt(Object aValue, int rowIndex, int columnIndex);

    public void setDate(DateTime date);

    public void setVisible(boolean visible);

    public boolean isVisible();

    public void invalidateData();

    public TableCellRenderer getCellRenderer(int columnIndex);

    public TableCellEditor getCellEditor(int columnIndex);
    
}
