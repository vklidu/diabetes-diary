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



package org.diabetesdiary.model.equation;

import java.util.Date;
import java.util.List;
import org.diabetesdiary.model.Const;
import org.diabetesdiary.model.InsMonData;

/**
 *
 * @author Jiri Majer
 */
public class InsulinFunction implements Function{
    
    private double weight;
    private List<InsMonData> insulins;
    private Date curDate;
    
    /** Creates a new instance of InsulinFunction */
    public InsulinFunction(double weight, List<InsMonData> insulins, Date curDate) {
        this.weight = weight;
        this.insulins = insulins;
        this.curDate = curDate;
    }
    
    public double count(double t, double ... x) {
        return countInsulin(t,weight,x[0],curDate,insulins);
    }
    
    private static double countIabs(double t, double s, double t50, double D){
        if(t == 0)return 0;
        double numerator = s*Math.pow(t,s-1)*Math.pow(t50,s)*D;
        double denominator = Math.pow(Math.pow(t50,s)+Math.pow(t,s),2);
        return numerator/denominator;
    }
    
    public static double countInsulin(double t, double weight, double lastInsulin, Date curDate, List<InsMonData> insulins){
        double sumLastIabs = 0;
        for(InsMonData ins : insulins){
            if(ins.getTime().before(curDate)){
                sumLastIabs += countIabs(t - ins.getGlobalTimeStart(),ins.getPara().getParameterS(),ins.getT50(),ins.getDoseUnit());
            }
        }
        return sumLastIabs / (Const.VI * weight) - Const.KE * lastInsulin;
    }
    
}
