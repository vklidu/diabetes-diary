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
package org.diabetesdiary.calendar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.diabetesdiary.calendar.ui.CalendarTopComponent;
import org.diabetesdiary.diary.domain.RecordActivity;
import org.diabetesdiary.diary.domain.RecordFood;
import org.diabetesdiary.diary.domain.RecordInsulin;
import org.diabetesdiary.diary.domain.RecordInvest;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class CalendarPopupMenu {

    public static JPopupMenu createPopupMenu(final Object value) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem menuItemDelete = new JMenuItem(NbBundle.getMessage(CalendarPopupMenu.class, "Delete"));
        menuItemDelete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (value instanceof RecordFood) {
                    RecordFood food = (RecordFood) value;
                    food.delete();
                } else if (value instanceof RecordFood[]) {
                    for (RecordFood food : (RecordFood[]) value) {
                        food.delete();
                    }
                } else if (value instanceof RecordActivity) {
                    ((RecordActivity) value).delete();
                } else if (value instanceof RecordInsulin) {
                    ((RecordInsulin) value).delete();
                } else if (value instanceof RecordInvest) {
                    ((RecordInvest) value).delete();
                } else {
                    throw new IllegalStateException();
                }
                CalendarTopComponent.getDefault().getModel().fillData();
                CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
            }
        });
        menu.add(menuItemDelete);
        return menu;
    }
}
