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

import java.awt.Component;
import org.diabetesdiary.commons.swing.calendar.datemania.CalendarPanel;
import java.awt.Dialog;
import java.util.Date;
import org.joda.time.DateTime;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

/**
 *
 * @author Jirka Majer
 */
public class DateTimePicker {

    private Dialog dialog;
    private final CalendarPanel calendar;
    private DateTime result;
    private final DialogDescriptor dialogDesc;

    public DateTimePicker() {
        calendar = new CalendarPanel() {
            @Override
            protected void onDaySelected(Date[] selectedDates) {
                if (selectedDates != null && selectedDates.length > 0 && selectedDates[0] != null) {
                    result = new DateTime(selectedDates[0].getTime());
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            }
        };
        dialogDesc = new DialogDescriptor(calendar, "");
        dialogDesc.setOptions(new Object[]{});
        calendar.requestFocus();
    }

    public synchronized DateTime getDateTimeFromUser(DateTime initialDateTime, Component caller) {
        result = null;
        dialog = DialogDisplayer.getDefault().createDialog(dialogDesc);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(caller);
        calendar.setSelectedDate(initialDateTime != null ? initialDateTime.toDate() : new Date());
        dialog.setVisible(true);
        return result;
    }

    public DateTime getDateTimeFromUser() {
        return getDateTimeFromUser(null, null);
    }
}
