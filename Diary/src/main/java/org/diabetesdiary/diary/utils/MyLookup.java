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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

/**
 *
 * @author Jiri Majer
 */
@Configurable
public class MyLookup {

    private Patient curPat;
    private static final MyLookup instance = new MyLookup();
    @Autowired
    private transient DiaryRepository repository;
    @Autowired
    private transient DiaryService diaryService;

    /** Creates a new instance of DbLookUp */
    private MyLookup() {
    }

    public static Patient getCurrentPatient() {
        return instance.curPat;
    }

    public synchronized static void setCurrentPatient(Patient patient) {
        instance.curPat = patient;
    }

    public static DiaryRepository getDiaryRepo() {
        return instance.repository;
    }

    public static DiaryService getDiaryService() {
        return instance.diaryService;
    }
}
