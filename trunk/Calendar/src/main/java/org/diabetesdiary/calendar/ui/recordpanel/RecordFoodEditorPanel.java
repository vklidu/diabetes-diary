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

import java.awt.event.ItemEvent;
import java.util.Arrays;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.calendar.table.model.recordeditor.RecordFoodEditTableModel;
import org.diabetesdiary.calendar.ui.RecordEditorTopComponent;
import org.diabetesdiary.diary.domain.Food;
import org.diabetesdiary.diary.domain.FoodGroup;
import org.diabetesdiary.diary.domain.FoodSeason;
import org.diabetesdiary.diary.domain.FoodUnit;
import org.diabetesdiary.diary.domain.RecordFood;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;
import org.openide.util.NbBundle;

/**
 *
 * @author Jirka Majer
 */
public class RecordFoodEditorPanel extends AbstractRecordEditorPanel<RecordFood> {

    private final RecordFoodEditTableModel foodModel = new RecordFoodEditTableModel(new DateTime());

    public RecordFoodEditorPanel() {
        initComponents();
        addDataChangedListener(foodModel);
    }

    private ComboBoxModel createFoodSeasonModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel(FoodSeason.values());
        return model;
    }

    private ComboBoxModel createFoodComboModel(FoodGroup group) {
        Object[] groups = group.getFoods().toArray();
        Arrays.sort(groups);
        DefaultComboBoxModel model = new DefaultComboBoxModel(groups);
        return model;
    }

    private ComboBoxModel createFoodUnitsModel(Food food) {
        DefaultComboBoxModel model = new DefaultComboBoxModel(food.getUnits().toArray());
        if (food.isSacharidUnit()) {
            model.setSelectedItem(diary.getSacharidUnit(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT)));
        }
        return model;
    }

    private ComboBoxModel createBaseFoodGroupModel() {
        Object[] groups = diary.getBaseFoodGroups().toArray();
        Arrays.sort(groups);
        DefaultComboBoxModel model = new DefaultComboBoxModel(groups);
        return model;
    }

    private ComboBoxModel createFoodGroupModel(FoodGroup parent) {
        Object[] foods = parent.getFoodGroups().toArray();
        Arrays.sort(foods);
        DefaultComboBoxModel model = new DefaultComboBoxModel(foods);
        return model;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        foodUnit = new javax.swing.JComboBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        food = new javax.swing.JComboBox();
        foodGroup = new javax.swing.JComboBox();
        foodSeason = new javax.swing.JComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        foodTable = new javax.swing.JTable();
        baseGroup = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        foodNote = new javax.swing.JTextArea();
        jLabel15 = new javax.swing.JLabel();
        foodValue = new javax.swing.JSpinner();

        jCheckBox1.setText(NbBundle.getMessage(RecordFoodEditorPanel.class,"detail")); // NOI18N
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel8.setText(NbBundle.getMessage(RecordFoodEditorPanel.class,"unit")); // NOI18N

        jLabel13.setText(NbBundle.getMessage(RecordFoodEditorPanel.class,"foodgroup")); // NOI18N

        jLabel7.setText(NbBundle.getMessage(RecordFoodEditorPanel.class,"recfood")); // NOI18N

        jLabel14.setText(NbBundle.getMessage(RecordFoodEditorPanel.class,"subgroup")); // NOI18N

        jLabel9.setText(NbBundle.getMessage(RecordFoodEditorPanel.class, "datum")); // NOI18N

        food.setMaximumRowCount(20);
        food.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodActionPerformed(evt);
            }
        });

        foodGroup.setMaximumRowCount(20);
        foodGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodGroupActionPerformed(evt);
            }
        });

        foodSeason.setModel(createFoodSeasonModel());

        foodTable.setModel(foodModel);
        foodTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                foodTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(foodTable);

        baseGroup.setMaximumRowCount(20);
        baseGroup.setModel(createBaseFoodGroupModel());
        baseGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                baseGroupActionPerformed(evt);
            }
        });

        jLabel10.setText(NbBundle.getMessage(RecordFoodEditorPanel.class,"NewGlykemieWizard.season")); // NOI18N

        jLabel11.setText(NbBundle.getMessage(RecordFoodEditorPanel.class,"amount")); // NOI18N

        jLabel12.setText(NbBundle.getMessage(RecordFoodEditorPanel.class, "NewGlykemieWizard.notice")); // NOI18N

        foodNote.setColumns(20);
        foodNote.setRows(5);
        jScrollPane2.setViewportView(foodNote);

        jLabel15.setText(NbBundle.getMessage(RecordFoodEditorPanel.class,"hintrecfood")); // NOI18N

        foodValue.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(1.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel12))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(errorLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(foodSeason, 0, 163, Short.MAX_VALUE)
                            .addComponent(foodUnit, 0, 163, Short.MAX_VALUE)
                            .addComponent(food, 0, 163, Short.MAX_VALUE)
                            .addComponent(foodGroup, javax.swing.GroupLayout.Alignment.TRAILING, 0, 163, Short.MAX_VALUE)
                            .addComponent(baseGroup, 0, 163, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dateTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(foodValue, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(errorLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(baseGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(foodGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(food, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(foodUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(foodSeason, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addComponent(dateTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel11))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(foodValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(saveButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(cancelButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        foodModel.setDetail(evt.getStateChange() == ItemEvent.SELECTED);
}//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void foodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodActionPerformed
        if (getFood() != null) {
            foodUnit.setModel(createFoodUnitsModel(getFood()));
        }
}//GEN-LAST:event_foodActionPerformed

    private void foodGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodGroupActionPerformed
        food.setModel(createFoodComboModel(getFoodGroup()));
        foodActionPerformed(evt);
}//GEN-LAST:event_foodGroupActionPerformed

    private void foodTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_foodTableMouseClicked
        int row = foodTable.rowAtPoint(evt.getPoint());
        int column = foodTable.columnAtPoint(evt.getPoint());

        //int column = foodTable.columnAtPoint(e.getPoint());
        if (!evt.isPopupTrigger()) {
            RecordFood rec = foodModel.getRecordAt(row);
            if (rec != null) {
                setRecord(new RecordFood[]{rec});
            }
        }

        if (column == foodModel.getColumnCount() - 1 && foodModel.getRowCount() > 1) {
            if (row == foodModel.getRowCount() - 1) {
                for (RecordFood rec : foodModel.getRecordFoods()) {
                    rec.delete();
                }
                setRecord(null);
            } else {
                if (foodModel.getRecordAt(row).equals(getSelectedRecord())) {
                    setRecord(null);
                }
                foodModel.getRecordAt(row).delete();
            }
            foodModel.refreshData();            
        }
}//GEN-LAST:event_foodTableMouseClicked

    private void baseGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_baseGroupActionPerformed
        if (getBaseFoodGroup().getFoodGroups().size() > 0) {
            foodGroup.setModel(createFoodGroupModel(getBaseFoodGroup()));
            foodGroup.setVisible(true);
            jLabel14.setVisible(true);
            foodGroupActionPerformed(evt);
        } else {
            foodGroup.setVisible(false);
            jLabel14.setVisible(false);
            food.setModel(createFoodComboModel(getBaseFoodGroup()));
            foodActionPerformed(evt);
        }
}//GEN-LAST:event_baseGroupActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    public void setNewRecordFood(DateTime date, Double amount, String notice, Food food, FoodUnit unit, FoodSeason season) {
        setRecord(null);
        setFoodComponents(date, amount, notice, food, unit, season);
    }

    private void setFoodComponents(DateTime date, Double amount, String notice, Food food, FoodUnit unit, FoodSeason season) {
        setFood(food);
        foodValue.setValue(amount);
        dateTimePanel.setDateTime(date);
        foodNote.setText(notice);
        foodUnit.setSelectedItem(unit);
        foodSeason.setSelectedItem(season);

        foodModel.setDate(date);
    }

    public void setFood(Food food) {
        if (food == null) {
            baseGroup.setSelectedItem(null);
            foodGroup.setSelectedItem(null);
            foodGroup.setVisible(false);
            jLabel14.setVisible(false);
            this.food.setSelectedItem(null);
            foodUnit.setSelectedItem(null);
            return;
        }
        FoodGroup group = food.getFoodGroup();
        if (group.getParent() != null) {
            baseGroup.setSelectedItem(group.getParent());
            foodGroup.setSelectedItem(group);
        } else {
            baseGroup.setSelectedItem(group);
        }
        this.food.setSelectedItem(food);
    }

    public Food getFood() {
        return (Food) food.getSelectedItem();
    }

    public FoodGroup getBaseFoodGroup() {
        return (FoodGroup) baseGroup.getSelectedItem();
    }

    public FoodGroup getFoodGroup() {
        return (FoodGroup) foodGroup.getSelectedItem();
    }

    public FoodUnit getFoodUnit() {
        if (foodUnit.getSelectedItem() != null) {
            return (FoodUnit) foodUnit.getSelectedItem();
        }
        return null;
    }

    public FoodSeason getFoodSeason() {
        return (FoodSeason) foodSeason.getSelectedItem();
    }

    public Double getFoodValue() {
        if (foodValue.getValue() instanceof Number) {
            return ((Number) foodValue.getValue()).doubleValue();
        }
        return null;
    }

    @Override
    public RecordFood saveRecord() {
        return MyLookup.getCurrentPatient().addRecordFood(dateTimePanel.getDateTime(), getFood(),
                getFoodValue(), getFoodValue(), getFoodUnit(), getFoodSeason(), foodNote.getText());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox baseGroup;
    private javax.swing.JComboBox food;
    private javax.swing.JComboBox foodGroup;
    private javax.swing.JTextArea foodNote;
    private javax.swing.JComboBox foodSeason;
    private javax.swing.JTable foodTable;
    private javax.swing.JComboBox foodUnit;
    private javax.swing.JSpinner foodValue;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables

    @Override
    protected String validateForm() {
        if (MyLookup.getCurrentPatient() == null) {
            return NbBundle.getMessage(RecordEditorTopComponent.class, "noopendiary");
        }
        if (getFoodUnit() == null) {
            return NbBundle.getMessage(RecordEditorTopComponent.class, "invalidfoodunit");
        }
        if (getFoodValue() == null) {
            return NbBundle.getMessage(RecordEditorTopComponent.class, "invalidamountfood");
        }
        return null;
    }

    @Override
    protected RecordFood updateRecord(RecordFood rec) {
        return rec.update(dateTimePanel.getDateTime(), getFood(), getFoodValue(), getFoodValue(), getFoodUnit(), getFoodSeason(), foodNote.getText());
    }

    @Override
    protected void onSetRecord(RecordFood[] recs) {
        if (recs != null && recs.length > 0 && recs[0] != null) {
            RecordFood rec = recs[0];
            setFoodComponents(rec.getDatetime(), rec.getAmount(), rec.getNotice(), rec.getFood(), rec.getUnit(), rec.getSeason());
        }
    }

    @Override
    protected RecordFood loadRecordByFormIfExist() {
        return MyLookup.getCurrentPatient().getRecordFood(dateTimePanel.getDateTime(), getFood());
    }

}
