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

import java.awt.Color;
import java.text.DateFormat;
import java.text.NumberFormat;
import org.diabetesdiary.diary.domain.RecordInsulin;

/**
 *
 * @author Jiri Majer
 */
public class InsulinCellRenderer extends AbstractDiaryCellRenderer<Object> {

    private NumberFormat format = NumberFormat.getInstance();
    private static final String VALUES_SEPARATOR = ";";

    @Override
    protected String getText(Object value) {
        if (value instanceof RecordInsulin) {
            RecordInsulin rec = (RecordInsulin) value;
            return format.format(rec.getAmount()) + (rec.getNotice() != null && rec.getNotice().length() > 0 ? "!" : "");
        } else if (value instanceof RecordInsulin[]) {
            RecordInsulin[] recs = (RecordInsulin[]) value;
            StringBuilder res = new StringBuilder();
            for (RecordInsulin rec : recs) {
                res.append(VALUES_SEPARATOR).append(format.format(rec.getAmount()));
                if (rec.getNotice() != null && rec.getNotice().length() > 0) {
                    res.append("!");
                }
            }
            return res.substring(VALUES_SEPARATOR.length());
        }
        return null;
    }

    @Override
    protected String getToolTip(Object value) {
        if (value instanceof RecordInsulin) {
            return createToolTip((RecordInsulin) value);
        } else if (value instanceof RecordInsulin[]) {
            return createToolTip((RecordInsulin[]) value);
        }
        return null;
    }

    @Override
    protected Color getBackgroundColor(Object value, boolean isSelected) {
        if (value instanceof RecordInsulin) {
            RecordInsulin rec = (RecordInsulin) value;
            if (rec.isBasal()) {
                return isSelected ? Color.DARK_GRAY : Color.LIGHT_GRAY;
            }
        } else if (value instanceof RecordInsulin[]) {
            RecordInsulin[] recs = (RecordInsulin[]) value;
            for (RecordInsulin rec : recs) {
                if (rec.isBasal()) {
                    return isSelected ? Color.DARK_GRAY : Color.LIGHT_GRAY;
                }
            }
        }
        return super.getBackgroundColor(value, isSelected);
    }

    private String createToolTip(RecordInsulin rec) {
        if (rec == null || rec.getAmount() == null) {
            return null;
        }
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        String result = timeFormat.format(rec.getDatetime().toDate()) + "\n";
        result += rec.getInsulin().getName() + " " + format.format(rec.getAmount()) + "U";
        if (rec.getNotice() != null && rec.getNotice().length() > 0) {
            return result + "\n(" + rec.getNotice() + ")";
        }

        return result;
    }

    private String createToolTip(RecordInsulin[] recs) {
        if (recs == null || recs.length < 1 || recs[0] == null || recs[0].getAmount() == null) {
            return null;
        }
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        StringBuffer result = new StringBuffer();
        for (RecordInsulin rec : recs) {
            result.append(timeFormat.format(rec.getDatetime().toDate())).append('\n');
            result.append(rec.getInsulin().getName()).append(' ').append(format.format(rec.getAmount())).append('U');
            if (rec.getNotice() != null && rec.getNotice().length() > 0) {
                result.append("\n(").append(rec.getNotice()).append(')');
            }
            result.append('\n');
        }
        return result.toString();
    }
}
