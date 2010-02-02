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


package org.diabetesdiary.datamodel.api;

import org.openide.ErrorManager;
import org.openide.util.Lookup;

/**
 *
 * @author Jiri Majer
 */
public class DbLookUp {
    
    /** Creates a new instance of DbLookUp */
    private DbLookUp() {
    }
    
    public static Diary getDiary(){
        Lookup lookup = Lookup.getDefault();
        Diary diary = (Diary)lookup.lookup(Diary.class);
        if (diary == null) {
            // this will show up as a flashing round button in the bottom-right corner
            ErrorManager.getDefault().notify(
                    new IllegalStateException("Cannot locate Diary implementation"));
        }
        return diary;
    }
    
    
    public static InvestigationAdministrator getInvesAdmin(){
        Lookup lookup = Lookup.getDefault();
        InvestigationAdministrator diary = (InvestigationAdministrator)lookup.lookup(InvestigationAdministrator.class);
        if (diary == null) {
            // this will show up as a flashing round button in the bottom-right corner
            ErrorManager.getDefault().notify(
                    new IllegalStateException("Cannot locate InvestigationAdministrator implementation"));
        }
        return diary;
    }
    
    
    public static FoodAdministrator getFoodAdmin(){
        Lookup lookup = Lookup.getDefault();
        FoodAdministrator diary = (FoodAdministrator)lookup.lookup(FoodAdministrator.class);
        if (diary == null) {
            // this will show up as a flashing round button in the bottom-right corner
            ErrorManager.getDefault().notify(
                    new IllegalStateException("Cannot locate FoodAdministrator implementation"));
        }
        return diary;
    }
    
    public static ActivityAdministrator getActivityAdmin(){
        Lookup lookup = Lookup.getDefault();
        ActivityAdministrator diary = (ActivityAdministrator) lookup.lookup(ActivityAdministrator.class);
        if (diary == null) {
            // this will show up as a flashing round button in the bottom-right corner
            ErrorManager.getDefault().notify(
                    new IllegalStateException("Cannot locate ActivityAdministrator implementation"));
        }
        return diary;
    }

    
    public static InsulinAdministrator getInsulinAdmin(){
        Lookup lookup = Lookup.getDefault();
        InsulinAdministrator diary = (InsulinAdministrator)lookup.lookup(InsulinAdministrator.class);
        if (diary == null) {
            // this will show up as a flashing round button in the bottom-right corner
            ErrorManager.getDefault().notify(
                    new IllegalStateException("Cannot locate InsulinAdministrator implementation"));
        }
        return diary;
    }
}
