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
import org.diabetesdiary.calendar.table.model.recordeditor.RecordInvestEditTableModel;
import org.diabetesdiary.calendar.utils.DataChangeEvent;
import org.diabetesdiary.diary.domain.InvSeason;
import org.diabetesdiary.diary.domain.Investigation;
import org.diabetesdiary.diary.domain.RecordInvest;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;
import org.openide.util.NbBundle;

/**
 *
 * @author Jirka Majer
 */
public class RecordInvestEditorPanel extends AbstractRecordEditorPanel<RecordInvest> {

    private final RecordInvestEditTableModel invModel = new RecordInvestEditTableModel(new DateTime());

    public RecordInvestEditorPanel() {
        initComponents();
        addDataChangeListener(invModel);
    }

    private ComboBoxModel createSeasonModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel(InvSeason.values());
        return model;
    }

    private ComboBoxModel createInvestModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel(diary.getInvestigations().toArray());
        return model;
    }

    private ComboBoxModel createUnitsModel(Investigation inv) {
        DefaultComboBoxModel model = new DefaultComboBoxModel(new Object[]{inv.getUnit()});
        return model;
    }

    public Investigation getInvest() {
        return (Investigation) invest.getSelectedItem();
    }

    public String getInvestUnit() {
        return investUnit.getSelectedItem().toString();
    }

    public InvSeason getInvestSeason() {
        return (InvSeason) glykSeason.getSelectedItem();
    }

    public Double getInvestValue() {
        if (invValue.getValue() instanceof Number) {
            return ((Number) invValue.getValue()).doubleValue();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        invValue = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        invest = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        investUnit = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        glykSeason = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        investNote = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        investTable = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();

        invValue.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(1.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        jLabel1.setText(NbBundle.getMessage(RecordInvestEditorPanel.class,"RecordInvestEditorPanel.jLabel1.text")); // NOI18N

        invest.setModel(createInvestModel());
        invest.setMinimumSize(new java.awt.Dimension(10, 22));
        invest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                investActionPerformed(evt);
            }
        });

        jLabel2.setText(NbBundle.getMessage(RecordInvestEditorPanel.class,"RecordInvestEditorPanel.jLabel2.text")); // NOI18N

        investUnit.setMinimumSize(new java.awt.Dimension(5, 22));

        jLabel3.setText(NbBundle.getMessage(RecordInvestEditorPanel.class, "RecordInvestEditorPanel.jLabel3.text")); // NOI18N

        jLabel5.setText(NbBundle.getMessage(RecordInvestEditorPanel.class,"RecordInvestEditorPanel.jLabel5.text")); // NOI18N

        glykSeason.setModel(createSeasonModel());

        jLabel6.setText(NbBundle.getMessage(RecordInvestEditorPanel.class,"RecordInvestEditorPanel.jLabel6.text")); // NOI18N

        jLabel4.setText(NbBundle.getMessage(RecordInvestEditorPanel.class, "RecordInvestEditorPanel.jLabel4.text")); // NOI18N

        investNote.setColumns(20);
        investNote.setRows(5);
        jScrollPane1.setViewportView(investNote);

        investTable.setModel(invModel);
        investTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                investTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(investTable);

        jLabel21.setText(org.openide.util.NbBundle.getMessage(RecordInvestEditorPanel.class, "RecordInvestEditorPanel.jLabel21.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel21))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(errorLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(invest, 0, 163, Short.MAX_VALUE)
                            .addComponent(investUnit, 0, 163, Short.MAX_VALUE)
                            .addComponent(glykSeason, 0, 163, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(invValue, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(errorLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(invest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(investUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(glykSeason, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(invValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveButton)
                    .addComponent(jLabel6))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(deleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void investActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_investActionPerformed
        Investigation inv = (Investigation) invest.getSelectedItem();
        investUnit.setModel(createUnitsModel(inv));
}//GEN-LAST:event_investActionPerformed

    private void investTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_investTableMouseClicked
        int row = investTable.rowAtPoint(evt.getPoint());
        int column = investTable.columnAtPoint(evt.getPoint());
        RecordInvest rec = invModel.getRecord(row, column);
        setRecord(new RecordInvest[]{rec});
        if (column == invModel.getColumnCount() - 1 && rec != null && rec.getValue() != null) {
            rec.delete();
            setRecord(null);
            fireDataChanged(new DataChangeEvent(this, RecordInvest.class));
        }
}//GEN-LAST:event_investTableMouseClicked

    public void setNewRecordFood(DateTime date, Double amount, String notice, Investigation invest, InvSeason season) {
        setRecord(null);
        setComponents(date, amount, notice, invest, season);
    }

    private void setComponents(DateTime date, Double amount, String notice, Investigation invest, InvSeason season) {
        invValue.setValue(amount);
        dateTimePanel.setDateTime(date);
        investNote.setText(notice);
        investUnit.setSelectedItem(invest.getUnit());
        this.invest.setSelectedItem(invest);
        glykSeason.setSelectedItem(season);

        invModel.setDate(date);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox glykSeason;
    private javax.swing.JSpinner invValue;
    private javax.swing.JComboBox invest;
    private javax.swing.JTextArea investNote;
    private javax.swing.JTable investTable;
    private javax.swing.JComboBox investUnit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables

    @Override
    protected String validateForm() {
        if (getInvest() == null) {
            return NbBundle.getMessage(RecordInvestEditorPanel.class, "invalidinvest");
        }
        if (getInvestValue() == null) {
            return NbBundle.getMessage(RecordInvestEditorPanel.class, "invalidvalue");
        }
        return null;
    }

    @Override
    protected void onSetRecord(RecordInvest[] recs) {
        if (recs != null && recs.length > 0 && recs[0] != null) {
            RecordInvest rec = recs[0];
            setComponents(rec.getDatetime(), rec.getValue(), rec.getNotice(), rec.getInvest(), rec.getSeason());
        }
    }

    @Override
    protected RecordInvest loadRecordByFormIfExist() {
        return MyLookup.getCurrentPatient().getRecordInvest(dateTimePanel.getDateTime(), getInvest());
    }

    @Override
    protected RecordInvest saveRecord() {
        return MyLookup.getCurrentPatient().addRecordInvest(dateTimePanel.getDateTime(), getInvestValue(), getInvest(), getInvestSeason(), investNote.getText());
    }

    @Override
    protected RecordInvest updateRecord(RecordInvest rec) {
        return rec.update(dateTimePanel.getDateTime(), getInvestValue(), getInvest(), getInvestSeason(), investNote.getText());
    }

    @Override
    public void onDataChange(DataChangeEvent evt) {
        invModel.onDataChange(evt);
    }
}
