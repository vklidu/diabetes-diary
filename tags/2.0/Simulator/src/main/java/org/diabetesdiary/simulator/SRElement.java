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



package org.diabetesdiary.simulator;

/**
 *
 * @author Jiri Majer
 */
public class SRElement {
    
    private double nhgb;
    private double glucose;
    private double plasmaInsulin;
    private double activeInsulin;
    private double eqInsulin;
    private double glucoseUtilisation;
    private double renalGlucoseExcretion;
    private double glucoseInGut;
    
    /**
     * Creates a new instance of SRElement
     */
    public SRElement() {
    }

    public double getNhgb() {
        return nhgb;
    }

    public void setNhgb(double nhgb) {
        this.nhgb = nhgb;
    }

    public double getGlucose() {
        return glucose;
    }

    public void setGlucose(double glucose) {
        this.glucose = glucose;
    }

    public double getPlasmaInsulin() {
        return plasmaInsulin;
    }

    public void setPlasmaInsulin(double plasmaInsulin) {
        this.plasmaInsulin = plasmaInsulin;
    }

    public double getActiveInsulin() {
        return activeInsulin;
    }

    public void setActiveInsulin(double activeInsulin) {
        this.activeInsulin = activeInsulin;
    }

    public double getEqInsulin() {
        return eqInsulin;
    }

    public void setEqInsulin(double eqInsulin) {
        this.eqInsulin = eqInsulin;
    }

    public double getGlucoseUtilisation() {
        return glucoseUtilisation;
    }

    public void setGlucoseUtilisation(double glucoseUtilisation) {
        this.glucoseUtilisation = glucoseUtilisation;
    }

    public double getRenalGlucoseExcretion() {
        return renalGlucoseExcretion;
    }

    public void setRenalGlucoseExcretion(double renalGlucoseExcretion) {
        this.renalGlucoseExcretion = renalGlucoseExcretion;
    }

    public double getGlucoseInGut() {
        return glucoseInGut;
    }

    public void setGlucoseInGut(double glucoseInGut) {
        this.glucoseInGut = glucoseInGut;
    }
    
}
