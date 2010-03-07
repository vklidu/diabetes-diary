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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import org.diabetesdiary.calendar.action.SelectPatientAction;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.calendar.table.DiaryTable;
import org.diabetesdiary.calendar.table.model.ActivityModel;
import org.diabetesdiary.calendar.table.model.OtherInvestModel;
import org.diabetesdiary.calendar.table.model.RecordFoodModel;
import org.diabetesdiary.calendar.table.model.RecordInsulinModel;
import org.diabetesdiary.calendar.table.model.RecordInsulinPumpModel;
import org.diabetesdiary.calendar.table.model.RecordInvestModel;
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
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.openide.ErrorManager;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public final class CalendarTopComponent extends TopComponent implements PropertyChangeListener, DataChangeListener {

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
                    } else if (subModel instanceof RecordInvestModel) {
                        RecordInvestModel model = (RecordInvestModel) subModel;
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
        selMonth.addPropertyChangeListener("value", this);
        DateTime pomCal = new DateTime().withTime(0, 0, 0, 0);
        selMonth.setValue(pomCal);
        RecordEditorTopComponent.getDefault().addDataChangeListener(this);
        setCurPatient(null);
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
        backward = new javax.swing.JButton();
        forward = new javax.swing.JButton();
        yearForward = new javax.swing.JButton();
        yearBackward = new javax.swing.JButton();
        selMonth = new JFormattedTextField(monthFormat);
        insulinVisible = new javax.swing.JCheckBox();
        investVisible = new javax.swing.JCheckBox();
        foodVisible = new javax.swing.JCheckBox();
        sumVisible = new javax.swing.JCheckBox();
        otherVisible = new javax.swing.JCheckBox();
        activityVisible = new javax.swing.JCheckBox();

        jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 400));

        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable1.setMinimumSize(new java.awt.Dimension(500, 0));
        jScrollPane1.setViewportView(jTable1);

        org.openide.awt.Mnemonics.setLocalizedText(backward, "<");
        backward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backwardActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(forward, ">");
        forward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forwardActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(yearForward, ">>");
        yearForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yearForwardActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(yearBackward, "<<");
        yearBackward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yearBackwardActionPerformed(evt);
            }
        });

        insulinVisible.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(insulinVisible, NbBundle.getMessage(CalendarTopComponent.class,"insulin")); // NOI18N
        insulinVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        insulinVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insulinVisibleActionPerformed(evt);
            }
        });

        investVisible.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(investVisible, NbBundle.getMessage(CalendarTopComponent.class,"glycemia")); // NOI18N
        investVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        investVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                investVisibleActionPerformed(evt);
            }
        });

        foodVisible.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(foodVisible, NbBundle.getMessage(CalendarTopComponent.class,"food")); // NOI18N
        foodVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        foodVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodViewActionPerformed(evt);
            }
        });

        sumVisible.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(sumVisible, NbBundle.getMessage(CalendarTopComponent.class,"sum")); // NOI18N
        sumVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sumVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumVisibleActionPerformed(evt);
            }
        });

        otherVisible.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(otherVisible, NbBundle.getMessage(CalendarTopComponent.class,"otherInvest")); // NOI18N
        otherVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        otherVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otherVisibleActionPerformed(evt);
            }
        });

        activityVisible.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(activityVisible, NbBundle.getMessage(CalendarTopComponent.class,"activity")); // NOI18N
        activityVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        activityVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activityVisibleActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(yearBackward)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(backward)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(selMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(forward, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(yearForward)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(insulinVisible)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(investVisible)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(otherVisible)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(foodVisible)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(activityVisible)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(sumVisible)
                .add(155, 155, 155))
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 906, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(yearBackward)
                    .add(backward)
                    .add(forward)
                    .add(yearForward)
                    .add(selMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(insulinVisible)
                    .add(investVisible)
                    .add(foodVisible)
                    .add(sumVisible)
                    .add(otherVisible)
                    .add(activityVisible))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
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

    private void yearBackwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearBackwardActionPerformed
        model.yearBackward();
        selMonth.setValue(model.getMonth());
    }//GEN-LAST:event_yearBackwardActionPerformed

    private void yearForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearForwardActionPerformed
        model.yearForward();
        selMonth.setValue(model.getMonth());
    }//GEN-LAST:event_yearForwardActionPerformed

    private void forwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forwardActionPerformed
        model.monthForward();
        selMonth.setValue(model.getMonth());
    }//GEN-LAST:event_forwardActionPerformed

    private void backwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backwardActionPerformed
        model.monthBackward();
        selMonth.setValue(model.getMonth());
    }//GEN-LAST:event_backwardActionPerformed

private void otherVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otherVisibleActionPerformed
    model.setOtherVisible(otherVisible.isSelected());
}//GEN-LAST:event_otherVisibleActionPerformed

private void activityVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activityVisibleActionPerformed
    model.setActivityVisible(activityVisible.isSelected());
}//GEN-LAST:event_activityVisibleActionPerformed

private void foodViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodViewActionPerformed
    model.setFoodVisible(foodVisible.isSelected());
}//GEN-LAST:event_foodViewActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox activityVisible;
    private javax.swing.JButton backward;
    private javax.swing.JCheckBox foodVisible;
    private javax.swing.JButton forward;
    private javax.swing.JCheckBox insulinVisible;
    private javax.swing.JCheckBox investVisible;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JCheckBox otherVisible;
    private javax.swing.JFormattedTextField selMonth;
    private javax.swing.JCheckBox sumVisible;
    private javax.swing.JButton yearBackward;
    private javax.swing.JButton yearForward;
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
    public void componentOpened() {
        if (MyLookup.getCurrentPatient() == null) {
            SelectPatientAction action = (SelectPatientAction) CallableSystemAction.findObject(SelectPatientAction.class);
            if (action != null) {
                action.performAction();
            }
        }
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
            return ret;
        }
    }

    @Override
    public void onDataChange(DataChangeEvent evt) {
        if (model != null) {
            model.reloadData(evt);
        }
    }

    public void setCurPatient(Patient pat) {
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

        selMonth.setEnabled(pat != null);
        backward.setEnabled(pat != null);
        forward.setEnabled(pat != null);
        yearForward.setEnabled(pat != null);
        yearBackward.setEnabled(pat != null);

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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();
        if (source == selMonth && model != null) {
            model.setDate((DateTime) selMonth.getValue());
        }
    }

}
