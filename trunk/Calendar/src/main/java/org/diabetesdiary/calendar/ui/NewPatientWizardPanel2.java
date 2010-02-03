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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.diabetesdiary.datamodel.pojo.PatientDO;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

public class NewPatientWizardPanel2 implements WizardDescriptor.Panel {
    
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private Component component;
    
    private WizardDescriptor settings;
    
    public void setDescriptor(WizardDescriptor wizardDescriptor) {
        this.settings = wizardDescriptor;
    }
    
    
    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    public Component getComponent() {
        if (component == null) {
            component = new NewPatientVisualPanel2(this);
        }
        return component;
    }
    
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx(SampleWizardPanel1.class);
    }
    
    private void setError(String error){
        if(settings != null){
            settings.putProperty("WizardPanel_errorMessage",error);
        }
    }
    
    private PatientDO patient;
    
    public boolean isValid() {
        NewPatientVisualPanel2 comp = (NewPatientVisualPanel2) getComponent();
        patient.setHepSensitivity(comp.getHepSensitivity());
        patient.setPerSensitivity(comp.getPerSensitivity());
        patient.setRenalThreshold(comp.getRenalThreshold());
        patient.setFiltrationRate(comp.getFiltrationRate());
        patient.setBasalInsulin(comp.getBasalInsulin());
        patient.setBolusInsulin(comp.getBolusInsulin());
        
        if(patient.getBasalInsulin() == null){
            setError(NbBundle.getMessage(NewPatientWizardPanel2.class,"Musite_vybrat_pouzivany_depotni_inzulin"));
            return false;
        }
        
        if(patient.getBolusInsulin() == null){
            setError(NbBundle.getMessage(NewPatientWizardPanel2.class,"Musite_vybrat_pouzivany_rychly_inzulin"));
            return false;
        }        
        
        if(patient.getHepSensitivity() < 0 || patient.getHepSensitivity() > 1 || patient.getPerSensitivity() < 0 || patient.getPerSensitivity() > 1){
            setError(NbBundle.getMessage(NewPatientWizardPanel2.class,"Citlivost_na_inzulin_zadavejte_v_intervalu_0-1"));
            return false;
        }
        if(patient.getFiltrationRate() > 130 || patient.getFiltrationRate() < 70){
            setError(NbBundle.getMessage(NewPatientWizardPanel2.class,"Rychlost_vylucovane_glukozy_do_moci_udavejte_v_rozsahu_70-130_mmol/h"));
            return false;
        }
        if(patient.getRenalThreshold() > 12 || patient.getRenalThreshold() < 7){
            setError(NbBundle.getMessage(NewPatientWizardPanel2.class,"Hladina_glykemie,_pri_ktere_se_vylucuje_glukoza_do_moci_7-11_mmol/l"));
            return false;
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
    
    public void setPatient(PatientDO patient) {
        NewPatientVisualPanel2 comp = (NewPatientVisualPanel2) getComponent();
        this.patient = patient;
        if(patient.getIdPatient() != null){
            comp.setHepSensitivity(patient.getHepSensitivity());
            comp.setPerSensitivity(patient.getPerSensitivity());
            comp.setRenalThreshold(patient.getRenalThreshold());
            comp.setFiltrationRate(patient.getFiltrationRate());
            comp.setBasalInsulin(patient.getBasalInsulin());
            comp.setBolusInsulin(patient.getBolusInsulin());
        }else{
            comp.setHepSensitivity(0.5);
            comp.setPerSensitivity(0.5);
            comp.setRenalThreshold(9.0);
            comp.setFiltrationRate(100.0);
        }
    }
    
    public PatientDO getPatient() {
        return patient;
    }
    
}

