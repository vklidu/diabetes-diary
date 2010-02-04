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

import java.util.List;
import org.diabetesdiary.diary.domain.Activity;
import org.diabetesdiary.diary.domain.ActivityGroup;
import org.diabetesdiary.diary.domain.Food;
import org.diabetesdiary.diary.domain.FoodGroup;
import org.diabetesdiary.diary.domain.FoodUnit;
import org.diabetesdiary.diary.domain.Insulin;
import org.diabetesdiary.diary.domain.InsulinType;
import org.diabetesdiary.diary.domain.Investigation;
import org.diabetesdiary.diary.domain.InvestigationGroup;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.domain.RecordFood;

/**
 *
 * @author Jiri Majer
 */
public interface DiaryRepository {

    public List<Patient> getPatients();

    public Patient getPatient(Long idPatient);

    public List<Activity> getActivities();

    public List<ActivityGroup> getActivityGroups();

    public Activity getActivity(Long idActivity);

    public FoodUnit getSacharidUnit(String unit);

    public List<FoodUnit> getSacharidUnits();

    public List<FoodGroup> getFoodGroups();

    public List<FoodGroup> getBaseFoodGroups();

    public Food getFood(Long idFood);

    public FoodGroup getFoodGroup(Long idGroup);

    public List<Insulin> getInsulines();

    public List<InsulinType> getInsulinTypes();

    public Insulin getInsulin(Long idInsulin);

    public List<Investigation> getInvestigations();

    public List<InvestigationGroup> getInvestigationGroups();

    public Investigation getInvestigation(Long idInvestigation);

    public InvestigationGroup getInvestigationGroup(Long idGroup);

    public RecordFood getRecordFood(Long id);
}
