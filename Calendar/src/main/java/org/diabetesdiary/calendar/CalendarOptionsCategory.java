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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.spi.options.OptionsCategory;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

public final class CalendarOptionsCategory extends OptionsCategory {
    
    public Icon getIcon() {
        return new ImageIcon(Utilities.loadImage("org/diabetesdiary/calendar/resources/Pencil.gif"));
    }
    
    public String getCategoryName() {
        return NbBundle.getMessage(CalendarOptionsCategory.class, "OptionsCategory_Name");
    }
    
    public String getTitle() {
        return NbBundle.getMessage(CalendarOptionsCategory.class, "OptionsCategory_Title");
    }
    
    public OptionsPanelController create() {
        return new CalendarOptionsPanelController();
    }
    
}
