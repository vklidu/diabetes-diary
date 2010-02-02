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

package org.diabetesdiary.calendar.ui;

import java.text.MessageFormat;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class NewPatientWizard extends WizardDescriptor {       
    
    
    public NewPatientWizard(Panel[] panel){        
        super(panel);                
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        setTitleFormat(new MessageFormat("{0}"));
        setTitle(NbBundle.getMessage(NewPatientWizard.class, "LBL_NewPatientWizard_Title"));        
    }
        
        
}
