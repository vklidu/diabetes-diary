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

import org.diabetesdiary.diary.utils.MyLookup;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class EditPatient extends CallableSystemAction {
    
    public void performAction() {
        if(MyLookup.getDiaryRepo().getCurrentPatient() != null){
            NewPatientaction action = (NewPatientaction) CallableSystemAction.findObject(NewPatientaction.class);
            if(action!=null){
                action.performAction(MyLookup.getDiaryRepo().getCurrentPatient());
            }
        }else{
            DialogDisplayer.getDefault().notify(
                    new NotifyDescriptor.Message(NbBundle.getMessage(EditPatient.class,"Neni_vybran_zadny_pacient."), NotifyDescriptor.INFORMATION_MESSAGE));
        }
    }
    
    public String getName() {
        return NbBundle.getMessage(EditPatient.class, "CTL_EditPatient");
    }
    
    protected String iconResource() {
        return "org/diabetesdiary/calendar/resources/editpatient.png";
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    protected boolean asynchronous() {
        return false;
    }
    
}
