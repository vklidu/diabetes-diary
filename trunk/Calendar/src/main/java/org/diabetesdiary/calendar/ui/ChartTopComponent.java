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

import org.diabetesdiary.calendar.utils.FormatUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.diary.utils.MyLookup;
import org.diabetesdiary.diary.api.DiaryRepository;
import org.diabetesdiary.diary.domain.FoodUnit;
import org.diabetesdiary.diary.domain.InsulinType;
import org.diabetesdiary.diary.domain.Investigation;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.domain.RecordFood;
import org.diabetesdiary.diary.domain.RecordInsulin;
import org.diabetesdiary.diary.domain.RecordInvest;
import org.diabetesdiary.simulator.Hemoglobin;
import org.diabetesdiary.simulator.SREnum;
import org.diabetesdiary.simulator.SimulationManager;
import org.diabetesdiary.simulator.SimulationResult;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;
import org.joda.time.DateTime;
import org.openide.ErrorManager;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
public final class ChartTopComponent extends TopComponent implements ListSelectionListener, PropertyChangeListener {

    private static ChartTopComponent instance;
    /** path to the icon used by the component and its open action */
    public static final String ICON_PATH = "org/diabetesdiary/calendar/resources/chart.png";
    private static final String PREFERRED_ID = "ChartTopComponent";
    private DiaryRepository diary = MyLookup.getDiaryRepo();
    private int row;
    private int max;

    private ChartTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ChartTopComponent.class, "CTL_ChartTopComponent"));
        setToolTipText(NbBundle.getMessage(ChartTopComponent.class, "HINT_ChartTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        //insulinRadio.setVisible(false);
        thisWeekRadio.setVisible(false);
        dateFrom.setValue(new Date());
        dateFrom.addPropertyChangeListener("value", this);
        dateTo.setValue(new Date());
        dateTo.addPropertyChangeListener("value", this);
        setError(null);
    }

    private SimulationResult simulate(DateTime from, DateTime to) {

        if (diary == null) {
            // this will show up as a flashing round button in the bottom-right corner
            ErrorManager.getDefault().notify(
                    new IllegalStateException("Cannot locate Diary implementation"));
        } else {
            Patient currPati = MyLookup.getCurrentPatient();
            SimulationManager man = new SimulationManager(currPati.getHepSensitivity().floatValue(), currPati.getPerSensitivity().floatValue(), 70.0f);
            List<RecordFood> foods = currPati.getRecordFoods(from, to);
            for (RecordFood food : foods) {
                man.addCarbohydrateIntake(food.getAmount(), food.getDatetime().toDate());
            }

            List<RecordInsulin> insulins = currPati.getRecordInsulins(from, to);
            for (RecordInsulin ins : insulins) {
                man.addInsulinInjection(ins.getInsulin().getType(), ins.getAmount(), ins.getDatetime().toDate());
            }
            return man.getSimulationResult();
        }
        return null;
    }

    public void createChart() {
        if (!isOpened() || !valid() || MyLookup.getCurrentPatient() == null) {
            return;
        }

        if (insulinRadio.isSelected()) {
            List<RecordInsulin> insulins = MyLookup.getCurrentPatient().getRecordInsulins(new DateTime(getFrom()), new DateTime(getTo()));
            //TimeSeries series = new TimeSeries(NbBundle.getMessage(ChartTopComponent.class, "insulin"), Minute.class);
            Map<InsulinType, TimeSeries> insSer = new HashMap<InsulinType, TimeSeries>();
            for (RecordInsulin rec : insulins) {
                Hour sec = new Hour(rec.getDatetime().toDate());
                if (insSer.get(rec.getInsulin().getType()) == null) {
                    TimeSeries ser = new TimeSeries(rec.getInsulin().getType().getName(), Hour.class);
                    ser.add(sec, rec.getAmount());
                    insSer.put(rec.getInsulin().getType(), ser);
                } else {
                    TimeSeries ser = insSer.get(rec.getInsulin().getType());
                    if (ser.getDataItem(sec) != null) {
                        ser.getDataItem(sec).setValue(ser.getDataItem(sec).getValue().doubleValue() + rec.getAmount());
                    } else {
                        ser.add(sec, rec.getAmount());
                    }
                }
            }
            TimeSeriesCollection dataset = new TimeSeriesCollection();
            for(TimeSeries ser : insSer.values()){
                dataset.addSeries(ser);
            }
            JFreeChart chart = ChartFactory.createTimeSeriesChart(null, NbBundle.getMessage(CalendarTopComponent.class, "Chart_date"), null, dataset, true, true, false);
            XYPlot plot = chart.getXYPlot();
            plot.setRenderer(0,new ClusteredXYBarRenderer(0.5,true));            
            viewChart(chart);
        } else if (investRadio.isSelected() && getInvest() != null) {
            List<RecordInvest> invests = MyLookup.getCurrentPatient().getRecordInvests(getFrom(), getTo());
            TimeSeries series = new TimeSeries(getInvest().getName(), Minute.class);
            for (RecordInvest rec : invests) {
                if (rec.getInvest().equals(getInvest())) {
                    Minute sec = new Minute(rec.getDatetime().toDate());
                    series.add(sec, rec.getValue());
                }
            }
            TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(series);
            JFreeChart chart = ChartFactory.createTimeSeriesChart(null, NbBundle.getMessage(CalendarTopComponent.class, "Chart_date"), null, dataset, true, true, false);

            //if type of investigation is glycemie, low and high bounds are drawed and simulation result are drawed too
            if (investRadio.isSelected() && getInvest() != null && getInvest().getId() == 1) {
                XYPlot plot = (XYPlot) chart.getPlot();
                double lowGly = Double.valueOf(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_GLYKEMIE_LOW_NORMAL));
                Marker start = new ValueMarker(lowGly);
                start.setPaint(Color.green);
                start.setLabel(NbBundle.getMessage(CalendarTopComponent.class, "Chart_lowBound"));
                start.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
                start.setLabelTextAnchor(TextAnchor.TOP_LEFT);
                plot.addRangeMarker(start);

                double highGly = Double.valueOf(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_GLYKEMIE_HIGH_NORMAL));
                Marker target = new ValueMarker(highGly);
                target.setPaint(Color.green);
                target.setLabel(NbBundle.getMessage(CalendarTopComponent.class, "Chart_highBound"));
                target.setLabelAnchor(RectangleAnchor.TOP_LEFT);
                target.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
                plot.addRangeMarker(target);

                //dataset.addSeries(createSeries(SREnum.GLUCOSE, "simulace", simulate(new Date(getFrom().getTime() - 24 * 60 * 60 * 1000), getTo())));
            }
            viewChart(chart);
        } else if (foodRadio.isSelected()) {
            List<RecordFood> foods = MyLookup.getCurrentPatient().getRecordFoods(getFrom(), getTo());
            double sumSach = 0;
            double sumFat = 0;
            double sumProt = 0;
            for (RecordFood rec : foods) {
                FoodUnit unit = (FoodUnit) rec.getFood().getUnits().toArray()[0];
                sumSach += rec.getFood().getSugar() / 100 * rec.getAmount() * unit.getKoef();
                sumFat += rec.getFood().getFat() / 100 * rec.getAmount() * unit.getKoef();
                sumProt += rec.getFood().getProtein() / 100 * rec.getAmount() * unit.getKoef();
            }
            double sumAll = sumSach + sumFat + sumProt;
            NumberFormat numFormat = FormatUtils.getDoubleFormat();
            DefaultPieDataset pieSet = new DefaultPieDataset();

            pieSet.setValue(NbBundle.getMessage(ChartTopComponent.class, "sacharidy") + " (" + numFormat.format(sumSach * 100 / sumAll) + "%)", sumSach);
            pieSet.setValue(NbBundle.getMessage(ChartTopComponent.class, "fats") + " (" + numFormat.format(sumFat * 100 / sumAll) + "%)", sumFat);
            pieSet.setValue(NbBundle.getMessage(ChartTopComponent.class, "proteins") + " (" + numFormat.format(sumProt * 100 / sumAll) + "%)", sumProt);
            viewChart(ChartFactory.createPieChart3D(NbBundle.getMessage(ChartTopComponent.class, "structureoffood"), pieSet, true, true, true));
        } else if (hemoglobin.isSelected()) {

            NumberFormat myFormat = NumberFormat.getInstance();
            myFormat.setMaximumFractionDigits(2);
            myFormat.setMinimumFractionDigits(1);

            List<RecordInvest> records = MyLookup.getCurrentPatient().getRecordInvests(getFrom(), getTo());
            Hemoglobin hemo = new Hemoglobin(records);

            StringBuffer buf = new StringBuffer();
            buf.append(NbBundle.getMessage(ChartTopComponent.class, "hemoglobinestimation")).append("\n");
            buf.append(NbBundle.getMessage(ChartTopComponent.class, "alfa")).append("\n");


            buf.append(NbBundle.getMessage(ChartTopComponent.class, "countofvalues")).append(hemo.getValueCount()).append("\n");
            buf.append(NbBundle.getMessage(ChartTopComponent.class, "vyberovyprumer")).append(myFormat.format(hemo.getAverage())).append("\n");
            buf.append(NbBundle.getMessage(ChartTopComponent.class, "vyberovyrozptyl")).append(myFormat.format(hemo.getRozptyl())).append("\n");

            buf.append(NbBundle.getMessage(ChartTopComponent.class, "dolniodhad")).append(myFormat.format(hemo.getLowGly())).append("\n");
            buf.append(NbBundle.getMessage(ChartTopComponent.class, "horniodhad")).append(myFormat.format(hemo.getHeighGly())).append("\n");


            buf.append(NbBundle.getMessage(ChartTopComponent.class, "dolniodhadhba1c")).append(myFormat.format(hemo.getLowHemo())).append("\n");
            buf.append(NbBundle.getMessage(ChartTopComponent.class, "horniodhadhba1c")).append(myFormat.format(hemo.getHeighHemo())).append("\n");
            JTextArea area = new JTextArea(buf.toString());
            area.setEditable(false);
            jScrollPane2.setViewportView(area);
        }
    }

    public TimeSeries createSeries(SREnum sre, String name, SimulationResult profile) {
        TimeSeries series = new TimeSeries(sre.toString(), Minute.class);
        if (profile != null) {
            for (Date date : profile.getProfile().keySet()) {
                if ((date.after(getFrom().toDate()) && date.before(getTo().toDate()))) {
                    double value = 0;
                    switch (sre) {
                        case ACTIVE_INSULIN:
                            value = profile.getProfile().get(date).getActiveInsulin();
                            break;
                        case EQ_INSULIN:
                            value = profile.getProfile().get(date).getEqInsulin();
                            break;
                        case GLUCOSE:
                            value = profile.getProfile().get(date).getGlucose();
                            break;
                        case GLUCOSE_GUT:
                            value = profile.getProfile().get(date).getGlucoseInGut();
                            break;
                        case GLUCOSE_UTIL:
                            value = profile.getProfile().get(date).getGlucoseUtilisation();
                            break;
                        case NHGB:
                            value = profile.getProfile().get(date).getNhgb();
                            break;
                        case PLASMA_INSULIN:
                            value = profile.getProfile().get(date).getPlasmaInsulin();
                            break;
                        case RENAL_EXCRETION:
                            value = profile.getProfile().get(date).getRenalGlucoseExcretion();
                            break;
                    }
                    series.add(new Minute(date), value);
                }
            }
        }
        return series;
    }

    private void viewChart(JFreeChart chart) {
        if (chart != null) {
            ChartPanel panel = new ChartPanel(chart);
            panel.setMouseZoomable(true, false);
            panel.setPreferredSize(new Dimension(800, 100));
            chart.setAntiAlias(false);
            jScrollPane2.setViewportView(panel);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        group = new javax.swing.ButtonGroup();
        groupChart = new javax.swing.ButtonGroup();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        dateFrom = new JFormattedTextField(FormatUtils.getDateFormat());
        jLabel2 = new javax.swing.JLabel();
        dateTo = new JFormattedTextField(FormatUtils.getDateFormat());
        diaryDateRadio = new javax.swing.JRadioButton();
        selectRadio = new javax.swing.JRadioButton();
        lastMonthRadio = new javax.swing.JRadioButton();
        curMonthRadio = new javax.swing.JRadioButton();
        yesterdayRadio = new javax.swing.JRadioButton();
        todayRadio = new javax.swing.JRadioButton();
        thisWeekRadio = new javax.swing.JRadioButton();
        errorLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        investCombo = new javax.swing.JComboBox();
        investRadio = new javax.swing.JRadioButton();
        foodRadio = new javax.swing.JRadioButton();
        insulinRadio = new javax.swing.JRadioButton();
        hemoglobin = new javax.swing.JRadioButton();
        jScrollPane2 = new javax.swing.JScrollPane();

        group.add(diaryDateRadio);
        group.add(selectRadio);
        group.add(lastMonthRadio);
        group.add(curMonthRadio);
        group.add(yesterdayRadio);
        group.add(todayRadio);
        group.add(thisWeekRadio);
        groupChart.add(investRadio);
        groupChart.add(foodRadio);
        groupChart.add(insulinRadio);
        groupChart.add(hemoglobin);

        jSplitPane1.setDividerLocation(250);
        jSplitPane1.setDividerSize(7);
        jSplitPane1.setLastDividerLocation(170);
        jSplitPane1.setOneTouchExpandable(true);
        jPanel3.setMaximumSize(new java.awt.Dimension(0, 32767));
        jPanel3.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(NbBundle.getMessage(ChartTopComponent.class,"date")));
        dateFrom.setColumns(10);
        dateFrom.setText("21.12.2006");
        dateFrom.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateFromPropertyChange(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, "-");

        dateTo.setColumns(10);
        dateTo.setText("15.01.2007");

        diaryDateRadio.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(diaryDateRadio, NbBundle.getMessage(ChartTopComponent.class,"selection"));
        diaryDateRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        diaryDateRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        diaryDateRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diaryDateRadioActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(selectRadio, NbBundle.getMessage(ChartTopComponent.class,"from"));
        selectRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        selectRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        selectRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectRadioActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(lastMonthRadio, NbBundle.getMessage(ChartTopComponent.class,"lastmonth"));
        lastMonthRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lastMonthRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        lastMonthRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastMonthRadioActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(curMonthRadio, NbBundle.getMessage(ChartTopComponent.class,"thismonth"));
        curMonthRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        curMonthRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        curMonthRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                curMonthRadioActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(yesterdayRadio, NbBundle.getMessage(ChartTopComponent.class,"tomorrow"));
        yesterdayRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        yesterdayRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        yesterdayRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesterdayRadioActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(todayRadio, NbBundle.getMessage(ChartTopComponent.class,"today"));
        todayRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        todayRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        todayRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                todayRadioActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(thisWeekRadio, NbBundle.getMessage(ChartTopComponent.class,"thisweek"));
        thisWeekRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        thisWeekRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        thisWeekRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thisWeekRadioActionPerformed(evt);
            }
        });

        errorLabel.setForeground(new java.awt.Color(255, 0, 0));
        org.openide.awt.Mnemonics.setLocalizedText(errorLabel, "Error");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(selectRadio)
                        .add(3, 3, 3)
                        .add(dateFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(dateTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(diaryDateRadio)
                            .add(todayRadio)
                            .add(curMonthRadio))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lastMonthRadio)
                            .add(yesterdayRadio)
                            .add(thisWeekRadio)))
                    .add(errorLabel))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(diaryDateRadio)
                    .add(thisWeekRadio))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(todayRadio)
                    .add(yesterdayRadio))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(curMonthRadio)
                    .add(lastMonthRadio))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(dateFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(dateTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(selectRadio))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(errorLabel))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(NbBundle.getMessage(ChartTopComponent.class,"chart")));
        investCombo.setMaximumRowCount(15);
        investCombo.setModel(createInvestModel());
        investCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                investComboActionPerformed(evt);
            }
        });

        investRadio.setSelected(true);
        investRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        investRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        investRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                investRadioActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(foodRadio, NbBundle.getMessage(ChartTopComponent.class,"food"));
        foodRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        foodRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        foodRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodRadioActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(insulinRadio, NbBundle.getMessage(ChartTopComponent.class,"insulin"));
        insulinRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        insulinRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        insulinRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insulinRadioActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(hemoglobin, NbBundle.getMessage(ChartTopComponent.class,"hemoglobin"));
        hemoglobin.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        hemoglobin.setMargin(new java.awt.Insets(0, 0, 0, 0));
        hemoglobin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hemoglobinActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(investRadio)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(investCombo, 0, 188, Short.MAX_VALUE))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(foodRadio)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(insulinRadio)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(hemoglobin)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(investRadio)
                    .add(investCombo))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(foodRadio)
                    .add(insulinRadio)
                    .add(hemoglobin)))
        );

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jSplitPane1.setLeftComponent(jPanel3);

        jSplitPane1.setRightComponent(jScrollPane2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void hemoglobinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hemoglobinActionPerformed
        createChart();
    }//GEN-LAST:event_hemoglobinActionPerformed

    private void lastMonthRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastMonthRadioActionPerformed
        Calendar cal = getInitCalendar();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        dateTo.setValue(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, -1);
        dateFrom.setValue(cal.getTime());
    }//GEN-LAST:event_lastMonthRadioActionPerformed

    private void selectRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectRadioActionPerformed
        createChart();
    }//GEN-LAST:event_selectRadioActionPerformed

    private void curMonthRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_curMonthRadioActionPerformed
        Calendar cal = getInitCalendar();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        dateTo.setValue(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        dateFrom.setValue(cal.getTime());
    }//GEN-LAST:event_curMonthRadioActionPerformed

    private void yesterdayRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesterdayRadioActionPerformed
        Calendar cal = getInitCalendar();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        dateFrom.setValue(cal.getTime());
        dateTo.setValue(cal.getTime());
    }//GEN-LAST:event_yesterdayRadioActionPerformed

    private void investComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_investComboActionPerformed
        if (investRadio.isSelected()) {
            createChart();
        }
    }//GEN-LAST:event_investComboActionPerformed

    private void thisWeekRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thisWeekRadioActionPerformed
        Calendar cal = getInitCalendar();
        dateTo.setValue(cal.getTime());

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        dateFrom.setValue(cal.getTime());
    }//GEN-LAST:event_thisWeekRadioActionPerformed

    private Calendar getInitCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    private ComboBoxModel createInvestModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel(diary.getInvestigations().toArray());
        return model;
    }

    private Investigation getInvest() {
        return (Investigation) investCombo.getSelectedItem();
    }

    private void todayRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_todayRadioActionPerformed
        Calendar cal = getInitCalendar();
        dateFrom.setValue(cal.getTime());
        dateTo.setValue(cal.getTime());
    }//GEN-LAST:event_todayRadioActionPerformed

    private void diaryDateRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diaryDateRadioActionPerformed
        setDatesByDiarySelect();
    }//GEN-LAST:event_diaryDateRadioActionPerformed

    private void insulinRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insulinRadioActionPerformed
        createChart();
    }//GEN-LAST:event_insulinRadioActionPerformed

    private void foodRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodRadioActionPerformed
        createChart();
    }//GEN-LAST:event_foodRadioActionPerformed

    private void investRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_investRadioActionPerformed
        createChart();
    }//GEN-LAST:event_investRadioActionPerformed

    private void dateFromPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateFromPropertyChange
        createChart();
    }//GEN-LAST:event_dateFromPropertyChange
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton curMonthRadio;
    private javax.swing.JFormattedTextField dateFrom;
    private javax.swing.JFormattedTextField dateTo;
    private javax.swing.JRadioButton diaryDateRadio;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JRadioButton foodRadio;
    private javax.swing.ButtonGroup group;
    private javax.swing.ButtonGroup groupChart;
    private javax.swing.JRadioButton hemoglobin;
    private javax.swing.JRadioButton insulinRadio;
    private javax.swing.JComboBox investCombo;
    private javax.swing.JRadioButton investRadio;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JRadioButton lastMonthRadio;
    private javax.swing.JRadioButton selectRadio;
    private javax.swing.JRadioButton thisWeekRadio;
    private javax.swing.JRadioButton todayRadio;
    private javax.swing.JRadioButton yesterdayRadio;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized ChartTopComponent getDefault() {
        if (instance == null) {
            instance = new ChartTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ChartTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ChartTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find Chart component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof ChartTopComponent) {
            return (ChartTopComponent) win;
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
        createChart();
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

    private void setError(String error) {
        if (error == null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        } else {
            errorLabel.setText(error);
            errorLabel.setVisible(true);
        }
    }

    private boolean valid() {
        Date from = (Date) dateFrom.getValue();
        Date to = (Date) dateTo.getValue();
        if (from.after(to)) {
            setError(NbBundle.getMessage(ChartTopComponent.class, "baddate"));
            return false;
        }
        setError(null);
        return true;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        createChart();
    }

    private DateTime getFrom() {
        return new DateTime(dateFrom.getValue());
    }

    private DateTime getTo() {
        Date to = (Date) dateTo.getValue();
        if (to != null) {
            return new DateTime(to).plusDays(1);
        }
        return null;
    }
    private int firstDay = 1;
    private int lastDay = 31;

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if (!lsm.isSelectionEmpty()) {
            firstDay = lsm.getMinSelectionIndex() + 1;
            lastDay = lsm.getMaxSelectionIndex() + 1;
        } else {
            firstDay = 1;
            lastDay = 31;
        }
        if (diaryDateRadio.isSelected() && isOpened()) {
            setDatesByDiarySelect();
        }
    }

    private void setDatesByDiarySelect() {
        Calendar cal = Calendar.getInstance();
        DateTime date = CalendarTopComponent.getDefault().getDateTime();
        if (date != null) {
            cal.setTime(date.toDate());
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        dateFrom.setValue(cal.getTime());
        if (lastDay >= cal.getMaximum(Calendar.DAY_OF_MONTH)) {
            cal.set(Calendar.DAY_OF_MONTH, cal.getMaximum(Calendar.DAY_OF_MONTH));
        } else {
            cal.set(Calendar.DAY_OF_MONTH, lastDay);
        }
        dateTo.setValue(cal.getTime());
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return ChartTopComponent.getDefault();
        }
    }
}
