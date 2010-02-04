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

import java.awt.Component;
import java.awt.Dialog;
import javax.swing.JComponent;
import org.diabetesdiary.calendar.ui.CalendarTopComponent;
import org.diabetesdiary.calendar.ui.NewPatientWizard;
import org.diabetesdiary.calendar.ui.NewPatientWizardPanel1;
import org.diabetesdiary.calendar.ui.NewPatientWizardPanel2;
import org.diabetesdiary.diary.utils.MyLookup;
import org.diabetesdiary.diary.api.DiaryService;
import org.diabetesdiary.diary.domain.Patient;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class NewPatientaction extends CallableSystemAction {
    
    
    private WizardDescriptor.Panel[] panels;
    private WizardDescriptor wizardDescriptor;
    
    @Override
    public void performAction(){
        performAction(null);
    }
    
    public void performAction(Patient patient) {
        wizardDescriptor = new NewPatientWizard(getPanels());
        NewPatientWizardPanel1 panel = (NewPatientWizardPanel1) getPanels()[0];
        NewPatientWizardPanel2 panel2 = (NewPatientWizardPanel2) getPanels()[1];
        panel.setDescriptor(wizardDescriptor);
        panel2.setDescriptor(wizardDescriptor);
        panel.setPatient(patient);
        panel2.setPatient(patient);
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            if (patient == null) {
                DiaryService diary = MyLookup.getDiaryService();
                patient = diary.newPatient(panel.getName(), panel.getSurname(), panel.isMale(), panel.getBorn(), null, panel.isPumpUsage(), panel.getEmail(), null, null,
                        panel2.getBasalInsulin(), panel2.getBolusInsulin(), panel2.getPerSensitivity(), panel2.getHepSensitivity(), panel2.getFiltrationRate(), panel2.getRenalThreshold());
                MyLookup.setCurrentPatient(patient);
            }
            else {
                patient.update(panel.getName(), panel.getSurname(), panel.isMale(), panel.getBorn(), null, panel.isPumpUsage(), panel.getEmail(), null, null,
                        panel2.getBasalInsulin(), panel2.getBolusInsulin(), panel2.getPerSensitivity(), panel2.getHepSensitivity(), panel2.getFiltrationRate(), panel2.getRenalThreshold());
                MyLookup.setCurrentPatient(MyLookup.getDiaryRepo().getPatient(patient.getId()));
            }
            
            CalendarTopComponent.getDefault().getModel().fillData();
            CalendarTopComponent.getDefault().getModel().fireTableDataChanged();            
        }
    }
    
    
    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            panels = new WizardDescriptor.Panel[] {
                new NewPatientWizardPanel1(),
                new NewPatientWizardPanel2()
            };
            String[] steps = new String[panels.length];
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                // Default step name to component name of panel. Mainly useful
                // for getting the name of the target chooser to appear in the
                // list of steps.
                steps[i] = c.getName();
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    // Sets step number of a component
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                    // Sets steps names for a panel
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    // Turn on subtitle creation on each step
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                    // Show steps on the left side with the image on the background
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                    // Turn on numbering of all steps
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
                }
            }
        }
        return panels;
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(NewPatientaction.class, "CTL_NewPatientaction");
    }
    
    @Override
    protected String iconResource() {
        return "org/diabetesdiary/calendar/resources/newpatient.png";
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
