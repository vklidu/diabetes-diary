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

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.diabetesdiary.diary.api.InsulinParameters;
import static org.diabetesdiary.simulator.Const.*;
import org.diabetesdiary.simulator.equation.DifferentialAproximation;
import org.diabetesdiary.simulator.equation.Function;
import org.diabetesdiary.simulator.equation.GlucoseFunction;
import org.diabetesdiary.simulator.equation.GlucoseInGutFunction;
import org.diabetesdiary.simulator.equation.InsulinActiveFunction;
import org.diabetesdiary.simulator.equation.InsulinFunction;

/**
 *
 * @author Jiri Majer
 */
public class SimulationManager {
    
    //one day after last injection
    private long totalTime = 24*60*60*1000;
    //insulin pattern
    private InsulinParam insulin;
    //default step is 0.1 hour
    private float dT = 0.25f;
    private double renalThreshold = DEFAULT_RENAL_THRESHOLD;
    private double glomerularFiltration = DEFAULT_GLOMERULAR_FILTRATION;
    
    private Map<Double,InsMonData> result = new HashMap<Double,InsMonData>();
    private List<InsMonData> injections;
    private List<GlucoseData> meals;
    private float insulinSensitivityPeri;
    private float insulinSensitivityHepatic;
    
    private float weight;
    
    /**
     * Creates a new instance of SimulationManager
     */
    public SimulationManager(float insulinSensitivityHepatic, float insulinSensitivityPeri, float weight) {
        if(insulinSensitivityPeri < 0 || insulinSensitivityPeri > 1){
            throw new IllegalArgumentException("Range of insulin sensitivity is 0-1.");
        }
        this.weight = weight;
        this.insulinSensitivityPeri = insulinSensitivityPeri;
        this.insulinSensitivityHepatic = insulinSensitivityHepatic;
        this.injections = new LinkedList<InsMonData>();
        meals = new LinkedList<GlucoseData>();
    }
    
    public void addInsulinInjection(InsulinParameters insulin, double doseUnit, Date timeOfInjection){
        injections.add(new InsMonData(insulin,doseUnit,timeOfInjection));
        Collections.sort(injections);
    }
    
    public void addBasalInsulinForDay(Date day, InsulinParameters insulin, int minInterval, Double ... hourDoses){
        if(hourDoses == null || hourDoses.length != 24){
            throw new IllegalArgumentException("Dose must be specified for each hour (24 doubles)");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        
        for(Double hourDose : hourDoses){
            int lastHour = cal.get(Calendar.HOUR_OF_DAY);
            int count = 60/minInterval;
            for(int i=0;i<count;i++){
                addInsulinInjection(insulin,hourDose/count,cal.getTime());
                cal.add(Calendar.MINUTE,minInterval);
            }
            if(cal.get(Calendar.HOUR_OF_DAY) == lastHour){
                cal.add(Calendar.HOUR_OF_DAY,1);
            }
            cal.set(Calendar.MINUTE,0);
        }
    }
    
    
    public void addCarbohydrateIntake(double grams, Date timeOfIntake){
        int MAX_MEAL = 80;
        while(grams > MAX_MEAL){
            meals.add(new GlucoseData(MAX_MEAL,timeOfIntake));
            grams -= MAX_MEAL;
        }
        meals.add(new GlucoseData(grams,timeOfIntake));
        Collections.sort(meals);
    }
    
    
    public SimulationResult getSimulationResult(){
        if(injections == null || injections.size() < 1)return null;
        
        SimulationResult result = new SimulationResult();
        double lastInsulin = 0;
        double lastGlucose = 0;
        double lastInsulinA = 0;
        double lastInsulinEq = 0;
        double lastGgut = 0;
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(injections.get(0).getTime().getTime());
        double globalTime = 0;
        Date last = new Date(injections.get(injections.size()-1).getTime().getTime()+totalTime);
        while(!current.getTime().after(last)){
            double sumLastIabs = 0;
            for(InsMonData ins : injections){
                if(ins.getTime().before(current.getTime())){
                    if(ins.getGlobalTimeStart() == -1){
                        ins.setGlobalTimeStart(globalTime);
                    }
                    double t = ins.getTimeAfterInjection();
                    //double iabs = countIabs(t,ins.getPara().getParameterS(),ins.getT50(),ins.getDoseUnit());
                    //ins.setLastValueIabs(iabs);
                    ins.setTimeAfterInjection(t+dT);
                    //sumLastIabs += iabs;
                }
            }
            //lastInsulin = (sumLastIabs /(VI * weight) - KE * lastInsulin) * dT + lastInsulin;
            double[] insRes = DifferentialAproximation.getNextX(new Function[]{
                new InsulinFunction(weight,injections,current.getTime()),
                new InsulinActiveFunction()},
                    dT,globalTime,lastInsulin,lastInsulinA);
            lastInsulin = insRes[0];
            //lastInsulinA = (K1 * lastInsulin - K2 * lastInsulinA) * dT + lastInsulinA;
            lastInsulinA = insRes[1];
            lastInsulinEq = K2 * lastInsulinA / K1;
            
            
            double sumGempt = 0;
            for(GlucoseData gluc : meals){
                if(gluc.getTimeOfIntake().before(current.getTime())){
                    double t = gluc.getTimeAfterIntake();
                    double gempt = gluc.getGempt(t);
                    gluc.setTimeAfterIntake(t+dT);
                    if(gempt <= 0){
                        continue;
                    }
                    sumGempt += gempt;
                    //gluc.setLastGgut(ggut);
                }
            }
            
            double nhgb = NetHepaticGlucoseBalance.getNhgb(lastInsulinEq*1000,insulinSensitivityHepatic,lastGlucose);
            double gout = getGout(lastGlucose,lastInsulinEq*1000)*weight;
            double gren = getGren(lastGlucose);
            
            double[] gluRes = DifferentialAproximation.getNextX(new Function[]{
                new GlucoseFunction(nhgb,weight,gout,gren),
                new GlucoseInGutFunction(sumGempt)},
                    dT,globalTime,lastGlucose,lastGgut);
            
            //lastGgut = (sumGempt-KGABS*lastGgut)*dT + lastGgut;
            lastGgut = gluRes[1];
            //rungeGgut = gluRes[1];
            //multiplication 1000; U => mU
            lastGlucose = gluRes[0];
            //lastGlucose = ((KGABS*lastGgut + nhgb - gout - gren)/(VG * weight))*dT + lastGlucose;
            //rungeGlu = gluRes[0];
            
            
            SRElement modelResult = new SRElement();
            modelResult.setActiveInsulin(lastInsulinA*1000);
            modelResult.setEqInsulin(lastInsulinEq*1000);
            modelResult.setGlucose(lastGlucose);
            modelResult.setGlucoseInGut(lastGgut);
            modelResult.setGlucoseUtilisation(gout);
            modelResult.setNhgb(nhgb);
            modelResult.setPlasmaInsulin(lastInsulin*1000);
            modelResult.setRenalGlucoseExcretion(gren);
            current.add(Calendar.SECOND,Math.round(dT*60*60));
            result.addResult(current.getTime(),modelResult);
            globalTime += dT;
        }
        return result;
    }
    
    
    private double getGout(double lastGlucose, double lastInsulinEq){
        double numerator = lastGlucose*(C*insulinSensitivityPeri*lastInsulinEq+GL)*(KM+GX);
        double denominator = GX*(KM+lastGlucose);
        return numerator / denominator;
    }
    
    private double countIabs(double t, double s, double t50, double D){
        if(t == 0)return 0;
        double numerator = s*Math.pow(t,s)*Math.pow(t50,s)*D;
        double denominator = t*(Math.pow(Math.pow(t50,s)+Math.pow(t,s),2));
        return numerator/denominator;
    }
    
    private double getGren(double glucose) {
        if(glucose > getRenalThreshold()){
            return getGlomerularFiltration()*(glucose - getRenalThreshold());
        }
        return 0;
    }
    
    public double getRenalThreshold() {
        return renalThreshold;
    }
    
    public void setRenalThreshold(double renalThreshold) {
        this.renalThreshold = renalThreshold;
    }
    
    public double getGlomerularFiltration() {
        return glomerularFiltration;
    }
    
    public void setGlomerularFiltration(double glomerularFiltration) {
        this.glomerularFiltration = glomerularFiltration;
    }
    
    
}
