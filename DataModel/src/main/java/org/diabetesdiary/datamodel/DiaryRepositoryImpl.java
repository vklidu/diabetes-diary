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
package org.diabetesdiary.datamodel;

import com.google.common.collect.Lists;
import java.util.List;
import org.diabetesdiary.datamodel.api.DiaryRepository;
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
import org.diabetesdiary.datamodel.pojo.PatientDO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jiri Majer
 */
@Repository
public class DiaryRepositoryImpl extends AbstractRepository implements DiaryRepository {

    private transient Patient curPat = null;

    @Override
    @Transactional(readOnly=true)
    public List<Patient> getPatients() {
        return Lists.newArrayList(Lists.transform(getSession().createCriteria(PatientDO.class).list(), Patient.CREATE_FUNCTION));
    }

    @Override
    @Transactional(readOnly=true)
    public Patient getPatient(Long idPatient) {
        return new Patient((PatientDO) getSession().load(PatientDO.class, idPatient));
    }

    @Override
    public Patient getCurrentPatient() {
        return curPat;
    }

    @Override
    public void setCurrentPatient(Patient patient) {
        this.curPat = patient;
    }

    @Override
    public List<Activity> getActivities() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ActivityGroup> getActivityGroups() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Activity getActivity(Long idActivity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Food> getFoods() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FoodUnit getFoodUnit(Long idFood, String unit) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<FoodGroup> getFoodGroups() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<FoodGroup> getBaseFoodGroups() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Food getFood(Long idFood) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FoodGroup getFoodGroup(Long idGroup) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Insulin> getInsulines() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<InsulinType> getInsulinTypes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Insulin getInsulin(Long idInsulin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Investigation> getInvestigations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<InvestigationGroup> getInvestigationGroups() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Investigation getInvestigation(Long idInvestigation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InvestigationGroup getInvestigationGroup(Long idGroup) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
