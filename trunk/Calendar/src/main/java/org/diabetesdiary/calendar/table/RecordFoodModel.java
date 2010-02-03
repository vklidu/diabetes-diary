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
import java.util.Date;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import org.diabetesdiary.calendar.ColumnGroup;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.calendar.utils.DbLookUp;
import org.diabetesdiary.datamodel.api.FoodAdministrator;
import org.diabetesdiary.datamodel.pojo.FoodSeason;
import org.diabetesdiary.datamodel.pojo.RecordFoodDO;
import org.diabetesdiary.datamodel.pojo.RecordFoodPK;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class RecordFoodModel implements TableSubModel, Comparable<TableSubModel> {

    private RecordFoodDO dataFood[][][];
    private int baseIndex;
    private Calendar month;

    /** Creates a new instance of RecordFoodModel */
    public RecordFoodModel(int baseIndex, Calendar month) {
        this.baseIndex = baseIndex;
        this.month = month;
    }

    public int getColumnCount() {
        return 6;
    }

    public ColumnGroup getColumnHeader(TableColumnModel columnModel) {
        ColumnGroup gFood = new ColumnGroup(NbBundle.getMessage(RecordFoodModel.class, "Column.food"));
        for (int i = baseIndex; i < baseIndex + getColumnCount(); i++) {
            gFood.add(columnModel.getColumn(i));
        }
        return gFood;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (dataFood != null && rowIndex > -1) {
            if (dataFood[rowIndex][columnIndex] != null && dataFood[rowIndex][columnIndex].length == 1) {
                RecordFoodDO res = dataFood[rowIndex][columnIndex][0];
                return res;
            } else {
                RecordFoodDO[] res = dataFood[rowIndex][columnIndex];
                return res;
            }
        }
        return null;
    }

    public Object getNewRecordValueAt(int rowIndex, int columnIndex) {
        RecordFoodDO food = new RecordFoodDO();
        RecordFoodPK pk = new RecordFoodPK();
        pk.setIdPatient(DbLookUp.getDiaryRepo().getCurrentPatient().getIdPatient());
        pk.setIdFood(1);
        pk.setDate(getClickCellDate(rowIndex, columnIndex));
        food.setId(pk);

        food.setSeason(FoodSeason.values()[columnIndex].name());
        FoodAdministrator foodAdmin = DbLookUp.getFoodAdmin();
        food.setFood(foodAdmin.getFood(pk.getIdFood()));
        food.setUnit(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT));
        food.setAmount(0d);
        return food;
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value instanceof Double) {
            RecordFoodDO rec = new RecordFoodDO();
            RecordFoodPK pk = new RecordFoodPK();
            pk.setIdPatient(DbLookUp.getDiaryRepo().getCurrentPatient().getIdPatient());
            pk.setIdFood(1);
            pk.setDate(getClickCellDate(rowIndex, columnIndex));
            rec.setId(pk);
            Double units = (Double) value;
            rec.setAmount(units);
            rec.setTotalAmount(units);
            rec.setUnit(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT));
            rec.setSeason(FoodSeason.values()[columnIndex].name());
            rec.setFood(DbLookUp.getFoodAdmin().getFood(pk.getIdFood()));

            DbLookUp.getDiaryRepo().addRecord(rec);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(rec.getId().getDate().getTime());
            int col = FoodSeason.valueOf(rec.getSeason()).ordinal();
            if (dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] == null) {
                dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] = rec;
            } else {
                if (rec.getId().getIdFood().equals(dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0].getId().getIdFood())) {
                    dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] = rec;
                } else {
                    RecordFoodDO[] pom = dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col];
                    dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col] = new RecordFoodDO[pom.length + 1];
                    dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] = rec;
                    for (int i = 0; i < pom.length; i++) {
                        dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][i + 1] = pom[i];
                    }
                }
            }
        }
    }

    public Class<?> getColumnClass(int columnIndex) {
        return RecordFoodDO.class;
    }

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

    public Date getClickCellDate(int row, int column) {
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
        return cal.getTime();
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public void setData(Collection<?> data) {
        //max number of days
        dataFood = new RecordFoodDO[31][6][1];
        Calendar cal = Calendar.getInstance();
        for (Object record : data) {
            if (record instanceof RecordFoodDO) {
                RecordFoodDO rec = (RecordFoodDO) record;
                cal.setTimeInMillis(rec.getId().getDate().getTime());
                int col = FoodSeason.valueOf(rec.getSeason()).ordinal();

                if (dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] == null) {
                    dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] = rec;
                } else {
                    RecordFoodDO[] pom = dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col];
                    dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col] = new RecordFoodDO[pom.length + 1];
                    for (int i = 0; i < pom.length; i++) {
                        dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][i] = pom[i];
                    }
                    dataFood[cal.get(Calendar.DAY_OF_MONTH) - 1][col][pom.length] = rec;
                }
            }
        }
    }

    public int getBaseIndex() {
        return baseIndex;
    }

    public void setBaseIndex(int baseIndex) {
        this.baseIndex = baseIndex;
    }

    public int compareTo(TableSubModel o) {
        return Integer.valueOf(baseIndex).compareTo(o.getBaseIndex());
    }

    public int getRowCount() {
        throw new IllegalStateException("Don't use it.");
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }
}
