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
package org.diabetesdiary.calendar.ui;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import org.diabetesdiary.diary.domain.FoodUnit;
import org.diabetesdiary.diary.domain.RecordInsulinPumpBasal;
import org.diabetesdiary.calendar.ui.recordpanel.RecordActivityEditorPanel;
import org.diabetesdiary.calendar.ui.recordpanel.RecordFoodEditorPanel;
import org.diabetesdiary.calendar.ui.recordpanel.RecordInsulinEditorPanel;
import org.diabetesdiary.calendar.ui.recordpanel.RecordInvestEditorPanel;
import org.diabetesdiary.calendar.utils.DataChangeEvent;
import org.diabetesdiary.calendar.utils.DataChangeListener;
import org.diabetesdiary.diary.domain.Activity;
import org.diabetesdiary.diary.domain.Food;
import org.diabetesdiary.diary.domain.Insulin;
import org.diabetesdiary.diary.domain.RecordActivity;
import org.diabetesdiary.diary.domain.RecordFood;
import org.diabetesdiary.diary.domain.RecordInsulin;
import org.diabetesdiary.diary.domain.RecordInvest;
import org.diabetesdiary.diary.domain.FoodSeason;
import org.diabetesdiary.diary.domain.InsulinSeason;
import org.diabetesdiary.diary.domain.InvSeason;
import org.diabetesdiary.diary.domain.Investigation;
import org.joda.time.DateTime;
import org.openide.ErrorManager;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public final class RecordEditorTopComponent extends TopComponent implements DataChangeListener {

    private static RecordEditorTopComponent instance;
    static final String ICON_PATH = "org/diabetesdiary/calendar/resources/recordedit.png";
    private static final String PREFERRED_ID = "RecordEditorTopComponent";
    private final RecordInsulinEditorPanel insulinPanel = new RecordInsulinEditorPanel();
    private final RecordInvestEditorPanel investPanel = new RecordInvestEditorPanel();
    private final RecordFoodEditorPanel foodPanel = new RecordFoodEditorPanel();
    private final RecordActivityEditorPanel actPanel = new RecordActivityEditorPanel();

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

    public void setFoodComponents(DateTime clickCellDate, Double amount, String notice, Food wellKnownFood, FoodUnit sacharidUnit, FoodSeason foodSeason) {
        foodPanel.setNewRecordFood(clickCellDate, amount, notice, wellKnownFood, sacharidUnit, foodSeason);
        activityPanel.setSelectedComponent(foodPanel);
    }

    public void setInsulinComponents(DateTime date, Double amount, String notice, InsulinSeason season, Insulin insulin, boolean basal) {
        insulinPanel.setNewRecord(date, amount, notice, season, insulin, basal);
        activityPanel.setSelectedComponent(insulinPanel);
    }

    public void setInvestComponents(DateTime date, Double amount, String notice, Investigation invest, InvSeason season) {
        investPanel.setNewRecordFood(date, amount, notice, invest, season);
        activityPanel.setSelectedComponent(investPanel);
    }

    public void setActivityComponents(DateTime date, Integer amount, String notice, Activity act) {
        actPanel.setNewRecord(date, amount, notice, act);
        activityPanel.setSelectedComponent(actPanel);
    }

    public void setRecord(Object record) {
        if (record instanceof RecordInvest[]) {
            investPanel.setRecord((RecordInvest[]) record);
            activityPanel.setSelectedComponent(investPanel);
        } else if (record instanceof RecordInvest) {
            investPanel.setRecord(new RecordInvest[]{(RecordInvest) record});
            activityPanel.setSelectedComponent(investPanel);
        } else if (record instanceof RecordInsulin[]) {
            insulinPanel.setRecord((RecordInsulin[]) record);
            activityPanel.setSelectedComponent(insulinPanel);
        } else if (record instanceof RecordInsulin) {
            insulinPanel.setRecord(new RecordInsulin[]{(RecordInsulin) record});
            activityPanel.setSelectedComponent(insulinPanel);
        } else if (record instanceof RecordActivity[]) {
            RecordActivity[] recs = (RecordActivity[]) record;
            actPanel.setRecord(recs);
            activityPanel.setSelectedComponent(actPanel);
        } else if (record instanceof RecordActivity) {
            actPanel.setRecord(new RecordActivity[]{(RecordActivity) record});
            activityPanel.setSelectedComponent(actPanel);
        } else if (record instanceof RecordFood) {
            foodPanel.setRecord(new RecordFood[]{(RecordFood) record});
            activityPanel.setSelectedComponent(foodPanel);
        } else if (record instanceof RecordFood[]) {
            foodPanel.setRecord((RecordFood[]) record);
            activityPanel.setSelectedComponent(foodPanel);
        } else if (record instanceof RecordInsulinPumpBasal) {
            insulinPanel.setRecord(((RecordInsulinPumpBasal) record).getData());
            activityPanel.setSelectedComponent(insulinPanel);
        }
    }

    private RecordEditorTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(RecordEditorTopComponent.class, "CTL_RecordEditorTopComponent"));
        setToolTipText(NbBundle.getMessage(RecordEditorTopComponent.class, "HINT_RecordEditorTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

        activityPanel.addTab(NbBundle.getMessage(RecordEditorTopComponent.class, "insulin"), insulinPanel);
        activityPanel.addTab(NbBundle.getMessage(RecordEditorTopComponent.class, "investigation"), investPanel);
        activityPanel.addTab(NbBundle.getMessage(RecordEditorTopComponent.class, "food"), foodPanel);
        activityPanel.addTab(NbBundle.getMessage(RecordEditorTopComponent.class, "activity"), actPanel);

        DataChangeListener listener = new DataChangeListener() {

            @Override
            public void onDataChange(DataChangeEvent evt) {
                fireDataChange(evt);
            }
        };
        insulinPanel.addDataChangeListener(listener);
        investPanel.addDataChangeListener(listener);
        foodPanel.addDataChangeListener(listener);
        actPanel.addDataChangeListener(listener);
    }

    /** This method is called from within the constructor to$
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        activityPanel = new javax.swing.JTabbedPane();

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(activityPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(activityPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane activityPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized RecordEditorTopComponent getDefault() {
        if (instance == null) {
            instance = new RecordEditorTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the RecordEditorTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized RecordEditorTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find RecordEditor component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof RecordEditorTopComponent) {
            return (RecordEditorTopComponent) win;
        }
        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    /**
     * This method is called if data are changed outside this Component and i have to update my edit tabs
     * @param evt
     */
    @Override
    public void onDataChange(DataChangeEvent evt) {
        insulinPanel.onDataChange(evt);
        investPanel.onDataChange(evt);
        foodPanel.onDataChange(evt);
        actPanel.onDataChange(evt);
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return RecordEditorTopComponent.getDefault();
        }
    }
}
