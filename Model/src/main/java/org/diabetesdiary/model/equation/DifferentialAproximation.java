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

/**
 *
 * @author Jiri Majer
 */
public class DifferentialAproximation {
    
    /** Creates a new instance of DifferentialAproximation */
    private DifferentialAproximation() {
    }
    
    public static double getNextX(Function f, double x, double t, double h){
        double a = f.count(t, x);
        double b = f.count(t + h/2, x + h*a/2);
        double c = f.count(t + h/2, x + h*b/2);
        double d = f.count(t + h, x + h*c);
        double result = x + h/6*(a + 2*b + 2*c + d);
        return result;
    }
    
    //spocita vice rovnic
    public static double[] getNextX(Function[] fces, double h, double t, double ... xs){
        if(fces.length != xs.length){
            throw new IllegalArgumentException("Length of parameter arrays differ");
        }
        
        double[] result = new double[fces.length];
        double[] a = new double[fces.length];
        double[] b = new double[fces.length];
        double[] c = new double[fces.length];
        double[] d = new double[fces.length];
        
        for(int i=0; i < fces.length; i++){
            a[i] = fces[i].count(t,xs);
        }
        for(int i=0; i < fces.length; i++){
            double[] par = new double[fces.length];
            for(int j=0; j < fces.length; j++){
                par[j] = xs[j] + h/2*a[j];
            }            
            b[i] = fces[i].count(t+h/2,par);
        }
        for(int i=0; i < fces.length; i++){
            double[] par = new double[fces.length];
            for(int j=0; j < fces.length; j++){
                par[j] = xs[j] + h/2*b[j];
            }            
            c[i] = fces[i].count(t+h/2,par);
        }
        
        for(int i=0; i < fces.length; i++){
            double[] par = new double[fces.length];
            for(int j=0; j < fces.length; j++){
                par[j] = xs[j] + h*c[j];
            }            
            d[i] = fces[i].count(t+h,par);
        }
        
        for(int i=0; i < fces.length; i++){
            result[i] = xs[i] + h/6*(a[i] + 2*b[i] + 2*c[i] + d[i]);
        }
                
        return result;
    }
    
}
