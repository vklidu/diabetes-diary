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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

final class CalendarOptionsPanelController extends OptionsPanelController {
    
    private CalendarPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;
    
    public void update() {
        getPanel().load();
        changed = false;
    }
    
    public void applyChanges() {
        if(isValid()){
            getPanel().store();
            changed = false;
        }
    }
    
    public void cancel() {
        // need not do anything special, if no changes have been persisted yet
    }
    
    public boolean isValid() {
        return getPanel().valid();
    }
    
    public boolean isChanged() {
        return changed;
    }
    
    public HelpCtx getHelpCtx() {
        return null; // new HelpCtx("...ID") if you have a help set
    }
    
    public JComponent getComponent(Lookup masterLookup) {
        return getPanel();
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
    
    private CalendarPanel getPanel() {
        if (panel == null) {
            panel = new CalendarPanel(this);
        }
        return panel;
    }
    
    void changed() {
        if (!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }
    
}
