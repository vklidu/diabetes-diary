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

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.diabetesdiary.datamodel.pojo.RecordInsulin;

/**
 *
 * @author Jiri Majer
 */
public class InsulinCellRenderer extends JLabel implements TableCellRenderer {

    private static NumberFormat format = NumberFormat.getInstance();
    private static final Color forColor = Color.BLACK;
    private static final Color backColor = Color.WHITE;
    private static final Color forSelColor = Color.WHITE;
    private static final Color backSelColor = new Color(30, 30, 100);
    private static final String VALUES_SEPARATOR = ";";

    /** Creates a new instance of CalendarCellRenderer */
    public InsulinCellRenderer() {
        super();
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return createCell(table, value, isSelected);
    }

    public static Component createCell(JTable table, Object value, boolean isSelected) {
        InsulinCellRenderer result = new InsulinCellRenderer();
        if (isSelected) {
            result.setBackground(backSelColor);
            result.setForeground(forSelColor);
        } else {
            result.setBackground(backColor);
            result.setForeground(forColor);
        }
        result.setHorizontalAlignment(CENTER);
        if (value instanceof RecordInsulin) {
            RecordInsulin rec = (RecordInsulin) value;
            if (rec.getAmount() != null) {
                if (rec.isBasal()) {
                    if (isSelected) {
                        result.setBackground(Color.DARK_GRAY);
                    } else {
                        result.setBackground(Color.LIGHT_GRAY);
                    }
                }
                result.setText(format.format(rec.getAmount()));
                if (rec.getNotice() != null && rec.getNotice().length() > 0) {
                    result.setText(result.getText() + "!");
                }
                result.setToolTipText(createToolTip(rec));
            }
        } else if (value instanceof RecordInsulin[]) {
            RecordInsulin[] recs = (RecordInsulin[]) value;
            for (RecordInsulin rec : recs) {
                if (rec.getAmount() != null) {
                    if (rec.isBasal()) {
                        if (isSelected) {
                            result.setBackground(Color.DARK_GRAY);
                        } else {
                            result.setBackground(Color.LIGHT_GRAY);
                        }
                    }
                    result.setText(result.getText() + VALUES_SEPARATOR + format.format(rec.getAmount()));
                    if (rec.getNotice() != null && rec.getNotice().length() > 0) {
                        result.setText(result.getText() + "!");
                    }
                }
            }
            result.setText(result.getText().substring(1));
            result.setToolTipText(createToolTip(recs));
        }

        return result;
    }

    private static String createToolTip(RecordInsulin rec) {
        if (rec == null || rec.getAmount() == null) {
            return null;
        }
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        String result = timeFormat.format(rec.getId().getDate()) + "\n";
        result += rec.getInsulin().getName() + " " + format.format(rec.getAmount()) + "U";
        if (rec.getNotice() != null && rec.getNotice().length() > 0) {
            return result + "\n(" + rec.getNotice() + ")";
        }

        return result;
    }

    private static String createToolTip(RecordInsulin[] recs) {
        if (recs == null || recs.length < 1 || recs[0] == null || recs[0].getAmount() == null) {
            return null;
        }
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        StringBuffer result = new StringBuffer();
        for (RecordInsulin rec : recs) {
            result.append(timeFormat.format(rec.getId().getDate())).append('\n');
            result.append(rec.getInsulin().getName()).append(' ').append(format.format(rec.getAmount())).append('U');
            if (rec.getNotice() != null && rec.getNotice().length() > 0) {
                result.append("\n(").append(rec.getNotice()).append(')');
            }
            result.append('\n');
        }
        return result.toString();
    }
}