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
public class Const {
    
    /**
     * Insulin elimination rate constant
     * ke = 5.4 /hr
     */
    public static final double KE = 5.4;
    
    /**
     * Parameter for insulin pharmacodynamics
     * k1 = 0.025 /hr
     */
    public static final double K1 = 0.025;

    /**
     * Parameter for insulin pharmacodynamics
     * k2 = 1.25 /hr
     */
    public static final double K2 = 1.25;
    
    /**
     * Reference basal level of insulin
     * Ibasal = 10 mU/l
     */
    public static final double IBASAL = 10;
    
    /**
     * Michaelis constant for enzyme mediated glucose uptake
     * Km = 10 mmol/l
     */
    public static final double KM = 10;
    
    /**
     * Insulin-independent glucose utilisation per kg body weight
     * Gl = 0.54 mmol/hr/kg
     */
    public static final double GL = 0.54;
    
    /**
     * Reference value for glucose utilisation
     * GX = 5.3 mmol/l
     */
    public static final double GX = 5.3;
    
    /**
     * Slope of periphereal glucose absorption from the gut
     * c = 0.015 mmol/hr/kg/mU*l
     */
    public static final double C = 0.015;
    
    /**
     * Rate constant for glucose absorption from the gut
     * kgabs = 1 /hr
     */
    public static final double KGABS = 1;
    
    /**
     * Maximal rate of gastric emptying
     * Vmaxge = 120 mmol/hr
     */
    public static final double VMAXGE = 120;
    
    /**
     * Volume of distribution for insulin per kg body weight
     * Vl = 0.142 l/kg
     */
    public static final double VI = 0.142;
    
    /**
     * Volume of distribution for glucose per kg body weight
     * Vg = 0.22 l/kg
     */
    public static final double VG = 0.22;
    
    
    /**
     * 1g of glucose = 1/180 * 1000 mmol
     */
    public static final double GLUCOSEMMOL = 1000/180;
    
    
    public static final double DEFAULT_RENAL_THRESHOLD = 9.0f;
    
    /**
     * l/h
     */
    public static final double DEFAULT_GLOMERULAR_FILTRATION = 6;
    
    /** 
     * No instances
     */
    private Const() {
    }
    
}
