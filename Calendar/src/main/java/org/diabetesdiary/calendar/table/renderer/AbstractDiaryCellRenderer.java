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
package org.diabetesdiary.calendar.table.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Jirka Majer
 */
public abstract class AbstractDiaryCellRenderer<T> implements TableCellRenderer {

    private static final Color forColor = Color.BLACK;
    private static final Color backColor = Color.WHITE;
    private static final Color forSelColor = Color.WHITE;
    private static final Color backSelColor = new Color(30, 30, 100);
    private final JLabel result = new JLabel();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        result.setOpaque(true);
        result.setBackground(getBackgroundColor((T) value, isSelected));
        result.setForeground(getTextColor((T) value, isSelected));
        result.setHorizontalAlignment(JLabel.CENTER);
        result.setText(getText((T) value));
        result.setToolTipText(getToolTip((T) value));
        return result;
    }

    protected abstract String getText(T rec);

    protected Color getTextColor(T rec, boolean selected) {
        return selected ? forSelColor : forColor;
    }

    protected Color getBackgroundColor(T rec, boolean selected) {
        return selected ? backSelColor : backColor;
    }

    protected String getToolTip(T rec) {
        return null;
    }
}
