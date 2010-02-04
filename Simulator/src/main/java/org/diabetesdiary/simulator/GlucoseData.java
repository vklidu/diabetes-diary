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

import java.util.Date;
import static org.diabetesdiary.simulator.Const.VMAXGE;
import static org.diabetesdiary.simulator.Const.KGABS;
import static org.diabetesdiary.simulator.Const.GLUCOSEMMOL;

/**
 *
 * @author Jiri Majer
 */
public class GlucoseData implements Comparable<GlucoseData>{
    
    private Date timeOfIntake;
    private double grams;
    private double mmol;
    private double tmaxge;
    private double time;
    private double lastGgut;
    private double lastGlucose;    
    
    
    /** Creates a new instance of GlucoseData */
    public GlucoseData(double grams, Date timeOfIntake) {
        this.timeOfIntake = timeOfIntake;
        this.grams = grams;        
        mmol = GLUCOSEMMOL * this.grams;
        tmaxge = (mmol - (VMAXGE*((getTascge()+getTdescge())/2)))/VMAXGE;
        time = 0;
        lastGgut = 0;
        lastGlucose = 0;        
    }
    
    
    public double getGempt(double t){
        if(t < getTascge()){
            return VMAXGE*t/getTascge();
        }else if(t <= getTascge() + getTmaxge()){
            return VMAXGE;
        }else if(t < getTmaxge() + getTascge() + getTdescge()){
            return VMAXGE - ((VMAXGE/getTdescge())*(t-getTascge()-getTmaxge()));
        }else{
            return 0;
        }
    }
    
        
    public int compareTo(GlucoseData o) {
        return this.timeOfIntake.compareTo(o.getTimeOfIntake());
    }
    
    public Date getTimeOfIntake() {
        return timeOfIntake;
    }
    
    public double getGrams() {
        return grams;
    }
    
    public double getTascge(){
        if(grams < 10){
            return 2*mmol/VMAXGE;
        }else{
            return 0.5;
        }
    }
    
    public double getTdescge(){
        return getTascge();
    }
    
    private double getTmaxge() {
        return tmaxge;
    }

    public double getTimeAfterIntake() {
        return time;
    }

    public void setTimeAfterIntake(double time) {
        this.time = time;
    }

    double getLastGgut() {
        return lastGgut;
    }

    public void setLastGgut(double lastGgut) {
        this.lastGgut = lastGgut;
    }

    public double getLastGlucose() {
        return lastGlucose;
    }

    public void setLastGlucose(double lastGlucose) {
        this.lastGlucose = lastGlucose;
    }
    
}
