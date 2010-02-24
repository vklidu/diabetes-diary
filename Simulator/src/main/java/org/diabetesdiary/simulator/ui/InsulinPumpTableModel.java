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



package org.diabetesdiary.simulator.ui;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.diabetesdiary.diary.domain.RecordInsulin;

/**
 *
 * @author Jiri Majer
 */
public class InsulinPumpTableModel extends AbstractTableModel{
    private Double[] values;
    
    /** Creates a new instance of SimulationTableModel */
    public InsulinPumpTableModel() {
        values = new Double[24];
        Arrays.fill(values,1d);
    }
    
    public Double[] getBasales(){
        return values;
    }
    
    public int getRowCount() {
        return 1;
    }
    
    public int getColumnCount() {
        return 25;
    }
    
    private static NumberFormat numFormat = NumberFormat.getInstance();
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0){
            switch(rowIndex){
                case 0: return "U/h";
            }
        }
        return numFormat.format(values[columnIndex-1]);
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(columnIndex > 0 && aValue != null){
            try{
                values[columnIndex-1] = numFormat.parse((String)aValue).doubleValue();
            }catch(ParseException nfe){}
        }
    }
    
    @Override
    public String getColumnName(int column) {
        if(column > 0){
            return String.valueOf(column-1)+"-"+String.valueOf(column);
        }
        return " ";
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex > 0;
    }
    
    public void setRecordInsulins(List<RecordInsulin> insulins){
        Collections.sort(insulins,new Comparator<RecordInsulin>(){
            public int compare(RecordInsulin f1, RecordInsulin f2) {
                return f1.getDatetime().compareTo(f2.getDatetime());
            }
        });
        Arrays.fill(values,0d);
        Calendar month = Calendar.getInstance();
        for(RecordInsulin rec : insulins){
            month.setTimeInMillis(rec.getDatetime().getMillis());
            if(rec.isBasal()){
                values[month.get(Calendar.HOUR_OF_DAY)] = rec.getAmount();
            }
        }
        fireTableDataChanged();
    }
    
}
