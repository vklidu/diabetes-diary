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
package org.diabetesdiary.calendar.table;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.diabetesdiary.diary.domain.RecordInvest;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class GlykemieCellEditor extends DefaultCellEditor {

    JFormattedTextField ftf;
    NumberFormat numberFormat;
    private Double minimum,  maximum;

    public GlykemieCellEditor() {
        super(new JFormattedTextField());
        ftf = (JFormattedTextField) getComponent();
        ftf.setBorder(null);
        minimum = 0d;
        maximum = 400d;

        //Set up the editor for the integer cells.
        numberFormat = NumberFormat.getNumberInstance();
        NumberFormatter intFormatter = new NumberFormatter(numberFormat);
        intFormatter.setFormat(numberFormat);
        intFormatter.setMinimum(minimum);
        intFormatter.setMaximum(maximum);

        ftf.setFormatterFactory(new DefaultFormatterFactory(intFormatter));
        ftf.setValue(minimum);
        ftf.setHorizontalAlignment(JTextField.TRAILING);
        ftf.setFocusLostBehavior(JFormattedTextField.PERSIST);

        //React when the user presses Enter while the editor is
        //active.  (Tab is handled as specified by
        //JFormattedTextField's focusLostBehavior property.)
        ftf.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_ENTER, 0),
                "check");
        ftf.getActionMap().put("check", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ftf.isEditValid()) { //The text is invalid.

                    if (userSaysRevert()) { //reverted

                        ftf.postActionEvent(); //inform the editor

                    }
                } else {
                    try {              //The text is valid,

                        ftf.commitEdit();     //so use it.

                        ftf.postActionEvent(); //stop editing

                    } catch (java.text.ParseException exc) {
                    }
                }
            }
        });
    }

    //Override to invoke setValue on the formatted text field.
    @Override
    public Component getTableCellEditorComponent(JTable table,
            Object value, boolean isSelected,
            int row, int column) {
        JFormattedTextField ftfLocal = (JFormattedTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        ftfLocal.setValue(null);
        if (value instanceof RecordInvest) {
            ftfLocal.setValue(((RecordInvest) value).getValue());
        } else if (value instanceof RecordInvest[]) {
            RecordInvest[] values = (RecordInvest[]) value;
            if (values.length > 0 && values[0] != null) {
                ftfLocal.setValue(((RecordInvest[]) value)[0].getValue());
            }
        }
        return ftfLocal;
    }

    //Override to ensure that the value remains an Integer.
    @Override
    public Object getCellEditorValue() {
        JFormattedTextField ftfLocal = (JFormattedTextField) getComponent();
        Object o = ftfLocal.getValue();
        if (o instanceof Double) {
            return o;
        } else if (o instanceof Number) {
            return ((Number) o).doubleValue();
        } else if (o != null) {
            try {
                return numberFormat.parse(o.toString()).doubleValue();
            } catch (ParseException exc) {
                return null;
            }
        }
        return null;
    }

    //Override to check whether the edit is valid,
    //setting the value if it is and complaining if
    //it isn't.  If it's OK for the editor to go
    //away, we need to invoke the superclass's version
    //of this method so that everything gets cleaned up.
    @Override
    public boolean stopCellEditing() {
        JFormattedTextField ftfLocal = (JFormattedTextField) getComponent();
        if (ftfLocal.isEditValid()) {
            try {
                ftfLocal.commitEdit();
            } catch (java.text.ParseException exc) {
            }

        } else { //text is invalid

            if (!userSaysRevert()) { //user wants to edit

                return false; //don't let the editor go away

            }
        }
        return super.stopCellEditing();
    }

    /**
     * Lets the user know that the text they entered is
     * bad. Returns true if the user elects to revert to
     * the last good value.  Otherwise, returns false,
     * indicating that the user wants to continue editing.
     */
    protected boolean userSaysRevert() {
        Toolkit.getDefaultToolkit().beep();
        ftf.selectAll();
        Object[] options = {NbBundle.getMessage(GlykemieCellEditor.class, "Editace"), NbBundle.getMessage(GlykemieCellEditor.class, "Zpet")};
        int answer = JOptionPane.showOptionDialog(
                SwingUtilities.getWindowAncestor(ftf),
                NbBundle.getMessage(GlykemieCellEditor.class, "Hodnotu_glykemie_zadavejte_od_1-40."),
                NbBundle.getMessage(GlykemieCellEditor.class, "Neplatna_hodnota_glykemie"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[1]);

        if (answer == 1) { //Revert!

            ftf.setValue(ftf.getValue());
            return true;
        }
        return false;
    }
}