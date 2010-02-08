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
package org.diabetesdiary.calendar.table.renderer;

import org.diabetesdiary.calendar.table.CalendarDay;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.joda.time.DateTimeConstants;

/**
 *
 * @author Jiri Majer
 */
public class DayRenderer extends JLabel implements TableCellRenderer {

    private static final Color forColor = Color.BLACK;
    private static final Color backColor = Color.WHITE;
    private static final Color forSelColor = Color.WHITE;
    private static final Color backSelColor = new Color(30, 30, 100);

    /** Creates a new instance of CalendarCellRenderer */
    public DayRenderer() {
        super();
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return createCell(table, value, isSelected);
    }

    public static Component createCell(JTable table, Object value, boolean isSelected) {
        DayRenderer result = new DayRenderer();
        if (isSelected) {
            result.setBackground(backSelColor);
            result.setForeground(forSelColor);
        } else {
            result.setBackground(backColor);
            result.setForeground(forColor);
        }
        result.setHorizontalAlignment(CENTER);
        if (value instanceof CalendarDay) {
            CalendarDay rec = (CalendarDay) value;
            if (rec.getDate() != null) {
                if (rec.getDate().getDayOfWeek() == DateTimeConstants.SUNDAY) {
                    if (isSelected) {
                        result.setBackground(Color.DARK_GRAY);
                    } else {
                        result.setBackground(Color.LIGHT_GRAY);
                    }
                }
                result.setText(String.valueOf(rec.getDate().getDayOfMonth()));
                result.setToolTipText(createToolTip(rec));
            }
        }

        return result;
    }

    private static String createToolTip(CalendarDay rec) {
        return null;
    }
}