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
package org.diabetesdiary.calendar.table.editor;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
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
import org.openide.util.NbBundle;

/**
 *
 * @author Jirka Majer
 */
public abstract class NumberEditor<T extends Number & Comparable, S> extends DefaultCellEditor {

    private JFormattedTextField textField;
    private NumberFormat integerFormat;
    private T minimum, maximum;

    public NumberEditor(T min, T max) {
        super(new JFormattedTextField());
        textField = (JFormattedTextField) getComponent();
        minimum = min;
        maximum = max;
        //Set up the editor for the integer cells.
        integerFormat = getFormat(min.getClass());
        NumberFormatter formatter = new NumberFormatter(integerFormat);
        formatter.setFormat(integerFormat);
        formatter.setMinimum(minimum);
        formatter.setMaximum(maximum);

        textField.setFormatterFactory(new DefaultFormatterFactory(formatter));
        textField.setValue(minimum);
        textField.setHorizontalAlignment(JTextField.TRAILING);
        textField.setFocusLostBehavior(JFormattedTextField.PERSIST);

        //React when the user presses Enter while the editor is
        //active.  (Tab is handled as specified by
        //JFormattedTextField's focusLostBehavior property.)
        textField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "check");
        textField.getActionMap().put("check", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!textField.isEditValid()) { //The text is invalid.
                    if (userSaysRevert()) { //reverted
                        textField.postActionEvent(); //inform the editor
                    }
                } else {
                    try {              //The text is valid,
                        textField.commitEdit();     //so use it.
                        textField.postActionEvent(); //stop editing
                    } catch (java.text.ParseException exc) {
                    }
                }
            }
        });
    }

    private NumberFormat getFormat(Class clazz) {
        if (clazz.equals(Integer.class)) {
            return NumberFormat.getIntegerInstance();
        } else {
            return NumberFormat.getNumberInstance();
        }
    }

    //Override to invoke setValue on the formatted text field.
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JFormattedTextField ftf = (JFormattedTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        ftf.setValue(getValue((S)value));
        return ftf;
    }

    public abstract T getValue(S object);

    //Override to ensure that the value remains an Integer.
    @Override
    public Object getCellEditorValue() {
        JFormattedTextField ftf = (JFormattedTextField) getComponent();
        return (T) ftf.getValue();
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
        textField.selectAll();
        Object[] options = {NbBundle.getMessage(NumberEditor.class, "Editace"), NbBundle.getMessage(NumberEditor.class, "Zpet")};
        int answer = JOptionPane.showOptionDialog(
                SwingUtilities.getWindowAncestor(textField), 
                NbBundle.getMessage(NumberEditor.class, "NumberMaxMinError", minimum, maximum),
                NbBundle.getMessage(NumberEditor.class, "NumberMaxMinErrorTitle"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[1]);

        if (answer == 1) { //Revert!
            textField.setValue(textField.getValue());
            return true;
        }
        return false;
    }
}
