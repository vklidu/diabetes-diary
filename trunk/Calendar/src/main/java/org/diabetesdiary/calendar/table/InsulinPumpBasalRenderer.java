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


package org.diabetesdiary.calendar.table;

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.diabetesdiary.diary.domain.RecordInsulin;
/**
 *
 * @author Jiri Majer
 */
public class InsulinPumpBasalRenderer extends JLabel implements TableCellRenderer{
    private static NumberFormat format = NumberFormat.getInstance();
    
    private static final Color forColor = Color.BLACK;
    private static final Color backColor = Color.LIGHT_GRAY;
    private static final Color forSelColor = Color.WHITE;
    private static final Color backSelColor = Color.DARK_GRAY;
    
    /** Creates a new instance of CalendarCellRenderer */
    public InsulinPumpBasalRenderer() {
        super();
        setOpaque(true);
    }
    
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return createCell(table, value, isSelected);
    }
    
    
    public static Component createCell(JTable table, Object value, boolean isSelected){
        InsulinPumpBasalRenderer result = new InsulinPumpBasalRenderer();
        if(isSelected){
            result.setBackground(backSelColor);
            result.setForeground(forSelColor);
        }else{
            result.setBackground(backColor);
            result.setForeground(forColor);
        }
        result.setHorizontalAlignment(CENTER);
        if(value instanceof RecordInsulinPumpBasal){
            result.setText(value.toString());
            result.setToolTipText(createToolTip((RecordInsulinPumpBasal) value));
        }
        return result;
    }
    
    
    private static String createToolTip(RecordInsulinPumpBasal records){
        StringBuffer result = new StringBuffer();
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        double lastVal = -1;
        Date firstDate = null;
        Date lastDate = null;        
        for(RecordInsulin rec : records.getData()){
            if(rec != null){
                if(lastVal == -1){
                    firstDate = rec.getDatetime().toDate();
                    lastDate = rec.getDatetime().toDate();
                    lastVal = rec.getAmount();
                }else if(rec.getAmount() != lastVal){
                    result.append(dateFormat.format(firstDate));
                    result.append("-");
                    result.append(dateFormat.format(new Date(rec.getDatetime().toDate().getTime())));
                    result.append(" ").append(lastVal).append("U\n");
                    lastVal = rec.getAmount();
                    firstDate = rec.getDatetime().toDate();
                    lastDate = rec.getDatetime().toDate();
                }else{
                    lastDate = rec.getDatetime().toDate();
                }
            }else if(lastVal != -1){
                result.append(dateFormat.format(firstDate));
                result.append("-");
                result.append(dateFormat.format(new Date(lastDate.getTime()+60*60*1000)));
                result.append(" ").append(lastVal).append("U\n");
                lastVal = -1;
            }
        }
        if(lastVal != -1){
            result.append(dateFormat.format(firstDate));
            result.append("-");
            result.append(dateFormat.format(new Date(lastDate.getTime()+60*60*1000)));
            result.append(" ").append(lastVal).append("U\n");
            
        }
        return result.substring(0,result.length()-1);
    }
    
    
}