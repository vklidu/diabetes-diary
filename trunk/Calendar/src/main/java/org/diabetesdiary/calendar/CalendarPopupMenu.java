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
import org.diabetesdiary.diary.utils.MyLookup;
import org.diabetesdiary.diary.api.DiaryRepository;
import org.diabetesdiary.diary.service.db.RecordFoodDO;
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

            public void actionPerformed(ActionEvent e) {
                DiaryRepository diary = MyLookup.getDiaryRepo();
                if (value instanceof RecordFoodDO) {
                    RecordFoodDO food = (RecordFoodDO) value;
                    diary.deleteRecordFood(food.getId().getIdPatient(), food.getId().getDate());
                } else if (value instanceof RecordFoodDO[]) {
                    for (RecordFoodDO food : (RecordFoodDO[]) value) {
                        diary.deleteRecordFood(food.getId().getIdPatient(), food.getId().getDate());
                    }
                } else {
                    diary.deleteRecord(value);
                }
                CalendarTopComponent.getDefault().getModel().fillData();
                CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
            }
        });
        menu.add(menuItemDelete);
        return menu;
    }
}
