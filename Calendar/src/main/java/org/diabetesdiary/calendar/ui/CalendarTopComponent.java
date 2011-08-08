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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.JToolTip;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.diabetesdiary.calendar.CalendarPopupMenu;
import org.diabetesdiary.calendar.table.model.DiaryTableModel;
import org.diabetesdiary.calendar.table.header.GroupableTableHeader;
import org.diabetesdiary.calendar.MultiLineToolTip;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.calendar.table.DiaryTable;
import org.diabetesdiary.calendar.table.model.ActivityModel;
import org.diabetesdiary.calendar.table.model.OtherInvestModel;
import org.diabetesdiary.calendar.table.model.RecordFoodModel;
import org.diabetesdiary.calendar.table.model.RecordInsulinModel;
import org.diabetesdiary.calendar.table.model.RecordInsulinPumpModel;
import org.diabetesdiary.calendar.table.model.GlycaemiaModel;
import org.diabetesdiary.calendar.table.model.TableSubModel;
import org.diabetesdiary.calendar.utils.DataChangeEvent;
import org.diabetesdiary.calendar.utils.DataChangeListener;
import org.diabetesdiary.diary.domain.AbstractRecord;
import org.diabetesdiary.diary.domain.InsulinSeason;
import org.diabetesdiary.diary.domain.InvSeason;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.domain.WKFood;
import org.diabetesdiary.diary.domain.WKInvest;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.openide.ErrorManager;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public final class CalendarTopComponent extends TopComponent implements DataChangeListener {

    private DiaryTableModel model;
    private static final long serialVersionUID = 1L;
    private static JFormattedTextField.AbstractFormatter monthFormat = new JFormattedTextField.AbstractFormatter() {

        @Override
        public Object stringToValue(String text) throws ParseException {
            return DateTimeFormat.forPattern(NbBundle.getMessage(CalendarTopComponent.class, "Calendar_selectedMonthPattern")).parseDateTime(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            return DateTimeFormat.forPattern(NbBundle.getMessage(CalendarTopComponent.class, "Calendar_selectedMonthPattern")).print((ReadableInstant) value);
        }
    };
    private static CalendarTopComponent instance;
    public static final String ICON_PATH = "org/diabetesdiary/calendar/resources/calendar.png";
    private static final String PREFERRED_ID = "CalendarTopComponent";
    public static String ICON_PATH_SMALL = "org/diabetesdiary/calendar/resources/calendar16.png";

    private CalendarTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(CalendarTopComponent.class, "CTL_CalendarTopComponent"));
        setToolTipText(NbBundle.getMessage(CalendarTopComponent.class, "HINT_CalendarTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH_SMALL, true));

        jTable1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jTable1.setRowSelectionAllowed(true);
        jTable1.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                int row = jTable1.rowAtPoint(e.getPoint());
                int column = jTable1.columnAtPoint(e.getPoint());

                if (model == null || model.isOutOfRange(row, column)) {
                    return;
                }
                if (e.isPopupTrigger()) {
                    jTable1.changeSelection(row, column, false, false);
                    Object value = model.getValueAt(row, column);
                    if (value instanceof AbstractRecord) {
                        new CalendarPopupMenu((AbstractRecord) value, CalendarTopComponent.this).show(jTable1, e.getX(), e.getY());
                    } else if (value instanceof AbstractRecord[]) {
                        new CalendarPopupMenu((AbstractRecord[]) value, CalendarTopComponent.this).show(jTable1, e.getX(), e.getY());
                    }
                } else if (MyLookup.getCurrentPatient() != null) {
                    Object record = model.getValueAt(row, column);
                    TableSubModel subModel = model.getSubModel(column);
                    RecordEditorTopComponent comp = RecordEditorTopComponent.getDefault();
                    int subCol = model.getIndexInSubModel(column);
                    if (record != null) {
                        comp.setRecord(record);
                    } else if (subModel instanceof RecordFoodModel) {
                        RecordFoodModel model = (RecordFoodModel) subModel;
                        comp.setFoodComponents(model.getClickCellDate(row, subCol), 1d, null,
                                MyLookup.getDiaryRepo().getWellKnownFood(WKFood.SACCHARIDE),
                                MyLookup.getDiaryRepo().getSacharidUnit(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT)),
                                model.getSeason(subCol));
                    } else if (subModel instanceof RecordInsulinModel) {
                        RecordInsulinModel model = (RecordInsulinModel) subModel;
                        comp.setInsulinComponents(model.getClickCellDate(row, subCol), 1d, null, model.getSeason(subCol),
                                model.isBasal(subCol) ? MyLookup.getCurrentPatient().getBasalInsulin() : MyLookup.getCurrentPatient().getBolusInsulin(),
                                model.isBasal(subCol));
                    } else if (subModel instanceof ActivityModel) {
                        ActivityModel model = (ActivityModel) subModel;
                        comp.setActivityComponents(model.getClickCellDate(row, subCol), 1, null, MyLookup.getDiaryRepo().getRandomActivity());
                    } else if (subModel instanceof OtherInvestModel) {
                        OtherInvestModel model = (OtherInvestModel) subModel;
                        comp.setInvestComponents(model.getClickCellDate(row, subCol), 1d, null,
                                MyLookup.getDiaryRepo().getWellKnownInvestigation(model.getClickCellInvestId(row, subCol)), InvSeason.BB);
                    } else if (subModel instanceof GlycaemiaModel) {
                        GlycaemiaModel model = (GlycaemiaModel) subModel;
                        comp.setInvestComponents(model.getClickCellDate(row, subCol), 1d, null,
                                MyLookup.getDiaryRepo().getWellKnownInvestigation(WKInvest.GLYCAEMIA), model.getSeason(subCol));
                    } else if (subModel instanceof RecordInsulinPumpModel) {
                        RecordInsulinPumpModel model = (RecordInsulinPumpModel) subModel;
                        comp.setInsulinComponents(model.getClickCellDate(row, subCol), 1d, null, model.getSeason(subCol),
                                MyLookup.getCurrentPatient().getBasalInsulin(),
                                model.getSeason(subCol) == InsulinSeason.BASAL);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int row = jTable1.rowAtPoint(e.getPoint());
                int column = jTable1.columnAtPoint(e.getPoint());
                //this is doubled, because platform independet popup trigger -> see java api doc
                if (e.isPopupTrigger() && model != null && !model.isOutOfRange(row, column)) {
                    jTable1.changeSelection(row, column, false, false);
                    Object value = model.getValueAt(row, column);
                    if (value instanceof AbstractRecord) {
                        new CalendarPopupMenu((AbstractRecord) value, CalendarTopComponent.this).show(jTable1, e.getX(), e.getY());
                    } else if (value instanceof AbstractRecord[]) {
                        new CalendarPopupMenu((AbstractRecord[]) value, CalendarTopComponent.this).show(jTable1, e.getX(), e.getY());
                    }
                }
            }
        });


        ListSelectionModel rowSM = jTable1.getSelectionModel();
        rowSM.addListSelectionListener(ChartTopComponent.getDefault());
        jTable1.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 400));
        RecordEditorTopComponent.getDefault().addDataChangeListener(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new DiaryTable() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }

            public JToolTip createToolTip() {
                return new MultiLineToolTip();
            }

        };
        jToolBar1 = new javax.swing.JToolBar();
        monthYearPanel1 = new org.diabetesdiary.commons.swing.calendar.MonthYearPanel();
        insulinVisible = new javax.swing.JCheckBox();
        investVisible = new javax.swing.JCheckBox();
        otherVisible = new javax.swing.JCheckBox();
        foodVisible = new javax.swing.JCheckBox();
        activityVisible = new javax.swing.JCheckBox();
        sumVisible = new javax.swing.JCheckBox();

        jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 400));

        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable1.setMinimumSize(new java.awt.Dimension(500, 0));
        jScrollPane1.setViewportView(jTable1);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setBorderPainted(false);
        jToolBar1.setMaximumSize(new java.awt.Dimension(33132, 50));
        jToolBar1.setMinimumSize(new java.awt.Dimension(486, 45));
        jToolBar1.setPreferredSize(new java.awt.Dimension(100, 45));

        monthYearPanel1.setMaximumSize(new java.awt.Dimension(300, 20));
        monthYearPanel1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                monthYearPanel1PropertyChange(evt);
            }
        });
        jToolBar1.add(monthYearPanel1);

        insulinVisible.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(insulinVisible, NbBundle.getMessage(CalendarTopComponent.class,"insulin")); // NOI18N
        insulinVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        insulinVisible.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        insulinVisible.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        insulinVisible.setMaximumSize(new java.awt.Dimension(70, 45));
        insulinVisible.setMinimumSize(new java.awt.Dimension(50, 45));
        insulinVisible.setPreferredSize(new java.awt.Dimension(50, 45));
        insulinVisible.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        insulinVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insulinVisibleActionPerformed(evt);
            }
        });
        jToolBar1.add(insulinVisible);

        investVisible.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(investVisible, NbBundle.getMessage(CalendarTopComponent.class,"glycemia")); // NOI18N
        investVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        investVisible.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        investVisible.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        investVisible.setMaximumSize(new java.awt.Dimension(70, 45));
        investVisible.setMinimumSize(new java.awt.Dimension(50, 45));
        investVisible.setPreferredSize(new java.awt.Dimension(50, 45));
        investVisible.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        investVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                investVisibleActionPerformed(evt);
            }
        });
        jToolBar1.add(investVisible);

        otherVisible.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(otherVisible, NbBundle.getMessage(CalendarTopComponent.class,"otherInvest")); // NOI18N
        otherVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        otherVisible.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        otherVisible.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        otherVisible.setMaximumSize(new java.awt.Dimension(70, 45));
        otherVisible.setMinimumSize(new java.awt.Dimension(50, 45));
        otherVisible.setPreferredSize(new java.awt.Dimension(50, 45));
        otherVisible.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        otherVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otherVisibleActionPerformed(evt);
            }
        });
        jToolBar1.add(otherVisible);

        foodVisible.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(foodVisible, NbBundle.getMessage(CalendarTopComponent.class,"food")); // NOI18N
        foodVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        foodVisible.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        foodVisible.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        foodVisible.setMaximumSize(new java.awt.Dimension(70, 45));
        foodVisible.setMinimumSize(new java.awt.Dimension(50, 45));
        foodVisible.setPreferredSize(new java.awt.Dimension(50, 45));
        foodVisible.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        foodVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodViewActionPerformed(evt);
            }
        });
        jToolBar1.add(foodVisible);

        activityVisible.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(activityVisible, NbBundle.getMessage(CalendarTopComponent.class,"activity")); // NOI18N
        activityVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        activityVisible.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        activityVisible.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        activityVisible.setMaximumSize(new java.awt.Dimension(70, 45));
        activityVisible.setMinimumSize(new java.awt.Dimension(50, 45));
        activityVisible.setPreferredSize(new java.awt.Dimension(50, 45));
        activityVisible.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        activityVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activityVisibleActionPerformed(evt);
            }
        });
        jToolBar1.add(activityVisible);

        sumVisible.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(sumVisible, NbBundle.getMessage(CalendarTopComponent.class,"sum")); // NOI18N
        sumVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sumVisible.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sumVisible.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        sumVisible.setMaximumSize(new java.awt.Dimension(70, 45));
        sumVisible.setMinimumSize(new java.awt.Dimension(50, 45));
        sumVisible.setPreferredSize(new java.awt.Dimension(50, 45));
        sumVisible.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        sumVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumVisibleActionPerformed(evt);
            }
        });
        jToolBar1.add(sumVisible);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 962, Short.MAX_VALUE)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 962, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sumVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumVisibleActionPerformed
        model.setSumVisible(sumVisible.isSelected());
    }//GEN-LAST:event_sumVisibleActionPerformed

    private void insulinVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insulinVisibleActionPerformed
        model.setInsulinVisible(insulinVisible.isSelected());
    }//GEN-LAST:event_insulinVisibleActionPerformed

    private void investVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_investVisibleActionPerformed
        model.setGlykemieVisible(investVisible.isSelected());
    }//GEN-LAST:event_investVisibleActionPerformed

private void otherVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otherVisibleActionPerformed
    model.setOtherVisible(otherVisible.isSelected());
}//GEN-LAST:event_otherVisibleActionPerformed

private void activityVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activityVisibleActionPerformed
    model.setActivityVisible(activityVisible.isSelected());
}//GEN-LAST:event_activityVisibleActionPerformed

private void foodViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodViewActionPerformed
    model.setFoodVisible(foodVisible.isSelected());
}//GEN-LAST:event_foodViewActionPerformed

private void monthYearPanel1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_monthYearPanel1PropertyChange
    if (evt.getPropertyName().equals("localDate")) {
        model.setDate(((LocalDate)evt.getNewValue()).toDateTimeAtCurrentTime());
    }
}//GEN-LAST:event_monthYearPanel1PropertyChange

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox activityVisible;
    private javax.swing.JCheckBox foodVisible;
    private javax.swing.JCheckBox insulinVisible;
    private javax.swing.JCheckBox investVisible;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    private org.diabetesdiary.commons.swing.calendar.MonthYearPanel monthYearPanel1;
    private javax.swing.JCheckBox otherVisible;
    private javax.swing.JCheckBox sumVisible;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized CalendarTopComponent getDefault() {
        if (instance == null) {
            instance = new CalendarTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the CalendarTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized CalendarTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find Calendar component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof CalendarTopComponent) {
            return (CalendarTopComponent) win;
        }
        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentClosed() {
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper(insulinVisible.isSelected(), investVisible.isSelected(), otherVisible.isSelected(),
                foodVisible.isSelected(), activityVisible.isSelected(), sumVisible.isSelected());
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;
        private final boolean insVisible;
        private final boolean glykVisible;
        private final boolean otherVisible;
        private final boolean foodVisible;
        private final boolean actVisible;
        private final boolean sumVisible;

        public ResolvableHelper(boolean insVisible, boolean glykVisible, boolean otherVisible, boolean foodVisible, boolean actVisible, boolean sumVisible) {
            this.insVisible = insVisible;
            this.glykVisible = glykVisible;
            this.otherVisible = otherVisible;
            this.foodVisible = foodVisible;
            this.actVisible = actVisible;
            this.sumVisible = sumVisible;
        }

        public Object readResolve() {
            CalendarTopComponent ret = CalendarTopComponent.getDefault();
            ret.insulinVisible.setSelected(insVisible);
            ret.investVisible.setSelected(glykVisible);
            ret.otherVisible.setSelected(otherVisible);
            ret.foodVisible.setSelected(foodVisible);
            ret.activityVisible.setSelected(actVisible);
            ret.sumVisible.setSelected(sumVisible);
            ret.firePatientChanged();
            return ret;
        }
    }

    @Override
    public void onDataChange(DataChangeEvent evt) {
        if (model != null) {
            model.reloadData(evt);
        }
    }

    public void firePatientChanged() {
        Patient pat = MyLookup.getCurrentPatient();
        if (pat != null) {
            model = new DiaryTableModel(pat, jTable1, insulinVisible.isSelected(),
                    investVisible.isSelected(), otherVisible.isSelected(), foodVisible.isSelected(),
                    activityVisible.isSelected(), sumVisible.isSelected());
            CalendarSettings.getSettings().addPropertyChangeListener(model);
            model.addDataChangeListener(RecordEditorTopComponent.getDefault());
            jTable1.setModel(model);
        } else {
            model = null;
            jTable1.setModel(new DefaultTableModel());
        }

        monthYearPanel1.setEnabled(pat != null);
        activityVisible.setEnabled(pat != null);
        foodVisible.setEnabled(pat != null);
        insulinVisible.setEnabled(pat != null);
        investVisible.setEnabled(pat != null);
        sumVisible.setEnabled(pat != null);
        investVisible.setEnabled(pat != null);
        otherVisible.setEnabled(pat != null);
    }

    public DateTime getDateTime() {
        return model != null ? model.getMonth() : null;
    }

}
