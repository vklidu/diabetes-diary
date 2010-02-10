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
import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.calendar.table.model.recordeditor.RecordFoodEditTableModel;
import org.diabetesdiary.calendar.ui.CalendarTopComponent;
import org.diabetesdiary.calendar.ui.RecordEditorTopComponent;
import org.diabetesdiary.commons.swing.calendar.DateTimePicker;
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
 * @author Jirka
 */
public class RecordFoodEditorPanel extends AbstractRecordEditorPanel<RecordFood> {

    private RecordFoodEditTableModel foodModel;

    public RecordFoodEditorPanel() {
        initComponents();
    }

    class MyFoodDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            recordFoodValidation();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            recordFoodValidation();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            //Plain text components don't fire these events
        }
    }

    private ComboBoxModel createFoodSeasonModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel(FoodSeason.values());
        return model;
    }

    public RecordFoodEditTableModel getFoodModel() {
        if (foodModel == null) {
            foodModel = new RecordFoodEditTableModel(new DateTime());
        }
        return foodModel;
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

        foodError = new javax.swing.JLabel();
        foodUnit = new javax.swing.JComboBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        foodSave = new javax.swing.JButton();
        cancelFoodEdit = new javax.swing.JButton();
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
        foodValue = new JFormattedTextField(numberFormat);
        jScrollPane2 = new javax.swing.JScrollPane();
        foodNote = new javax.swing.JTextArea();
        jLabel15 = new javax.swing.JLabel();
        dateTimePanel1 = new org.diabetesdiary.commons.swing.calendar.DateTimePanel();

        foodError.setForeground(new java.awt.Color(255, 0, 0));
        foodError.setText("error");

        jCheckBox1.setText(NbBundle.getMessage(RecordFoodEditorPanel.class,"detail")); // NOI18N
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });

        jLabel8.setText(NbBundle.getMessage(RecordFoodEditorPanel.class,"unit")); // NOI18N

        foodSave.setText(NbBundle.getMessage(RecordFoodEditorPanel.class,"foodadd")); // NOI18N
        foodSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodSaveActionPerformed(evt);
            }
        });

        cancelFoodEdit.setText(org.openide.util.NbBundle.getMessage(RecordFoodEditorPanel.class, "foodcancel")); // NOI18N
        cancelFoodEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelFoodEditActionPerformed(evt);
            }
        });

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

        foodTable.setModel(getFoodModel());
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

        foodValue.setColumns(5);
        foodValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodValueActionPerformed(evt);
            }
        });

        foodNote.setColumns(20);
        foodNote.setRows(5);
        jScrollPane2.setViewportView(foodNote);

        jLabel15.setText(NbBundle.getMessage(RecordFoodEditorPanel.class,"hintrecfood")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 205, Short.MAX_VALUE)
                        .addComponent(jLabel15))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateTimePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(foodValue, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(foodSave, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelFoodEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(foodSeason, 0, 335, Short.MAX_VALUE)
                            .addComponent(foodUnit, javax.swing.GroupLayout.Alignment.TRAILING, 0, 335, Short.MAX_VALUE)
                            .addComponent(baseGroup, javax.swing.GroupLayout.Alignment.TRAILING, 0, 335, Short.MAX_VALUE)
                            .addComponent(food, javax.swing.GroupLayout.Alignment.TRAILING, 0, 335, Short.MAX_VALUE)
                            .addComponent(foodGroup, 0, 335, Short.MAX_VALUE)))
                    .addComponent(jLabel12)
                    .addComponent(foodError))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(foodError)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(baseGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(foodGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(food, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(foodUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateTimePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(foodSeason, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel10)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel12))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(foodSave)
                            .addComponent(foodValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cancelFoodEdit))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        getFoodModel().setDetail(evt.getStateChange() == ItemEvent.SELECTED);
}//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void foodSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodSaveActionPerformed
        DateTime date = new DateTimePicker().getDateTimeFromUser();
        if (date != null) {
            setError(date.toString());
        } else {
            setError("TEST");
        }
        /*
        if (recordFoodValidation()) {
        if (selectedFoodRecord == null) {
        MyLookup.getCurrentPatient().addRecordFood(getFoodDate(), getFood(),
        getFoodValue(), getFoodValue(), getFoodUnit(), getFoodSeason(), getFoodNote());
        } else {
        selectedFoodRecord.update(getFoodDate(), getFood(), getFoodValue(), getFoodValue(), getFoodUnit(), getFoodSeason(), getFoodNote());
        }
        CalendarTopComponent.getDefault().getModel().reloadData();
        CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
        getFoodModel().setDate(getFoodDate());
        }
         * 
         */
}//GEN-LAST:event_foodSaveActionPerformed

    private void cancelFoodEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelFoodEditActionPerformed
        selectedFoodRecord = null;
        cancelFoodEdit.setVisible(false);
}//GEN-LAST:event_cancelFoodEditActionPerformed

    private void foodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodActionPerformed
        Food foodf = (Food) food.getSelectedItem();
        if (foodf != null) {
            foodUnit.setModel(createFoodUnitsModel(foodf));
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
            RecordFood rec = getFoodModel().getRecordAt(row);
            setRecordFoods(new RecordFood[]{rec});
        }

        if (column == getFoodModel().getColumnCount() - 1 && getFoodModel().getRowCount() > 1) {
            if (row == getFoodModel().getRowCount() - 1) {
                for (RecordFood rec : getFoodModel().getRecordFoods()) {
                    rec.delete();
                }
            } else {
                getFoodModel().getRecordAt(row).delete();
            }
            CalendarTopComponent.getDefault().getModel().reloadData();
            CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
            getFoodModel().fillData();
            getFoodModel().fireTableDataChanged();
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

    private void foodValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodValueActionPerformed
}//GEN-LAST:event_foodValueActionPerformed

    public void setFoodComponents(DateTime date, Double amount, String notice, Food food, FoodUnit unit, FoodSeason season) {
        foodModel.setDate(date);
        cancelFoodEdit.setVisible(selectedFoodRecord != null);
        FoodGroup group = food.getFoodGroup();
        if (group.getParent() != null) {
            baseGroup.setSelectedItem(group.getParent());
            foodGroup.setSelectedItem(group);
        } else {
            baseGroup.setSelectedItem(group);
        }
        setFoodValue(amount);
        setFoodDate(date);
        setFoodNote(notice);
        setFood(food);
        setFoodUnit(unit);
        setFoodSeason(season);

        getFoodModel().fillData();
        getFoodModel().fireTableDataChanged();
    }

    public void setRecordFoods(RecordFood[] recs) {
        selectedFoodRecord = null;
        if (recs.length > 0 && recs[0] != null) {
            RecordFood rec = recs[0];
            selectedFoodRecord = rec;
            setFoodComponents(rec.getDatetime(), rec.getAmount(), rec.getNotice(), rec.getFood(), rec.getUnit(), rec.getSeason());
            cancelFoodEdit.setVisible(true);
        }
    }
    private RecordFood selectedFoodRecord;

    private boolean recordFoodValidation() {
        foodSave.setVisible(false);
        if (MyLookup.getCurrentPatient() == null) {
            setFoodError(NbBundle.getMessage(RecordEditorTopComponent.class, "noopendiary"));
            return false;
        }
        if (getFoodUnit() == null) {
            setFoodError(NbBundle.getMessage(RecordEditorTopComponent.class, "invalidfoodunit"));
            return false;
        }
        if (getFoodDate() == null) {
            setFoodError(NbBundle.getMessage(RecordEditorTopComponent.class, "nocorrectdate"));
            return false;
        }
        if (getFoodValue() == null) {
            setFoodError(NbBundle.getMessage(RecordEditorTopComponent.class, "invalidamountfood"));
            return false;
        }
        setFoodError(null);
        foodSave.setVisible(true);
        return true;
    }

    private void setFoodError(String error) {
        if (error == null) {
            foodError.setText("");
            foodError.setVisible(false);
        }
        foodError.setText(error);
        foodError.setVisible(true);
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

    public void setFood(Food foodf) {
        food.setSelectedItem(foodf);
    }

    public FoodUnit getFoodUnit() {
        if (foodUnit.getSelectedItem() != null) {
            return (FoodUnit) foodUnit.getSelectedItem();
        }
        return null;
    }

    public void setFoodUnit(FoodUnit foodUnitS) {
        foodUnit.setSelectedItem(foodUnitS);
    }

    public DateTime getFoodDate() {
        return dateTimePanel1.getDateTime();
    }

    public void setFoodDate(DateTime foodDate) {
        dateTimePanel1.setDateTime(foodDate);
    }

    public FoodSeason getFoodSeason() {
        return (FoodSeason) foodSeason.getSelectedItem();
    }

    public void setFoodSeason(FoodSeason foodSeas) {
        foodSeason.setSelectedItem(foodSeas);
    }

    public Double getFoodValue() {
        if (foodValue.getValue() instanceof Number) {
            return ((Number) foodValue.getValue()).doubleValue();
        }
        return null;
    }

    public void setFoodValue(Double foodV) {
        foodValue.setValue(foodV);
    }

    public String getFoodNote() {
        return foodNote.getText();
    }

    public void setFoodNote(String note) {
        foodNote.setText(note);
    }

    @Override
    public void setRecord(RecordFood[] recs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRecordValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateRecord(RecordFood rec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox baseGroup;
    private javax.swing.JButton cancelFoodEdit;
    private org.diabetesdiary.commons.swing.calendar.DateTimePanel dateTimePanel1;
    private javax.swing.JComboBox food;
    protected javax.swing.JLabel foodError;
    private javax.swing.JComboBox foodGroup;
    private javax.swing.JTextArea foodNote;
    private javax.swing.JButton foodSave;
    private javax.swing.JComboBox foodSeason;
    private javax.swing.JTable foodTable;
    private javax.swing.JComboBox foodUnit;
    private javax.swing.JFormattedTextField foodValue;
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
}
