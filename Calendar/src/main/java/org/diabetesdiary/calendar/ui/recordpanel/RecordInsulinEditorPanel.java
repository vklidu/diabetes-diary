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

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.diabetesdiary.calendar.table.model.recordeditor.RecordInsulinEditTableModel;
import org.diabetesdiary.diary.domain.Insulin;
import org.diabetesdiary.diary.domain.InsulinSeason;
import org.diabetesdiary.diary.domain.InsulinType;
import org.diabetesdiary.diary.domain.RecordInsulin;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;
import org.openide.util.NbBundle;

/**
 *
 * @author Jirka Majer
 */
public class RecordInsulinEditorPanel extends AbstractRecordEditorPanel<RecordInsulin> {

    private final RecordInsulinEditTableModel insModel = new RecordInsulinEditTableModel(new DateTime());

    public RecordInsulinEditorPanel() {
        initComponents();
        addDataChangedListener(insModel);
    }

    private ComboBoxModel createInsulinSeasonModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel(InsulinSeason.values());
        return model;
    }

    private ComboBoxModel createInsTypeComboModel() {
        Object[] groups = diary.getInsulinTypes().toArray();
        DefaultComboBoxModel model = new DefaultComboBoxModel(groups);
        return model;
    }

    private Double getAmount() {
        if (amount.getValue() instanceof Double) {
            return (Double) amount.getValue();
        }
        return null;
    }

    @Override
    protected String validateForm() {
        if (getAmount() == null) {
            return NbBundle.getMessage(RecordInsulinEditorPanel.class, "invalidamount");
        }
        return null;
    }

    public void setNewRecord(DateTime date, Double amount, String notice, InsulinSeason season, Insulin insulin, boolean basal) {
        setRecord(null);
        setInsComponents(date, amount, notice, season, insulin, basal);
    }

    private void setInsComponents(DateTime date, Double amount, String notice, InsulinSeason season, Insulin insulin, boolean basal) {
        this.amount.setValue(amount);
        dateTimePanel.setDateTime(date);
        insulinNote.setText(notice);
        insulinUnit.setSelectedItem(InsUnit.getInstance(basal));
        insulinSeason.setSelectedItem(season);
        insulinType.setSelectedItem(insulin.getType());
        this.insulin.setSelectedItem(insulin);
        insModel.setDate(date);
    }


    @Override
    protected void onSetRecord(RecordInsulin[] recs) {
        if (recs != null && recs.length > 0 && recs[0] != null) {
            RecordInsulin rec = recs[0];
            setInsComponents(rec.getDatetime(), rec.getAmount(), rec.getNotice(), rec.getSeason(), rec.getInsulin(), rec.isBasal());
        }
    }

    @Override
    protected RecordInsulin updateRecord(RecordInsulin rec) {
        return rec.update(dateTimePanel.getDateTime(), getInsUnit().isBasal(), getInsulin(), getAmount(), getInsulinSeason(), insulinNote.getText());
    }

    @Override
    protected RecordInsulin loadRecordByFormIfExist() {
        return MyLookup.getCurrentPatient().getRecordInsulin(dateTimePanel.getDateTime(), getInsulin());
    }

    @Override
    protected RecordInsulin saveRecord() {
        return MyLookup.getCurrentPatient().addRecordInsulin(dateTimePanel.getDateTime(), getInsUnit().isBasal(), getInsulin(), getAmount(), getInsulinSeason(), insulinNote.getText());
    }

    private InsUnit getInsUnit() {
        return (InsUnit) insulinUnit.getSelectedItem();
    }

    private enum InsUnit {

        bolus, basal;

        @Override
        public String toString() {
            return this.equals(bolus) ? "Bolus (U)" : "Bazál (U)";
        }

        public static InsUnit getInstance(boolean isBasal) {
            return isBasal ? basal : bolus;
        }

        private boolean isBasal() {
            return this.equals(basal);
        }
    }

    public Insulin getInsulin() {
        return (Insulin) insulin.getSelectedItem();
    }

    public InsulinSeason getInsulinSeason() {
        return (InsulinSeason) insulinSeason.getSelectedItem();
    }

    private ComboBoxModel createInsulinUnitsModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel(InsUnit.values());
        return model;
    }

    private ComboBoxModel createInsulinModel(InsulinType type) {
        Object[] groups = type.getInsulines().toArray();
        DefaultComboBoxModel model = new DefaultComboBoxModel(groups);
        return model;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel22 = new javax.swing.JLabel();
        insulinType = new javax.swing.JComboBox();
        insulin = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        insulinUnit = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        insulinSeason = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        insulinNote = new javax.swing.JTextArea();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        insulinTable = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        amount = new javax.swing.JSpinner();

        jLabel22.setText(NbBundle.getMessage(RecordInsulinEditorPanel.class,"RecordInsulinEditorPanel.jLabel22.text")); // NOI18N

        insulinType.setMaximumRowCount(20);
        insulinType.setModel(createInsTypeComboModel());
        insulinType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insulinTypeActionPerformed(evt);
            }
        });

        insulin.setMaximumRowCount(20);
        insulin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insulinActionPerformed(evt);
            }
        });

        jLabel16.setText(NbBundle.getMessage(RecordInsulinEditorPanel.class,"RecordInsulinEditorPanel.jLabel16.text")); // NOI18N

        jLabel17.setText(NbBundle.getMessage(RecordInsulinEditorPanel.class,"RecordInsulinEditorPanel.jLabel17.text")); // NOI18N

        insulinUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insulinUnitActionPerformed(evt);
            }
        });

        jLabel18.setText(NbBundle.getMessage(RecordInsulinEditorPanel.class, "RecordInsulinEditorPanel.jLabel18.text")); // NOI18N

        jLabel19.setText(NbBundle.getMessage(RecordInsulinEditorPanel.class,"RecordInsulinEditorPanel.jLabel19.text")); // NOI18N

        insulinSeason.setModel(createInsulinSeasonModel());

        jLabel20.setText(NbBundle.getMessage(RecordInsulinEditorPanel.class,"RecordInsulinEditorPanel.jLabel20.text")); // NOI18N

        insulinNote.setColumns(20);
        insulinNote.setRows(5);
        jScrollPane5.setViewportView(insulinNote);

        jLabel23.setText(NbBundle.getMessage(RecordInsulinEditorPanel.class, "RecordInsulinEditorPanel.jLabel23.text")); // NOI18N

        insulinTable.setModel(insModel);
        insulinTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                insulinTableMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(insulinTable);

        jLabel24.setText(org.openide.util.NbBundle.getMessage(RecordInsulinEditorPanel.class, "RecordInsulinEditorPanel.jLabel24.text")); // NOI18N

        amount.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(1.0d), Double.valueOf(0.1d), null, Double.valueOf(1.0d)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                    .addComponent(errorLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel23)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel20)
                            .addGap(10, 10, 10)
                            .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jLabel18)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(insulinType, 0, 163, Short.MAX_VALUE)
                            .addComponent(dateTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(insulin, 0, 163, Short.MAX_VALUE)
                            .addComponent(insulinUnit, 0, 163, Short.MAX_VALUE)
                            .addComponent(insulinSeason, 0, 163, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(errorLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insulinType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insulin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insulinUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(insulinSeason, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(dateTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(deleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void insulinTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insulinTypeActionPerformed
        InsulinType type = (InsulinType) insulinType.getSelectedItem();
        insulin.setModel(createInsulinModel(type));
}//GEN-LAST:event_insulinTypeActionPerformed

    private void insulinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insulinActionPerformed
        insulinUnit.setModel(createInsulinUnitsModel());
}//GEN-LAST:event_insulinActionPerformed

    private void insulinUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insulinUnitActionPerformed
}//GEN-LAST:event_insulinUnitActionPerformed

    private void insulinTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_insulinTableMouseClicked
        int row = insulinTable.rowAtPoint(evt.getPoint());
        int column = insulinTable.columnAtPoint(evt.getPoint());
        RecordInsulin rec = insModel.getRecord(row, column);
        setRecord(new RecordInsulin[]{rec});
        if (column == insModel.getColumnCount() - 1 && rec != null && rec.getAmount() != null) {
            rec.delete();
            setRecord(null);
            insModel.reloadData();
        }
}//GEN-LAST:event_insulinTableMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner amount;
    private javax.swing.JComboBox insulin;
    private javax.swing.JTextArea insulinNote;
    private javax.swing.JComboBox insulinSeason;
    private javax.swing.JTable insulinTable;
    private javax.swing.JComboBox insulinType;
    private javax.swing.JComboBox insulinUnit;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    // End of variables declaration//GEN-END:variables
}
