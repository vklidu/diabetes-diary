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
import javax.swing.text.NumberFormatter;
import org.diabetesdiary.datamodel.pojo.RecordFoodDO;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class FoodCellEditor extends DefaultCellEditor {

    JFormattedTextField formattedTF;
    NumberFormat doubleFormat;
    private double minimum,  maximum;

    public FoodCellEditor() {
        super(new JFormattedTextField(NumberFormat.getNumberInstance()));
        formattedTF = (JFormattedTextField) getComponent();
        formattedTF.setBorder(null);
        NumberFormatter doubleFormatter = (NumberFormatter) formattedTF.getFormatter();

        minimum = 0d;
        maximum = 50d;

        doubleFormatter.setMinimum(minimum);
        doubleFormatter.setMaximum(maximum);

        formattedTF.setValue(minimum);
        formattedTF.setHorizontalAlignment(JTextField.TRAILING);
        formattedTF.setFocusLostBehavior(JFormattedTextField.PERSIST);

        //React when the user presses Enter while the editor is
        //active.  (Tab is handled as specified by
        //JFormattedTextField's focusLostBehavior property.)
        formattedTF.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_ENTER, 0),
                "check");
        formattedTF.getActionMap().put("check", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                if (!formattedTF.isEditValid()) { //The text is invalid.

                    if (userSaysRevert()) { //reverted

                        formattedTF.postActionEvent(); //inform the editor

                    }
                } else {
                    try {              //The text is valid,

                        formattedTF.commitEdit();     //so use it.

                        formattedTF.postActionEvent(); //stop editing

                    } catch (java.text.ParseException exc) {
                    }
                }
            }
        });
    }

    //Override to invoke setValue on the formatted text field.
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JFormattedTextField ftf = (JFormattedTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        ftf.setValue(null);
        if (value instanceof RecordFoodDO) {
            RecordFoodDO recFood = (RecordFoodDO) value;
            if (recFood.getAmount() != null) {
                double unit = recFood.getAmount();
                ftf.setValue(unit);
            }
        } else {
            //ftf.setValue(minimum);
        }
        return ftf;
    }

    //Override to ensure that the value remains an Integer.
    @Override
    public Object getCellEditorValue() {
        JFormattedTextField ftf = (JFormattedTextField) getComponent();
        Object o = ftf.getValue();
        if (o instanceof Double) {
            return o;
        } else if (o instanceof Number) {
            return ((Number) o).doubleValue();
        } else if (o != null) {
            try {
                return doubleFormat.parse(o.toString()).doubleValue();
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
        JFormattedTextField ftf = (JFormattedTextField) getComponent();
        if (ftf.isEditValid()) {
            try {
                ftf.commitEdit();
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
        formattedTF.selectAll();
        Object[] options = {NbBundle.getMessage(FoodCellEditor.class, "Editace"), NbBundle.getMessage(FoodCellEditor.class, "Zpet")};
        int answer = JOptionPane.showOptionDialog(
                SwingUtilities.getWindowAncestor(formattedTF),
                NbBundle.getMessage(FoodCellEditor.class, "Pocet_jednotek_zadavejte_od_0-50."),
                NbBundle.getMessage(FoodCellEditor.class, "Neplatna_hodnota_jednotek"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[1]);

        if (answer == 1) { //Revert!

            formattedTF.setValue(formattedTF.getValue());
            return true;
        }
        return false;
    }
}