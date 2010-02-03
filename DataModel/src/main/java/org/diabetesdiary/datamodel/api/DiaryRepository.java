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

import java.util.List;
import org.diabetesdiary.datamodel.domain.Activity;
import org.diabetesdiary.datamodel.domain.ActivityGroup;
import org.diabetesdiary.datamodel.domain.Food;
import org.diabetesdiary.datamodel.domain.FoodGroup;
import org.diabetesdiary.datamodel.domain.FoodUnit;
import org.diabetesdiary.datamodel.domain.Insulin;
import org.diabetesdiary.datamodel.domain.InsulinType;
import org.diabetesdiary.datamodel.domain.Investigation;
import org.diabetesdiary.datamodel.domain.InvestigationGroup;
import org.diabetesdiary.datamodel.domain.Patient;

/**
 *
 * @author Jiri Majer
 */
public interface DiaryRepository {

    public void setCurrentPatient(Patient patient);

    public Patient getCurrentPatient();

    public List<Patient> getPatients();

    public Patient getPatient(Long idPatient);

    public List<Activity> getActivities();

    public List<ActivityGroup> getActivityGroups();

    public Activity getActivity(Long idActivity);

    public List<Food> getFoods();

    public FoodUnit getFoodUnit(Long idFood, String unit);

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
}
