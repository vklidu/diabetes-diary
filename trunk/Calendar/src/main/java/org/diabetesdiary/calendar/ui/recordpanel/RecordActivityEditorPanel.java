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
package org.diabetesdiary.calendar.ui.recordpanel;

import java.util.Arrays;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.diabetesdiary.calendar.table.model.recordeditor.RecordActivityEditTableModel;
import org.diabetesdiary.calendar.ui.RecordEditorTopComponent;
import org.diabetesdiary.diary.domain.Activity;
import org.diabetesdiary.diary.domain.ActivityGroup;
import org.diabetesdiary.diary.domain.RecordActivity;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;
import org.openide.util.NbBundle;

/**
 *
 * @author Jirka Majer
 */
public class RecordActivityEditorPanel extends AbstractRecordEditorPanel<RecordActivity> {

    private final RecordActivityEditTableModel actModel = new RecordActivityEditTableModel(new DateTime());

    public RecordActivityEditorPanel() {
        initComponents();
        addDataChangedListener(actModel);
    }

    private ComboBoxModel createActGroupModel() {
        Object[] groups = diary.getActivityGroups().toArray();
        Arrays.sort(groups);
        DefaultComboBoxModel model = new DefaultComboBoxModel(groups);
        return model;
    }

    private ComboBoxModel createActComboModel(ActivityGroup group) {
        Object[] groups = group.getActivities().toArray();
        Arrays.sort(groups);
        DefaultComboBoxModel model = new DefaultComboBoxModel(groups);
        return model;
    }


    private Activity getActivity() {
        return (Activity) activity.getSelectedItem();
    }

    public Integer getActValue() {
        if (actValue.getValue() instanceof Number) {
            return ((Number) actValue.getValue()).intValue();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        actValue = new javax.swing.JSpinner();
        jLabel30 = new javax.swing.JLabel();
        actType = new javax.swing.JComboBox();
        activity = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        actNote = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        actTable = new javax.swing.JTable();
        jLabel32 = new javax.swing.JLabel();

        actValue.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(1.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        jLabel30.setText(NbBundle.getMessage(RecordActivityEditorPanel.class,"RecordActivityEditorPanel.jLabel30.text")); // NOI18N

        actType.setMaximumRowCount(20);
        actType.setModel(createActGroupModel());
        actType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actTypeActionPerformed(evt);
            }
        });

        activity.setMaximumRowCount(20);
        activity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activityActionPerformed(evt);
            }
        });

        jLabel25.setText(NbBundle.getMessage(RecordActivityEditorPanel.class,"RecordActivityEditorPanel.jLabel25.text")); // NOI18N

        jLabel27.setText(NbBundle.getMessage(RecordActivityEditorPanel.class, "RecordActivityEditorPanel.jLabel27.text")); // NOI18N

        jLabel29.setText(NbBundle.getMessage(RecordActivityEditorPanel.class,"RecordActivityEditorPanel.jLabel29.text")); // NOI18N

        jLabel31.setText(NbBundle.getMessage(RecordActivityEditorPanel.class, "RecordActivityEditorPanel.jLabel31.text")); // NOI18N

        actNote.setColumns(20);
        actNote.setRows(5);
        jScrollPane7.setViewportView(actNote);

        actTable.setModel(actModel);
        actTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                actTableMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(actTable);

        jLabel32.setText(org.openide.util.NbBundle.getMessage(RecordActivityEditorPanel.class, "RecordActivityEditorPanel.jLabel32.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(errorLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27)
                            .addComponent(jLabel30)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(dateTimePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(actValue, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                        .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))))
                            .addComponent(actType, 0, 166, Short.MAX_VALUE)
                            .addComponent(activity, javax.swing.GroupLayout.Alignment.TRAILING, 0, 166, Short.MAX_VALUE)))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(errorLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(actType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(activity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel27)
                    .addComponent(dateTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(actValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(saveButton))
                .addGap(4, 4, 4)
                .addComponent(deleteButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel31)
                    .addComponent(cancelButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void actTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actTypeActionPerformed
        activity.setModel(createActComboModel((ActivityGroup) actType.getSelectedItem()));
}//GEN-LAST:event_actTypeActionPerformed

    private void activityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activityActionPerformed

}//GEN-LAST:event_activityActionPerformed

    private void actTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_actTableMouseClicked
        int row = actTable.rowAtPoint(evt.getPoint());
        int column = actTable.columnAtPoint(evt.getPoint());
        RecordActivity rec = actModel.getRecord(row, column);
        setRecord(new RecordActivity[]{rec});
        if (column == actModel.getColumnCount() - 1 && rec != null && rec.getDuration() != null) {
            rec.delete();
            setRecord(null);
            actModel.reloadData();
        }
}//GEN-LAST:event_actTableMouseClicked

    public void setNewRecord(DateTime date, Integer amount, String notice, Activity act) {
        setRecord(null);
        setComponents(date, amount, notice, act);
    }

    private void setComponents(DateTime date, Integer amount, String notice, Activity act) {
        actValue.setValue(amount);
        dateTimePanel.setDateTime(date);
        actNote.setText(notice);
        actType.setSelectedItem(act.getActivityGroup());
        activity.setSelectedItem(act);

        actModel.setDate(date);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea actNote;
    private javax.swing.JTable actTable;
    private javax.swing.JComboBox actType;
    private javax.swing.JSpinner actValue;
    private javax.swing.JComboBox activity;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    // End of variables declaration//GEN-END:variables

    @Override
    protected String validateForm() {
        if (getActivity() == null) {
            return NbBundle.getMessage(RecordEditorTopComponent.class, "activity.notdefined");
        }
        if (getActValue() == null) {
            return NbBundle.getMessage(RecordEditorTopComponent.class, "invalidamount");
        }
        return null;
    }


    @Override
    protected void onSetRecord(RecordActivity[] recs) {
        if (recs != null && recs.length > 0 && recs[0] != null) {
            RecordActivity rec = recs[0];
            setComponents(rec.getDatetime(), rec.getDuration(), rec.getNotice(), rec.getActivity());
        }
    }

    @Override
    protected RecordActivity loadRecordByFormIfExist() {
        return MyLookup.getCurrentPatient().getRecordActivity(dateTimePanel.getDateTime(), getActivity());
    }

    @Override
    protected RecordActivity saveRecord() {
        return MyLookup.getCurrentPatient().addRecordActivity(dateTimePanel.getDateTime(), getActivity(), getActValue(), actNote.getText());
    }

    @Override
    protected RecordActivity updateRecord(RecordActivity rec) {
        return rec.update(dateTimePanel.getDateTime(), getActivity(), getActValue(), actNote.getText());
    }

}
