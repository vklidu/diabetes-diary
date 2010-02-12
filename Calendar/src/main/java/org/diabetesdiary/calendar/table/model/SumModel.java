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
package org.diabetesdiary.calendar.table.model;

import java.util.List;
import javax.swing.table.TableColumn;
import org.diabetesdiary.calendar.table.header.ColumnGroup;
import org.diabetesdiary.calendar.utils.FormatUtils;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.calendar.utils.DataChangeEvent;
import org.diabetesdiary.diary.domain.FoodUnit;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.domain.RecordFood;
import org.diabetesdiary.diary.domain.RecordInsulin;
import org.diabetesdiary.diary.domain.RecordInvest;
import org.diabetesdiary.diary.domain.WKInvest;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class SumModel extends AbstractRecordSubModel {

    private FoodUnit sachUnit;
    private List<RecordInsulin> insulines;
    private List<RecordInvest> weights;
    private List<RecordFood> foods;

    public SumModel(DateTime dateTime, Patient patient) {
        super(dateTime, patient);
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public ColumnGroup getColumnHeader(List<TableColumn> cols) {
        ColumnGroup gSum = new ColumnGroup(NbBundle.getMessage(SumModel.class, "Column.suma"));
        gSum.add(cols.get(0));
        gSum.add(cols.get(1));
        gSum.add(cols.get(2));
        gSum.add(cols.get(3));

        return gSum;
    }

    @Override
    public String getValueAt(int row, int col) {
        checkData();
        DateTime rowDateFrom = dateTime.withDayOfMonth(row + 1).toDateMidnight().toDateTime();
        DateTime rowDateTo = rowDateFrom.plusDays(1);
        switch (col) {
            //suma insulin
            case 0:
                double sumaInsulines = 0;
                for (RecordInsulin rec : insulines) {
                    if (!rec.getDatetime().isBefore(rowDateFrom) && !rec.getDatetime().isAfter(rowDateTo)) {
                        sumaInsulines += rec.getAmount();
                    }
                }
                return FormatUtils.getDoubleFormat().format(sumaInsulines);
            //suma food units
            case 1:
                double sumaFoodUnits = 0;
                for (RecordFood rec : foods) {
                    if (!rec.getDatetime().isBefore(rowDateFrom) && !rec.getDatetime().isAfter(rowDateTo)) {
                        sumaFoodUnits += rec.getSachUnits(sachUnit);
                    }
                }
                return FormatUtils.getDoubleFormat().format(sumaFoodUnits);
            //bolus/food
            case 2:
                double sumFoodUnits = 0;
                for (RecordFood rec : foods) {
                    if (!rec.getDatetime().isBefore(rowDateFrom) && !rec.getDatetime().isAfter(rowDateTo)) {
                        sumFoodUnits += rec.getSachUnits(sachUnit);
                    }
                }
                double sumBolus = 0;
                for (RecordInsulin rec : insulines) {
                    if (!rec.isBasal() && !rec.getDatetime().isBefore(rowDateFrom) && !rec.getDatetime().isAfter(rowDateTo)) {
                        sumBolus += rec.getAmount();
                    }
                }
                
                return sumFoodUnits > 0 && sumBolus > 0 ? FormatUtils.getDoubleFormat().format(sumBolus / sumFoodUnits) : null;
            //ins./kg
            case 3:
                double sumaInsulin = 0;
                for (RecordInsulin rec : insulines) {
                    if (!rec.getDatetime().isBefore(rowDateFrom) && !rec.getDatetime().isAfter(rowDateTo)) {
                        sumaInsulin += rec.getAmount();
                    }
                }
                Double weight = getWeight(row);
                if (weight == null) {
                    return NbBundle.getMessage(SumModel.class, "unknown.weight");
                }
                return weight > 0 && sumaInsulin > 0 ? FormatUtils.getDoubleFormat().format(sumaInsulin / weight) : null;
            default:
                throw new IllegalStateException();
        }
    }

     private Double getWeight(int rowIndex) {
        Double result = null;
        for (RecordInvest weight : weights) {
            if (dateTime.withDayOfMonth(rowIndex + 1).isBefore(weight.getDatetime())) {
                break;
            }
            result = weight.getValue();
        }
        return result == null && !weights.isEmpty() ? weights.get(0).getValue() : result;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        //nothing
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return NbBundle.getMessage(SumModel.class, "Column.insulin");
            case 1:
                return NbBundle.getMessage(SumModel.class, "Column.food");
            case 2:
                return NbBundle.getMessage(SumModel.class, "bulus.sacharid");
            case 3:
                return NbBundle.getMessage(SumModel.class, "insulin.per.kilogram");
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public void onDataChange(DataChangeEvent evt) {
        if (evt.getDataChangedClazz() == null) {
            insulines = null;
            weights = null;
            foods = null;
        } else if (evt.getDataChangedClazz().equals(RecordInsulin.class)) {
            insulines = null;
        } else if (evt.getDataChangedClazz().equals(RecordInvest.class)) {
            weights = null;
        } else if (evt.getDataChangedClazz().equals(RecordFood.class)) {
            foods = null;
            sachUnit = null;
        }        
    }


    private void checkData() {
        if (insulines == null) {
            insulines = MyLookup.getCurrentPatient().getRecordInsulins(getFrom(), getTo());
        }
        if (weights == null) {
            weights = MyLookup.getCurrentPatient().getRecordInvests(getFrom(), getTo(), WKInvest.WEIGHT);
        }
        if (foods == null) {
            foods = MyLookup.getCurrentPatient().getRecordFoods(getFrom(), getTo());
        }
        if (sachUnit == null) {
            sachUnit = MyLookup.getDiaryRepo().getSacharidUnit(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT));
        }
    }
}
