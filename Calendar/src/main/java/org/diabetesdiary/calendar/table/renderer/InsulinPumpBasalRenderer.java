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
import java.util.Date;
import org.diabetesdiary.diary.domain.RecordInsulinPumpBasal;
import org.diabetesdiary.diary.domain.RecordInsulin;

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
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        double lastVal = -1;
        Date firstDate = null;
        Date lastDate = null;
        for (RecordInsulin rec : records.getData()) {
            if (rec != null) {
                if (lastVal == -1) {
                    firstDate = rec.getDatetime().toDate();
                    lastDate = rec.getDatetime().toDate();
                    lastVal = rec.getAmount();
                } else if (rec.getAmount() != lastVal) {
                    result.append(dateFormat.format(firstDate));
                    result.append("-");
                    result.append(dateFormat.format(new Date(rec.getDatetime().toDate().getTime())));
                    result.append(" ").append(lastVal).append("U\n");
                    lastVal = rec.getAmount();
                    firstDate = rec.getDatetime().toDate();
                    lastDate = rec.getDatetime().toDate();
                } else {
                    lastDate = rec.getDatetime().toDate();
                }
            } else if (lastVal != -1) {
                result.append(dateFormat.format(firstDate));
                result.append("-");
                result.append(dateFormat.format(new Date(lastDate.getTime() + 60 * 60 * 1000)));
                result.append(" ").append(lastVal).append("U\n");
                lastVal = -1;
            }
        }
        if (lastVal != -1) {
            result.append(dateFormat.format(firstDate));
            result.append("-");
            result.append(dateFormat.format(new Date(lastDate.getTime() + 60 * 60 * 1000)));
            result.append(" ").append(lastVal).append("U\n");

        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }

    @Override
    protected Color getBackgroundColor(RecordInsulinPumpBasal rec, boolean selected) {
        return selected ? backSelColor : backColor;
    }
}
