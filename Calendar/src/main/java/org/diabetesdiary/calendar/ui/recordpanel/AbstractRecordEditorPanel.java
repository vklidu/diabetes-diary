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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import org.diabetesdiary.calendar.utils.DataChangedEvent;
import org.diabetesdiary.calendar.utils.DataChangedListener;
import org.diabetesdiary.diary.api.DiaryRepository;
import org.diabetesdiary.diary.domain.AbstractRecord;
import org.diabetesdiary.diary.utils.MyLookup;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jirka Majer
 */
public abstract class AbstractRecordEditorPanel<T extends AbstractRecord> extends javax.swing.JPanel {

    private final EventListenerList eventList = new EventListenerList();
    protected Logger log = LoggerFactory.getLogger(getClass());
    protected DiaryRepository diary;
    private T selectedRecord;

    public AbstractRecordEditorPanel() {
        diary = MyLookup.getDiaryRepo();
        initComponents();
        setError(null);
        setRecord(null);
    }

    public void addDataChangedListener(DataChangedListener listener) {
        eventList.add(DataChangedListener.class, listener);
    }

    public void removeDataChangedListener(DataChangedListener listener) {
        eventList.remove(DataChangedListener.class, listener);
    }

    protected void fireDataChanged(DataChangedEvent evt) {
        for (DataChangedListener listener : eventList.getListeners(DataChangedListener.class)) {
            listener.onDataChanged(evt);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        errorLabel.setForeground(new java.awt.Color(255, 0, 0));
        errorLabel.setText("error");

        saveButton.setText(NbBundle.getMessage(AbstractRecordEditorPanel.class,"save")); // NOI18N
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(org.openide.util.NbBundle.getMessage(AbstractRecordEditorPanel.class, "cancel")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        deleteButton.setText(org.openide.util.NbBundle.getMessage(AbstractRecordEditorPanel.class, "delete")); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(errorLabel))
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(dateTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(errorLabel)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveButton)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(deleteButton)
                        .addComponent(cancelButton)))
                .addGap(37, 37, 37)
                .addComponent(dateTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (isFormValid()) {
            try {
                if (selectedRecord == null) {
                    selectedRecord = loadRecordByFormIfExist();
                }
                if (selectedRecord == null || !selectedRecord.isStillPersistent()) {
                    selectedRecord = saveRecord();
                } else {
                    selectedRecord = updateRecord(selectedRecord);
                }
                fireDataChanged(new DataChangedEvent(this, selectedRecord.getClass()));
                selectedRecord = null;
                onRecordSelectionChanged();
            } catch (Exception e) {
                log.error("Save or update record failed", e);
                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(NbBundle.getMessage(AbstractRecordEditorPanel.class, "save.error"), NotifyDescriptor.ERROR_MESSAGE));
            }
        }
}//GEN-LAST:event_saveButtonActionPerformed

    private boolean isFormValid() {
        setError(null);
        if (MyLookup.getCurrentPatient() == null) {
            setError(NbBundle.getMessage(AbstractRecordEditorPanel.class, "noopendiary"));
            return false;
        }
        if (dateTimePanel == null) {
            setError(NbBundle.getMessage(AbstractRecordEditorPanel.class, "date.error"));
            return false;
        }
        String error = validateForm();
        if (error != null) {
            setError(error);
            return false;
        }
        return true;
    }

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if (selectedRecord != null && selectedRecord.isStillPersistent()) {
            selectedRecord.delete();
        }
        fireDataChanged(new DataChangedEvent(this, selectedRecord.getClass()));
        selectedRecord = null;
        onRecordSelectionChanged();
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        selectedRecord = null;
        onRecordSelectionChanged();
}//GEN-LAST:event_cancelButtonActionPerformed

    public T getSelectedRecord() {
        return selectedRecord;
    }

    public final void setRecord(T[] recs) {
        selectedRecord = recs != null && recs.length > 0 ? recs[0] : null;
        onRecordSelectionChanged();
        onSetRecord(recs);
    }

    private void onRecordSelectionChanged() {
        deleteButton.setVisible(selectedRecord != null);
        cancelButton.setVisible(selectedRecord != null);
    }

    protected void setError(String error) {
        if (error == null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        }
        errorLabel.setText(error);
        errorLabel.setVisible(true);
    }

    protected abstract void onSetRecord(T[] recs);

    protected abstract T loadRecordByFormIfExist();

    /**
     *
     * @return null on succes in other case localized error message
     */
    protected abstract String validateForm();

    protected abstract T saveRecord();

    protected abstract T updateRecord(T rec);

    protected final DocumentListener validatorListener = new DocumentListener() {

        @Override
        public void insertUpdate(DocumentEvent e) {
            validateForm();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validateForm();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            //Plain text components don't fire these events
        }
    };
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected final javax.swing.JButton cancelButton = new javax.swing.JButton();
    protected final org.diabetesdiary.commons.swing.calendar.DateTimePanel dateTimePanel = new org.diabetesdiary.commons.swing.calendar.DateTimePanel();
    protected final javax.swing.JButton deleteButton = new javax.swing.JButton();
    protected final javax.swing.JLabel errorLabel = new javax.swing.JLabel();
    protected final javax.swing.JButton saveButton = new javax.swing.JButton();
    // End of variables declaration//GEN-END:variables
}
