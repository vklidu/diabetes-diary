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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.calendar.ui.CalendarTopComponent;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;

/**
 *
 * @author Jiri Majer
 */
public class DiaryTableModel extends AbstractTableModelWithSubmodels implements PropertyChangeListener {

    private DateTime dateTime = new DateTime();
    private RecordFoodModel foodModel;
    private RecordInvestModel investModel;
    private TableSubModel insulinModel;
    private OtherInvestModel otherModel;
    private ActivityModel activityModel;
    private DayModel dayModel;
    private SumModel sumModel;    

    public DiaryTableModel() {
        CalendarSettings.getSettings().addPropertyChangeListener(this);
        addModel(dayModel = new DayModel(dateTime));

        if (MyLookup.getCurrentPatient() != null && MyLookup.getCurrentPatient().isPumpUsage()) {
            addModel(insulinModel = new RecordInsulinPumpModel(dateTime));
        } else {
            addModel(insulinModel = new RecordInsulinModel(dateTime));
        }
        addModel(investModel = new RecordInvestModel(dateTime));
        addModel(otherModel = new OtherInvestModel(MyLookup.getCurrentPatient() != null ? MyLookup.getCurrentPatient().isMale() : true, dateTime));
        addModel(foodModel = new RecordFoodModel(dateTime));
        addModel(activityModel = new ActivityModel(dateTime));
        addModel(sumModel = new SumModel(dateTime));

        reloadData();
    }

    @Override
    public int getRowCount() {
        return dateTime.dayOfMonth().withMaximumValue().getDayOfMonth();
    }

    public void refresh() {
        fireTableDataChanged();
        fireTableStructureChanged();
    }

    public void reloadData() {
        //no data => end
        if (MyLookup.getCurrentPatient() == null) {
            return;
        }

        if (MyLookup.getCurrentPatient().isPumpUsage() != insulinModel instanceof RecordInsulinPumpModel) {
            TableSubModel newInsulinModel;
            if (MyLookup.getCurrentPatient().isPumpUsage()) {
                newInsulinModel = new RecordInsulinPumpModel(dateTime);
            } else {
                newInsulinModel = new RecordInsulinModel(dateTime);
            }
            replaceModel(insulinModel, newInsulinModel);
            insulinModel = newInsulinModel;
            refresh();
            CalendarTopComponent.getDefault().recreateTableHeader();
        }

        if (MyLookup.getCurrentPatient().isMale() != otherModel.isMale()) {
            otherModel.setMale(MyLookup.getCurrentPatient().isMale());
            refresh();
            CalendarTopComponent.getDefault().recreateTableHeader();
        }

        invalidateData();
        fireTableDataChanged();
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

        reloadData();
        fireTableDataChanged();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //change in sacharid unit size 10g or 12g per unit
        if (evt.getPropertyName().equals(CalendarSettings.KEY_CARBOHYDRATE_UNIT)) {
            reloadData();
            fireTableDataChanged();
        } else {
            fireTableDataChanged();
        }
    }

    public boolean isGlykemieVisible() {
        return investModel.isVisible();
    }

    public void setGlykemieVisible(boolean glykemieVisible) {
        investModel.setVisible(glykemieVisible);
    }

    public boolean isFoodVisible() {
        return foodModel.isVisible();
    }

    public void setFoodVisible(boolean foodVisible) {
        foodModel.setVisible(foodVisible);
    }

    public boolean isInsulinVisible() {
        return insulinModel.isVisible();
    }

    public void setInsulinVisible(boolean insulinVisible) {
        insulinModel.setVisible(insulinVisible);
    }

    public boolean isSumVisible() {
        return sumModel.isVisible();
    }

    public void setSumVisible(boolean sumVisible) {
        sumModel.setVisible(sumVisible);
    }

    public boolean isOtherVisible() {
        return otherModel.isVisible();
    }

    public void setOtherVisible(boolean otherVisible) {
        otherModel.setVisible(otherVisible);
    }

    public boolean isActivityVisible() {
        return activityModel.isVisible();
    }

    public void setActivityVisible(boolean activityVisible) {
        activityModel.setVisible(activityVisible);
    }
}
