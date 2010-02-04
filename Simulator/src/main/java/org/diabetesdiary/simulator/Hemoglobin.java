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

import java.util.Iterator;
import java.util.List;
import org.apache.commons.math.stat.Frequency;
import org.diabetesdiary.diary.domain.RecordInvest;
import org.diabetesdiary.diary.domain.WKInvest;

/**
 *
 * @author Jiri Majer
 */
public class Hemoglobin {
    
    private double lowHemo;
    private double heighHemo;
    private double lowGly;
    private double heighGly;
    
    private long valueCount;
    private double average;
    private double rozptyl;
    
    
    /** Creates a new instance of Hemoglobin */
    public Hemoglobin(List<RecordInvest> records) {        
        Frequency f = new Frequency();
        for(RecordInvest rec : records){
            if(rec.getInvest().anyType(WKInvest.GLYCEMIE)){
                f.addValue(rec.getValue());
            }
        }
        
        average = 0;
        Iterator valueIter = f.valuesIterator();
        while(valueIter.hasNext()){
            double value = (Double) valueIter.next();
            average += value*f.getCount(value)/f.getSumFreq();
        }
        this.valueCount = f.getSumFreq();
        double s2 = 0;
        valueIter = f.valuesIterator();
        while(valueIter.hasNext()){
            double value = (Double) valueIter.next();
            for(int i=0; i<f.getCount(value);i++){
                s2 += Math.pow(value - average,2);
            }
        }
        s2 = s2/(f.getSumFreq()-1);
        this.rozptyl = s2;
        double pom = Math.sqrt(s2)/Math.sqrt(f.getSumFreq());
        this.lowGly = (average - 1.95996 * pom);
        this.heighGly = (average + 1.95996 * pom);        
        
        this.lowHemo = plasmaGlucose2HbA1c(lowGly);
        this.heighHemo = plasmaGlucose2HbA1c(heighGly);
    }
    
    /**
     * Diabetes Care Volume 25, Number 2, February 2002
     */
    public double plasmaGlucose2HbA1c(double plasmaGlucose){
        return (plasmaGlucose + 4.29) / 1.98;        
    }
    
    public double getLowHemo(){
        return lowHemo;
    }
    
    
    public double getHeighHemo(){
        return heighHemo;
    }
    
    public long getValueCount() {
        return valueCount;
    }
    
    public double getAverage() {
        return average;
    }
    
    public double getRozptyl() {
        return rozptyl;
    }
    
     /**
     * Preconditions: x2 > x1; f(x2) = y2; f(x1) = y1
     */
    private static double getLinearInterpolation(double x1, double x2, double y1, double y2, double realx){
        if(y2 > y1){
            double dx = x2 - x1;
            double dy = y2 - y1;
            double result = (realx - x1) * dy / dx + y1;
            return result;
        }else{
            double dx = x2 - x1;
            double dy = y1 - y2;
            double result = (x2 - realx) * dy / dx + y2;
            return result;
        }
    }

    public double getLowGly() {
        return lowGly;
    }

    public double getHeighGly() {
        return heighGly;
    }

    
}


