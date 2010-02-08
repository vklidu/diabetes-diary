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
import javax.swing.JTable;
import javax.swing.JToolTip;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import org.diabetesdiary.calendar.CalendarPopupMenu;
import org.diabetesdiary.calendar.table.header.ColumnGroup;
import org.diabetesdiary.calendar.table.model.DiaryTableModel;
import org.diabetesdiary.calendar.table.renderer.GlykemieCellRenderer;
import org.diabetesdiary.calendar.table.header.GroupableTableHeader;
import org.diabetesdiary.calendar.MultiLineToolTip;
import org.diabetesdiary.calendar.action.SelectPatientAction;
import org.diabetesdiary.calendar.table.editor.ActivityCellEditor;
import org.diabetesdiary.calendar.table.renderer.ActivityCellRenderer;
import org.diabetesdiary.calendar.table.CalendarDay;
import org.diabetesdiary.calendar.table.renderer.DayRenderer;
import org.diabetesdiary.calendar.table.editor.FoodCellEditor;
import org.diabetesdiary.calendar.table.renderer.FoodCellRenderer;
import org.diabetesdiary.calendar.table.editor.GlykemieCellEditor;
import org.diabetesdiary.calendar.table.editor.InsulinCellEditor;
import org.diabetesdiary.calendar.table.renderer.InsulinCellRenderer;
import org.diabetesdiary.calendar.table.editor.InsulinPumpBasalEditor;
import org.diabetesdiary.calendar.table.renderer.InsulinPumpBasalRenderer;
import org.diabetesdiary.diary.domain.RecordInsulinPumpBasal;
import org.diabetesdiary.calendar.table.model.SumModel;
import org.diabetesdiary.calendar.table.model.TableSubModel;
import org.diabetesdiary.diary.domain.RecordActivity;
import org.diabetesdiary.diary.domain.RecordFood;
import org.diabetesdiary.diary.domain.RecordInsulin;
import org.diabetesdiary.diary.domain.RecordInvest;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.openide.ErrorManager;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
public final class CalendarTopComponent extends TopComponent
        implements PropertyChangeListener {

    private DiaryTableModel model;
    private static final long serialVersionUID = 1L;
    private static JFormattedTextField.AbstractFormatter monthFormat = new JFormattedTextField.AbstractFormatter() {

        @Override
        public Object stringToValue(String text) throws ParseException {
            return DateTimeFormat.forPattern(NbBundle.getMessage(CalendarTopComponent.class, "Calendar_selectedMonthPattern")).parseDateTime(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            return DateTimeFormat.forPattern(NbBundle.getMessage(CalendarTopComponent.class, "Calendar_selectedMonthPattern")).print((ReadableInstant)value);
        }
    };
    private static CalendarTopComponent instance;
    /** path to the icon used by the component and its open action */
    public static final String ICON_PATH = "org/diabetesdiary/calendar/resources/calendar.png";
    private static final String PREFERRED_ID = "CalendarTopComponent";
    public static String ICON_PATH_SMALL = "org/diabetesdiary/calendar/resources/calendar16.png";

    private CalendarTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(CalendarTopComponent.class, "CTL_CalendarTopComponent"));
        setToolTipText(NbBundle.getMessage(CalendarTopComponent.class, "HINT_CalendarTopComponent"));
        setIcon(Utilities.loadImage(ICON_PATH_SMALL, true));
        recreateTableHeader();

        jTable1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jTable1.setRowSelectionAllowed(true);
        jTable1.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                int row = jTable1.rowAtPoint(e.getPoint());
                int column = jTable1.columnAtPoint(e.getPoint());
                if (e.isPopupTrigger()) {
                    jTable1.changeSelection(row, column, false, false);
                    if (!(getModel().getSubModel(column) instanceof SumModel)) {
                        popupMenu = CalendarPopupMenu.createPopupMenu(getModel().getValueAt(row, column));
                        popupMenu.show(jTable1, e.getX(), e.getY());
                    }
                } else if (MyLookup.getCurrentPatient() != null) {
                    Object record = getModel().getEverRecordValueAt(row, column);
                    RecordEditorTopComponent.getDefault().setRecord(record);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int row = jTable1.rowAtPoint(e.getPoint());
                int column = jTable1.columnAtPoint(e.getPoint());
                //this is doubled, because platform independet popup trigger -> see java api doc
                if (e.isPopupTrigger()) {
                    jTable1.changeSelection(row, column, false, false);
                    if (!(getModel().getSubModel(column) instanceof SumModel)) {
                        popupMenu = CalendarPopupMenu.createPopupMenu(getModel().getValueAt(row, column));
                        popupMenu.show(jTable1, e.getX(), e.getY());
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
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new JTable() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }

            public JToolTip createToolTip() {
                return new MultiLineToolTip();
            }

        };
        jTable1.setDefaultEditor(RecordInvest.class,new GlykemieCellEditor());
        jTable1.setDefaultEditor(RecordActivity.class,new ActivityCellEditor());
        jTable1.setDefaultEditor(RecordFood.class,new FoodCellEditor());
        jTable1.setDefaultEditor(RecordInsulin.class,new InsulinCellEditor());
        jTable1.setDefaultEditor(RecordInsulinPumpBasal.class,new InsulinPumpBasalEditor());

        jTable1.setDefaultRenderer(CalendarDay.class,new DayRenderer());
        jTable1.setDefaultRenderer(RecordInvest.class,new GlykemieCellRenderer());
        jTable1.setDefaultRenderer(RecordFood.class,new FoodCellRenderer());
        jTable1.setDefaultRenderer(RecordActivity.class,new ActivityCellRenderer());
        jTable1.setDefaultRenderer(RecordInsulin.class,new InsulinCellRenderer());
        jTable1.setDefaultRenderer(RecordInsulinPumpBasal.class,new InsulinPumpBasalRenderer());
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

        jTable1.setModel(getModel());
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
                foodVisibleActionPerformed(evt);
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
        foodVisibleActionPerformed(evt);
    }//GEN-LAST:event_sumVisibleActionPerformed

    private void insulinVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insulinVisibleActionPerformed
        foodVisibleActionPerformed(evt);
    }//GEN-LAST:event_insulinVisibleActionPerformed

    private void investVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_investVisibleActionPerformed
        foodVisibleActionPerformed(evt);
    }//GEN-LAST:event_investVisibleActionPerformed

    private void foodVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodVisibleActionPerformed
        getModel().setFoodVisible(foodVisible.isSelected());
        getModel().setGlykemieVisible(investVisible.isSelected());
        getModel().setOtherVisible(otherVisible.isSelected());
        getModel().setInsulinVisible(insulinVisible.isSelected());
        getModel().setActivityVisible(activityVisible.isSelected());
        getModel().setSumVisible(sumVisible.isSelected());
        getModel().refresh();
        recreateTableHeader();
    }//GEN-LAST:event_foodVisibleActionPerformed

    public void recreateTableHeader() {
        TableColumnModel cm = jTable1.getColumnModel();
        GroupableTableHeader header = (GroupableTableHeader) jTable1.getTableHeader();
        header.removeAll();
        for (TableSubModel subModel : getModel().getActiveSubModels()) {
            ColumnGroup group = subModel.getColumnHeader(cm);
            if (group != null) {
                header.addColumnGroup(group);
            }
        }
    }

    private void yearBackwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearBackwardActionPerformed
        getModel().yearBackward();
        selMonth.setValue(getModel().getMonth());
    }//GEN-LAST:event_yearBackwardActionPerformed

    private void yearForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearForwardActionPerformed
        getModel().yearForward();
        selMonth.setValue(getModel().getMonth());
    }//GEN-LAST:event_yearForwardActionPerformed

    private void forwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forwardActionPerformed
        getModel().monthForward();
        selMonth.setValue(getModel().getMonth());
    }//GEN-LAST:event_forwardActionPerformed

    private void backwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backwardActionPerformed
        getModel().monthBackward();
        selMonth.setValue(getModel().getMonth());
    }//GEN-LAST:event_backwardActionPerformed

private void otherVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otherVisibleActionPerformed
    foodVisibleActionPerformed(evt);
}//GEN-LAST:event_otherVisibleActionPerformed

private void activityVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activityVisibleActionPerformed
    foodVisibleActionPerformed(evt);
}//GEN-LAST:event_activityVisibleActionPerformed
    private javax.swing.JPopupMenu popupMenu;
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

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return CalendarTopComponent.getDefault();
        }
    }

    public DiaryTableModel getModel() {
        if (model == null) {
            model = new DiaryTableModel();
        }
        return model;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();
        if (source == selMonth) {
            getModel().setDate((DateTime) selMonth.getValue());
        }
    }
}
