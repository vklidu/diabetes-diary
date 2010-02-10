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

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.diabetesdiary.calendar.table.renderer.DefaultRenderer;
import org.joda.time.DateTime;

/**
 *
 * @author Jirka Majer
 */
public abstract class AbstractRecordSubModel implements TableSubModel {

    protected DateTime dateTime;
    private boolean visible = true;

    public AbstractRecordSubModel(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setDate(DateTime date) {
        this.dateTime = date;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    protected DateTime getFrom() {
        return dateTime.withDayOfMonth(1).toDateMidnight().toDateTime();
    }

    protected DateTime getTo() {
        return dateTime.dayOfMonth().withMaximumValue().plusDays(1).toDateMidnight().toDateTime();
    }

    @Override
    public TableCellRenderer getCellRenderer(int columnIndex) {
        return new DefaultRenderer();
    }

    @Override
    public TableCellEditor getCellEditor(int columnIndex) {
        return new DefaultCellEditor(new JTextField());
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return null;
    }

}
