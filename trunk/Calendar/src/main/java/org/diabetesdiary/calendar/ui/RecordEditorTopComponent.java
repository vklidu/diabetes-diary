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

import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.calendar.table.RecordActivityEditTableModel;
import org.diabetesdiary.calendar.table.RecordFoodEditTableModel;
import org.diabetesdiary.calendar.table.RecordInsulinEditTableModel;
import org.diabetesdiary.calendar.table.RecordInsulinPumpBasal;
import org.diabetesdiary.calendar.table.RecordInvestEditTableModel;
import org.diabetesdiary.datamodel.api.ActivityAdministrator;
import org.diabetesdiary.diary.utils.MyLookup;
import org.diabetesdiary.diary.api.DiaryRepository;
import org.diabetesdiary.datamodel.api.FoodAdministrator;
import org.diabetesdiary.datamodel.api.InsulinAdministrator;
import org.diabetesdiary.datamodel.api.InvestigationAdministrator;
import org.diabetesdiary.diary.service.db.ActivityDO;
import org.diabetesdiary.diary.service.db.ActivityGroupDO;
import org.diabetesdiary.diary.service.db.FoodDO;
import org.diabetesdiary.diary.service.db.FoodGroupDO;
import org.diabetesdiary.diary.service.db.FoodSeason;
import org.diabetesdiary.diary.service.db.FoodUnitDO;
import org.diabetesdiary.diary.service.db.InvSeason;
import org.diabetesdiary.diary.service.db.InsulinDO;
import org.diabetesdiary.diary.service.db.InsulinSeason;
import org.diabetesdiary.diary.service.db.InsulinTypeDO;
import org.diabetesdiary.diary.service.db.InvestigationDO;
import org.diabetesdiary.diary.service.db.RecordActivityDO;
import org.diabetesdiary.datamodel.pojo.RecordActivityPK;
import org.diabetesdiary.diary.service.db.RecordFoodDO;
import org.diabetesdiary.datamodel.pojo.RecordFoodPK;
import org.diabetesdiary.diary.service.db.RecordInsulinDO;
import org.diabetesdiary.datamodel.pojo.RecordInsulinPK;
import org.diabetesdiary.diary.service.db.RecordInvestDO;
import org.diabetesdiary.datamodel.pojo.RecordInvestPK;
import org.openide.ErrorManager;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
public final class RecordEditorTopComponent extends TopComponent {

    private static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    private static DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    private static DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    private static NumberFormat numberFormat = NumberFormat.getInstance();
    private static RecordEditorTopComponent instance;
    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "org/diabetesdiary/calendar/resources/recordedit.png";
    private static final String PREFERRED_ID = "RecordEditorTopComponent";
    private InvestigationAdministrator invAdmin;
    private FoodAdministrator foodAdmin;
    private InsulinAdministrator insulinAdmin;
    private ActivityAdministrator actAdmin;
    private DiaryRepository diary;
    private RecordFoodEditTableModel foodModel;
    private RecordInvestEditTableModel investModel;
    private RecordInsulinEditTableModel insulinModel;
    private RecordActivityEditTableModel actModel;

    class MyInsulinDocumentListener implements DocumentListener {

        public void insertUpdate(DocumentEvent e) {
            recordInsulinValidation();
        }

        public void removeUpdate(DocumentEvent e) {
            recordInsulinValidation();
        }

        public void changedUpdate(DocumentEvent e) {
            //Plain text components don't fire these events
        }
    }

    class MyActivityDocumentListener implements DocumentListener {

        public void insertUpdate(DocumentEvent e) {
            recordActivityValidation();
        }

        public void removeUpdate(DocumentEvent e) {
            recordActivityValidation();
        }

        public void changedUpdate(DocumentEvent e) {
            //Plain text components don't fire these events
        }
    }

    class MyDocumentListener implements DocumentListener {

        public void insertUpdate(DocumentEvent e) {
            recordInvestValidation();
        }

        public void removeUpdate(DocumentEvent e) {
            recordInvestValidation();
        }

        public void changedUpdate(DocumentEvent e) {
            //Plain text components don't fire these events
        }
    }

    class MyFoodDocumentListener implements DocumentListener {

        public void insertUpdate(DocumentEvent e) {
            recordFoodValidation();
        }

        public void removeUpdate(DocumentEvent e) {
            recordFoodValidation();
        }

        public void changedUpdate(DocumentEvent e) {
            //Plain text components don't fire these events
        }
    }

    private RecordEditorTopComponent() {
        invAdmin = MyLookup.getInvesAdmin();
        foodAdmin = MyLookup.getFoodAdmin();
        insulinAdmin = MyLookup.getInsulinAdmin();
        actAdmin = MyLookup.getActivityAdmin();
        diary = MyLookup.getDiaryRepo();
        numberFormat.setMaximumFractionDigits(2);

        initComponents();
        setName(NbBundle.getMessage(RecordEditorTopComponent.class, "CTL_RecordEditorTopComponent"));
        setToolTipText(NbBundle.getMessage(RecordEditorTopComponent.class, "HINT_RecordEditorTopComponent"));
        setIcon(Utilities.loadImage(ICON_PATH, true));

        insulinDatum.getDocument().addDocumentListener(new MyInsulinDocumentListener());
        insulinValue.getDocument().addDocumentListener(new MyInsulinDocumentListener());
        insulinTime.getDocument().addDocumentListener(new MyInsulinDocumentListener());

        investDatum.getDocument().addDocumentListener(new MyDocumentListener());
        investValue.getDocument().addDocumentListener(new MyDocumentListener());
        investNote.getDocument().addDocumentListener(new MyDocumentListener());
        investTime.getDocument().addDocumentListener(new MyDocumentListener());

        actDate.getDocument().addDocumentListener(new MyActivityDocumentListener());
        actValue.getDocument().addDocumentListener(new MyActivityDocumentListener());
        actNote.getDocument().addDocumentListener(new MyActivityDocumentListener());
        actTime.getDocument().addDocumentListener(new MyActivityDocumentListener());

        foodDatum.getDocument().addDocumentListener(new MyFoodDocumentListener());
        foodValue.getDocument().addDocumentListener(new MyFoodDocumentListener());
        foodNote.getDocument().addDocumentListener(new MyFoodDocumentListener());
        foodTime.getDocument().addDocumentListener(new MyFoodDocumentListener());

        jScrollPane2.setVisible(false);
        jLabel12.setVisible(false);
        investError.setVisible(false);
        foodError.setVisible(false);
        insulinError.setVisible(false);
        actError.setVisible(false);
        if (baseGroup.getItemCount() > 0) {
            baseGroup.setSelectedIndex(0);
        }
    }

    private ComboBoxModel createSeasonModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel(InvSeason.values());
        //model.insertElementAt(null,0);
        //model.setSelectedItem(null);
        return model;
    }

    private ComboBoxModel createActGroupModel() {
        Object[] groups = actAdmin.getActivityGroups().toArray();
        Arrays.sort(groups);
        DefaultComboBoxModel model = new DefaultComboBoxModel(groups);
        return model;
    }

    private ComboBoxModel createInsulinSeasonModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel(InsulinSeason.values());
        return model;
    }

    private ComboBoxModel createFoodSeasonModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel(FoodSeason.values());
        return model;
    }

    public RecordFoodEditTableModel getFoodModel() {
        if (foodModel == null) {
            foodModel = new RecordFoodEditTableModel(new Date());
        }
        return foodModel;
    }

    public RecordInsulinEditTableModel getInsulinModel() {
        if (insulinModel == null) {
            insulinModel = new RecordInsulinEditTableModel(null);
        }
        return insulinModel;
    }

    public RecordActivityEditTableModel getActivityModel() {
        if (actModel == null) {
            actModel = new RecordActivityEditTableModel(null);
        }
        return actModel;
    }

    public RecordInvestEditTableModel getInvestModel() {
        if (investModel == null) {
            investModel = new RecordInvestEditTableModel(null);
        }
        return investModel;
    }

    private ComboBoxModel createFoodComboModel(FoodGroupDO group) {
        Object[] groups = foodAdmin.getFoodsByGroup(group).toArray();
        Arrays.sort(groups);
        DefaultComboBoxModel model = new DefaultComboBoxModel(groups);
        return model;
    }

    private ComboBoxModel createActComboModel(ActivityGroupDO group) {
        Object[] groups = actAdmin.getActivities(group.getId()).toArray();
        Arrays.sort(groups);
        DefaultComboBoxModel model = new DefaultComboBoxModel(groups);
        return model;
    }

    private ComboBoxModel createFoodUnitsModel(FoodDO food) {
        DefaultComboBoxModel model = new DefaultComboBoxModel(foodAdmin.getFoodUnits(food.getIdFood()).toArray());
        if (food.getIdFood().equals(1)) {
            model.setSelectedItem(foodAdmin.getFoodUnit(1, CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT)));
        }
        return model;
    }

    private ComboBoxModel createBaseFoodGroupModel() {
        Object[] groups = foodAdmin.getBaseFoodGroups().toArray();
        Arrays.sort(groups);
        DefaultComboBoxModel model = new DefaultComboBoxModel(groups);
        return model;
    }

    private ComboBoxModel createFoodGroupModel(FoodGroupDO parent) {
        Object[] foods = foodAdmin.getFoodGroupByParent(parent).toArray();
        Arrays.sort(foods);
        DefaultComboBoxModel model = new DefaultComboBoxModel(foods);
        return model;
    }

    private ComboBoxModel createInvestModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel(invAdmin.getInvestigations().toArray());
        return model;
    }

    private ComboBoxModel createUnitsModel(InvestigationDO inv) {
        DefaultComboBoxModel model = new DefaultComboBoxModel(new Object[]{inv.getUnit()});
        return model;
    }

    private ComboBoxModel createInsTypeComboModel() {
        Object[] groups = insulinAdmin.getInsulinTypes().toArray();
        DefaultComboBoxModel model = new DefaultComboBoxModel(groups);
        return model;
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

    private ComboBoxModel createInsulinUnitsModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel(InsUnit.values());
        return model;
    }

    private ComboBoxModel createInsulinModel(InsulinTypeDO type) {
        Object[] groups = insulinAdmin.getInsulines(type.getId()).toArray();
        DefaultComboBoxModel model = new DefaultComboBoxModel(groups);
        return model;
    }

    /** This method is called from within the constructor to$
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        activityPanel = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        insulin = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        insulinUnit = new javax.swing.JComboBox();
        insulinDatum = new javax.swing.JTextField();
        insulinTime = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        insulinSeason = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        insulinSave = new javax.swing.JButton();
        insulinError = new javax.swing.JLabel();
        insulinType = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        insulinValue = new JFormattedTextField(numberFormat);
        insulinDel = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        insulinNote = new javax.swing.JTextArea();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        insulinTable = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        invest = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        investUnit = new javax.swing.JComboBox();
        investDatum = new javax.swing.JTextField();
        investTime = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        glykSeason = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        investNote = new javax.swing.JTextArea();
        investSave = new javax.swing.JButton();
        investError = new javax.swing.JLabel();
        investDel = new javax.swing.JButton();
        investValue = new JFormattedTextField(numberFormat);
        jScrollPane4 = new javax.swing.JScrollPane();
        investTable = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        foodTable = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        food = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        foodUnit = new javax.swing.JComboBox();
        foodDatum = new javax.swing.JTextField();
        foodTime = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        foodSeason = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        foodNote = new javax.swing.JTextArea();
        foodSave = new javax.swing.JButton();
        foodError = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        foodGroup = new javax.swing.JComboBox();
        baseGroup = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        foodValue = new JFormattedTextField(numberFormat);
        jLabel15 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        activity = new javax.swing.JComboBox();
        actDate = new javax.swing.JTextField();
        actTime = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        actSave = new javax.swing.JButton();
        actError = new javax.swing.JLabel();
        actType = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        actValue = new JFormattedTextField(numberFormat);
        actDel = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        actNote = new javax.swing.JTextArea();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        actTable = new javax.swing.JTable();
        jLabel32 = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel16, NbBundle.getMessage(RecordEditorTopComponent.class,"recinsulin")); // NOI18N

        insulin.setMaximumRowCount(20);
        insulin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insulinActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel17, NbBundle.getMessage(RecordEditorTopComponent.class,"unit")); // NOI18N

        insulinUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insulinUnitActionPerformed(evt);
            }
        });

        insulinDatum.setColumns(10);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel18, NbBundle.getMessage(RecordEditorTopComponent.class, "NewGlykemieWizard.datum")); // NOI18N

        insulinSeason.setModel(createInsulinSeasonModel());

        org.openide.awt.Mnemonics.setLocalizedText(jLabel19, NbBundle.getMessage(RecordEditorTopComponent.class,"season")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel20, NbBundle.getMessage(RecordEditorTopComponent.class,"amount")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(insulinSave, NbBundle.getMessage(RecordEditorTopComponent.class,"foodadd")); // NOI18N
        insulinSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insulinSaveActionPerformed(evt);
            }
        });

        insulinError.setForeground(new java.awt.Color(255, 0, 0));
        org.openide.awt.Mnemonics.setLocalizedText(insulinError, "error");

        insulinType.setMaximumRowCount(20);
        insulinType.setModel(createInsTypeComboModel());
        insulinType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insulinTypeActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel22, NbBundle.getMessage(RecordEditorTopComponent.class,"typesofinsulin")); // NOI18N

        insulinValue.setColumns(5);
        insulinValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insulinValueActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(insulinDel, org.openide.util.NbBundle.getMessage(RecordEditorTopComponent.class, "delete")); // NOI18N
        insulinDel.setEnabled(false);
        insulinDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insulinDelActionPerformed(evt);
            }
        });

        insulinNote.setColumns(20);
        insulinNote.setRows(5);
        jScrollPane5.setViewportView(insulinNote);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel23, NbBundle.getMessage(RecordEditorTopComponent.class, "NewGlykemieWizard.notice")); // NOI18N

        insulinTable.setModel(getInsulinModel());
        insulinTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                insulinTableMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(insulinTable);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel24, org.openide.util.NbBundle.getMessage(RecordEditorTopComponent.class, "label.edit.insulintable.allrecords")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(insulinError)
                    .add(jLabel16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel17)
                            .add(jLabel18)
                            .add(jLabel19)
                            .add(jLabel20)
                            .add(jLabel22))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(insulinType, 0, 274, Short.MAX_VALUE)
                            .add(jPanel6Layout.createSequentialGroup()
                                .add(insulinDatum, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(insulinTime, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
                            .add(jPanel6Layout.createSequentialGroup()
                                .add(insulinValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(insulinSave, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(insulinDel))
                            .add(insulinSeason, 0, 274, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, insulinUnit, 0, 274, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, insulin, 0, 274, Short.MAX_VALUE)))
                    .add(jLabel23)
                    .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .add(jLabel24))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(insulinError)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel22)
                    .add(insulinType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel16)
                    .add(insulin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel17)
                    .add(insulinUnit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel18)
                    .add(insulinDatum, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(insulinTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel19)
                    .add(insulinSeason, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel20)
                    .add(insulinValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(insulinDel)
                    .add(insulinSave))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel23)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel24)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel16.getAccessibleContext().setAccessibleName("jLabel16");
        jLabel22.getAccessibleContext().setAccessibleName("jLabel22");
        insulinDel.getAccessibleContext().setAccessibleName("insulinDel");

        activityPanel.addTab(org.openide.util.NbBundle.getMessage(RecordEditorTopComponent.class, "insulin"), jPanel6); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, NbBundle.getMessage(RecordEditorTopComponent.class,"investtype")); // NOI18N

        invest.setModel(createInvestModel());
        invest.setMinimumSize(new java.awt.Dimension(10, 22));
        invest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                investActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, NbBundle.getMessage(RecordEditorTopComponent.class,"unit")); // NOI18N

        investUnit.setMinimumSize(new java.awt.Dimension(5, 22));

        investDatum.setColumns(10);
        investDatum.setMinimumSize(new java.awt.Dimension(10, 22));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, NbBundle.getMessage(RecordEditorTopComponent.class, "NewGlykemieWizard.datum")); // NOI18N

        glykSeason.setModel(createSeasonModel());

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, NbBundle.getMessage(RecordEditorTopComponent.class,"season")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, NbBundle.getMessage(RecordEditorTopComponent.class,"value")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, NbBundle.getMessage(RecordEditorTopComponent.class, "NewGlykemieWizard.notice")); // NOI18N

        investNote.setColumns(20);
        investNote.setRows(5);
        jScrollPane1.setViewportView(investNote);

        org.openide.awt.Mnemonics.setLocalizedText(investSave, NbBundle.getMessage(RecordEditorTopComponent.class,"save")); // NOI18N
        investSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                investSaveActionPerformed(evt);
            }
        });

        investError.setForeground(new java.awt.Color(255, 0, 0));
        org.openide.awt.Mnemonics.setLocalizedText(investError, "error");

        org.openide.awt.Mnemonics.setLocalizedText(investDel, NbBundle.getMessage(RecordEditorTopComponent.class,"delete")); // NOI18N
        investDel.setEnabled(false);
        investDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                investDelActionPerformed(evt);
            }
        });

        investValue.setColumns(5);
        investValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                investValueActionPerformed(evt);
            }
        });

        investTable.setModel(getInvestModel());
        investTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                investTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(investTable);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel21, org.openide.util.NbBundle.getMessage(RecordEditorTopComponent.class, "label.edit.investtable.allrecords")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel4)
                    .add(investError)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel2)
                            .add(jLabel3)
                            .add(jLabel5)
                            .add(jLabel6)
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(investDatum, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(investTime, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(investValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(investSave, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(investDel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                            .add(investUnit, 0, 262, Short.MAX_VALUE)
                            .add(invest, 0, 262, Short.MAX_VALUE)
                            .add(glykSeason, 0, 262, Short.MAX_VALUE)))
                    .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .add(jLabel21))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(investError)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(invest, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(investUnit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(investDatum, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(investTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(glykSeason, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(40, 40, 40)
                        .add(jLabel4))
                    .add(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel6)
                            .add(investSave)
                            .add(investValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(investDel))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel21)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                .addContainerGap())
        );

        activityPanel.addTab(NbBundle.getMessage(RecordEditorTopComponent.class,"investigation"), jPanel1); // NOI18N

        foodTable.setModel(getFoodModel());
        foodTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                foodTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(foodTable);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, NbBundle.getMessage(RecordEditorTopComponent.class,"recfood")); // NOI18N

        food.setMaximumRowCount(20);
        food.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, NbBundle.getMessage(RecordEditorTopComponent.class,"unit")); // NOI18N

        foodDatum.setColumns(10);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, NbBundle.getMessage(RecordEditorTopComponent.class, "NewGlykemieWizard.datum")); // NOI18N

        foodSeason.setModel(createFoodSeasonModel());

        org.openide.awt.Mnemonics.setLocalizedText(jLabel10, NbBundle.getMessage(RecordEditorTopComponent.class,"season")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel11, NbBundle.getMessage(RecordEditorTopComponent.class,"amount")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel12, NbBundle.getMessage(RecordEditorTopComponent.class, "NewGlykemieWizard.notice")); // NOI18N

        foodNote.setColumns(20);
        foodNote.setRows(5);
        jScrollPane2.setViewportView(foodNote);

        org.openide.awt.Mnemonics.setLocalizedText(foodSave, NbBundle.getMessage(RecordEditorTopComponent.class,"foodadd")); // NOI18N
        foodSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodSaveActionPerformed(evt);
            }
        });

        foodError.setForeground(new java.awt.Color(255, 0, 0));
        org.openide.awt.Mnemonics.setLocalizedText(foodError, "error");

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBox1, NbBundle.getMessage(RecordEditorTopComponent.class,"detail")); // NOI18N
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });

        foodGroup.setMaximumRowCount(20);
        foodGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodGroupActionPerformed(evt);
            }
        });

        baseGroup.setMaximumRowCount(20);
        baseGroup.setModel(createBaseFoodGroupModel());
        baseGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                baseGroupActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel13, NbBundle.getMessage(RecordEditorTopComponent.class,"foodgroup")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel14, NbBundle.getMessage(RecordEditorTopComponent.class,"subgroup")); // NOI18N

        foodValue.setColumns(5);
        foodValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodValueActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel15, NbBundle.getMessage(RecordEditorTopComponent.class,"hintrecfood")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jCheckBox1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 106, Short.MAX_VALUE)
                        .add(jLabel15))
                    .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel8)
                            .add(jLabel9)
                            .add(jLabel10)
                            .add(jLabel11)
                            .add(jLabel13)
                            .add(jLabel14))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel5Layout.createSequentialGroup()
                                .add(foodDatum, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(foodTime, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel5Layout.createSequentialGroup()
                                .add(foodValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(foodSave, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, foodSeason, 0, 274, Short.MAX_VALUE)
                            .add(foodUnit, 0, 274, Short.MAX_VALUE)
                            .add(baseGroup, 0, 274, Short.MAX_VALUE)
                            .add(food, 0, 274, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, foodGroup, 0, 274, Short.MAX_VALUE)))
                    .add(foodError)
                    .add(jLabel12))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(foodError)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel13)
                    .add(baseGroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel14)
                    .add(foodGroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(food, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(foodUnit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel9)
                    .add(foodDatum, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(foodTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel10)
                    .add(foodSeason, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(40, 40, 40)
                        .add(jLabel12))
                    .add(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel11)
                            .add(foodSave)
                            .add(foodValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCheckBox1)
                    .add(jLabel15))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                .addContainerGap())
        );

        activityPanel.addTab(NbBundle.getMessage(RecordEditorTopComponent.class,"food"), jPanel5); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel25, NbBundle.getMessage(RecordEditorTopComponent.class,"recactivity")); // NOI18N

        activity.setMaximumRowCount(20);
        activity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activityActionPerformed(evt);
            }
        });

        actDate.setColumns(10);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel27, NbBundle.getMessage(RecordEditorTopComponent.class, "NewGlykemieWizard.datum")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel29, NbBundle.getMessage(RecordEditorTopComponent.class,"minutes")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(actSave, NbBundle.getMessage(RecordEditorTopComponent.class,"foodadd")); // NOI18N
        actSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actSaveActionPerformed(evt);
            }
        });

        actError.setForeground(new java.awt.Color(255, 0, 0));
        org.openide.awt.Mnemonics.setLocalizedText(actError, "error");

        actType.setMaximumRowCount(20);
        actType.setModel(createActGroupModel());
        actType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actTypeActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel30, NbBundle.getMessage(RecordEditorTopComponent.class,"typesofactivity")); // NOI18N

        actValue.setColumns(5);
        actValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actValueActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(actDel, org.openide.util.NbBundle.getMessage(RecordEditorTopComponent.class, "delete")); // NOI18N
        actDel.setEnabled(false);
        actDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actDelActionPerformed(evt);
            }
        });

        actNote.setColumns(20);
        actNote.setRows(5);
        jScrollPane7.setViewportView(actNote);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel31, NbBundle.getMessage(RecordEditorTopComponent.class, "NewGlykemieWizard.notice")); // NOI18N

        actTable.setModel(getActivityModel());
        actTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                actTableMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(actTable);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel32, org.openide.util.NbBundle.getMessage(RecordEditorTopComponent.class, "label.edit.activitytable.allrecords")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(actError)
                    .add(jLabel25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel29)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(actValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(actSave, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(actDel))
                    .add(jLabel31)
                    .add(jScrollPane8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .add(jLabel32)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel30)
                            .add(jLabel27))
                        .add(15, 15, 15)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel7Layout.createSequentialGroup()
                                .add(actDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(actTime, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
                            .add(actType, 0, 283, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, activity, 0, 283, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(actError)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel30)
                    .add(actType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel25)
                    .add(activity, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel27)
                    .add(actTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(actDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel29)
                    .add(actDel)
                    .add(actSave)
                    .add(actValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel31)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel32)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                .add(59, 59, 59))
        );

        activityPanel.addTab(org.openide.util.NbBundle.getMessage(RecordEditorTopComponent.class, "activity"), jPanel7); // NOI18N

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

private void insulinTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insulinTypeActionPerformed
    InsulinTypeDO type = (InsulinTypeDO) insulinType.getSelectedItem();
    insulin.setModel(createInsulinModel(type));
}//GEN-LAST:event_insulinTypeActionPerformed

private void insulinSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insulinSaveActionPerformed
    if (recordInsulinValidation()) {
        RecordInsulinDO recordInsulin = new RecordInsulinDO();
        RecordInsulinPK pk = new RecordInsulinPK(getInsulin().getId(), diary.getCurrentPatient().getIdPatient(), getInsulinDate());
        recordInsulin.setId(pk);
        recordInsulin.setInsulin(getInsulin());
        recordInsulin.setSeason(getInsulinSeason().name());
        recordInsulin.setAmount(getInsulinValue());
        recordInsulin.setNotice(getInsulinNote());
        InsUnit un = (InsUnit) insulinUnit.getSelectedItem();
        recordInsulin.getId().setBasal(un.isBasal());
        diary.addRecord(recordInsulin);
        selectedInsulinRecord = recordInsulin;
        CalendarTopComponent.getDefault().getModel().fillData();
        CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
    }
}//GEN-LAST:event_insulinSaveActionPerformed

private void insulinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insulinActionPerformed
    insulinUnit.setModel(createInsulinUnitsModel());
}//GEN-LAST:event_insulinActionPerformed

private void baseGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_baseGroupActionPerformed
    if (foodAdmin.getFoodGroupByParent(getBaseFoodGroup()).size() > 0) {
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

private void foodGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodGroupActionPerformed
    food.setModel(createFoodComboModel(getFoodGroup()));
    foodActionPerformed(evt);
}//GEN-LAST:event_foodGroupActionPerformed

private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
    getFoodModel().setDetail(evt.getStateChange() == ItemEvent.SELECTED);
}//GEN-LAST:event_jCheckBox1ItemStateChanged

private void foodSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodSaveActionPerformed
    if (recordFoodValidation()) {
        RecordFoodDO record = new RecordFoodDO();
        RecordFoodPK pk = new RecordFoodPK(getFood().getIdFood(), diary.getCurrentPatient().getIdPatient(), getFoodDate());
        record.setId(pk);
        record.setFood(getFood());
        record.setNotice(getFoodNote());
        record.setSeason(getFoodSeason().name());
        record.setAmount(getFoodValue());
        record.setTotalAmount(getFoodValue());
        record.setUnit(getFoodUnit());
        diary.addRecord(record);
        CalendarTopComponent.getDefault().getModel().fillData();
        CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
        getFoodModel().setDate(getFoodDate());
    }
}//GEN-LAST:event_foodSaveActionPerformed

private void foodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodActionPerformed
    FoodDO foodf = (FoodDO) food.getSelectedItem();
    if (foodf != null) {
        foodUnit.setModel(createFoodUnitsModel(foodf));
    }
}//GEN-LAST:event_foodActionPerformed

private void foodTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_foodTableMouseClicked
    int row = foodTable.rowAtPoint(evt.getPoint());
    int column = foodTable.columnAtPoint(evt.getPoint());

    //int column = foodTable.columnAtPoint(e.getPoint());
    if (!evt.isPopupTrigger()) {
        RecordFoodDO rec = getFoodModel().getRecordAt(row);
        setRecordFoods(new RecordFoodDO[]{rec});
    }

    if (column == getFoodModel().getColumnCount() - 1 && getFoodModel().getRowCount() > 1) {
        if (row == getFoodModel().getRowCount() - 1) {
            for(RecordFoodDO food : getFoodModel().getRecordFoods()){
                diary.deleteRecord(food);
            }            
        } else {
            diary.deleteRecord(getFoodModel().getRecordAt(row));
        }
        CalendarTopComponent.getDefault().getModel().fillData();
        CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
        getFoodModel().fillData();
        getFoodModel().fireTableDataChanged();
    }
}//GEN-LAST:event_foodTableMouseClicked

private void investDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_investDelActionPerformed
    if (selectedInvestRecord != null) {
        diary.deleteRecord(selectedInvestRecord);
        selectedInvestRecord = null;
        investDel.setEnabled(false);
        CalendarTopComponent.getDefault().getModel().fillData();
        CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
        getInvestModel().fillData();
        getInvestModel().fireTableDataChanged();
    }
}//GEN-LAST:event_investDelActionPerformed

private void investSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_investSaveActionPerformed
    if (recordInvestValidation()) {
        RecordInvestDO recordInvest = new RecordInvestDO();
        RecordInvestPK pk = new RecordInvestPK(getInvest().getId(), diary.getCurrentPatient().getIdPatient(), getInvestDate());
        recordInvest.setId(pk);
        recordInvest.setInvest(getInvest());
        recordInvest.setNotice(getInvestNote());
        recordInvest.setSeason(getInvestSeason().name());
        recordInvest.setValue(getInvestValue());
        diary.addRecord(recordInvest);
        selectedInvestRecord = recordInvest;
        CalendarTopComponent.getDefault().getModel().fillData();
        CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
        getInvestModel().fillData();
        getInvestModel().fireTableDataChanged();
    }
}//GEN-LAST:event_investSaveActionPerformed

private void investActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_investActionPerformed
    InvestigationDO inv = (InvestigationDO) invest.getSelectedItem();
    investUnit.setModel(createUnitsModel(inv));
}//GEN-LAST:event_investActionPerformed

private void insulinDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insulinDelActionPerformed
    if (selectedInsulinRecord != null) {
        diary.deleteRecord(selectedInsulinRecord);
        selectedInsulinRecord = null;
        insulinDel.setEnabled(false);
        CalendarTopComponent.getDefault().getModel().fillData();
        CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
    }
}//GEN-LAST:event_insulinDelActionPerformed

private void insulinValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insulinValueActionPerformed
}//GEN-LAST:event_insulinValueActionPerformed

private void investValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_investValueActionPerformed
}//GEN-LAST:event_investValueActionPerformed

private void foodValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodValueActionPerformed
}//GEN-LAST:event_foodValueActionPerformed

private void investTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_investTableMouseClicked
    int row = investTable.rowAtPoint(evt.getPoint());
    int column = investTable.columnAtPoint(evt.getPoint());
    RecordInvestDO rec = getInvestModel().getRecord(row, column);
    setRecord(rec);
    if (column == getInvestModel().getColumnCount() - 1 && rec != null && rec.getValue() != null) {
        diary.deleteRecord(rec);
        investDel.setEnabled(false);
        CalendarTopComponent.getDefault().getModel().fillData();
        CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
        getInvestModel().fillData();
        getInvestModel().fireTableDataChanged();
    }
}//GEN-LAST:event_investTableMouseClicked

private void insulinTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_insulinTableMouseClicked
    int row = insulinTable.rowAtPoint(evt.getPoint());
    int column = insulinTable.columnAtPoint(evt.getPoint());
    RecordInsulinDO rec = getInsulinModel().getRecord(row, column);
    setRecord(rec);
    if (column == getInsulinModel().getColumnCount() - 1 && rec != null && rec.getAmount() != null) {
        diary.deleteRecord(rec);
        insulinDel.setEnabled(false);
        CalendarTopComponent.getDefault().getModel().fillData();
        CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
        getInsulinModel().fillData();
        getInsulinModel().fireTableDataChanged();
    }
}//GEN-LAST:event_insulinTableMouseClicked

private void activityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activityActionPerformed
}//GEN-LAST:event_activityActionPerformed

private void actSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actSaveActionPerformed
    if (recordActivityValidation()) {
        RecordActivityDO recordActivity = new RecordActivityDO();
        RecordActivityPK pk = new RecordActivityPK(getActivity().getId(), diary.getCurrentPatient().getIdPatient(), getActDate());
        recordActivity.setId(pk);
        recordActivity.setActivity(getActivity());
        recordActivity.setDuration(getActValue());
        recordActivity.setNotice(getActNote());
        diary.addRecord(recordActivity);
        selectedActivityRecord = recordActivity;
        CalendarTopComponent.getDefault().getModel().fillData();
        CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
        getActivityModel().fillData();
        getActivityModel().fireTableDataChanged();
    }
}//GEN-LAST:event_actSaveActionPerformed

private void actTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actTypeActionPerformed
    activity.setModel(createActComboModel((ActivityGroupDO) actType.getSelectedItem()));
}//GEN-LAST:event_actTypeActionPerformed

private void actValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actValueActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_actValueActionPerformed

private void actDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actDelActionPerformed
    if (selectedActivityRecord != null) {
        diary.deleteRecord(selectedActivityRecord);
        selectedActivityRecord = null;
        actDel.setEnabled(false);
        CalendarTopComponent.getDefault().getModel().fillData();
        CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
        getActivityModel().fillData();
        getActivityModel().fireTableDataChanged();
    }
}//GEN-LAST:event_actDelActionPerformed

private void actTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_actTableMouseClicked
    int row = actTable.rowAtPoint(evt.getPoint());
    int column = actTable.columnAtPoint(evt.getPoint());
    RecordActivityDO rec = getActivityModel().getRecord(row, column);
    setRecord(rec);
    if (column == getActivityModel().getColumnCount() - 1 && rec != null && rec.getDuration() != null) {
        diary.deleteRecord(rec);
        actDel.setEnabled(false);
        CalendarTopComponent.getDefault().getModel().fillData();
        CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
        getActivityModel().fillData();
        getActivityModel().fireTableDataChanged();
    }
}//GEN-LAST:event_actTableMouseClicked

private void insulinUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insulinUnitActionPerformed
    recordFoodValidation();
}//GEN-LAST:event_insulinUnitActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField actDate;
    private javax.swing.JButton actDel;
    private javax.swing.JLabel actError;
    private javax.swing.JTextArea actNote;
    private javax.swing.JButton actSave;
    private javax.swing.JTable actTable;
    private javax.swing.JTextField actTime;
    private javax.swing.JComboBox actType;
    private javax.swing.JFormattedTextField actValue;
    private javax.swing.JComboBox activity;
    private javax.swing.JTabbedPane activityPanel;
    private javax.swing.JComboBox baseGroup;
    private javax.swing.JComboBox food;
    private javax.swing.JTextField foodDatum;
    private javax.swing.JLabel foodError;
    private javax.swing.JComboBox foodGroup;
    private javax.swing.JTextArea foodNote;
    private javax.swing.JButton foodSave;
    private javax.swing.JComboBox foodSeason;
    private javax.swing.JTable foodTable;
    private javax.swing.JTextField foodTime;
    private javax.swing.JComboBox foodUnit;
    private javax.swing.JFormattedTextField foodValue;
    private javax.swing.JComboBox glykSeason;
    private javax.swing.JComboBox insulin;
    private javax.swing.JTextField insulinDatum;
    private javax.swing.JButton insulinDel;
    private javax.swing.JLabel insulinError;
    private javax.swing.JTextArea insulinNote;
    private javax.swing.JButton insulinSave;
    private javax.swing.JComboBox insulinSeason;
    private javax.swing.JTable insulinTable;
    private javax.swing.JTextField insulinTime;
    private javax.swing.JComboBox insulinType;
    private javax.swing.JComboBox insulinUnit;
    private javax.swing.JFormattedTextField insulinValue;
    private javax.swing.JComboBox invest;
    private javax.swing.JTextField investDatum;
    private javax.swing.JButton investDel;
    private javax.swing.JLabel investError;
    private javax.swing.JTextArea investNote;
    private javax.swing.JButton investSave;
    private javax.swing.JTable investTable;
    private javax.swing.JTextField investTime;
    private javax.swing.JComboBox investUnit;
    private javax.swing.JFormattedTextField investValue;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
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

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return RecordEditorTopComponent.getDefault();
        }
    }

    private ActivityDO getActivity() {
        return (ActivityDO) activity.getSelectedItem();
    }

    private void setActivity(ActivityDO act) {
        activity.setSelectedItem(act);
    }

    public Date getActDate() {
        try {
            return dateTimeFormat.parse(actDate.getText() + " " + actTime.getText());
        } catch (ParseException ex) {
            return null;
        }
    }

    public void setActDate(Date investDate) {
        actDate.setText(dateFormat.format(investDate));
        actTime.setText(timeFormat.format(investDate));
    }

    public Integer getActValue() {
        if (actValue.getValue() instanceof Number) {
            return ((Number) actValue.getValue()).intValue();
        }
        return null;
    }

    public void setActValue(Integer invest) {
        actValue.setValue(invest);
    }

    public void setActNote(String note) {
        actNote.setText(note);
    }

    public String getActNote() {
        return actNote.getText();
    }

    public InvestigationDO getInvest() {
        return (InvestigationDO) invest.getSelectedItem();
    }

    public void setInvest(InvestigationDO investig) {
        invest.setSelectedItem(investig);
    }

    public String getInvestUnit() {
        return investUnit.getSelectedItem().toString();
    }

    public void setInvestUnit(String investUnitS) {
        investUnit.setSelectedItem(investUnitS);
    }

    public Date getInvestDate() {
        try {
            return dateTimeFormat.parse(investDatum.getText() + " " + investTime.getText());
        } catch (ParseException ex) {
            return null;
        }
    }

    public void setInvestDate(Date investDate) {
        investDatum.setText(dateFormat.format(investDate));
        investTime.setText(timeFormat.format(investDate));
    }

    public InvSeason getInvestSeason() {
        return (InvSeason) glykSeason.getSelectedItem();
    }

    public void setInvestSeason(InvSeason investSeason) {
        glykSeason.setSelectedItem(investSeason);
    }

    public Double getInvestValue() {
        if (investValue.getValue() instanceof Number) {
            return ((Number) investValue.getValue()).doubleValue();
        }
        return null;
    }

    public void setInvestValue(Double invest) {
        investValue.setValue(invest);
    }

    public String getInvestNote() {
        return investNote.getText();
    }

    public void setInvestNote(String note) {
        investNote.setText(note);
    }

    //insulin start
    public InsulinDO getInsulin() {
        return (InsulinDO) insulin.getSelectedItem();
    }

    public void setInsulin(InsulinDO ins) {
        insulin.setSelectedItem(ins);
    }

    public String getInsulinUnit() {
        return insulinUnit.getSelectedItem().toString();
    }

    public void setInsulinUnit(String insulin) {
        insulinUnit.setSelectedItem(insulin);
    }

    public Date getInsulinDate() {
        try {
            return dateTimeFormat.parse(insulinDatum.getText() + " " + insulinTime.getText());
        } catch (ParseException ex) {
            return null;
        }
    }

    public void setInsulinDate(Date insDate) {
        insulinDatum.setText(dateFormat.format(insDate));
        insulinTime.setText(timeFormat.format(insDate));
    }

    public InsulinSeason getInsulinSeason() {
        return (InsulinSeason) insulinSeason.getSelectedItem();
    }

    public void setInsulinSeason(InsulinSeason insSeason) {
        insulinSeason.setSelectedItem(insSeason);
    }

    public Double getInsulinValue() {
        if (insulinValue.getValue() instanceof Number) {
            return ((Number) insulinValue.getValue()).doubleValue();
        }
        return null;
    }

    public void setInsulinValue(Double insulin) {
        insulinValue.setValue(insulin);
    }

    //insulin end
    public FoodDO getFood() {
        return (FoodDO) food.getSelectedItem();
    }

    public FoodGroupDO getBaseFoodGroup() {
        return (FoodGroupDO) baseGroup.getSelectedItem();
    }

    public FoodGroupDO getFoodGroup() {
        return (FoodGroupDO) foodGroup.getSelectedItem();
    }

    public void setFood(FoodDO foodf) {
        food.setSelectedItem(foodf);
    }

    public String getFoodUnit() {
        if (foodUnit.getSelectedItem() != null) {
            return ((FoodUnitDO) foodUnit.getSelectedItem()).getId().getUnit();
        }
        return null;
    }

    public void setFoodUnit(String foodUnitS) {
        foodUnit.setSelectedItem(foodUnitS);
    }

    public Date getFoodDate() {
        try {
            return dateTimeFormat.parse(foodDatum.getText() + " " + foodTime.getText());
        } catch (ParseException ex) {
            return null;
        }
    }

    public void setFoodDate(Date foodDate) {
        foodDatum.setText(dateFormat.format(foodDate));
        foodTime.setText(timeFormat.format(foodDate));
    }

    public FoodSeason getFoodSeason() {
        return (FoodSeason) foodSeason.getSelectedItem();
    }

    public void setFoodSeason(FoodSeason foodSeas) {
        foodSeason.setSelectedItem(foodSeas);
    }

    public Double getFoodValue() {
        return ((Number) foodValue.getValue()).doubleValue();
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

    public String getInsulinNote() {
        return insulinNote.getText();
    }

    public void setInsulinNote(String note) {
        insulinNote.setText(note);
    }

    private boolean recordInvestValidation() {
        investSave.setVisible(false);
        if (diary.getCurrentPatient() == null) {
            setInvestError(NbBundle.getMessage(RecordEditorTopComponent.class, "noopendiary"));
            return false;
        }
        if (getInvestDate() == null) {
            setInvestError(NbBundle.getMessage(RecordEditorTopComponent.class, "nocorrectdate"));
            return false;
        }
        if (getInvestValue() == null) {
            setInvestError(NbBundle.getMessage(RecordEditorTopComponent.class, "invalidamount"));
            return false;
        }
        setInvestError(null);
        investSave.setVisible(true);
        return true;
    }

    private void setInsulinError(String error) {
        if (error == null) {
            insulinError.setText("");
            insulinError.setVisible(false);
        }
        insulinError.setText(error);
        insulinError.setVisible(true);
    }

    private boolean recordInsulinValidation() {
        insulinSave.setVisible(false);
        if (diary.getCurrentPatient() == null) {
            setInsulinError(NbBundle.getMessage(RecordEditorTopComponent.class, "noopendiary"));
            return false;
        }
        if (getInsulinDate() == null) {
            setInsulinError(NbBundle.getMessage(RecordEditorTopComponent.class, "nocorrectdate"));
            return false;
        }
        if (getInsulinValue() == null) {
            setInsulinError(NbBundle.getMessage(RecordEditorTopComponent.class, "invalidamount"));
            return false;
        }
        setInsulinError(null);
        insulinSave.setVisible(true);
        return true;
    }

    private void setInvestError(String error) {
        if (error == null) {
            investError.setText("");
            investError.setVisible(false);
        }
        investError.setText(error);
        investError.setVisible(true);
    }

    private boolean recordFoodValidation() {
        foodSave.setVisible(false);
        if (diary.getCurrentPatient() == null) {
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

    private boolean recordActivityValidation() {
        actSave.setVisible(false);
        if (diary.getCurrentPatient() == null) {
            setActError(NbBundle.getMessage(RecordEditorTopComponent.class, "noopendiary"));
            return false;
        }
        if (getActDate() == null) {
            setActError(NbBundle.getMessage(RecordEditorTopComponent.class, "nocorrectdate"));
            return false;
        }
        if (getActValue() == null) {
            setActError(NbBundle.getMessage(RecordEditorTopComponent.class, "invalidamountfood"));
            return false;
        }
        setActError(null);
        actSave.setVisible(true);
        return true;
    }

    private void setActError(String error) {
        if (error == null) {
            actError.setText("");
            actError.setVisible(false);
        }
        actError.setText(error);
        actError.setVisible(true);
    }
    private RecordInvestDO selectedInvestRecord;
    private RecordInsulinDO selectedInsulinRecord;
    private RecordActivityDO selectedActivityRecord;

    public void setRecordInvests(RecordInvestDO[] recs) {
        selectedInvestRecord = null;
        investDel.setEnabled(false);

        if (recs.length > 0 && recs[0] != null) {
            RecordInvestDO rec = recs[0];
            if (rec.getValue() != null) {
                selectedInvestRecord = rec;
                investDel.setEnabled(true);
            }
            getInvestModel().setDate(rec.getId().getDate());
            activityPanel.setSelectedComponent(jPanel1);
            setInvestDate(rec.getId().getDate());
            setInvest(rec.getInvest());
            setInvestValue(rec.getValue() != null ? rec.getValue() : 0d);
            if (rec.getSeason() != null) {
                setInvestSeason(InvSeason.valueOf(rec.getSeason()));
            }
            setInvestNote(rec.getNotice());
        }
        getInvestModel().fillData();
        getInvestModel().fireTableDataChanged();
    }

    public void setRecordFoods(RecordFoodDO[] recs) {
        if (recs.length > 0 && recs[0] != null) {
            RecordFoodDO rec = recs[0];
            activityPanel.setSelectedComponent(jPanel5);
            setFoodValue(rec.getAmount());
            foodModel.setDate(rec.getId().getDate());
            setFoodDate(rec.getId().getDate());
            setFoodNote(rec.getNotice());
            FoodGroupDO foodGrp = foodAdmin.getFoodGroup(rec.getFood().getFoodGroup().getId());
            if (foodGrp.getParent() != null && foodGrp.getParent().getId() != null) {
                FoodGroupDO parent = foodAdmin.getFoodGroup(foodGrp.getParent().getId());
                baseGroup.setSelectedItem(parent);
                foodGroup.setSelectedItem(foodGrp);
            } else {
                baseGroup.setSelectedItem(foodGrp);
            }
            setFood(rec.getFood());
            setFoodUnit(rec.getUnit());
            if (rec.getSeason() != null) {
                setFoodSeason(FoodSeason.valueOf(rec.getSeason()));
            }
        }
        getFoodModel().fillData();
        getFoodModel().fireTableDataChanged();
    }

    public void setRecordInsulins(RecordInsulinDO[] recs) {
        selectedInsulinRecord = null;
        insulinDel.setEnabled(false);

        if (recs.length > 0 && recs[0] != null) {
            RecordInsulinDO rec = recs[0];
            getInsulinModel().setDate(rec.getId().getDate());
            setInsulinNote(rec.getNotice());
            if (rec.getAmount() != null) {
                selectedInsulinRecord = rec;
                insulinDel.setEnabled(true);
            }
            activityPanel.setSelectedComponent(jPanel6);
            setInsulinDate(rec.getId().getDate());
            insulinType.setSelectedItem(rec.getInsulin().getType());
            setInsulin(rec.getInsulin());
            setInsulinValue(rec.getAmount() != null ? rec.getAmount() : 0d);
            insulinUnit.setSelectedItem(InsUnit.getInstance(rec.isBasal()));
            if (rec.getSeason() != null) {
                setInsulinSeason(InsulinSeason.valueOf(rec.getSeason()));
            }
            getInsulinModel().fillData();
            getInsulinModel().fireTableDataChanged();
        }
    }

    public void setRecordActivities(RecordActivityDO[] recs) {
        selectedActivityRecord = null;
        actDel.setEnabled(false);

        if (recs.length > 0 && recs[0] != null) {
            RecordActivityDO rec = recs[0];
            getActivityModel().setDate(rec.getId().getDate());
            setActNote(rec.getNotice());
            if (rec.getDuration() != null) {
                selectedActivityRecord = rec;
                actDel.setEnabled(true);
            }
            activityPanel.setSelectedComponent(jPanel7);
            setActDate(rec.getId().getDate());
            actType.setSelectedItem(rec.getActivity().getActivityGroup());
            setActivity(rec.getActivity());
            setActValue(rec.getDuration() != null ? rec.getDuration() : 0);
            getActivityModel().fillData();
            getActivityModel().fireTableDataChanged();
        }
    }

    public void setRecord(Object record) {
        if (record instanceof RecordInvestDO[]) {
            RecordInvestDO[] recs = (RecordInvestDO[]) record;
            setRecordInvests(recs);
        } else if (record instanceof RecordInvestDO) {
            setRecordInvests(new RecordInvestDO[]{(RecordInvestDO) record});
        } else if (record instanceof RecordInsulinDO[]) {
            RecordInsulinDO[] recs = (RecordInsulinDO[]) record;
            setRecordInsulins(recs);
        } else if (record instanceof RecordInsulinDO) {
            setRecordInsulins(new RecordInsulinDO[]{(RecordInsulinDO) record});
        } else if (record instanceof RecordActivityDO[]) {
            RecordActivityDO[] recs = (RecordActivityDO[]) record;
            setRecordActivities(recs);
        } else if (record instanceof RecordActivityDO) {
            setRecordActivities(new RecordActivityDO[]{(RecordActivityDO) record});
        } else if (record instanceof RecordFoodDO) {
            setRecordFoods(new RecordFoodDO[]{(RecordFoodDO) record});
        } else if (record instanceof RecordFoodDO[]) {
            setRecordFoods((RecordFoodDO[]) record);
        } else if (record instanceof RecordInsulinPumpBasal) {
            setRecordInsulins(((RecordInsulinPumpBasal) record).getData());
        }
    }
}