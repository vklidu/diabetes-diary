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

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.diabetesdiary.calendar.table.header.ColumnGroup;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.calendar.table.editor.NumberEditor;
import org.diabetesdiary.calendar.table.renderer.FoodCellRenderer;
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
public class RecordFoodModel extends AbstractRecordSubModel {

    private RecordFood dataFood[][][];

    public RecordFoodModel(DateTime month) {
        super(month);
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public ColumnGroup getColumnHeader(List<TableColumn> cols) {
        ColumnGroup gFood = new ColumnGroup(NbBundle.getMessage(RecordFoodModel.class, "Column.food"));
        for (int i = 0; i < getColumnCount(); i++) {
            gFood.add(cols.get(i));
        }
        return gFood;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (dataFood == null) {
            setData();
        }
        if (dataFood != null && rowIndex > -1) {
            if (dataFood[rowIndex][columnIndex] != null && dataFood[rowIndex][columnIndex].length == 1) {
                return dataFood[rowIndex][columnIndex][0];
            } else {
                return dataFood[rowIndex][columnIndex];
            }
        }
        return null;
    }

    public FoodSeason getSeason(int columnIndex) {
        return FoodSeason.values()[columnIndex];
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value instanceof Double) {
            Food food = MyLookup.getDiaryRepo().getWellKnownFood(WKFood.SACCHARIDE);
            FoodUnit unit = MyLookup.getDiaryRepo().getSacharidUnit(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT));
            DateTime recDateTime = getClickCellDate(rowIndex, columnIndex);
            int col = FoodSeason.values()[columnIndex].ordinal();
            RecordFood edited = dataFood[recDateTime.getDayOfMonth() - 1][col][0];
            if (edited != null) {
                edited = edited.update((Double) value);
            } else {
                edited = MyLookup.getCurrentPatient().addRecordFood(getClickCellDate(rowIndex, columnIndex),
                        food, (Double) value, (Double) value, unit, getSeason(columnIndex), null);
            }

            dataFood[recDateTime.getDayOfMonth() - 1][col][0] = edited;
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
        int hourOfDay;
        switch (column) {
            case 0:
                hourOfDay = 7;
                break;
            case 1:
                hourOfDay = 10;
                break;
            case 2:
                hourOfDay = 12;
                break;
            case 3:
                hourOfDay = 15;
                break;
            case 4:
                hourOfDay = 17;
                break;
            case 5:
                hourOfDay = 20;
                break;
            default:
                throw new IllegalStateException();
        }
        return dateTime.withDayOfMonth(row + 1).withTime(hourOfDay, 0, 0, 0);
    }

    private void setData() {
        Collection<RecordFood> data = MyLookup.getCurrentPatient().getRecordFoods(getFrom(), getTo());
        //max number of days
        dataFood = new RecordFood[31][6][1];
        Calendar cal = Calendar.getInstance();
        for (RecordFood rec : data) {
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

    @Override
    public void invalidateData() {
        dataFood = null;
    }

    @Override
    public TableCellRenderer getCellRenderer(int columnIndex) {
        return new FoodCellRenderer();
    }

    @Override
    public TableCellEditor getCellEditor(int columnIndex) {
        return new NumberEditor<Double, Object>(0d, 50d) {

            @Override
            public Double getValue(Object object) {
                if (object instanceof RecordFood) {
                    return ((RecordFood) object).getAmount();
                } else if (object instanceof RecordFood[]) {
                    return ((RecordFood[]) object)[0].getAmount();
                }
                return null;
            }
        };
    }
}
