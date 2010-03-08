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

import com.google.common.base.Preconditions;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTable;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.calendar.utils.DataChangeEvent;
import org.diabetesdiary.calendar.utils.DataChangeListener;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.domain.RecordFood;
import org.joda.time.DateTime;

/**
 *
 * @author Jiri Majer
 */
public class DiaryTableModel extends AbstractTableModelWithSubmodels implements PropertyChangeListener {

    private DateTime dateTime = new DateTime();
    private final RecordFoodModel foodModel;
    private final GlycaemiaModel investModel;
    private final AbstractRecordSubModel insulinModel;
    private final OtherInvestModel otherModel;
    private final ActivityModel activityModel;
    private final DayModel dayModel;
    private final SumModel sumModel;

    public DiaryTableModel(Patient patient, JTable jTable) {
        this(patient, jTable, true, true, true, true, true, true);
    }

    public DiaryTableModel(Patient patient, JTable jTable, boolean insVisible, boolean investVisible, boolean otherInvestVisible, boolean foodVisible, boolean actVisible, boolean sumVisible) {
        super(jTable);
        Preconditions.checkNotNull(patient);

        DataChangeListener listener = new DataChangeListener() {
            //Listen on all submodels and by some change send message to all
            @Override
            public void onDataChange(DataChangeEvent evt) {
                reloadData(evt);
            }
        };
        dayModel = new DayModel(dateTime, patient);
        dayModel.addDataChangeListener(listener);

        if (patient.isPumpUsage()) {
            insulinModel = new RecordInsulinPumpModel(dateTime, patient);
        } else {
            insulinModel = new RecordInsulinModel(dateTime, patient);
        }
        insulinModel.setVisible(insVisible);
        insulinModel.addDataChangeListener(listener);

        investModel = new GlycaemiaModel(dateTime, patient);
        investModel.addDataChangeListener(listener);
        investModel.setVisible(investVisible);

        otherModel = new OtherInvestModel(dateTime, patient);
        otherModel.addDataChangeListener(listener);
        otherModel.setVisible(otherInvestVisible);

        foodModel = new RecordFoodModel(dateTime, patient);
        foodModel.addDataChangeListener(listener);
        foodModel.setVisible(foodVisible);

        activityModel = new ActivityModel(dateTime, patient);
        activityModel.addDataChangeListener(listener);
        activityModel.setVisible(actVisible);

        sumModel = new SumModel(dateTime, patient);
        sumModel.addDataChangeListener(listener);
        sumModel.setVisible(sumVisible);

        addModel(dayModel);
        addModel(insulinModel);
        addModel(investModel);
        addModel(otherModel);
        addModel(foodModel);
        addModel(activityModel);
        addModel(sumModel);
    }

    @Override
    public int getRowCount() {
        return dateTime.dayOfMonth().withMaximumValue().getDayOfMonth();
    }

    public void reloadData(DataChangeEvent evt) {
        invalidateData(evt);
        fireTableDataChanged();
        fireDataChange(evt);
    }

    public void addDataChangeListener(DataChangeListener listener) {
        listenerList.add(DataChangeListener.class, Preconditions.checkNotNull(listener));
    }

    public void removeDataChangeListener(DataChangeListener listener) {
        listenerList.remove(DataChangeListener.class, listener);
    }

    protected void fireDataChange(DataChangeEvent evt) {
        for (DataChangeListener list : listenerList.getListeners(DataChangeListener.class)) {
            list.onDataChange(evt);
        }
    }

    public void monthForward() {
        setDate(dateTime.plusMonths(1));
    }

    public void monthBackward() {
        setDate(dateTime.minusMonths(1));
    }

    public void yearForward() {
        setDate(dateTime.plusYears(1));
    }

    public void yearBackward() {
        setDate(dateTime.minusYears(1));
    }

    public DateTime getMonth() {
        return dateTime;
    }

    public void setDate(DateTime date) {
        if (date != null) {
            dateTime = date;
        }
        dayModel.setDate(dateTime);
        insulinModel.setDate(dateTime);
        investModel.setDate(dateTime);
        otherModel.setDate(dateTime);
        foodModel.setDate(dateTime);
        activityModel.setDate(dateTime);
        sumModel.setDate(dateTime);

        reloadData(new DataChangeEvent(this));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //change in sacharid unit size 10g or 12g per unit
        if (evt.getPropertyName().equals(CalendarSettings.KEY_CARBOHYDRATE_UNIT)) {
            reloadData(new DataChangeEvent(this, RecordFood.class));
        }
    }

    public void setGlykemieVisible(boolean glykemieVisible) {
        investModel.setVisible(glykemieVisible);
        fireTableStructureChanged();
    }

    public void setFoodVisible(boolean foodVisible) {
        foodModel.setVisible(foodVisible);
        fireTableStructureChanged();
    }

    public void setInsulinVisible(boolean insulinVisible) {
        insulinModel.setVisible(insulinVisible);
        fireTableStructureChanged();
    }

    public void setSumVisible(boolean sumVisible) {
        sumModel.setVisible(sumVisible);
        fireTableStructureChanged();
    }

    public void setOtherVisible(boolean otherVisible) {
        otherModel.setVisible(otherVisible);
        fireTableStructureChanged();
    }

    public void setActivityVisible(boolean activityVisible) {
        activityModel.setVisible(activityVisible);
        fireTableStructureChanged();
    }

    public boolean isOutOfRange(int row, int column) {
        return row < 0 || column < 0 || row >= getRowCount() || column >= getColumnCount();
    }

}
