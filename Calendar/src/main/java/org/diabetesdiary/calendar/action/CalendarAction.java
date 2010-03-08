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
package org.diabetesdiary.calendar.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.diabetesdiary.calendar.ui.*;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Action which shows Calendar component.
 */
public class CalendarAction extends AbstractAction {

    public CalendarAction() {
        super(NbBundle.getMessage(CalendarAction.class, "CTL_CalendarAction"));
        putValue(SMALL_ICON, new ImageIcon(ImageUtilities.loadImage(CalendarTopComponent.ICON_PATH, true)));
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = CalendarTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
