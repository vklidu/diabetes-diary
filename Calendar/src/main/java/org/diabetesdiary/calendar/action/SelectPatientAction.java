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
import org.diabetesdiary.datamodel.api.DiaryRepository;
import org.diabetesdiary.datamodel.pojo.PatientDO;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.ErrorManager;
import org.openide.NotifyDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class SelectPatientAction extends CallableSystemAction {
    
    public void performAction() {
        Lookup lookup = Lookup.getDefault();
        DiaryRepository diary = (DiaryRepository)lookup.lookup(DiaryRepository.class);
        if (diary == null) {
            // this will show up as a flashing round button in the bottom-right corner
            ErrorManager.getDefault().notify(
                    new IllegalStateException(NbBundle.getMessage(SelectPatientAction.class,"Cannot_locate_Diary_implementation")));
        }else{
            if(diary.getPatients() == null || diary.getPatients().size() == 0){
                DialogDisplayer.getDefault().notify(
                        new NotifyDescriptor.Message(NbBundle.getMessage(SelectPatientAction.class,"Dosud_nebyl_vytvoren_zadny_pacient._Pro_praci_musite_nejakeho_vytvorit."), NotifyDescriptor.WARNING_MESSAGE));
                return;
            }
        }
        
        SelectPatientPanel mp = new SelectPatientPanel(); // create new MyPanel
        DialogDescriptor dd = new DialogDescriptor(mp, NbBundle.getMessage(SelectPatientAction.class,"Vyber_pacienta")); //create new DialogDescriptor
        mp.requestFocus(); // set focus to component which was specified in MyPanel's requestFocus() method
        DialogDisplayer.getDefault().createDialog(dd).setVisible(true);
        if (dd.getValue() == DialogDescriptor.OK_OPTION) {
            PatientDO pat = mp.getPatient();
            if(pat != null){
                diary.setCurrentPatient(pat);
                CalendarTopComponent.getDefault().getModel().fillData();
                CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
            }
        } else{
            //cancel button was pressed
        }
    }
    
    
    public String getName() {
        return NbBundle.getMessage(SelectPatientAction.class, "CTL_SelectPatientAction");
    }
    
    protected String iconResource() {
        return "org/diabetesdiary/calendar/resources/opendiary.png";
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    protected boolean asynchronous() {
        return false;
    }
    
}
