/*
 *   Copyright (C) 2006-2010 Jiri Majer. All Rights Reserved.
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
package org.diabetesdiary.commons.swing.calendar;

import java.awt.Component;
import java.util.Locale;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicArrowButton;
import org.joda.time.LocalDate;

/**
 *
 * @author Jirka Majer
 */
public class MonthYearPanel extends javax.swing.JPanel {

    public MonthYearPanel() {
        this(null, null);
    }

    public MonthYearPanel(LocalDate ldate, Locale locale) {
        this.locale = locale == null ? Locale.getDefault() : locale;
        initComponents();
        setLocalDate(ldate != null ? ldate : new LocalDate());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new BasicArrowButton(BasicArrowButton.WEST);
        jButton2 = new BasicArrowButton(BasicArrowButton.EAST);
        jComboBox1 = new javax.swing.JComboBox();
        yearSpinner = new YearSpinner();

        jButton1.setText("<"); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(">"); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new DefaultComboBoxModel(new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12}));
        jComboBox1.setPreferredSize(new java.awt.Dimension(100, 20));
        jComboBox1.setRenderer(new MonthRenderer(locale));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        yearSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                yearSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, 0, 105, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(yearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(yearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        setLocalDate(date.plusMonths(1));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setLocalDate(date.minusMonths(1));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        setLocalDate(date.withMonthOfYear((Integer) jComboBox1.getSelectedItem()));
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void yearSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_yearSpinnerStateChanged
        YearSpinner spin = (YearSpinner) yearSpinner;
        setLocalDate(date.withYear(spin.getDateTime().getYear()));
    }//GEN-LAST:event_yearSpinnerStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame fr = new JFrame();
                fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                fr.add(new MonthYearPanel(new LocalDate(), Locale.getDefault()));
                fr.pack();
                fr.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JSpinner yearSpinner;
    // End of variables declaration//GEN-END:variables
    private LocalDate date;
    private Locale locale;

    public void setLocalDate(LocalDate newDate) {
        if (date == null || !date.withDayOfMonth(1).equals(newDate.withDayOfMonth(1))) {
            LocalDate oldDate = date;
            date = newDate;
            jComboBox1.setSelectedItem(newDate.getMonthOfYear());
            YearSpinner spin = (YearSpinner) yearSpinner;
            spin.setDateTime(newDate.toDateTimeAtCurrentTime());
            if (oldDate != null) {
                firePropertyChange("localDate", oldDate, newDate);
            }
        }
    }

    public LocalDate getLocalDate() {
        return date;
    }

    @Override
    public void setEnabled(boolean enabled) {
        jButton1.setEnabled(enabled);
        jButton2.setEnabled(enabled);
        jComboBox1.setEnabled(enabled);
        yearSpinner.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    private static class MonthRenderer extends JLabel implements ListCellRenderer {

        private Locale locale;

        public MonthRenderer(Locale locale) {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
            this.locale = locale;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            Integer val = (Integer) value;
            setText(new LocalDate().withMonthOfYear(val).toString("MMMM", locale));
            return this;
        }
    }
}
