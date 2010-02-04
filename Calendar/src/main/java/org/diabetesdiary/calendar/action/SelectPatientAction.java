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
package org.diabetesdiary.calendar.action;

import org.diabetesdiary.calendar.ui.CalendarTopComponent;
import org.diabetesdiary.calendar.ui.SelectPatientPanel;
import org.diabetesdiary.diary.api.DiaryRepository;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.utils.MyLookup;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class SelectPatientAction extends CallableSystemAction {

    @Override
    public void performAction() {
        if (MyLookup.getDiaryRepo().getPatients() == null || MyLookup.getDiaryRepo().getPatients().size() == 0) {
            DialogDisplayer.getDefault().notify(
                    new NotifyDescriptor.Message(NbBundle.getMessage(SelectPatientAction.class, "Dosud_nebyl_vytvoren_zadny_pacient._Pro_praci_musite_nejakeho_vytvorit."), NotifyDescriptor.WARNING_MESSAGE));
            return;
        }

        SelectPatientPanel mp = new SelectPatientPanel(); // create new MyPanel
        DialogDescriptor dd = new DialogDescriptor(mp, NbBundle.getMessage(SelectPatientAction.class, "Vyber_pacienta")); //create new DialogDescriptor
        mp.requestFocus(); // set focus to component which was specified in MyPanel's requestFocus() method
        DialogDisplayer.getDefault().createDialog(dd).setVisible(true);
        if (dd.getValue() == DialogDescriptor.OK_OPTION) {
            Patient pat = mp.getPatient();
            if (pat != null) {
                MyLookup.setCurrentPatient(pat);
                CalendarTopComponent.getDefault().getModel().fillData();
                CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
            }
        } else {
            //cancel button was pressed
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(SelectPatientAction.class, "CTL_SelectPatientAction");
    }

    @Override
    protected String iconResource() {
        return "org/diabetesdiary/calendar/resources/opendiary.png";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
