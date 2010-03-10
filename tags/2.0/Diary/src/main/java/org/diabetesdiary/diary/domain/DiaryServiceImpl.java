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
package org.diabetesdiary.diary.domain;

import org.diabetesdiary.diary.api.DiaryService;
import org.diabetesdiary.diary.service.db.ActivityGroupDO;
import org.diabetesdiary.diary.service.db.FoodGroupDO;
import org.diabetesdiary.diary.service.db.InsulinDO;
import org.diabetesdiary.diary.service.db.InsulinTypeDO;
import org.diabetesdiary.diary.service.db.InvestigationGroupDO;
import org.diabetesdiary.diary.service.db.PatientDO;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jiri Majer
 */
@Service
@Transactional
public class DiaryServiceImpl extends AbstractService implements DiaryService {

    @Override
    public Patient newPatient(String name, String surname, boolean male, LocalDate born, LocalDate sufferFrom, boolean pumpUsage, String email, String phone, String adress, Insulin basalInsulin, Insulin bolusInsulin, Double perSensitivity, Double hepSensitivity, Double filtrationRate, Double renalThreshold) {
        PatientDO pat = new PatientDO();
        pat.setAdress(adress);
        pat.setBasalInsulin((InsulinDO) getSession().load(InsulinDO.class, basalInsulin.getId()));
        pat.setBolusInsulin((InsulinDO) getSession().load(InsulinDO.class, bolusInsulin.getId()));
        pat.setBorn(born);
        pat.setEmail(email);
        pat.setFiltrationRate(filtrationRate);
        pat.setHepSensitivity(hepSensitivity);
        pat.setMale(male);
        pat.setName(name);
        pat.setPerSensitivity(perSensitivity);
        pat.setPhone(phone);
        pat.setPumpUsage(pumpUsage);
        pat.setRenalThreshold(renalThreshold);
        pat.setSufferFrom(sufferFrom);
        pat.setSurname(surname);
        getSession().save(pat);
        return new Patient(pat);
    }

    @Override
    public FoodGroup newFoodGroup(String name, FoodGroup group) {
        FoodGroupDO groupDO = new FoodGroupDO();
        groupDO.setName(name);
        if (group != null) {
            groupDO.setParent((FoodGroupDO) getSession().load(InsulinDO.class, group.getId()));
        }
        getSession().save(groupDO);
        return new FoodGroup(groupDO);
    }

    @Override
    public InvestigationGroup newInvestigationGroup(String name) {
        InvestigationGroupDO groupDO = new InvestigationGroupDO();
        groupDO.setName(name);
        getSession().save(groupDO);
        return new InvestigationGroup(groupDO);
    }

    @Override
    public InsulinType newInsulinType(String name, Double parameterS, Double parameterA, Double parameterB, String description) {
        InsulinTypeDO typeDO = new InsulinTypeDO();
        typeDO.setDescription(description);
        typeDO.setName(name);
        typeDO.setParameterA(parameterA);
        typeDO.setParameterB(parameterB);
        typeDO.setParameterS(parameterS);
        getSession().save(typeDO);
        return new InsulinType(typeDO);
    }

    @Override
    public ActivityGroup newActivityGroup(String name, String description) {
        ActivityGroupDO groupDO = new ActivityGroupDO();
        groupDO.setDescription(description);
        groupDO.setName(name);
        getSession().save(groupDO);
        return new ActivityGroup(groupDO);
    }

}
