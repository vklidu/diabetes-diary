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
package org.diabetesdiary.commons.swing.calendar;

import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import org.joda.time.DateTime;

/**
 *
 * @author Jirka Majer
 */
public class BaseDateTimeJSpinner extends JSpinner {

    private final SpinnerDateModel sm;

    public BaseDateTimeJSpinner(DateTime dateTime, String pattern, int calendarField) {
        super();
        sm = new SpinnerDateModel(dateTime != null ? dateTime.toDate() : new Date(), null, null, calendarField);
        setModel(sm);
        setEditor(new JSpinner.DateEditor(this, pattern));
    }

    public DateTime getDateTime() {
        return sm.getDate() == null ? null : new DateTime(sm.getDate());
    }

    public void setDateTime(DateTime dateTime) {
        sm.setValue(dateTime.toDate());
    }
}
