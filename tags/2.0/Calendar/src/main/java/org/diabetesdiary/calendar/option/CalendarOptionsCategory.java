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

package org.diabetesdiary.calendar.option;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.spi.options.OptionsCategory;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

public final class CalendarOptionsCategory extends OptionsCategory {
    
    @Override
    public Icon getIcon() {
        return new ImageIcon(ImageUtilities.loadImage("org/diabetesdiary/calendar/resources/settings.png"));
    }
    
    @Override
    public String getCategoryName() {
        return NbBundle.getMessage(CalendarOptionsCategory.class, "OptionsCategory_Name");
    }
    
    @Override
    public String getTitle() {
        return NbBundle.getMessage(CalendarOptionsCategory.class, "OptionsCategory_Title");
    }
    
    @Override
    public OptionsPanelController create() {
        return new CalendarOptionsPanelController();
    }
    
}
