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
import org.diabetesdiary.diary.domain.RecordInsulinPumpBasal;
import org.diabetesdiary.diary.domain.RecordInsulin;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Jiri Majer
 */
public class InsulinPumpBasalRenderer extends AbstractDiaryCellRenderer<RecordInsulinPumpBasal> {

    private static final Color backColor = Color.LIGHT_GRAY;
    private static final Color backSelColor = Color.DARK_GRAY;

    @Override
    protected String getText(RecordInsulinPumpBasal rec) {
        return rec == null ? null : rec.toString();
    }

    @Override
    protected String getToolTip(RecordInsulinPumpBasal records) {
        if (records == null) {
            return null;
        }
        StringBuffer result = new StringBuffer();
        DateTimeFormatter dateFormat = DateTimeFormat.shortTime();
        double lastVal = -1;
        DateTime firstDate = null;
        DateTime lastDate = null;
        for (RecordInsulin rec : records.getData()) {
            if (rec != null) {
                if (lastVal == -1) {
                    firstDate = rec.getDatetime();
                    lastDate = rec.getDatetime();
                    lastVal = rec.getAmount();
                } else if (rec.getAmount() != lastVal) {
                    result.append(dateFormat.print(firstDate));
                    result.append("-");
                    result.append(dateFormat.print(rec.getDatetime()));
                    result.append(" ").append(lastVal).append("U\n");
                    lastVal = rec.getAmount();
                    firstDate = rec.getDatetime();
                    lastDate = rec.getDatetime();
                } else {
                    lastDate = rec.getDatetime();
                }
            } else if (lastVal != -1) {
                result.append(dateFormat.print(firstDate));
                result.append("-");
                result.append(dateFormat.print(lastDate.plusHours(1)));
                result.append(" ").append(lastVal).append("U\n");
                lastVal = -1;
            }
        }
        if (lastVal != -1) {
            result.append(dateFormat.print(firstDate));
            result.append("-");
            result.append(dateFormat.print(lastDate.plusHours(1)));
            result.append(" ").append(lastVal).append("U\n");

        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }

    @Override
    protected Color getBackgroundColor(RecordInsulinPumpBasal rec, boolean selected) {
        return selected ? backSelColor : backColor;
    }
}
