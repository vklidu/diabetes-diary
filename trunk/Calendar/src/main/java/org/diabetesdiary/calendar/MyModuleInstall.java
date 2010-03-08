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
package org.diabetesdiary.calendar;

import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.calendar.ui.CalendarTopComponent;
import org.diabetesdiary.diary.utils.MyLookup;
import org.openide.modules.ModuleInstall;

/**
 *
 * @author Jiri Majer
 */
public class MyModuleInstall extends ModuleInstall {

    @Override
    public synchronized void restored() {
        super.restored();
        Long idPatient = CalendarSettings.getSettings().getValueAsLong(CalendarSettings.CURRENT_PATIENT_ID);
        if (idPatient != null) {
            MyLookup.setCurrentPatient(MyLookup.getDiaryRepo().getPatient(idPatient));
            CalendarTopComponent.getDefault().firePatientChanged();
        }
    }

    @Override
    public void close() {
        if (MyLookup.getCurrentPatient() != null) {
            CalendarSettings.getSettings().setValue(CalendarSettings.CURRENT_PATIENT_ID, MyLookup.getCurrentPatient().getId());
            CalendarSettings.getSettings().store();
        }
    }
}
