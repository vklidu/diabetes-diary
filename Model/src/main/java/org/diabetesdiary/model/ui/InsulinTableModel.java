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



package org.diabetesdiary.model.ui;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.diabetesdiary.datamodel.pojo.RecordInsulin;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class InsulinTableModel extends AbstractTableModel{
    private static SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    
    private Date[] time;
    private Double[][] values;
    private boolean pumpUsage;
    
    /** Creates a new instance of SimulationTableModel */
    public InsulinTableModel(boolean pumpUsage) {
        this.pumpUsage = pumpUsage;
        time = new Date[5];
        try {
            time[0] = format.parse("7:45");
            time[1] = format.parse("10:00");
            time[2] = format.parse("12:00");
            time[3] = format.parse("17:30");
            time[4] = format.parse("22:30");
        } catch (ParseException ex) {}
        
        values = new Double[2][5];
        Arrays.fill(values[0],0d);
        Arrays.fill(values[1],0d);
        values[0][0] = 3d;
        values[1][0] = 12d;
        values[0][3] = 4d;
        values[1][4] = 18d;
    }
    
    public Map<Date,Double> getBoluses(){
        Map<Date,Double> res = new HashMap<Date,Double>();
        int i = 0;
        for(Date date : time){
            if(values[0][i] > 0){
                res.put(date,values[0][i]);
            }
            i++;
        }
        return res;
    }
    
    public Map<Date,Double> getBasals(){
        Map<Date,Double> res = new HashMap<Date,Double>();
        int i = 0;
        for(Date date : time){
            if(values[1][i] > 0){
                res.put(date,values[1][i]);
            }
            i++;
        }
        return res;
    }
    
    
    public int getRowCount() {
        return pumpUsage ? 2 : 3;
    }
    
    public int getColumnCount() {
        return 6;
    }
    
    private static NumberFormat numFormat = NumberFormat.getInstance();
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0){
            switch(rowIndex){
                case 0: return NbBundle.getMessage(InsulinTableModel.class,"time");
                case 1: return NbBundle.getMessage(InsulinTableModel.class,"Insulin_bolus_(Jednotek)");
                case 2: return NbBundle.getMessage(InsulinTableModel.class,"insulin_basal");
            }
        }
        if(rowIndex == 0){
            return format.format(time[columnIndex-1]);
        }else{
            return numFormat.format(values[rowIndex-1][columnIndex-1]);
        }
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(aValue != null){
            switch(rowIndex){
                case 0:
                    try{
                        time[columnIndex-1] = format.parse((String) aValue);
                    }catch(ParseException e){}
                    break;
                default:
                    try{
                        values[rowIndex-1][columnIndex-1] = numFormat.parse((String) aValue).doubleValue();
                    }catch(ParseException e){}
                    break;
            }
        }
    }
    
    public String getColumnName(int column) {
        return String.valueOf(column);
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (pumpUsage ? true : rowIndex < 3) && columnIndex > 0;
    }
    
    public boolean isPumpUsage() {
        return pumpUsage;
    }
    
    public void setPumpUsage(boolean pumpUsage) {
        this.pumpUsage = pumpUsage;
        fireTableDataChanged();
    }
    
    public void setRecordInsulins(List<RecordInsulin> insulins){
        Collections.sort(insulins,new Comparator<RecordInsulin>(){
            public int compare(RecordInsulin f1, RecordInsulin f2) {
                return f1.getId().getDate().compareTo(f2.getId().getDate());
            }
        });
        
        for(int i = 0; i < values.length; i++){
            Arrays.fill(values[i],0d);
        }
        
        int i = 0;
        Date lastDate = new Date();
        for(RecordInsulin ins : insulins){
            if(pumpUsage && ins.isBasal()){
                continue;
            }
            int firstIndex = ins.isBasal() ? 1 : 0;
            if(i < 5){
                if(lastDate.equals(ins.getId().getDate())){
                    values[firstIndex][--i] = ins.getAmount();
                }else{                    
                    time[i] = ins.getId().getDate();
                    values[firstIndex][i] = ins.getAmount();
                }
            }
            lastDate = ins.getId().getDate();
            i++;           
        }
        fireTableDataChanged();
    }
    
}
