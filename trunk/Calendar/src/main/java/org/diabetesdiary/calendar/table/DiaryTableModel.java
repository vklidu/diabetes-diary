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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.calendar.ui.CalendarTopComponent;
import org.diabetesdiary.calendar.ui.RecordEditorTopComponent;
import org.diabetesdiary.datamodel.api.DbLookUp;
import org.diabetesdiary.datamodel.api.Diary;
import org.diabetesdiary.datamodel.pojo.RecordActivity;
import org.diabetesdiary.datamodel.pojo.RecordFood;
import org.diabetesdiary.datamodel.pojo.RecordInsulin;
import org.diabetesdiary.datamodel.pojo.RecordInvest;

/**
 *
 * @author Jiri Majer
 */
public class DiaryTableModel extends AbstractTableModel implements PropertyChangeListener {

    private boolean glykemieVisible = true;
    private boolean otherVisible = true;
    private boolean foodVisible = true;
    private boolean insulinVisible = true;
    private boolean sumVisible = true;
    private boolean activityVisible = true;
    private TableSubModel foodModel;
    private TableSubModel investModel;
    private TableSubModel insulinModel;
    private TableSubModel otherModel;
    private TableSubModel activityModel;
    private DayModel dayModel;
    private SumModel sumModel;
    private List<TableSubModel> activeModels;
    private Calendar month;
    private Diary diary;

    /** Creates a new instance of CalendarTableModel */
    public DiaryTableModel() {
        CalendarSettings.getSettings().addPropertyChangeListener(this);
        month = Calendar.getInstance();
        month.set(Calendar.HOUR_OF_DAY, 0);
        month.set(Calendar.MINUTE, 0);
        month.set(Calendar.SECOND, 0);
        month.set(Calendar.MILLISECOND, 0);
        month.set(Calendar.DAY_OF_MONTH, 1);

        diary = DbLookUp.getDiary();
        dayModel = new DayModel(month);

        if (diary.getCurrentPatient() != null && diary.getCurrentPatient().isPumpUsage()) {
            insulinModel = new RecordInsulinPumpModel(dayModel.getBaseIndex() + dayModel.getColumnCount(), month);
        } else {
            insulinModel = new RecordInsulinModel(dayModel.getBaseIndex() + dayModel.getColumnCount(), month);
        }
        investModel = new RecordInvestModel(insulinModel.getBaseIndex() + insulinModel.getColumnCount(), month);
        otherModel = new OtherInvestModel(investModel.getBaseIndex() + investModel.getColumnCount(), diary.getCurrentPatient() != null ? diary.getCurrentPatient().isMale() : true, month);
        foodModel = new RecordFoodModel(otherModel.getBaseIndex() + otherModel.getColumnCount(), month);
        activityModel = new ActivityModel(foodModel.getBaseIndex() + foodModel.getColumnCount(), month, foodModel, otherModel);
        sumModel = new SumModel(activityModel.getBaseIndex() + activityModel.getColumnCount(), foodModel, insulinModel, otherModel);

        activeModels = new LinkedList<TableSubModel>();
        activeModels.add(dayModel);
        activeModels.add(insulinModel);
        activeModels.add(investModel);
        activeModels.add(otherModel);
        activeModels.add(foodModel);
        activeModels.add(activityModel);
        activeModels.add(sumModel);

        fillData();
    }

    public TableSubModel getSubModel(int column) {
        for (TableSubModel model : activeModels) {
            if (column >= model.getBaseIndex() && column <= model.getColumnCount() + model.getBaseIndex() - 1) {
                return model;
            }
        }
        return null;
    }

    public void fillData() {
        Calendar aktual = Calendar.getInstance();
        aktual.setTimeInMillis(month.getTimeInMillis());

        //no data => end
        if (getDiary().getCurrentPatient() == null) {
            return;
        }

        if (diary.getCurrentPatient().isPumpUsage() != insulinModel instanceof RecordInsulinPumpModel) {
            activeModels.remove(insulinModel);
            activeModels.remove(sumModel);
            if (diary.getCurrentPatient().isPumpUsage()) {
                insulinModel = new RecordInsulinPumpModel(insulinModel.getBaseIndex(), month);
                sumModel = new SumModel(sumModel.getBaseIndex(), foodModel, insulinModel, otherModel);
            } else {
                insulinModel = new RecordInsulinModel(insulinModel.getBaseIndex(), month);
                sumModel = new SumModel(sumModel.getBaseIndex(), foodModel, insulinModel, otherModel);
            }
            activeModels.add(insulinModel);
            activeModels.add(sumModel);
            refresh();
            CalendarTopComponent.getDefault().recreateTableHeader();
        }

        if (diary.getCurrentPatient().isMale() != ((OtherInvestModel) otherModel).isMale()) {
            activeModels.remove(otherModel);
            activeModels.remove(sumModel);
            activeModels.remove(activityModel);
            otherModel = new OtherInvestModel(investModel.getBaseIndex() + investModel.getColumnCount(), diary.getCurrentPatient().isMale(), month);
            activityModel = new ActivityModel(foodModel.getBaseIndex() + foodModel.getColumnCount(), month, foodModel, otherModel);
            sumModel = new SumModel(sumModel.getBaseIndex(), foodModel, insulinModel, otherModel);
            activeModels.add(otherModel);
            activeModels.add(sumModel);
            activeModels.add(activityModel);
            refresh();
            CalendarTopComponent.getDefault().recreateTableHeader();
        }


        aktual.set(Calendar.DAY_OF_MONTH, 1);
        aktual.set(Calendar.HOUR_OF_DAY, 0);
        aktual.set(Calendar.MINUTE, 0);
        aktual.set(Calendar.SECOND, 0);
        Date from = aktual.getTime();
        aktual.set(Calendar.DAY_OF_MONTH, aktual.getActualMaximum(Calendar.DAY_OF_MONTH));
        aktual.set(Calendar.HOUR_OF_DAY, 24);
        Date to = aktual.getTime();


        List<RecordInvest> records = getDiary().getRecordInvests(from, to, getDiary().getCurrentPatient().getIdPatient());
        investModel.setData(records);
        otherModel.setData(records);

        List<RecordActivity> recordActs = getDiary().getRecordActivities(from, to, getDiary().getCurrentPatient().getIdPatient());        
        activityModel.setData(recordActs);

        List<RecordFood> recordFoods = getDiary().getRecordFoods(from, to, getDiary().getCurrentPatient().getIdPatient());
        foodModel.setData(recordFoods);


        List<RecordInsulin> recordIns = diary.getRecordInsulins(from, to, diary.getCurrentPatient().getIdPatient());
        insulinModel.setData(recordIns);

        RecordEditorTopComponent.getDefault().getFoodModel().fillData();
        RecordEditorTopComponent.getDefault().getFoodModel().fireTableDataChanged();
    }

    public void monthForward() {
        month.add(Calendar.MONTH, 1);
        fillData();
        fireTableDataChanged();
    }

    public void monthBackward() {
        month.add(Calendar.MONTH, -1);
        fillData();
        fireTableDataChanged();
    }

    public void yearForward() {
        month.add(Calendar.YEAR, 1);
        fillData();
        fireTableDataChanged();
    }

    public void yearBackward() {
        month.add(Calendar.YEAR, -1);
        fillData();
        fireTableDataChanged();
    }

    public int getRowCount() {
        return getMonth().getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public int getColumnCount() {
        int sum = 0;
        for (TableSubModel model : getActiveSubModels()) {
            sum += model.getColumnCount();
        }
        return sum;
    }

    public Object getEverRecordValueAt(int rowIndex, int columnIndex) {
        TableSubModel sub = getSubModel(columnIndex);
        Object result = null;
        if (sub != null) {
            result = sub.getValueAt(rowIndex, columnIndex - sub.getBaseIndex());
            if (result == null) {
                result = sub.getNewRecordValueAt(rowIndex, columnIndex - sub.getBaseIndex());
            }
        }

        return result;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        TableSubModel sub = getSubModel(columnIndex);
        if (sub != null) {
            return sub.getValueAt(rowIndex, columnIndex - sub.getBaseIndex());
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        TableSubModel sub = getSubModel(columnIndex);
        if (sub != null) {
            return sub.isCellEditable(rowIndex, columnIndex - sub.getBaseIndex());
        }
        return false;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (getDiary().getCurrentPatient() == null) {
            return;
        }
        TableSubModel sub = getSubModel(columnIndex);
        if (sub != null) {
            sub.setValueAt(value, rowIndex, columnIndex - sub.getBaseIndex());
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        TableSubModel sub = getSubModel(columnIndex);
        if (sub != null) {
            return sub.getColumnName(columnIndex - sub.getBaseIndex());
        }
        return "";
    }

    public Calendar getMonth() {
        return month;
    }

    public void setDate(Date date) {
        if (date != null) {
            month.setTimeInMillis(date.getTime());
        }
        fillData();
        fireTableDataChanged();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        TableSubModel sub = getSubModel(columnIndex);
        if (sub != null) {
            return sub.getColumnClass(columnIndex - sub.getBaseIndex());
        }
        return Object.class;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        //change in sacharid unit size 10g or 12g per unit
        if (evt.getPropertyName().equals(CalendarSettings.KEY_CARBOHYDRATE_UNIT)) {
            fillData();
            fireTableDataChanged();
        } else {
            fireTableDataChanged();
        }
    }

    public List<TableSubModel> getActiveSubModels() {
        return activeModels;
    }

    public boolean isGlykemieVisible() {
        return glykemieVisible;
    }

    public void setGlykemieVisible(boolean glykemieVisible) {
        this.glykemieVisible = glykemieVisible;
        if (glykemieVisible) {
            if (!activeModels.contains(investModel)) {
                activeModels.add(investModel);
            }
        } else {
            activeModels.remove(investModel);
        }
    }

    public boolean isFoodVisible() {
        return foodVisible;
    }

    public void setFoodVisible(boolean foodVisible) {
        this.foodVisible = foodVisible;
        if (foodVisible) {
            if (!activeModels.contains(foodModel)) {
                activeModels.add(foodModel);
            }
        } else {
            activeModels.remove(foodModel);
        }
    }

    public boolean isInsulinVisible() {
        return insulinVisible;
    }

    public void setInsulinVisible(boolean insulinVisible) {
        this.insulinVisible = insulinVisible;
        if (insulinVisible) {
            if (!activeModels.contains(insulinModel)) {
                activeModels.add(insulinModel);
            }
        } else {
            activeModels.remove(insulinModel);
        }
    }

    public boolean isSumVisible() {
        return sumVisible;
    }

    public void setSumVisible(boolean sumVisible) {
        this.sumVisible = sumVisible;
        if (sumVisible) {
            if (!activeModels.contains(sumModel)) {
                activeModels.add(sumModel);
            }
        } else {
            activeModels.remove(sumModel);
        }
    }

    public Diary getDiary() {
        return diary;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
    }

    public void refresh() {
        int aktual = 1;

        if (insulinVisible) {
            insulinModel.setBaseIndex(aktual);
            aktual = insulinModel.getBaseIndex() + insulinModel.getColumnCount();
        }

        if (glykemieVisible) {
            investModel.setBaseIndex(aktual);
            aktual = investModel.getBaseIndex() + investModel.getColumnCount();
        }

        if (otherVisible) {
            otherModel.setBaseIndex(aktual);
            aktual = otherModel.getBaseIndex() + otherModel.getColumnCount();
        }

        if (foodVisible) {
            foodModel.setBaseIndex(aktual);
            aktual = foodModel.getBaseIndex() + foodModel.getColumnCount();
        }

        if (activityVisible) {
            activityModel.setBaseIndex(aktual);
            aktual = activityModel.getBaseIndex() + activityModel.getColumnCount();
        }

        if (sumVisible) {
            sumModel.setBaseIndex(aktual);
        }


        fireTableDataChanged();
        fireTableStructureChanged();
    }

    public boolean isOtherVisible() {
        return otherVisible;
    }

    public void setOtherVisible(boolean otherVisible) {
        this.otherVisible = otherVisible;
        if (otherVisible) {
            if (!activeModels.contains(otherModel)) {
                activeModels.add(otherModel);
            }
        } else {
            activeModels.remove(otherModel);
        }

    }

    public boolean isActivityVisible() {
        return activityVisible;
    }

    public void setActivityVisible(boolean activityVisible) {
        this.activityVisible = activityVisible;
        if (activityVisible) {
            if (!activeModels.contains(activityModel)) {
                activeModels.add(activityModel);
            }
        } else {
            activeModels.remove(activityModel);
        }
    }
}
