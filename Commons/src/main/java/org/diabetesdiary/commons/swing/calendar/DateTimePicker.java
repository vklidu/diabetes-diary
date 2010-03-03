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
import java.awt.Dialog;
import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
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
    private DateTime initiDateTime;

    public DateTimePicker() {
        calendar = new CalendarPanel(new LocalDate(), Locale.getDefault()) {

            @Override
            public void onDayClicked(LocalDate date) {
                result = initiDateTime != null ? date.toDateTime(initiDateTime.toLocalTime()) : date.toDateTimeAtCurrentTime();
                dialog.setVisible(false);
                dialog.dispose();
            }
        };
        dialogDesc = new DialogDescriptor(calendar, "");
        dialogDesc.setOptions(new Object[]{});
        calendar.requestFocus();
    }

    public synchronized DateTime getDateTimeFromUser(DateTime initialDateTime, Component caller) {
        result = null;
        this.initiDateTime = initialDateTime;
        dialog = DialogDisplayer.getDefault().createDialog(dialogDesc);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(caller);
        calendar.setLocalDate(initialDateTime != null ? initialDateTime.toLocalDate() : new LocalDate());
        dialog.setVisible(true);
        return result;
    }

    public DateTime getDateTimeFromUser() {
        return getDateTimeFromUser(null, null);
    }
}
