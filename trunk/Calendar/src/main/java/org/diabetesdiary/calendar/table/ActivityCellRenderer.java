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
import org.diabetesdiary.datamodel.pojo.RecordActivity;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class ActivityCellRenderer extends JLabel implements TableCellRenderer {

    private static NumberFormat format = NumberFormat.getIntegerInstance();
    private static final Color backColor = Color.WHITE;
    private static final Color backSelColor = new Color(30, 30, 100);
    private static final String VALUES_SEPARATOR = ";";

    /** Creates a new instance of CalendarCellRenderer */
    public ActivityCellRenderer() {
        super();
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return createCell(table, value, isSelected);
    }

    public static Component createCell(JTable table, Object value, boolean isSelected) {
        ActivityCellRenderer result = new ActivityCellRenderer();
        result.setHorizontalAlignment(CENTER);
        if (isSelected) {
            result.setBackground(backSelColor);
        } else {
            result.setBackground(backColor);
        }
        if (value instanceof Energy) {
            Energy rec = (Energy) value;
            if (rec.getValue() != null) {
                result.setText(format.format(rec.getValue()));
            } else {
                result.setText(NbBundle.getMessage(ActivityCellRenderer.class, "unknown.weight.tall"));
                result.setToolTipText(result.getText());
            }
        } else if (value instanceof RecordActivity) {
            RecordActivity rec = (RecordActivity) value;
            if (rec.getDuration() != null && rec.getActivity() != null) {
                if (rec.getWeight() != null) {
                    result.setText(format.format(rec.getActivity().getPower() * rec.getDuration() * rec.getWeight()));
                } else {
                    result.setText(NbBundle.getMessage(SumModel.class, "unknown.weight"));                    
                }
                if (rec.getNotice() != null && rec.getNotice().length() > 0) {
                    result.setText(result.getText() + "!");
                }
                result.setToolTipText(createToolTip(rec));
            }
        } else if (value instanceof RecordActivity[]) {
            RecordActivity[] values = (RecordActivity[]) value;
            if (values.length > 0 && values[0] != null && values[0].getActivity() != null) {
                boolean note = false;
                Double sum = 0d;
                for (RecordActivity val : values) {
                    if (val.getWeight() != null) {
                        sum += val.getActivity().getPower() * val.getDuration() * val.getWeight();
                    } else {
                        sum = Double.NEGATIVE_INFINITY;
                    }
                    if (val.getNotice() != null && val.getNotice().length() > 0) {
                        note = true;
                    }
                }
                if (sum > 0) {
                    result.setText(format.format(sum));
                    if (note) {
                        result.setText(result.getText() + "!");
                    }
                } else {
                    result.setText(NbBundle.getMessage(SumModel.class, "unknown.weight"));
                }

                result.setToolTipText(createToolTip(values));
            }
        }
        return result;
    }

    private static String createToolTip(RecordActivity rec) {
        if (rec == null || rec.getDuration() == null || rec.getActivity() == null) {
            return null;
        }
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        String result = timeFormat.format(rec.getId().getDate()) + "\n";
        result += rec.getActivity().getName() + ": " + format.format(rec.getDuration()) + " min";
        if (rec.getNotice() != null && rec.getNotice().length() > 0) {
            return result + "\n(" + rec.getNotice() + ")";
        }
        return result;
    }

    private static String createToolTip(RecordActivity[] values) {
        if (values == null || values.length < 1 || values[0] == null || values[0].getDuration() == null || values[0].getActivity() == null) {
            return null;
        }
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        StringBuffer result = new StringBuffer();
        for (RecordActivity rec : values) {
            result.append(timeFormat.format(rec.getId().getDate())).append('\n');
            result.append(rec.getActivity().getName()).append(": ").append(format.format(rec.getDuration())).append(' ').append(" min");
            if (rec.getNotice() != null && rec.getNotice().length() > 0) {
                result.append("\n(").append(rec.getNotice()).append(')');
            }
            result.append('\n');
        }
        return result.toString();
    }
}
