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
package org.diabetesdiary.print;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Action which shows PrintPreview component.
 */
public class PrintPreviewAction extends AbstractAction {

    public PrintPreviewAction() {
        super(NbBundle.getMessage(PrintPreviewAction.class, "CTL_PrintPreviewAction"));
        putValue(SMALL_ICON, new ImageIcon(ImageUtilities.loadImage(PrintPreviewTopComponent.ICON_PATH, true)));
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = PrintPreviewTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
