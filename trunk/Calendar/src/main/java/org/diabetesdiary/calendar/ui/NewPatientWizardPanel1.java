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

package org.diabetesdiary.calendar.ui;

import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.diabetesdiary.datamodel.api.DbLookUp;
import org.diabetesdiary.datamodel.pojo.Patient;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

public class NewPatientWizardPanel1 implements WizardDescriptor.Panel {
    
    private Patient patient;
    
    private WizardDescriptor settings;
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private Component component;
    
    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    public Component getComponent() {
        if (component == null) {
            component = new NewPatientVisualPanel1(this);
        }
        return component;
    }
    
    private void setError(String error){
        if(settings != null){
            settings.putProperty("WizardPanel_errorMessage",error);
        }
    }
    
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx(SampleWizardPanel1.class);
    }
    
    public Patient getPatient(){
        return patient;
    }
    
    public boolean isValid() {
        NewPatientVisualPanel1 comp = (NewPatientVisualPanel1) getComponent();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                NbBundle.getMessage(NewPatientWizardPanel1.class,"NewRecord_DatePattern"));
        patient.setIdPatient(comp.getLogin());
        patient.setEmail(comp.getEmail());
        patient.setMale(comp.isMale());
        patient.setName(comp.getPatientName());
        patient.setSurname(comp.getSurname());
        patient.setPumpUsage(comp.isPumpUsage());
        
        if(patient.getName() == null || patient.getName().length() < 1 || patient.getSurname() == null || patient.getSurname().length() < 1){
            setError(NbBundle.getMessage(NewPatientWizardPanel1.class,"Musite_vyplnit_jmeno_i_prijmeni."));
            return false;
        }
        try {
            patient.setBorn(dateFormat.parse(comp.getDateBorn()));
        } catch (ParseException ex) {
            setError(NbBundle.getMessage(NewPatientWizardPanel1.class, "NewRecord_DatePatternMessage",
                    NbBundle.getMessage(NewPatientWizardPanel1.class,"NewRecord_DatePattern"),
                    NbBundle.getMessage(NewPatientWizardPanel1.class,"NewRecord_TimePattern")));
            return false;
        }
        if(patient.getIdPatient() == null || patient.getIdPatient().length() < 1){
            setError(NbBundle.getMessage(NewPatientWizardPanel1.class,"Musite_vyplnit_login."));
            return false;
        }
        if(DbLookUp.getDiary().getPatient(patient.getIdPatient()) != null){
            setError(NbBundle.getMessage(NewPatientWizardPanel1.class,"Pacient_s_timto_loginem_ji_existuje._Ulozenim_zmenite_jeho_udaje."));
            return true;
        }        
        
        setError(null);
        return true;
    }
    
    
    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1);
    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }
    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }
    protected final void fireChangeEvent() {
        Iterator<ChangeListener> it;
        synchronized (listeners) {
            it = new HashSet<ChangeListener>(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (it.hasNext()) {
            it.next().stateChanged(ev);
        }
    }
    
    
// You can use a settings object to keep track of state. Normally the
// settings object will be the WizardDescriptor, so you can use
// WizardDescriptor.getProperty & putProperty to store information entered
// by the user.
    public void readSettings(Object settings) {}
    public void storeSettings(Object settings) {}
    
    public void setDescriptor(WizardDescriptor wizardDescriptor) {
        this.settings = wizardDescriptor;
    }
    
    public void setPatient(Patient patient) {
        NewPatientVisualPanel1 comp = (NewPatientVisualPanel1) getComponent();
        this.patient = patient;
        
        if(patient.getIdPatient() == null){
            comp.setDateBorn(null);
            comp.setEmail(null);
            comp.setLogin(null);
            comp.setPatientName(null);
            comp.setSex(true);
            comp.setSurname(null);
            comp.setPumpUsage(false);            
        }else{
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    NbBundle.getMessage(NewPatientWizardPanel1.class,"NewRecord_DatePattern"));            
            comp.setDateBorn(dateFormat.format(patient.getBorn()));
            comp.setEmail(patient.getEmail());
            comp.setLogin(patient.getIdPatient());
            comp.setPatientName(patient.getName());
            comp.setSex(patient.isMale());
            comp.setSurname(patient.getSurname());
            comp.setPumpUsage(patient.isPumpUsage());
        }
        comp.setPatientEdit(patient.getIdPatient() != null);
    }
    
}

