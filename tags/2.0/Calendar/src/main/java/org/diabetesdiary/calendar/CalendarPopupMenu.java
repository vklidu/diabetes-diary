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
import org.diabetesdiary.calendar.utils.DataChangeEvent;
import org.diabetesdiary.calendar.utils.DataChangeListener;
import org.diabetesdiary.diary.domain.AbstractRecord;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class CalendarPopupMenu extends JPopupMenu {

    private static final String ICON_PATH = "org/diabetesdiary/calendar/resources/delete16.png";

    public CalendarPopupMenu(final AbstractRecord[] value, final DataChangeListener listener) {
        JMenuItem menuItemDelete = new JMenuItem(NbBundle.getMessage(CalendarPopupMenu.class, "Delete"), ImageUtilities.loadImageIcon(ICON_PATH, true));
        menuItemDelete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractRecord last = null;
                for (AbstractRecord rec : (AbstractRecord[]) value) {
                    last = rec;
                    rec.delete();
                }
                if (last != null) {
                    listener.onDataChange(new DataChangeEvent(this, last.getClass()));
                }
            }
        });
        if (value != null && value.length > 0) {
            add(menuItemDelete);
        }
    }

    public CalendarPopupMenu(final AbstractRecord value, final DataChangeListener listener) {
        this(new AbstractRecord[]{value}, listener);
    }
}
