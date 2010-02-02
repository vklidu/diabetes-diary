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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.diabetesdiary.datamodel.api.DbLookUp;
import org.diabetesdiary.datamodel.api.FoodAdministrator;
import org.diabetesdiary.datamodel.pojo.RecordFood;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class MealTableModel extends AbstractTableModel{
    
    private Date[] time;
    private Double[] sacharid;
    private static SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    private static NumberFormat numFormat = NumberFormat.getInstance();
    
    /** Creates a new instance of MealTableModel */
    public MealTableModel() {
        time = new Date[6];
        try {
            time[0] = format.parse("8:00");
            time[1] = format.parse("10:00");
            time[2] = format.parse("12:00");
            time[3] = format.parse("15:50");
            time[4] = format.parse("18:00");
            time[5] = format.parse("20:00");
        } catch (ParseException ex) {}
        sacharid = new Double[6];
        sacharid[0] = 30d;
        sacharid[1] = 20d;
        sacharid[2] = 40d;
        sacharid[3] = 10d;
        sacharid[4] = 30d;
        sacharid[5] = 0d;
    }
    
    public Map<Date,Double> getMeals(){
        Map<Date,Double> res = new HashMap<Date,Double>();
        int i = 0;
        for(Date date : time){
            if(sacharid[i] > 0){
                res.put(date,sacharid[i]);
            }
            i++;
        }
        return res;
    }
    
    public int getRowCount() {
        return 2;
    }
    
    public int getColumnCount() {
        return 7;
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0){
            return rowIndex == 0 ? NbBundle.getMessage(MealTableModel.class,"time") : NbBundle.getMessage(MealTableModel.class,"Sacharidy_(gram)");
        }
        if(rowIndex == 0){
            return format.format(time[columnIndex-1]);
        }else{
            return numFormat.format(sacharid[columnIndex-1]);
        }
    }
    
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(rowIndex == 0 && aValue != null){
            try{
                time[columnIndex-1] = format.parse((String) aValue);
            }catch(ParseException e){}
        }else if(rowIndex == 1 && aValue != null){
            try{
                sacharid[columnIndex-1] = numFormat.parse((String) aValue).doubleValue();
            }catch(ParseException e){}
        }
    }
    
    public String getColumnName(int column) {
        switch(column){
            case 1: return NbBundle.getMessage(MealTableModel.class,"breakfast");
            case 2: return NbBundle.getMessage(MealTableModel.class,"snack");
            case 3: return NbBundle.getMessage(MealTableModel.class,"lunch");
            case 4: return NbBundle.getMessage(MealTableModel.class,"snack");
            case 5: return NbBundle.getMessage(MealTableModel.class,"supper");
            case 6: return NbBundle.getMessage(MealTableModel.class,"sec_supper");
            default: return " ";
        }
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex > 0;
    }
    
    
    public void setRecordFoods(List<RecordFood> foods){
        Collections.sort(foods,new Comparator<RecordFood>(){
            public int compare(RecordFood f1, RecordFood f2) {
                return f1.getId().getDate().compareTo(f2.getId().getDate());
            }
        });
        for(int i = 0; i < 6; i++){
            sacharid[i] = 0d;
        }
        FoodAdministrator foodAdmin = DbLookUp.getFoodAdmin();
        int i = 0;
        Date lastDate = new Date();
        for(RecordFood food : foods){
            if(i < 6){
                double koef = foodAdmin.getFoodUnit(food.getId().getIdFood(),food.getUnit()).getKoef();
                double grams = food.getAmount() * koef;
                
                if(lastDate.equals(food.getId().getDate())){
                    sacharid[--i] += food.getFood().getSugar()*grams/100;
                }else{                    
                    time[i] = food.getId().getDate();
                    sacharid[i] = food.getFood().getSugar()*grams/100;
                }
            }
            lastDate = food.getId().getDate();
            i++;
        }
        fireTableDataChanged();
    }
    
    
}
