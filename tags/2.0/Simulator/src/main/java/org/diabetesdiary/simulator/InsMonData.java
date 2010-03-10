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
import org.diabetesdiary.diary.api.InsulinParameters;

/**
 *
 * @author Jiri Majer
 */
public class InsMonData implements Comparable<InsMonData>{
    
    private InsulinParameters para;
    private double doseUnit;
    private Date time;
    private double t50;
    private double lastValueIabs;
    private double globalTimeStart = -1;
    //in hours
    private double timeAfterInjection;
    
    
    /** Creates a new instance of InsMonData */
    public InsMonData(InsulinParameters insulin, double doseUnit, Date timeOfInjection) {
        this.para = insulin;
        this.doseUnit = doseUnit;
        this.time = timeOfInjection;
        t50 = para.getParameterA()*this.doseUnit + para.getParameterB();
        lastValueIabs = 0;
        timeAfterInjection = 0;        
    }

    public InsulinParameters getPara() {
        return para;
    }

    public double getDoseUnit() {
        return doseUnit;
    }

    public Date getTime() {
        return time;
    }    
    
    @Override
    public int compareTo(InsMonData o) {
        return time.compareTo(o.getTime());
    }

    public double getT50() {
        return t50;
    }

    public double getTimeAfterInjection() {
        return timeAfterInjection;
    }

    public void setTimeAfterInjection(double timeAfterInjection) {
        this.timeAfterInjection = timeAfterInjection;
    }

    public double getLastValueIabs() {
        return lastValueIabs;
    }

    public void setLastValueIabs(double lastValueIabs) {
        this.lastValueIabs = lastValueIabs;
    }

    public double getGlobalTimeStart() {
        return globalTimeStart;
    }

    public void setGlobalTimeStart(double globalTimeStart) {
        this.globalTimeStart = globalTimeStart;
    }
    
    
    
}
