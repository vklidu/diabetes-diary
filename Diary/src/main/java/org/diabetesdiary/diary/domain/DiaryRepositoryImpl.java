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

import com.google.common.collect.Lists;
import java.util.List;
import org.diabetesdiary.diary.api.DiaryRepository;
import org.diabetesdiary.diary.service.db.ActivityDO;
import org.diabetesdiary.diary.service.db.ActivityGroupDO;
import org.diabetesdiary.diary.service.db.FoodDO;
import org.diabetesdiary.diary.service.db.FoodGroupDO;
import org.diabetesdiary.diary.service.db.FoodUnitDO;
import org.diabetesdiary.diary.service.db.InsulinDO;
import org.diabetesdiary.diary.service.db.InsulinTypeDO;
import org.diabetesdiary.diary.service.db.InvestigationDO;
import org.diabetesdiary.diary.service.db.InvestigationGroupDO;
import org.diabetesdiary.diary.service.db.PatientDO;
import org.diabetesdiary.diary.service.db.RecordFoodDO;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jiri Majer
 */
@Repository
@Transactional(readOnly=true)
public class DiaryRepositoryImpl extends AbstractRepository implements DiaryRepository {

    @Override
    public List<Patient> getPatients() {
        return Lists.newArrayList(Lists.transform(getSession().createCriteria(PatientDO.class).list(), Patient.CREATE_FUNCTION));
    }

    @Override
    public Patient getPatient(Long idPatient) {
        return new Patient((PatientDO) getSession().load(PatientDO.class, idPatient));
    }

    @Override
    public List<Activity> getActivities() {
        return Lists.newArrayList(Lists.transform(getSession().createCriteria(ActivityDO.class).list(), Activity.CREATE_FUNCTION));
    }

    @Override
    public List<ActivityGroup> getActivityGroups() {
        return Lists.newArrayList(Lists.transform(getSession().createCriteria(ActivityGroupDO.class).list(), ActivityGroup.CREATE_FUNCTION));
    }

    @Override
    public Activity getActivity(Long idActivity) {
        return new Activity((ActivityDO) getSession().load(ActivityDO.class, idActivity));
    }

    @Override
    public List<FoodGroup> getFoodGroups() {
        return Lists.newArrayList(Lists.transform(getSession().createCriteria(FoodGroupDO.class).list(), FoodGroup.CREATE_FUNCTION));
    }

    @Override
    public List<FoodGroup> getBaseFoodGroups() {
        return Lists.newArrayList(Lists.transform(getSession()
                .createCriteria(FoodGroupDO.class)
                .add(Restrictions.isNull("parent"))
                .list(), FoodGroup.CREATE_FUNCTION));
    }

    @Override
    public Food getFood(Long idFood) {
        return new Food((FoodDO) getSession().load(FoodDO.class, idFood));
    }

    @Override
    public FoodGroup getFoodGroup(Long idGroup) {
        return new FoodGroup((FoodGroupDO) getSession().load(FoodGroupDO.class, idGroup));
    }

    @Override
    public List<Insulin> getInsulines() {
        return Lists.newArrayList(Lists.transform(getSession().createCriteria(InsulinDO.class).list(), Insulin.CREATE_FUNCTION));
    }

    @Override
    public List<InsulinType> getInsulinTypes() {
        return Lists.newArrayList(Lists.transform(getSession().createCriteria(InsulinTypeDO.class).list(), InsulinType.CREATE_FUNCTION));
    }

    @Override
    public Insulin getInsulin(Long idInsulin) {
        return new Insulin((InsulinDO) getSession().load(InsulinDO.class, idInsulin));
    }

    @Override
    public List<Investigation> getInvestigations() {
        return Lists.newArrayList(Lists.transform(getSession().createCriteria(InvestigationDO.class).list(), Investigation.CREATE_FUNCTION));
    }

    @Override
    public List<InvestigationGroup> getInvestigationGroups() {
        return Lists.newArrayList(Lists.transform(getSession().createCriteria(InvestigationGroupDO.class).list(), InvestigationGroup.CREATE_FUNCTION));
    }

    @Override
    public Investigation getInvestigation(Long idInvestigation) {
        return new Investigation((InvestigationDO) getSession().load(InvestigationDO.class, idInvestigation));
    }

    @Override
    public Investigation getWellKnownInvestigation(WKInvest wKInvest) {
        return new Investigation((InvestigationDO) getSession().createCriteria(InvestigationDO.class).add(Restrictions.eq("wkinvest", wKInvest)).uniqueResult());
    }

    @Override
    public Food getWellKnownFood(WKFood wKFood) {
        return new Food((FoodDO) getSession().createCriteria(FoodDO.class).add(Restrictions.eq("wkfood", wKFood)).uniqueResult());
    }

    @Override
    public InvestigationGroup getInvestigationGroup(Long idGroup) {
        return new InvestigationGroup((InvestigationGroupDO) getSession().load(InvestigationGroupDO.class, idGroup));
    }

    @Override
    public FoodUnit getSacharidUnit(String unit) {
        FoodUnitDO res = (FoodUnitDO) getSession().createCriteria(FoodUnitDO.class)
                .createAlias("food", "food")
                .add(Restrictions.eq("food.wkfood", WKFood.SACCHARIDE))
                .add(Restrictions.eq("unit", unit)).uniqueResult();
        return res == null ? null : new FoodUnit(res);
    }

    @Override
    public List<FoodUnit> getSacharidUnits() {
        List<FoodUnitDO> res = getSession().createCriteria(FoodUnitDO.class)
                .createAlias("food", "food")
                .add(Restrictions.eq("food.wkfood", WKFood.SACCHARIDE))
                .list();
        return Lists.newArrayList(Lists.transform(res, FoodUnit.CREATE_FUNCTION));
    }

    @Override
    public RecordFood getRecordFood(Long id) {
        return new RecordFood((RecordFoodDO) getSession().load(RecordFoodDO.class, id));
    }

}
