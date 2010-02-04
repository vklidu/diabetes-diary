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
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.diary.utils.MyLookup;
import org.diabetesdiary.diary.service.db.FoodUnitDO;
import org.diabetesdiary.diary.service.db.RecordFoodDO;

/**
 *
 * @author Jiri Majer
 */
public class FoodCellRenderer extends JLabel implements TableCellRenderer {

    private static NumberFormat format = NumberFormat.getInstance();
    private static DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    private static final Color forColor = Color.BLACK;
    private static final Color backColor = Color.WHITE;
    private static final Color forSelColor = Color.WHITE;
    private static final Color backSelColor = new Color(30, 30, 100);

    /** Creates a new instance of CalendarCellRenderer */
    public FoodCellRenderer() {
        super();
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return createCell(table, value, isSelected);
    }

    public static Component createCell(JTable table, Object value, boolean isSelected) {
        FoodCellRenderer result = new FoodCellRenderer();
        result.setHorizontalAlignment(CENTER);
        FoodUnitDO sachUnit = MyLookup.getFoodAdmin().getFoodUnit(1, CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT));
        if (value instanceof RecordFoodDO) {
            RecordFoodDO rec = (RecordFoodDO) value;
            //Double unit = rec.getAmount()
            if (rec.getAmount() != null) {
                result.setText(format.format(countSachUnits(rec, sachUnit)));
                result.setToolTipText(createToolTip(rec));
            }
        } else if (value instanceof RecordFoodDO[]) {
            RecordFoodDO[] recs = (RecordFoodDO[]) value;
            double sum = 0;
            for (RecordFoodDO rec : recs) {
                if (rec != null && rec.getAmount() != null) {
                    sum += countSachUnits(rec, sachUnit);
                }
            }
            result.setText(format.format(sum));
            result.setToolTipText(createToolTip(recs));
        }

        if (isSelected) {
            result.setBackground(backSelColor);
            result.setForeground(forSelColor);
        } else {
            result.setBackground(backColor);
            result.setForeground(forColor);
        }

        return result;
    }

    private static Double countSachUnits(RecordFoodDO rec, FoodUnitDO sachUnit) {
        if (rec.getAmount() == null) {
            return null;
        }
        FoodUnitDO unit = null;
        if (rec.getFood() != null && rec.getFood().getUnits() != null) {
            for (Object un : rec.getFood().getUnits()) {
                unit = (FoodUnitDO) un;
                if (unit.getId().getUnit().equals(rec.getUnit())) {
                    break;
                }
            }
        }
        if (unit == null) {
            unit = MyLookup.getFoodAdmin().getFoodUnit(rec.getId().getIdFood(), rec.getUnit());
        }
        double sachUnits = unit.getKoef() * rec.getAmount() * rec.getFood().getSugar() / (100 * sachUnit.getKoef());
        return sachUnits;
    }

    private static String createToolTip(RecordFoodDO rec) {
        if (rec == null || rec.getAmount() == null || rec.getFood() == null) {
            return null;
        }

        String result = timeFormat.format(rec.getId().getDate()) + "\n";
        result += rec.getFood().getName();
        result += " " + format.format(rec.getAmount()) + " " + rec.getUnit();

        return result;
    }

    private static String createToolTip(RecordFoodDO[] recs) {
        if (recs == null || recs.length < 1 || recs[0] == null || recs[0].getAmount() == null || recs[0].getFood() == null) {
            return null;
        }
        StringBuffer result = new StringBuffer();
        Date lastDate = null;
        for (RecordFoodDO rec : recs) {
            if (rec != null) {
                if (lastDate == null || !lastDate.equals(rec.getId().getDate())) {
                    result.append(timeFormat.format(rec.getId().getDate())).append('\n');
                }
                result.append(rec.getFood().getName());
                result.append(' ').append(format.format(rec.getAmount()));
                result.append(' ').append(rec.getUnit());
                result.append('\n');
                lastDate = rec.getId().getDate();
            }
        }

        return result.toString();
    }
}