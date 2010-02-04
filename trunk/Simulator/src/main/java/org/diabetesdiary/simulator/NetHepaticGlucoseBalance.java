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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jiri Majer
 */
public class NetHepaticGlucoseBalance {
    
    private static class NHGBKey{
        private int epi;
        private double ag;
        
        public NHGBKey(int epi, double ag){
            this.epi = epi;
            this.ag = ag;
        }
        
        public boolean equals(Object obj) {
            if(obj instanceof NHGBKey){
                NHGBKey nhgb = (NHGBKey) obj;
                return this.ag == nhgb.ag && this.epi == nhgb.epi;
            }
            return false;
        }
        
        public int hashCode() {
            return (int)Math.round(17*this.ag + 19*this.epi);
        }
        
        
    }
    
    private static Map<NHGBKey,Double> nhgb;
    
    static{
        nhgb = new HashMap<NHGBKey,Double>();
        nhgb.put(new NHGBKey(0,1.1),291.6);
        nhgb.put(new NHGBKey(0,3.3),160.0);
        nhgb.put(new NHGBKey(0,4.4),78.3);
        
        nhgb.put(new NHGBKey(1,1.1),194.6);
        nhgb.put(new NHGBKey(1,3.3),114.6);
        nhgb.put(new NHGBKey(1,4.4),53.3);
        
        nhgb.put(new NHGBKey(2,1.1),129.3);
        nhgb.put(new NHGBKey(2,3.3),66.0);
        nhgb.put(new NHGBKey(2,4.4),-1.7);
        
        nhgb.put(new NHGBKey(3,1.1),95.7);
        nhgb.put(new NHGBKey(3,3.3),46.3);
        nhgb.put(new NHGBKey(3,4.4),-54.3);
        
        nhgb.put(new NHGBKey(4,1.1),85.0);
        nhgb.put(new NHGBKey(4,3.3),22.6);
        nhgb.put(new NHGBKey(4,4.4),-76.0);
        
        nhgb.put(new NHGBKey(5,1.1),76.3);
        nhgb.put(new NHGBKey(5,3.3),4.3);
        nhgb.put(new NHGBKey(5,4.4),-85.0);
        
        nhgb.put(new NHGBKey(6,1.1),69.0);
        nhgb.put(new NHGBKey(6,3.3),-10.0);
        nhgb.put(new NHGBKey(6,4.4),-92.0);
        
        nhgb.put(new NHGBKey(7,1.1),62.0);
        nhgb.put(new NHGBKey(7,3.3),-25.3);
        nhgb.put(new NHGBKey(7,4.4),-97.3);
        
        nhgb.put(new NHGBKey(8,1.1),52.0);
        nhgb.put(new NHGBKey(8,3.3),-43.3);
        nhgb.put(new NHGBKey(8,4.4),-101.0);
        
        nhgb.put(new NHGBKey(9,1.1),48.0);
        nhgb.put(new NHGBKey(9,3.3),-47.3);
        nhgb.put(new NHGBKey(9,4.4),-104.0);
        
        nhgb.put(new NHGBKey(10,1.1),41.7);
        nhgb.put(new NHGBKey(10,3.3),-49.3);
        nhgb.put(new NHGBKey(10,4.4),-106.7);
    }
    
    
    
    /** Creates a new instance of NetHepaticGlucoseBalance */
    private NetHepaticGlucoseBalance() {
    }
    
    public static double getNhgb(double effectivePlasmaInsulin, float insulinSens, double bloodGlucose){
        int epi = (int)Math.round(effectivePlasmaInsulin*insulinSens/Const.IBASAL);
        if(epi < 0 || epi > 10 || bloodGlucose < 0){
            throw new IllegalArgumentException();
        }
        double ag1;
        double ag2;
        if(bloodGlucose <= 1.1){
            ag1 = ag2 = 1.1;
        }else if(bloodGlucose < 3.3){
            ag1 = 1.1;
            ag2 = 3.3;
        }else if(bloodGlucose < 4.4) {
            ag1 = 3.3;
            ag2 = 4.4;
        }else{
            ag1 = ag2 = 4.4;
        }
        
        int heigh = (int)Math.ceil(effectivePlasmaInsulin*insulinSens/Const.IBASAL);
        int low = (int)Math.floor(effectivePlasmaInsulin*insulinSens/Const.IBASAL);
        
        if(ag1 == ag2){
            if(heigh > low){
                double epiLow = nhgb.get(new NHGBKey(low,ag1));
                double epiHeigh = nhgb.get(new NHGBKey(heigh,ag1));
                double realx = effectivePlasmaInsulin*insulinSens/Const.IBASAL;
                return getLinearInterpolation(low,heigh,epiLow,epiHeigh,realx);
            }else{
                return nhgb.get(new NHGBKey(epi,ag1));
            }
        }else{
            if(heigh > low){
                double epiLow = nhgb.get(new NHGBKey(low,ag1));
                double epiHeigh = nhgb.get(new NHGBKey(heigh,ag1));
                double realx = effectivePlasmaInsulin*insulinSens/Const.IBASAL;
                double result1 = getLinearInterpolation(low,heigh,epiLow,epiHeigh,realx);
                
                epiLow = nhgb.get(new NHGBKey(low,ag2));
                epiHeigh = nhgb.get(new NHGBKey(heigh,ag2));
                double result2 = getLinearInterpolation(low,heigh,epiLow,epiHeigh,realx);
                return getLinearInterpolation(ag1,ag2,result1,result2,bloodGlucose);
            }else{
                double result1 = nhgb.get(new NHGBKey(epi,ag1));
                double result2 = nhgb.get(new NHGBKey(epi,ag2));
                return getLinearInterpolation(ag1,ag2,result1,result2,bloodGlucose);
            }
        }
    }
    
    
    public static double getNhgbn(double effectivePlasmaInsulin, float insulinSens, double bloodGlucose){
        double epi = effectivePlasmaInsulin*insulinSens/Const.IBASAL;
        if(epi < 0 || epi > 10 || bloodGlucose < 0){
            //    throw new IllegalArgumentException();
            return 0;
        }
        double ag1;
        double ag2;
        if(bloodGlucose < 3.3){
            ag1 = 1.1;
            ag2 = 3.3;
        }else if(bloodGlucose == 3.3){
            ag1 = 3.3;
            ag2 = 3.3;
        }else if(bloodGlucose < 4.4){
            ag1 = 3.3;
            ag2 = 4.4;
        }else{
            ag1 = 4.4;
            ag2 = 4.4;
        }
        
        if(ag1 == ag2){
            return expAg(epi,ag1);
        }else{
            double realx = effectivePlasmaInsulin*insulinSens/Const.IBASAL;
            double result1 = expAg(epi,ag1);
            double result2 = expAg(epi,ag2);
            return getLinearInterpolation(ag1,ag2,result1,result2,bloodGlucose);
        }
    }
    
    
    private static double expFunc(double c, double b0, double b1, double x){
        return c + Math.exp(b0 + b1 * x);
    }
    
    private static double expAg(double x, double ag){
        if(ag == 1.1){
            return expFunc(50.2161,5.48162,-0.5173,x);
        }else if (ag == 3.3){
            return expFunc(-78.7445,5.469739,-0.217106,x);
        }else if (ag == 4.4){
            return expFunc(-117.403,5.341358,-0.341395,x);
        }
        throw new IllegalArgumentException();
    }
    
    /*
    public static double getNhgb(double effectivePlasmaInsulin, float insulinSens, double bloodGlucose){
        int epi = (int)Math.round(effectivePlasmaInsulin*insulinSens/Const.IBASAL);
        if(epi < 0 || epi > 10 || bloodGlucose < 0){
        //    throw new IllegalArgumentException();
            return 0;
        }
        double ag1;
        double ag2;
        if(bloodGlucose <= 1.1){
            ag1 = ag2 = 1.1;
        }else if(bloodGlucose < 3.3){
            ag1 = 1.1;
            ag2 = 3.3;
        }else if(bloodGlucose < 4.4) {
            ag1 = 3.3;
            ag2 = 4.4;
        }else{
            ag1 = ag2 = 4.4;
        }
     
        int heigh = (int)Math.ceil(effectivePlasmaInsulin*insulinSens/Const.IBASAL);
        int low = (int)Math.floor(effectivePlasmaInsulin*insulinSens/Const.IBASAL);
     
        if(ag1 == ag2){
            if(heigh > low){
                double epiLow = nhgb.get(new NHGBKey(low,ag1));
                double epiHeigh = nhgb.get(new NHGBKey(heigh,ag1));
                double realx = effectivePlasmaInsulin*insulinSens/Const.IBASAL;
                return getLinearInterpolation(low,heigh,epiLow,epiHeigh,realx);
            }else{
                return nhgb.get(new NHGBKey(epi,ag1));
            }
        }else{
            if(heigh > low){
                double epiLow = nhgb.get(new NHGBKey(low,ag1));
                double epiHeigh = nhgb.get(new NHGBKey(heigh,ag1));
                double realx = effectivePlasmaInsulin*insulinSens/Const.IBASAL;
                double result1 = getLinearInterpolation(low,heigh,epiLow,epiHeigh,realx);
     
                epiLow = nhgb.get(new NHGBKey(low,ag2));
                epiHeigh = nhgb.get(new NHGBKey(heigh,ag2));
                double result2 = getLinearInterpolation(low,heigh,epiLow,epiHeigh,realx);
                return getLinearInterpolation(ag1,ag2,result1,result2,bloodGlucose);
            }else{
                double result1 = nhgb.get(new NHGBKey(epi,ag1));
                double result2 = nhgb.get(new NHGBKey(epi,ag2));
                return getLinearInterpolation(ag1,ag2,result1,result2,bloodGlucose);
            }
        }
    }
     */
    
    
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
}
