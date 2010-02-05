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

import java.util.Calendar;
import java.util.Collection;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import org.diabetesdiary.calendar.ColumnGroup;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.diary.domain.Food;
import org.diabetesdiary.diary.domain.FoodSeason;
import org.diabetesdiary.diary.domain.FoodUnit;
import org.diabetesdiary.diary.domain.RecordFood;
import org.diabetesdiary.diary.domain.WKFood;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class RecordFoodModel implements TableSubModel, Comparable<TableSubModel> {

    private RecordFood dataFood[][][];
    private int baseIndex;
    private Calendar month;

    /** Creates a new instance of RecordFoodModel */
    public RecordFoodModel(int baseIndex, Calendar month) {
        this.baseIndex = baseIndex;
        this.month = month;
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public ColumnGroup getColumnHeader(TableColumnModel columnModel) {
        ColumnGroup gFood = new ColumnGroup(NbBundle.getMessage(RecordFoodModel.class, "Column.food"));
        for (int i = baseIndex; i < baseIndex + getColumnCount(); i++) {
            gFood.add(columnModel.getColumn(i));
        }
        return gFood;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (dataFood != null && rowIndex > -1) {
            if (dataFood[rowIndex][columnIndex] != null && dataFood[rowIndex][columnIndex].length == 1) {
                RecordFood res = dataFood[rowIndex][columnIndex][0];
                return res;
            } else {
                RecordFood[] res = dataFood[rowIndex][columnIndex];
                return res;
            }
        }
        return null;
    }

    @Override
    public Object getNewRecordValueAt(int rowIndex, int columnIndex) {
        /*
        RecordFood food = new RecordFood();
        RecordFoodPK pk = new RecordFoodPK();
        pk.setIdPatient(MyLookup.getDiaryRepo().getCurrentPatient().getIdPatient());
        pk.setIdFood(1);
        pk.setDate(getClickCellDate(rowIndex, columnIndex));
        food.setId(pk);

        food.setSeason(FoodSeason.values()[columnIndex].name());
        FoodAdministrator foodAdmin = MyLookup.getFoodAdmin();
        food.setFood(foodAdmin.getFood(pk.getIdFood()));
        food.setUnit(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT));
        food.setAmount(0d);
        return food;
         * 
         */
        return null;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value instanceof Double) {
            Food food = MyLookup.getDiaryRepo().getWellKnownFood(WKFood.SACCHARIDE);
            FoodUnit unit = MyLookup.getDiaryRepo().getSacharidUnit(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT));
            RecordFood rec = MyLookup.getCurrentPatient().addRecordFood(getClickCellDate(rowIndex, columnIndex),
                    food, (Double) value, (Double) value, unit, FoodSeason.values()[columnIndex], null);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(rec.getDatetime().getMillis());
            int col = rec.getSeason().ordinal();
            if (dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] == null) {
                dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] = rec;
            } else {
                if (rec.getFood().equals(dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0].getFood())) {
                    dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] = rec;
                } else {
                    RecordFood[] pom = dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col];
                    dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col] = new RecordFood[pom.length + 1];
                    dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] = rec;
                    for (int i = 0; i < pom.length; i++) {
                        dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][i + 1] = pom[i];
                    }
                }
            }
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return RecordFood.class;
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return NbBundle.getMessage(RecordFoodModel.class, "Column.breakfest");
            case 1:
                return NbBundle.getMessage(RecordFoodModel.class, "Column.snack");
            case 2:
                return NbBundle.getMessage(RecordFoodModel.class, "Column.dinner");
            case 3:
                return NbBundle.getMessage(RecordFoodModel.class, "Column.snack");
            case 4:
                return NbBundle.getMessage(RecordFoodModel.class, "Column.Launch");
            case 5:
                return NbBundle.getMessage(RecordFoodModel.class, "Column.SecondLaunch");
            default:
                return "";
        }
    }

    public DateTime getClickCellDate(int row, int column) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(month.getTimeInMillis());
        cal.set(Calendar.DAY_OF_MONTH, row + 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        switch (column) {
            case 0:
                cal.set(Calendar.HOUR_OF_DAY, 7);
                break;
            case 1:
                cal.set(Calendar.HOUR_OF_DAY, 10);
                break;
            case 2:
                cal.set(Calendar.HOUR_OF_DAY, 12);
                break;
            case 3:
                cal.set(Calendar.HOUR_OF_DAY, 15);
                break;
            case 4:
                cal.set(Calendar.HOUR_OF_DAY, 17);
                break;
            case 5:
                cal.set(Calendar.HOUR_OF_DAY, 20);
                break;
        }
        return new DateTime(cal.getTime());
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void setData(Collection<?> data) {
        //max number of days
        dataFood = new RecordFood[31][6][1];
        Calendar cal = Calendar.getInstance();
        for (Object record : data) {
            if (record instanceof RecordFood) {
                RecordFood rec = (RecordFood) record;
                cal.setTimeInMillis(rec.getDatetime().getMillis());
                int col = rec.getSeason().ordinal();

                if (dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] == null) {
                    dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] = rec;
                } else {
                    RecordFood[] pom = dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col];
                    dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col] = new RecordFood[pom.length + 1];
                    for (int i = 0; i < pom.length; i++) {
                        dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][i] = pom[i];
                    }
                    dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][pom.length] = rec;
                }
            }
        }
    }

    @Override
    public int getBaseIndex() {
        return baseIndex;
    }

    @Override
    public void setBaseIndex(int baseIndex) {
        this.baseIndex = baseIndex;
    }

    @Override
    public int compareTo(TableSubModel o) {
        return Integer.valueOf(baseIndex).compareTo(o.getBaseIndex());
    }

    @Override
    public int getRowCount() {
        throw new IllegalStateException("Don't use it.");
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }
}
