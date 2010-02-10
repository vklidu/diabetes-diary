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

import java.text.DateFormat;
import java.text.NumberFormat;
import org.diabetesdiary.diary.api.DiaryRepository;
import org.diabetesdiary.diary.utils.MyLookup;


/**
 *
 * @author Jirka Majer
 */
public abstract class AbstractRecordEditorPanel<T> extends javax.swing.JPanel {

    protected static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    protected static DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    protected static DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    protected static NumberFormat numberFormat = NumberFormat.getInstance();
    protected DiaryRepository diary;

    protected T selectedRecord;

    public AbstractRecordEditorPanel() {
        diary = MyLookup.getDiaryRepo();
        numberFormat.setMaximumFractionDigits(2);
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        foodError = new javax.swing.JLabel();

        foodError.setForeground(new java.awt.Color(255, 0, 0));
        foodError.setText("error");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(foodError)
                .addContainerGap(561, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(foodError)
                .addContainerGap(498, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public abstract void setRecord(T[] recs);

    public abstract boolean isRecordValid();

    public abstract void saveRecord();

    public abstract void updateRecord(T rec);

    protected void setError(String error) {
        if (error == null) {
            foodError.setText("");
            foodError.setVisible(false);
        }
        foodError.setText(error);
        foodError.setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JLabel foodError;
    // End of variables declaration//GEN-END:variables
}
