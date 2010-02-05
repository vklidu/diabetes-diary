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
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.diabetesdiary.diary.domain.Patient;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

public class NewPatientWizardPanel1 implements WizardDescriptor.Panel {
    
    private String name;
    private String surname;
    private boolean male;
    private LocalDate born;
    private String email;
    private boolean pumpUsage;

    
    private WizardDescriptor settings;
    private Component component;
    
    @Override
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
    
    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    public boolean isValid() {
        NewPatientVisualPanel1 comp = (NewPatientVisualPanel1) getComponent();

        DateTimeFormatter dateFormat = DateTimeFormat.forPattern(NbBundle.getMessage(NewPatientWizardPanel1.class,"NewRecord_DatePattern"));
        email = comp.getEmail();
        male = comp.isMale();
        name = comp.getPatientName();
        surname = comp.getSurname();
        pumpUsage = comp.isPumpUsage();
        
        if(getName() == null || getName().length() < 1 || getSurname() == null || getSurname().length() < 1){
            setError(NbBundle.getMessage(NewPatientWizardPanel1.class,"Musite_vyplnit_jmeno_i_prijmeni."));
            return false;
        }
        try {
            born = dateFormat.parseDateTime(comp.getDateBorn()).toLocalDate();
        } catch (IllegalArgumentException ex) {
            setError(NbBundle.getMessage(NewPatientWizardPanel1.class, "NewRecord_DatePatternMessage",
                    NbBundle.getMessage(NewPatientWizardPanel1.class,"NewRecord_DatePattern"),
                    NbBundle.getMessage(NewPatientWizardPanel1.class,"NewRecord_TimePattern")));
            return false;
        }
        
        setError(null);
        return true;
    }
    
    
    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1);
    
    @Override
    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }
    
    @Override
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
    
    @Override
    public void readSettings(Object settings) {}
    @Override
    public void storeSettings(Object settings) {}
    
    public void setDescriptor(WizardDescriptor wizardDescriptor) {
        this.settings = wizardDescriptor;
    }
    
    public void setPatient(Patient patient) {
        NewPatientVisualPanel1 comp = (NewPatientVisualPanel1) getComponent();      
        
        if(patient == null){
            comp.setDateBorn(null);
            comp.setEmail(null);
            comp.setPatientName(null);
            comp.setSex(true);
            comp.setSurname(null);
            comp.setPumpUsage(false);            
        }else{
            String pattern = NbBundle.getMessage(NewPatientWizardPanel1.class,"NewRecord_DatePattern");
            comp.setDateBorn(patient.getBorn().toString(pattern));
            comp.setEmail(patient.getEmail());
            comp.setPatientName(patient.getName());
            comp.setSex(patient.isMale());
            comp.setSurname(patient.getSurname());
            comp.setPumpUsage(patient.isPumpUsage());
        }
        comp.setPatientEdit(patient != null);
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public boolean isMale() {
        return male;
    }

    public LocalDate getBorn() {
        return born;
    }

    public String getEmail() {
        return email;
    }

    public boolean isPumpUsage() {
        return pumpUsage;
    }
    
}

