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
package org.diabetesdiary.diary.utils;

import org.diabetesdiary.diary.api.DiaryRepository;
import org.diabetesdiary.diary.api.DiaryService;
import org.diabetesdiary.diary.domain.Patient;
import org.openide.ErrorManager;
import org.openide.util.Lookup;

/**
 *
 * @author Jiri Majer
 */
public class MyLookup {
    
    private static Patient curPat;

    /** Creates a new instance of DbLookUp */
    private MyLookup() {
    }

    public static Patient getCurrentPatient() {
        return curPat;
    }

    public synchronized static void setCurrentPatient(Patient patient) {
        curPat = patient;
    }

    
    public static DiaryRepository getDiaryRepo(){
        Lookup lookup = Lookup.getDefault();
        DiaryRepository diary = (DiaryRepository)lookup.lookup(DiaryRepository.class);
        if (diary == null) {
            // this will show up as a flashing round button in the bottom-right corner
            ErrorManager.getDefault().notify(new IllegalStateException("Cannot locate DiaryRepository implementation"));
        }
        return diary;
    }

    public static DiaryService getDiaryService(){
        Lookup lookup = Lookup.getDefault();
        DiaryService diary = (DiaryService)lookup.lookup(DiaryService.class);
        if (diary == null) {
            // this will show up as a flashing round button in the bottom-right corner
            ErrorManager.getDefault().notify(new IllegalStateException("Cannot locate DiaryService implementation"));
        }
        return diary;
    }
    
}
