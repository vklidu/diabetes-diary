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
import org.diabetesdiary.diary.domain.Insulin;
import org.diabetesdiary.diary.domain.Patient;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

public class NewPatientWizardPanel2 implements WizardDescriptor.Panel {

    private Insulin basalInsulin;
    private Insulin bolusInsulin;
    private Double perSensitivity;
    private Double hepSensitivity;
    private Double filtrationRate;
    private Double renalThreshold;
    private Component component;
    private WizardDescriptor settings;

    public void setDescriptor(WizardDescriptor wizardDescriptor) {
        this.settings = wizardDescriptor;
    }

    @Override
    public Component getComponent() {
        if (component == null) {
            component = new NewPatientVisualPanel2(this);
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    private void setError(String error) {
        if (settings != null) {
            settings.putProperty("WizardPanel_errorMessage", error);
        }
    }

    @Override
    public boolean isValid() {
        NewPatientVisualPanel2 comp = (NewPatientVisualPanel2) getComponent();
        hepSensitivity = comp.getHepSensitivity();
        perSensitivity = comp.getPerSensitivity();
        renalThreshold = comp.getRenalThreshold();
        filtrationRate = comp.getFiltrationRate();
        basalInsulin = comp.getBasalInsulin();
        bolusInsulin = comp.getBolusInsulin();

        if (getBasalInsulin() == null) {
            setError(NbBundle.getMessage(NewPatientWizardPanel2.class, "Musite_vybrat_pouzivany_depotni_inzulin"));
            return false;
        }
        if (getBolusInsulin() == null) {
            setError(NbBundle.getMessage(NewPatientWizardPanel2.class, "Musite_vybrat_pouzivany_rychly_inzulin"));
            return false;
        }
        if (getHepSensitivity() < 0 || getHepSensitivity() > 1 || getPerSensitivity() < 0 || getPerSensitivity() > 1) {
            setError(NbBundle.getMessage(NewPatientWizardPanel2.class, "Citlivost_na_inzulin_zadavejte_v_intervalu_0-1"));
            return false;
        }
        if (getFiltrationRate() > 130 || getFiltrationRate() < 70) {
            setError(NbBundle.getMessage(NewPatientWizardPanel2.class, "Rychlost_vylucovane_glukozy_do_moci_udavejte_v_rozsahu_70-130_mmol/h"));
            return false;
        }
        if (getRenalThreshold() > 12 || getRenalThreshold() < 7) {
            setError(NbBundle.getMessage(NewPatientWizardPanel2.class, "Hladina_glykemie,_pri_ktere_se_vylucuje_glukoza_do_moci_7-11_mmol/l"));
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
    public void readSettings(Object settings) {
    }

    @Override
    public void storeSettings(Object settings) {
    }

    public void setPatient(Patient patient) {
        NewPatientVisualPanel2 comp = (NewPatientVisualPanel2) getComponent();
        if (patient != null) {
            comp.setHepSensitivity(patient.getHepSensitivity());
            comp.setPerSensitivity(patient.getPerSensitivity());
            comp.setRenalThreshold(patient.getRenalThreshold());
            comp.setFiltrationRate(patient.getFiltrationRate());
            comp.setBasalInsulin(patient.getBasalInsulin());
            comp.setBolusInsulin(patient.getBolusInsulin());
        } else {
            comp.setHepSensitivity(0.5);
            comp.setPerSensitivity(0.5);
            comp.setRenalThreshold(9.0);
            comp.setFiltrationRate(100.0);
        }
    }

    public Insulin getBasalInsulin() {
        return basalInsulin;
    }

    public Insulin getBolusInsulin() {
        return bolusInsulin;
    }

    public Double getPerSensitivity() {
        return perSensitivity;
    }

    public Double getHepSensitivity() {
        return hepSensitivity;
    }

    public Double getFiltrationRate() {
        return filtrationRate;
    }

    public Double getRenalThreshold() {
        return renalThreshold;
    }
}

