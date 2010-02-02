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

package org.diabetesdiary.model.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import org.diabetesdiary.datamodel.api.DbLookUp;
import org.diabetesdiary.datamodel.api.Diary;
import org.diabetesdiary.datamodel.api.InsulinAdministrator;
import org.diabetesdiary.datamodel.pojo.Insulin;
import org.diabetesdiary.datamodel.pojo.InsulinType;
import org.diabetesdiary.datamodel.pojo.Patient;
import org.diabetesdiary.datamodel.pojo.RecordFood;
import org.diabetesdiary.datamodel.pojo.RecordInsulin;
import org.diabetesdiary.model.SREnum;
import org.diabetesdiary.model.SimulationManager;
import org.diabetesdiary.model.SimulationResult;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.openide.DialogDisplayer;
import org.openide.ErrorManager;
import org.openide.NotifyDescriptor;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
final class SimulationTopComponentTopComponent extends TopComponent {
    
    private InsulinTableModel insModel = new InsulinTableModel(false);;
    private InsulinPumpTableModel insPumpModel;
    private MealTableModel mealModel;
    private Date beginDate;
    private Date endDate;
    
    private static SimulationTopComponentTopComponent instance;
    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "org/diabetesdiary/model/resources/simulation.png";
    static final String ICON_PATH_SMALL = "org/diabetesdiary/model/resources/simulation16.png";
    
    private static final String PREFERRED_ID = "SimulationTopComponentTopComponent";
    
    private SimulationTopComponentTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(SimulationTopComponentTopComponent.class, "CTL_SimulationTopComponentTopComponent"));
        setToolTipText(NbBundle.getMessage(SimulationTopComponentTopComponent.class, "HINT_SimulationTopComponentTopComponent"));
        setIcon(Utilities.loadImage(ICON_PATH_SMALL, true));
        DateFormat form = DateFormat.getDateInstance(DateFormat.MEDIUM);
        try {
            dataDate.setValue(form.parse(form.format(new Date())));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        
        weight.setValue(70d);
        weight.setColumns(4);
        insSens1.setValue(0.5f);
        insSens2.setValue(0.5f);
        renalThres.setValue(9.0f);
        ccr.setValue(100f);
        
        
        pen.setSelected(true);
        pump.setSelected(false);
        jScrollPane3.setVisible(false);
        graphChose.setModel(createGraphModel());
        pumpComb.setVisible(false);
        jLabel7.setVisible(false);
        
        JFormattedTextField dateField = new JFormattedTextField(new SimpleDateFormat("HH:mm"));
        JFormattedTextField numberField = new JFormattedTextField(NumberFormat.getInstance());
        mealTable.setDefaultEditor(Date.class,new DefaultCellEditor(dateField));
        insTable.setDefaultEditor(Date.class,new DefaultCellEditor(dateField));
        insTable.setDefaultEditor(Double.class,new DefaultCellEditor(numberField));
        insPumpTable.setDefaultEditor(Double.class,new DefaultCellEditor(numberField));
        
    }
    
    
    private InsulinTableModel getInsModel(){
        return insModel;
    }
    
    
    private MealTableModel getMealModel(){
        if(mealModel == null){
            mealModel = new MealTableModel();
        }
        return mealModel;
    }
    
    private InsulinPumpTableModel getInsPumpModel(){
        if(insPumpModel == null){
            insPumpModel = new InsulinPumpTableModel();
        }
        return insPumpModel;
    }
    
    public float getWeight(){
        return ((Number)weight.getValue()).floatValue();
    }
    
    public float getInsSensitivityHepatic(){
        return ((Number)insSens1.getValue()).floatValue();
    }
    
    public float getInsSensitivityPeriph(){
        return ((Number)insSens2.getValue()).floatValue();
    }
    
    public float getRenalThreshold(){
        return ((Number)renalThres.getValue()).floatValue();
    }
    
    public float getGlomerularFiltrationRate(){
        return ((Number)ccr.getValue()).floatValue();
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        treatment = new javax.swing.ButtonGroup();
        treatment.add(pump);
        treatment.add(pen);
        jScrollPane1 = new javax.swing.JScrollPane();
        insTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        mealTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        insPumpTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        basalComb = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        bolusComb = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        pumpComb = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        insSens1 = new JFormattedTextField(NumberFormat.getInstance());
        jLabel3 = new javax.swing.JLabel();
        insSens2 = new JFormattedTextField(NumberFormat.getInstance());
        renalThres = new JFormattedTextField(NumberFormat.getInstance());
        jLabel4 = new javax.swing.JLabel();
        weight = new JFormattedTextField(NumberFormat.getInstance());
        jLabel1 = new javax.swing.JLabel();
        ccr = new JFormattedTextField(NumberFormat.getInstance());
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        graphChose = new javax.swing.JComboBox();
        startSim = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        pen = new javax.swing.JRadioButton();
        pump = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        dataDate = new JFormattedTextField(DateFormat.getDateInstance(DateFormat.MEDIUM));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(NbBundle.getMessage(SimulationTopComponentTopComponent.class,"Injekce_inzulinu")));
        insTable.setModel(getInsModel());
        jScrollPane1.setViewportView(insTable);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(NbBundle.getMessage(SimulationTopComponentTopComponent.class,"prijemsacharidu")));
        mealTable.setModel(getMealModel());
        jScrollPane2.setViewportView(mealTable);

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(NbBundle.getMessage(SimulationTopComponentTopComponent.class,"hlaskabazalpumpa")));
        insPumpTable.setModel(getInsPumpModel());
        jScrollPane3.setViewportView(insPumpTable);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(NbBundle.getMessage(SimulationTopComponentTopComponent.class,"pouzivane_inzuliny")));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, NbBundle.getMessage(SimulationTopComponentTopComponent.class,"Basal"));

        basalComb.setModel(createInsulinTypeModel());

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, NbBundle.getMessage(SimulationTopComponentTopComponent.class,"Bolus"));

        bolusComb.setModel(createInsulinTypeModel());

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, NbBundle.getMessage(SimulationTopComponentTopComponent.class,"Pumpa"));

        pumpComb.setModel(createInsulinTypeModel());

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel7)
                    .add(jLabel5)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, basalComb, 0, 168, Short.MAX_VALUE)
                    .add(pumpComb, 0, 168, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, bolusComb, 0, 168, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(bolusComb, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(basalComb, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(pumpComb, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(NbBundle.getMessage(SimulationTopComponentTopComponent.class,"Parametry_pacienta")));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, NbBundle.getMessage(SimulationTopComponentTopComponent.class,"Citlivost_na_inzulinjatra"));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, NbBundle.getMessage(SimulationTopComponentTopComponent.class,"Citlivost_na_inzulin_zbytek"));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, NbBundle.getMessage(SimulationTopComponentTopComponent.class,"renaltreshold"));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, NbBundle.getMessage(SimulationTopComponentTopComponent.class,"Hmotnost_(Kg):"));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, NbBundle.getMessage(SimulationTopComponentTopComponent.class,"Funkce_ledvin:"));

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(insSens1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel3)
                                    .add(jLabel1))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(weight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(insSens2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jLabel8))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel2)
                        .add(79, 79, 79)
                        .add(jLabel4)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(renalThres, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(ccr, 0, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(insSens1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4)
                    .add(renalThres, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(insSens2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(ccr, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel8))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(weight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(NbBundle.getMessage(SimulationTopComponentTopComponent.class,"chart")));
        graphChose.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        graphChose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphChoseActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(startSim, NbBundle.getMessage(SimulationTopComponentTopComponent.class,"Spustit_simulaci"));
        startSim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startSimActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(graphChose, 0, 240, Short.MAX_VALUE)
            .add(jPanel3Layout.createSequentialGroup()
                .add(54, 54, 54)
                .add(startSim)
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(graphChose, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(startSim, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(NbBundle.getMessage(SimulationTopComponentTopComponent.class,"zpusoblecby")));
        pen.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(pen, NbBundle.getMessage(SimulationTopComponentTopComponent.class,"inspen"));
        pen.setActionCommand("pen");
        pen.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pen.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(pump, NbBundle.getMessage(SimulationTopComponentTopComponent.class,"ins_pump"));
        pump.setActionCommand("pump");
        pump.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pump.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pump.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pumpActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pump)
                    .add(pen))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(pump)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pen)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(NbBundle.getMessage(SimulationTopComponentTopComponent.class,"nactenidat.title")));
        org.openide.awt.Mnemonics.setLocalizedText(jButton1, NbBundle.getMessage(SimulationTopComponentTopComponent.class,"nacistdata.button"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(65, 65, 65)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, dataDate)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE))
                .addContainerGap(89, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(dataDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 15, Short.MAX_VALUE)
                .add(jButton1)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 728, Short.MAX_VALUE))))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 954, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 974, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(16, 16, 16)
                        .add(jScrollPane2, 0, 0, Short.MAX_VALUE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel1, 0, 93, Short.MAX_VALUE)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(DbLookUp.getDiary().getCurrentPatient() != null){
            Patient pat = DbLookUp.getDiary().getCurrentPatient();
            pat.getIdPatient();
            pumpActionPerformed(new ActionEvent(this,
              ActionEvent.ACTION_PERFORMED, pat.isPumpUsage() ? "pump" : "pen"));
            ccr.setValue(pat.getFiltrationRate());
            renalThres.setValue(pat.getRenalThreshold());
            insSens1.setValue(pat.getHepSensitivity());
            insSens2.setValue(pat.getPerSensitivity());
            basalComb.setSelectedItem(pat.getBasalInsulin());
            bolusComb.setSelectedItem(pat.getBolusInsulin());
            pumpComb.setSelectedItem(pat.getBolusInsulin());
            Diary diary = DbLookUp.getDiary();
            Date datDat = (Date) dataDate.getValue();
            List<RecordFood> foods = diary.getRecordFoods(datDat, new Date(datDat.getTime() + 24 * 60 * 60 * 1000), pat.getIdPatient());
            List<RecordInsulin> ins = diary.getRecordInsulins(datDat, new Date(datDat.getTime() + 24 * 60 * 60 * 1000), pat.getIdPatient());
            mealModel.setRecordFoods(foods);
            insModel.setRecordInsulins(ins);
            insPumpModel.setRecordInsulins(ins);
        }else{
            DialogDisplayer.getDefault().notify(
                    new NotifyDescriptor.Message(
                    NbBundle.getMessage(SimulationTopComponentAction.class,"nopatient"), NotifyDescriptor.WARNING_MESSAGE));
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void drawSelectedGraph(){
        if(graphChose.getSelectedItem() != null){
            SREnum en = (SREnum)graphChose.getSelectedItem();
            ChartPanel panel1 = createChart(createSeries(en,en.name(),res),en.getUnit());
            jScrollPane4.setViewportView(panel1);
        }
    }
    
    private void graphChoseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_graphChoseActionPerformed
        drawSelectedGraph();
    }//GEN-LAST:event_graphChoseActionPerformed
    
    
    public TimeSeries createSeries(SREnum sre, String name, SimulationResult profile){
        TimeSeries series = new TimeSeries(sre.toString(),Millisecond.class);
        if(profile != null){
            for(Date date : profile.getProfile().keySet()){
                if((date.after(beginDate) && date.before(endDate))){
                    double value = 0;
                    switch(sre){
                        case ACTIVE_INSULIN: value = profile.getProfile().get(date).getActiveInsulin();
                        break;
                        case EQ_INSULIN: value = profile.getProfile().get(date).getEqInsulin();
                        break;
                        case GLUCOSE: value = profile.getProfile().get(date).getGlucose();
                        break;
                        case GLUCOSE_GUT: value = profile.getProfile().get(date).getGlucoseInGut();
                        break;
                        case GLUCOSE_UTIL: value = profile.getProfile().get(date).getGlucoseUtilisation();
                        break;
                        case NHGB: value = profile.getProfile().get(date).getNhgb();
                        break;
                        case PLASMA_INSULIN: value = profile.getProfile().get(date).getPlasmaInsulin();
                        break;
                        case RENAL_EXCRETION: value = profile.getProfile().get(date).getRenalGlucoseExcretion();
                        break;
                    }
                    series.add(new Millisecond(date),value);
                }
            }
        }
        return series;
    }
    
    
    public ChartPanel createChart(TimeSeries series, String name){
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(null,"Date",name,dataset,true,true,false);
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseZoomable(true, false);
        panel.setPreferredSize(new Dimension(800,100));
        chart.setAntiAlias(false);
        return panel;
    }
    
    private void startSimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startSimActionPerformed
        SimulationManager manager = new SimulationManager(getInsSensitivityHepatic(),getInsSensitivityPeriph(),getWeight());
        double gfr = getGlomerularFiltrationRate();
        //convert from ml/min => l/h
        manager.setGlomerularFiltration(gfr*6/100);
        manager.setRenalThreshold(getRenalThreshold());
        Calendar now = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,3);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        beginDate = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH,4);
        endDate = cal.getTime();
        
        cal.set(Calendar.DAY_OF_MONTH,1);
        
        
        for(int day=1;day<4;day++){
            Map<Date,Double> meals = mealModel.getMeals();
            for(Date date : meals.keySet()){
                cal.setTime(date);
                cal.set(Calendar.DAY_OF_MONTH,day);
                cal.set(Calendar.MONTH,now.get(Calendar.MONTH));
                cal.set(Calendar.YEAR,now.get(Calendar.YEAR));
                manager.addCarbohydrateIntake(meals.get(date),cal.getTime());
            }
            
            Map<Date,Double> boluses = insModel.getBoluses();
            for(Date date : boluses.keySet()){
                cal.setTime(date);
                cal.set(Calendar.DAY_OF_MONTH,day);
                cal.set(Calendar.MONTH,now.get(Calendar.MONTH));
                cal.set(Calendar.YEAR,now.get(Calendar.YEAR));
                InsulinType type = null;
                if(pump.isSelected()){
                    type = ((Insulin)pumpComb.getSelectedItem()).getType();
                }else{
                    type = ((Insulin)bolusComb.getSelectedItem()).getType();
                }
                manager.addInsulinInjection(type,boluses.get(date),cal.getTime());
            }
            
            if(pump.isSelected()){
                cal.setTime(now.getTime());
                cal.set(Calendar.DAY_OF_MONTH,day);
                InsulinType type = ((Insulin)pumpComb.getSelectedItem()).getType();
                manager.addBasalInsulinForDay(cal.getTime(),type,3,insPumpModel.getBasales());
            } else{
                Map<Date,Double> basals = insModel.getBasals();
                for(Date date : basals.keySet()){
                    cal.setTime(date);
                    cal.set(Calendar.DAY_OF_MONTH,day);
                    cal.set(Calendar.MONTH,now.get(Calendar.MONTH));
                    cal.set(Calendar.YEAR,now.get(Calendar.YEAR));
                    InsulinType type = ((Insulin)basalComb.getSelectedItem()).getType();
                    manager.addInsulinInjection(type,basals.get(date),cal.getTime());
                }
            }
            
        }
        res = manager.getSimulationResult();
        jPanel3.setVisible(true);
        drawSelectedGraph();
    }//GEN-LAST:event_startSimActionPerformed
    
    public ComboBoxModel createGraphModel(){
        DefaultComboBoxModel model = new DefaultComboBoxModel(SREnum.getGraphEnums());
        model.setSelectedItem(SREnum.GLUCOSE);
        return model;
    }
    
    
    private InsulinAdministrator insAdmin;
    
    private InsulinAdministrator getInsAdmin(){
        if(insAdmin == null){
            Lookup lookup = Lookup.getDefault();
            insAdmin = (InsulinAdministrator)lookup.lookup(InsulinAdministrator.class);
            if (insAdmin == null) {
                // this will show up as a flashing round button in the bottom-right corner
                ErrorManager.getDefault().notify(
                        new IllegalStateException("Cannot locate InsulinAdministrator implementation"));
            }
        }
        return insAdmin;
    }
    
    public ComboBoxModel createInsulinTypeModel(){
        List<Insulin> insulines = getInsAdmin().getInsulines();
        DefaultComboBoxModel model = new DefaultComboBoxModel(insulines.toArray());
        return model;
    }
    
    
    private SimulationResult res;
    
    
    
    private void penActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penActionPerformed
        pumpActionPerformed(evt);
    }//GEN-LAST:event_penActionPerformed
    
    private void pumpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pumpActionPerformed
        boolean pumpSel = evt.getActionCommand().equals("pump");
        pump.setSelected(pumpSel);
        pen.setSelected(!pumpSel);
        jScrollPane3.setVisible(pumpSel);
        pumpComb.setVisible(pumpSel);
        jLabel7.setVisible(pumpSel);
        basalComb.setVisible(!pumpSel);
        jLabel6.setVisible(!pumpSel);
        bolusComb.setVisible(!pumpSel);
        jLabel5.setVisible(!pumpSel);
        insModel.setPumpUsage(pumpSel);
        revalidate();
    }//GEN-LAST:event_pumpActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox basalComb;
    private javax.swing.JComboBox bolusComb;
    private javax.swing.JFormattedTextField ccr;
    private javax.swing.JFormattedTextField dataDate;
    private javax.swing.JComboBox graphChose;
    private javax.swing.JTable insPumpTable;
    private javax.swing.JFormattedTextField insSens1;
    private javax.swing.JFormattedTextField insSens2;
    private javax.swing.JTable insTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable mealTable;
    private javax.swing.JRadioButton pen;
    private javax.swing.JRadioButton pump;
    private javax.swing.JComboBox pumpComb;
    private javax.swing.JFormattedTextField renalThres;
    private javax.swing.JButton startSim;
    private javax.swing.ButtonGroup treatment;
    private javax.swing.JFormattedTextField weight;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized SimulationTopComponentTopComponent getDefault() {
        if (instance == null) {
            instance = new SimulationTopComponentTopComponent();
        }
        return instance;
    }
    
    /**
     * Obtain the SimulationTopComponentTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized SimulationTopComponentTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find SimulationTopComponent component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof SimulationTopComponentTopComponent) {
            return (SimulationTopComponentTopComponent)win;
        }
        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }
    
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }
    
    public void componentOpened() {
        // TODO add custom code on component opening
    }
    
    public void componentClosed() {
        // TODO add custom code on component closing
    }
    
    /** replaces this in object stream */
    public Object writeReplace() {
        return new ResolvableHelper();
    }
    
    protected String preferredID() {
        return PREFERRED_ID;
    }
    
    final static class ResolvableHelper implements Serializable {
        private static final long serialVersionUID = 1L;
        public Object readResolve() {
            return SimulationTopComponentTopComponent.getDefault();
        }
    }
    
}
