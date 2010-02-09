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
import org.joda.time.DateTimeConstants;

/**
 *
 * @author Jiri Majer
 */
public class DayRenderer extends AbstractDiaryCellRenderer<CalendarDay> {

    @Override
    protected Color getBackgroundColor(CalendarDay rec, boolean selected) {
        if (rec.getDate().getDayOfWeek() == DateTimeConstants.SUNDAY) {
            return selected ? Color.DARK_GRAY : Color.LIGHT_GRAY;
        }
        return super.getBackgroundColor(rec, selected);
    }

    @Override
    protected String getText(CalendarDay rec) {
        return String.valueOf(rec.getDate().getDayOfMonth());
    }
}
