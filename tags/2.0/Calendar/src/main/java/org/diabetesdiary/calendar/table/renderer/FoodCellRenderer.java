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

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.diary.domain.FoodUnit;
import org.diabetesdiary.diary.domain.RecordFood;
import org.diabetesdiary.diary.utils.MyLookup;

/**
 *
 * @author Jiri Majer
 */
public class FoodCellRenderer extends AbstractDiaryCellRenderer<Object> {

    private NumberFormat format = NumberFormat.getInstance();
    private DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

    @Override
    protected String getText(Object value) {
        if (value instanceof RecordFood) {
            FoodUnit sachUnit = MyLookup.getDiaryRepo().getSacharidUnit(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT));
            return format.format(((RecordFood) value).getSachUnits(sachUnit));
        } else if (value instanceof RecordFood[]) {
            FoodUnit sachUnit = MyLookup.getDiaryRepo().getSacharidUnit(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT));
            RecordFood[] recs = (RecordFood[]) value;
            double sum = 0;
            for (RecordFood rec : recs) {
                if (rec != null && rec.getAmount() != null) {
                    sum += rec.getSachUnits(sachUnit);
                }
            }
            return format.format(sum);
        }
        return null;
    }

    @Override
    protected String getToolTip(Object rec) {
        if (rec instanceof RecordFood) {
            return createToolTip((RecordFood)rec);
        } else if (rec instanceof RecordFood[]) {
            return createToolTip((RecordFood[])rec);
        }
        return null;
    }

    private String createToolTip(RecordFood rec) {
        if (rec == null || rec.getAmount() == null || rec.getFood() == null) {
            return null;
        }

        String result = timeFormat.format(rec.getDatetime().toDate()) + "\n";
        result += rec.getFood().getName();
        result += " " + format.format(rec.getAmount()) + " " + rec.getUnit();

        return result;
    }

    private String createToolTip(RecordFood[] recs) {
        if (recs == null || recs.length < 1 || recs[0] == null || recs[0].getAmount() == null || recs[0].getFood() == null) {
            return null;
        }
        StringBuffer result = new StringBuffer();
        Date lastDate = null;
        for (RecordFood rec : recs) {
            if (rec != null) {
                if (lastDate == null || !lastDate.equals(rec.getDatetime().toDate())) {
                    result.append(timeFormat.format(rec.getDatetime().toDate())).append('\n');
                }
                result.append(rec.getFood().getName());
                result.append(' ').append(format.format(rec.getAmount()));
                result.append(' ').append(rec.getUnit());
                result.append('\n');
                lastDate = rec.getDatetime().toDate();
            }
        }

        return result.toString();
    }
}
