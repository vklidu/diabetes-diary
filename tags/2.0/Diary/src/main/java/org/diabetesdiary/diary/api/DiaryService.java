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
package org.diabetesdiary.diary.api;

import org.diabetesdiary.diary.domain.ActivityGroup;
import org.diabetesdiary.diary.domain.FoodGroup;
import org.diabetesdiary.diary.domain.Insulin;
import org.diabetesdiary.diary.domain.InsulinType;
import org.diabetesdiary.diary.domain.InvestigationGroup;
import org.diabetesdiary.diary.domain.Patient;
import org.joda.time.LocalDate;

/**
 *
 * @author Jirka Majer
 */
public interface DiaryService {

    public Patient newPatient(String name, String surname, boolean male, LocalDate born, LocalDate sufferFrom,
            boolean pumpUsage, String email, String phone, String adress, Insulin basalInsulin, Insulin bolusInsulin,
            Double perSensitivity, Double hepSensitivity, Double filtrationRate, Double renalThreshold);

    public FoodGroup newFoodGroup(String name, FoodGroup group);

    public InvestigationGroup newInvestigationGroup(String name);

    public InsulinType newInsulinType(String name, Double parameterS, Double parameterA, Double parameterB, String description);

    public ActivityGroup newActivityGroup(String name, String description);
}
