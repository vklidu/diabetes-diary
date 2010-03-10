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

import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public enum SREnum {
    NHGB,
    GLUCOSE,
    PLASMA_INSULIN,
    ACTIVE_INSULIN,
    EQ_INSULIN,
    GLUCOSE_UTIL,
    RENAL_EXCRETION,
    GLUCOSE_GUT;
    
    public static SREnum[] getGraphEnums(){
        return new SREnum[]{GLUCOSE,PLASMA_INSULIN,GLUCOSE_GUT,RENAL_EXCRETION,GLUCOSE_UTIL,NHGB};
    }
    
    @Override
    public String toString(){
        switch(this){
            case NHGB: return NbBundle.getMessage(SREnum.class,"Net_hepatic_glucose_balance");
            case GLUCOSE: return NbBundle.getMessage(SREnum.class,"Blood_glucose");
            case PLASMA_INSULIN: return NbBundle.getMessage(SREnum.class,"Plasma_insulin_level");
            case GLUCOSE_UTIL: return NbBundle.getMessage(SREnum.class,"Peripheral_glucose_utilization");
            case RENAL_EXCRETION: return NbBundle.getMessage(SREnum.class,"Renal_excretion");
            case GLUCOSE_GUT: return NbBundle.getMessage(SREnum.class,"Glucose_absorption_rate");
            case ACTIVE_INSULIN: return NbBundle.getMessage(SREnum.class,"Active_insulin_level");
            case EQ_INSULIN: return NbBundle.getMessage(SREnum.class,"Insulin_equilibrium");
        }
        return "";
    }
    
    public String getUnit(){
        switch(this){
            case NHGB: return "mmol/h";
            case GLUCOSE: return "mmol/l";
            case PLASMA_INSULIN: return "mU/l";
            case GLUCOSE_UTIL: return "mmol/h";
            case RENAL_EXCRETION: return "mmol/h";
            case GLUCOSE_GUT: return "mmol/h";
            case ACTIVE_INSULIN: return "mU/l";
            case EQ_INSULIN: return "mU/l";
        }
        return "";
    }
}
