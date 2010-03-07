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
package org.diabetesdiary.print;

import com.sun.pdfview.OutlineNode;
import com.sun.pdfview.PDFDestination;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFObject;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;
import com.sun.pdfview.action.GoToAction;
import com.sun.pdfview.action.PDFAction;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputMap;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.diabetesdiary.commons.swing.calendar.DateTimePanel;
import org.diabetesdiary.diary.utils.MyLookup;
import org.diabetesdiary.print.pdf.PDFGenerator;
import org.diabetesdiary.print.pdf.PageSize;
import org.diabetesdiary.print.print.PDFPrintSupport;
import org.joda.time.DateTime;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

final class PrintPreviewTopComponent extends TopComponent implements TreeSelectionListener, KeyListener {

    private static PrintPreviewTopComponent instance;
    static final String ICON_PATH = "org/diabetesdiary/print/images/print_preview.png";
    static final String ICON_PATH_SMALL = "org/diabetesdiary/print/images/print_preview16.png";
    private static final String PREFERRED_ID = "PrintPreviewTopComponent";
    public final static Color DEFAULT_BACKGROUND = Color.DARK_GRAY;
    public final static Color THUMBS_BACKGROUND = new Color(0xF5F5F5);
    private PDFFile curFile;
    private int curpage = -1;
    private PagePreparer pagePrep;
    private Action unitScrollUp;
    private Action unitScrollDown;
    private Action scrollUp;
    private Action scrollDown;
    private PDFGenerator generator;

    private PrintPreviewTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(PrintPreviewTopComponent.class, "CTL_PrintPreviewTopComponent"));
        setToolTipText(NbBundle.getMessage(PrintPreviewTopComponent.class, "HINT_PrintPreviewTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH_SMALL, true));
        pagePanel = new PagePanel();
        pagePanel.setBackground(Color.DARK_GRAY);
        pageScroll.setViewportView(pagePanel);
        pagePanel.addKeyListener(this);
        //custom page panel key function
        InputMap im = pageScroll.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        unitScrollUp = pageScroll.getActionMap().get(im.get(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0)));
        unitScrollDown = pageScroll.getActionMap().get(im.get(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0)));
        scrollUp = pageScroll.getActionMap().get(im.get(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0)));
        scrollDown = pageScroll.getActionMap().get(im.get(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0)));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "none");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "none");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "none");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "none");

        add(pageScroll, BorderLayout.CENTER);
        pageScroll.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                PrintPreviewTopComponent.this.gotoPage(curpage);
            }
        });

        generator = new PDFGenerator(MyLookup.getCurrentPatient());
        generator.setVisibleActivity(false);
        generator.setFontSize(9);
        dateTo.setTimeVisible(false);
        dateFrom.setTimeVisible(false);
        generator.setFrom(dateFrom.getDateTime());
        generator.setTo(dateTo.getDateTime());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jFileChooser = new javax.swing.JFileChooser();
        pageScroll = new javax.swing.JScrollPane();
        jToolBar1 = new javax.swing.JToolBar();
        prevButton = new javax.swing.JButton();
        pageField = new javax.swing.JLabel();
        nextButton = new javax.swing.JButton();
        printButton = new javax.swing.JButton();
        savePDFButton = new javax.swing.JButton();
        refresh = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();
        dateFrom = new org.diabetesdiary.commons.swing.calendar.DateTimePanel();
        dateTo = new org.diabetesdiary.commons.swing.calendar.DateTimePanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        insuline = new javax.swing.JCheckBox();
        glycemia = new javax.swing.JCheckBox();
        otherInvest = new javax.swing.JCheckBox();
        food = new javax.swing.JCheckBox();
        activity = new javax.swing.JCheckBox();
        sum = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        fontSizeLabel = new javax.swing.JLabel();
        fontSize = new javax.swing.JSpinner();
        pageSize = new javax.swing.JComboBox();
        horizontal = new javax.swing.JCheckBox();
        colors = new javax.swing.JCheckBox();

        jFileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        jFileChooser.setFileFilter(new FileNameExtensionFilter("*.pdf", "pdf"));

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        prevButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/diabetesdiary/print/images/prev.png"))); // NOI18N
        prevButton.setToolTipText(org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.prevButton.toolTipText")); // NOI18N
        prevButton.setFocusable(false);
        prevButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        prevButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        prevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(prevButton);

        org.openide.awt.Mnemonics.setLocalizedText(pageField, "1 of 1"); // NOI18N
        jToolBar1.add(pageField);

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/diabetesdiary/print/images/next.png"))); // NOI18N
        nextButton.setToolTipText(org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.nextButton.toolTipText")); // NOI18N
        nextButton.setFocusable(false);
        nextButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        nextButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(nextButton);

        printButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/diabetesdiary/print/images/printer.png"))); // NOI18N
        printButton.setToolTipText(org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.printButton.toolTipText")); // NOI18N
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(printButton);

        savePDFButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/diabetesdiary/print/images/pdf.png"))); // NOI18N
        savePDFButton.setToolTipText(org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.savePDFButton.toolTipText")); // NOI18N
        savePDFButton.setFocusable(false);
        savePDFButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        savePDFButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        savePDFButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePDFButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(savePDFButton);

        refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/diabetesdiary/print/images/refresh.png"))); // NOI18N
        refresh.setToolTipText(org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.refresh.toolTipText")); // NOI18N
        refresh.setFocusable(false);
        refresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });
        jToolBar1.add(refresh);
        jToolBar1.add(jSeparator1);

        jPanel1.setMaximumSize(new java.awt.Dimension(150, 50));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        dateFrom.setDateTime(new DateTime().dayOfMonth().withMinimumValue());
        dateFrom.setTimeVisible(false);
        dateFrom.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateFromPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(dateFrom, gridBagConstraints);

        dateTo.setDateTime(new DateTime().dayOfMonth().withMaximumValue());
        dateTo.setTimeVisible(false);
        dateTo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateToPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(dateTo, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jLabel1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jLabel2, gridBagConstraints);

        jToolBar1.add(jPanel1);

        insuline.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(insuline, org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.insuline.text")); // NOI18N
        insuline.setFocusable(false);
        insuline.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        insuline.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        insuline.setInheritsPopupMenu(true);
        insuline.setMaximumSize(new java.awt.Dimension(50, 50));
        insuline.setMinimumSize(new java.awt.Dimension(50, 20));
        insuline.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        insuline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insulineActionPerformed(evt);
            }
        });
        jToolBar1.add(insuline);

        glycemia.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(glycemia, org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.glycemia.text")); // NOI18N
        glycemia.setFocusable(false);
        glycemia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        glycemia.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        glycemia.setInheritsPopupMenu(true);
        glycemia.setMaximumSize(new java.awt.Dimension(50, 50));
        glycemia.setMinimumSize(new java.awt.Dimension(50, 20));
        glycemia.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        glycemia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                glycemiaActionPerformed(evt);
            }
        });
        jToolBar1.add(glycemia);

        otherInvest.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(otherInvest, org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.otherInvest.text")); // NOI18N
        otherInvest.setFocusable(false);
        otherInvest.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        otherInvest.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        otherInvest.setInheritsPopupMenu(true);
        otherInvest.setMaximumSize(new java.awt.Dimension(50, 50));
        otherInvest.setMinimumSize(new java.awt.Dimension(50, 20));
        otherInvest.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        otherInvest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otherInvestActionPerformed(evt);
            }
        });
        jToolBar1.add(otherInvest);

        food.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(food, org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.food.text")); // NOI18N
        food.setFocusable(false);
        food.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        food.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        food.setInheritsPopupMenu(true);
        food.setMaximumSize(new java.awt.Dimension(50, 50));
        food.setMinimumSize(new java.awt.Dimension(50, 20));
        food.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        food.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodActionPerformed(evt);
            }
        });
        jToolBar1.add(food);

        org.openide.awt.Mnemonics.setLocalizedText(activity, org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.activity.text")); // NOI18N
        activity.setFocusable(false);
        activity.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        activity.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        activity.setInheritsPopupMenu(true);
        activity.setMaximumSize(new java.awt.Dimension(50, 50));
        activity.setMinimumSize(new java.awt.Dimension(50, 20));
        activity.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        activity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activityActionPerformed(evt);
            }
        });
        jToolBar1.add(activity);

        sum.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(sum, org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.sum.text")); // NOI18N
        sum.setFocusable(false);
        sum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sum.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        sum.setInheritsPopupMenu(true);
        sum.setMaximumSize(new java.awt.Dimension(50, 50));
        sum.setMinimumSize(new java.awt.Dimension(50, 20));
        sum.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        sum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumActionPerformed(evt);
            }
        });
        jToolBar1.add(sum);
        jToolBar1.add(jSeparator2);

        org.openide.awt.Mnemonics.setLocalizedText(fontSizeLabel, org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.fontSizeLabel.text")); // NOI18N
        jToolBar1.add(fontSizeLabel);

        fontSize.setModel(new javax.swing.SpinnerNumberModel(10, 1, 30, 1));
        fontSize.setMaximumSize(new java.awt.Dimension(50, 25));
        fontSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fontSizeStateChanged(evt);
            }
        });
        jToolBar1.add(fontSize);

        pageSize.setModel(new DefaultComboBoxModel(PageSize.values()));
        pageSize.setSelectedItem(PageSize.A4);
        pageSize.setMaximumSize(new java.awt.Dimension(70, 25));
        pageSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pageSizeActionPerformed(evt);
            }
        });
        jToolBar1.add(pageSize);

        horizontal.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(horizontal, org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.horizontal.text")); // NOI18N
        horizontal.setFocusable(false);
        horizontal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        horizontal.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        horizontal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                horizontalActionPerformed(evt);
            }
        });
        jToolBar1.add(horizontal);

        colors.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(colors, org.openide.util.NbBundle.getMessage(PrintPreviewTopComponent.class, "PrintPreviewTopComponent.colors.text")); // NOI18N
        colors.setFocusable(false);
        colors.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        colors.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        colors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorsActionPerformed(evt);
            }
        });
        jToolBar1.add(colors);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pageScroll, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 913, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 913, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pageScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        new PDFPrintSupport(curFile, "test").print();
    }//GEN-LAST:event_printButtonActionPerformed

    private void insulineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insulineActionPerformed
        generator.setVisibleInsulin(insuline.isSelected());
        generatePDF();
    }//GEN-LAST:event_insulineActionPerformed

    private void prevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevButtonActionPerformed
        doPrev();
}//GEN-LAST:event_prevButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        doNext();
}//GEN-LAST:event_nextButtonActionPerformed

    private void glycemiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_glycemiaActionPerformed
        generator.setVisibleGlycemie(glycemia.isSelected());
        generatePDF();
    }//GEN-LAST:event_glycemiaActionPerformed

    private void otherInvestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otherInvestActionPerformed
        generator.setVisibleInvest(otherInvest.isSelected());
        generatePDF();
    }//GEN-LAST:event_otherInvestActionPerformed

    private void foodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodActionPerformed
        generator.setVisibleFood(food.isSelected());
        generatePDF();
    }//GEN-LAST:event_foodActionPerformed

    private void activityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activityActionPerformed
        generator.setVisibleActivity(activity.isSelected());
        generatePDF();
    }//GEN-LAST:event_activityActionPerformed

    private void sumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumActionPerformed
        generator.setVisibleSum(sum.isSelected());
        generatePDF();
    }//GEN-LAST:event_sumActionPerformed

    private void pageSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pageSizeActionPerformed
        generator.setPageSize((PageSize) pageSize.getSelectedItem());
        generatePDF();
    }//GEN-LAST:event_pageSizeActionPerformed

    private void horizontalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_horizontalActionPerformed
        generator.setHorizontal(horizontal.isSelected());
        generatePDF();
    }//GEN-LAST:event_horizontalActionPerformed

    private void fontSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fontSizeStateChanged
        generator.setFontSize((Integer) fontSize.getValue());
        generatePDF();
    }//GEN-LAST:event_fontSizeStateChanged

    private void colorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorsActionPerformed
        generator.setColors(colors.isSelected());
        generatePDF();
    }//GEN-LAST:event_colorsActionPerformed

    private void savePDFButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePDFButtonActionPerformed
        int returnVal = jFileChooser.showSaveDialog(PrintPreviewTopComponent.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            if (file.exists()) {
                Confirmation msg = new NotifyDescriptor.Confirmation(String.format("Soubor %s existuje. Chcete ho přepsat?", file.getName()),
                        NotifyDescriptor.OK_CANCEL_OPTION,
                        NotifyDescriptor.QUESTION_MESSAGE);
                Object result = DialogDisplayer.getDefault().notify(msg);
                if (!NotifyDescriptor.YES_OPTION.equals(result)) {
                    return;
                }
            }
            BufferedOutputStream out = null;
            try {
                FileOutputStream fstream = new FileOutputStream(file);
                out = new BufferedOutputStream(fstream);
                generator.generateDocument(out);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    out.close();
                } catch (Exception ex) {
                }
            }
        }
    }//GEN-LAST:event_savePDFButtonActionPerformed

    private void dateFromPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateFromPropertyChange
        if (evt.getPropertyName().equals(DateTimePanel.DATE_PROPERTY)) {
            generator.setFrom(dateFrom.getDateTime());
            generatePDF();
        }
    }//GEN-LAST:event_dateFromPropertyChange

    private void dateToPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateToPropertyChange
        if (evt.getPropertyName().equals(DateTimePanel.DATE_PROPERTY)) {
            generator.setTo(dateTo.getDateTime());
            generatePDF();
        }
    }//GEN-LAST:event_dateToPropertyChange

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        generator.reloadData();
        generatePDF();
    }//GEN-LAST:event_refreshActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox activity;
    private javax.swing.JCheckBox colors;
    private org.diabetesdiary.commons.swing.calendar.DateTimePanel dateFrom;
    private org.diabetesdiary.commons.swing.calendar.DateTimePanel dateTo;
    private javax.swing.JSpinner fontSize;
    private javax.swing.JLabel fontSizeLabel;
    private javax.swing.JCheckBox food;
    private javax.swing.JCheckBox glycemia;
    private javax.swing.JCheckBox horizontal;
    private javax.swing.JCheckBox insuline;
    private javax.swing.JFileChooser jFileChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton nextButton;
    private javax.swing.JCheckBox otherInvest;
    private javax.swing.JLabel pageField;
    private javax.swing.JScrollPane pageScroll;
    private javax.swing.JComboBox pageSize;
    private javax.swing.JButton prevButton;
    private javax.swing.JButton printButton;
    private javax.swing.JButton refresh;
    private javax.swing.JButton savePDFButton;
    private javax.swing.JCheckBox sum;
    // End of variables declaration//GEN-END:variables
    private PagePanel pagePanel;

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized PrintPreviewTopComponent getDefault() {
        if (instance == null) {
            instance = new PrintPreviewTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the PrintPreviewTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized PrintPreviewTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(PrintPreviewTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof PrintPreviewTopComponent) {
            return (PrintPreviewTopComponent) win;
        }
        Logger.getLogger(PrintPreviewTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
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
            return PrintPreviewTopComponent.getDefault();
        }
    }

    public void generatePDF() {
        try {
            if (generator.getPatient() == null || !generator.getPatient().equals(MyLookup.getCurrentPatient())) {
                generator.setPatient(MyLookup.getCurrentPatient());
            }
            curFile = new PDFFile(ByteBuffer.wrap(generator.generateDocument().toByteArray()));
            gotoPage(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Changes the displayed page.
     * @param pagenum the page to display
     */
    public void gotoPage(int pagenum) {
        if (curFile == null) {
            return;
        }
        if (pagenum < 0) {
            pagenum = 0;
        } else if (pagenum >= curFile.getNumPages()) {
            pagenum = curFile.getNumPages() - 1;
        }
        curpage = pagenum;

        // update the page text field
        pageField.setText(String.format("%d of %d", curpage + 1, curFile.getNumPages()));

        // fetch the page and show it in the appropriate place
        PDFPage pg = curFile.getPage(pagenum + 1);
        pagePanel.showPage(pg);
        pagePanel.requestFocus();
        // stop any previous page prepper, and start a new one
        if (pagePrep != null) {
            pagePrep.quit();
        }
        pagePrep = new PagePreparer(pagenum);
        pagePrep.start();

        setEnabling();
    }

    /**
     * A class to pre-cache the next page for better UI response
     */
    class PagePreparer extends Thread {

        int waitforPage;
        int prepPage;

        /**
         * Creates a new PagePreparer to prepare the page after the current
         * one.
         * @param waitforPage the current page number, 0 based
         */
        public PagePreparer(int waitforPage) {
            setDaemon(true);

            this.waitforPage = waitforPage;
            this.prepPage = waitforPage + 1;
        }

        public void quit() {
            waitforPage = -1;
        }

        @Override
        public void run() {
            Dimension size = null;
            pagePanel.waitForCurrentPage();
            size = pagePanel.getCurSize();

            if (waitforPage == curpage) {
                PDFPage pdfPage = curFile.getPage(prepPage + 1, true);
                if (pdfPage != null && waitforPage == curpage) {
                    pdfPage.getImage(size.width, size.height, null, null, true, true);
                }
            }
        }
    }

    /**
     * Enable or disable all of the actions based on the current state.
     */
    public void setEnabling() {
        prevButton.setEnabled(curpage > 0);
        nextButton.setEnabled(curpage < curFile.getNumPages() - 1);
    }

    /**
     * Open a specific pdf file.  Creates a DocumentInfo from the file,
     * and opens that.
     */
    public void openFile(PDFFile o) throws IOException {
        this.curFile = o;
        gotoPage(0);
    }

    /**
     * Goes to the next page
     */
    public void doNext() {
        gotoPage(curpage + 1);
    }

    /**
     * Goes to the previous page
     */
    public void doPrev() {
        gotoPage(curpage - 1);
    }

    /**
     * Goes to the first page
     */
    public void doFirst() {
        gotoPage(0);
    }

    /**
     * Goes to the last page
     */
    public void doLast() {
        gotoPage(curFile.getNumPages() - 1);
    }
    /**
     * Handle a key press for navigation
     */
    int lastPos = 0;

    @Override
    public void keyPressed(KeyEvent evt) {
        int code = evt.getKeyCode();
        if (code == KeyEvent.VK_LEFT) {
            doPrev();
        } else if (code == KeyEvent.VK_RIGHT) {
            doNext();
        } else if (code == KeyEvent.VK_UP) {
            unitScrollUp.actionPerformed(new ActionEvent(pageScroll, code, TOOL_TIP_TEXT_KEY));
            if (pageScroll.getVerticalScrollBar().getValue() == lastPos) {
                doPrev();
            }
        } else if (code == KeyEvent.VK_DOWN) {
            unitScrollDown.actionPerformed(new ActionEvent(pageScroll, code, TOOL_TIP_TEXT_KEY));
            if (pageScroll.getVerticalScrollBar().getValue() == lastPos) {
                doNext();
            }
        } else if (code == KeyEvent.VK_HOME) {
            doFirst();
        } else if (code == KeyEvent.VK_END) {
            doLast();
        } else if (code == KeyEvent.VK_PAGE_UP) {
            scrollUp.actionPerformed(new ActionEvent(pageScroll, code, TOOL_TIP_TEXT_KEY));
            if (pageScroll.getVerticalScrollBar().getValue() == lastPos) {
                doPrev();
            }
        } else if (code == KeyEvent.VK_PAGE_DOWN) {
            scrollDown.actionPerformed(new ActionEvent(pageScroll, code, TOOL_TIP_TEXT_KEY));
            if (pageScroll.getVerticalScrollBar().getValue() == lastPos) {
                doNext();
            }
        } else if (code == KeyEvent.VK_SPACE) {
            doNext();
        }
        lastPos = pageScroll.getVerticalScrollBar().getValue();
    }

    /**
     * Combines numeric key presses to build a multi-digit page number.
     */
    class PageBuilder implements Runnable {

        int value = 0;
        long timeout;
        Thread anim;
        static final long TIMEOUT = 500;

        /** add the digit to the page number and start the timeout thread */
        public synchronized void keyTyped(int keyval) {
            value = value * 10 + keyval;
            timeout = System.currentTimeMillis() + TIMEOUT;
            if (anim == null) {
                anim = new Thread(this);
                anim.start();
            }
        }

        /**
         * waits for the timeout, and if time expires, go to the specified
         * page number
         */
        @Override
        public void run() {
            long now, then;
            synchronized (this) {
                now = System.currentTimeMillis();
                then = timeout;
            }
            while (now < then) {
                try {
                    Thread.sleep(timeout - now);
                } catch (InterruptedException ie) {
                }
                synchronized (this) {
                    now = System.currentTimeMillis();
                    then = timeout;
                }
            }
            synchronized (this) {
                gotoPage(value - 1);
                anim = null;
                value = 0;
            }
        }
    }
    PageBuilder pb = new PageBuilder();

    @Override
    public void keyReleased(KeyEvent evt) {
    }

    /**
     * gets key presses and tries to build a page if they're numeric
     */
    @Override
    public void keyTyped(KeyEvent evt) {
        char key = evt.getKeyChar();
        if (key >= '0' && key <= '9') {
            int val = key - '0';
            pb.keyTyped(val);
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (e.isAddedPath()) {
            OutlineNode node = (OutlineNode) e.getPath().getLastPathComponent();
            if (node == null) {
                return;
            }
            try {
                PDFAction action = node.getAction();
                if (action == null) {
                    return;
                }

                if (action instanceof GoToAction) {
                    PDFDestination dest = ((GoToAction) action).getDestination();
                    if (dest == null) {
                        return;
                    }

                    PDFObject pageL = dest.getPage();
                    if (pageL == null) {
                        return;
                    }

                    int pageNum = curFile.getPageNumber(pageL);
                    if (pageNum >= 0) {
                        gotoPage(pageNum);
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
