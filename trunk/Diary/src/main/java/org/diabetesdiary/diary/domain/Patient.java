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

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.List;
import org.diabetesdiary.diary.service.db.ActivityDO;
import org.diabetesdiary.diary.service.db.FoodDO;
import org.diabetesdiary.diary.service.db.FoodUnitDO;
import org.diabetesdiary.diary.service.db.InsulinDO;
import org.diabetesdiary.diary.service.db.InvestigationDO;
import org.diabetesdiary.diary.service.db.PatientDO;
import org.diabetesdiary.diary.service.db.RecordActivityDO;
import org.diabetesdiary.diary.service.db.RecordFoodDO;
import org.diabetesdiary.diary.service.db.RecordInsulinDO;
import org.diabetesdiary.diary.service.db.RecordInvestDO;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jirka Majer
 */
@Configurable
public class Patient extends AbstractDomainObject {

    private final String name;
    private final String surname;
    private final boolean male;
    private final LocalDate born;
    private final LocalDate sufferFrom;
    private final boolean pumpUsage;
    private final String email;
    private final String phone;
    private final String adress;
    private final Insulin basalInsulin;
    private final Insulin bolusInsulin;
    private final Double perSensitivity;
    private final Double hepSensitivity;
    private final Double filtrationRate;
    private final Double renalThreshold;

    public Patient(PatientDO pat) {
        super(pat.getId());
        this.name = pat.getName();
        this.surname = pat.getSurname();
        this.male = pat.isMale();
        this.born = pat.getBorn();
        this.sufferFrom = pat.getSufferFrom();
        this.pumpUsage = pat.isPumpUsage();
        this.email = pat.getEmail();
        this.phone = pat.getPhone();
        this.adress = pat.getAdress();
        this.basalInsulin = new Insulin(pat.getBasalInsulin());
        this.bolusInsulin = new Insulin(pat.getBolusInsulin());
        this.perSensitivity = pat.getPerSensitivity();
        this.hepSensitivity = pat.getHepSensitivity();
        this.filtrationRate = pat.getFiltrationRate();
        this.renalThreshold = pat.getRenalThreshold();
    }

    @Transactional(readOnly=true)
    public Double getWeightBefore(DateTime date) {
        List<RecordInvestDO> result = getSession().createCriteria(RecordInvestDO.class)
                .createAlias("invest", "invest")
                .add(Restrictions.lt("datetime", date))
                .add(Restrictions.eq("invest.wkinvest", WKInvest.WEIGHT))
                .add(Restrictions.eq("patient.id", id))
                .addOrder(Order.desc("datetime")).list();
        return result.size() > 0 ? new RecordInvest(result.get(0)).getValue() : null;
    }

    @Transactional(readOnly=true)
    public Double getTallBefore(DateTime date) {
        List<RecordInvestDO> result = getSession().createCriteria(RecordInvestDO.class)
                .createAlias("invest", "invest")
                .add(Restrictions.lt("datetime", date))
                .add(Restrictions.eq("invest.wkinvest", WKInvest.HEIGHT))
                .add(Restrictions.eq("patient.id", id))
                .addOrder(Order.desc("datetime")).list();
        return result.size() > 0 ? new RecordInvest(result.get(0)).getValue() : null;
    }

    @Transactional(readOnly=true)
    public List<RecordInvest> getRecordInvests(DateTime from, DateTime to) {
        List<RecordInvestDO> result = getSession().createCriteria(RecordInvestDO.class)
                .add(Restrictions.ge("datetime", from))
                .add(Restrictions.le("datetime", to))
                .add(Restrictions.eq("patient.id", id)).list();
        return Lists.newArrayList(Lists.transform(result, RecordInvest.CREATE_FUNCTION));

    }

    @Transactional(readOnly=true)
    public List<RecordInvest> getRecordInvests(DateTime from, DateTime to, WKInvest wKInvest) {
        List<RecordInvestDO> result = getSession().createCriteria(RecordInvestDO.class)
                .createAlias("invest", "invest")
                .add(Restrictions.ge("datetime", from))
                .add(Restrictions.le("datetime", to))
                .add(Restrictions.eq("patient.id", id))
                .add(Restrictions.eq("invest.wkinvest", wKInvest))
                .addOrder(Order.asc("datetime"))
                .list();
        return Lists.newArrayList(Lists.transform(result, RecordInvest.CREATE_FUNCTION));

    }

    @Transactional(readOnly=true)
    public List<RecordFood> getRecordFoods(DateTime from, DateTime to) {
        List<RecordFoodDO> result = getSession().createCriteria(RecordFoodDO.class)
                .add(Restrictions.ge("datetime", from))
                .add(Restrictions.le("datetime", to))
                .add(Restrictions.eq("patient.id", id)).list();
        return Lists.newArrayList(Lists.transform(result, RecordFood.CREATE_FUNCTION));
    }

    @Transactional(readOnly=true)
    public List<RecordActivity> getRecordActivities(DateTime from, DateTime to) {
        List<RecordActivityDO> result = getSession().createCriteria(RecordActivityDO.class)
                .add(Restrictions.ge("datetime", from))
                .add(Restrictions.le("datetime", to))
                .add(Restrictions.eq("patient.id", id)).list();
        return Lists.newArrayList(Lists.transform(result, RecordActivity.CREATE_FUNCTION));
    }

    @Transactional(readOnly=true)
    public List<RecordInsulin> getRecordInsulins(DateTime from, DateTime to) {
        List<RecordInsulinDO> result = getSession().createCriteria(RecordInsulinDO.class)
                .add(Restrictions.ge("datetime", from))
                .add(Restrictions.le("datetime", to))
                .add(Restrictions.eq("patient.id", id)).list();
        return Lists.newArrayList(Lists.transform(result, RecordInsulin.CREATE_FUNCTION));
    }

    @Transactional
    public void update(String name, String surname, boolean male, LocalDate born, LocalDate sufferFrom, boolean pumpUsage, String email, String phone, String adress, Insulin basalInsulin, Insulin bolusInsulin, Double perSensitivity, Double hepSensitivity, Double filtrationRate, Double renalThreshold) {
        PatientDO pat = (PatientDO) getSession().load(PatientDO.class, id);
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
    }

    @Transactional
    public void delete() {
        getSession().delete(getSession().load(PatientDO.class, id));
    }

    @Transactional
    public RecordInvest addRecordInvest(DateTime datetime, Double value, Investigation invest, InvSeason season, String notice) {
        RecordInvestDO investDO = new RecordInvestDO();
        investDO.setDate(datetime);
        investDO.setInvest((InvestigationDO) getSession().load(InvestigationDO.class, invest.getId()));
        investDO.setNotice(notice);
        investDO.setPatient((PatientDO) getSession().load(PatientDO.class, id));
        investDO.setSeason(season);
        investDO.setValue(value);
        getSession().save(investDO);
        return new RecordInvest(investDO);
    }

    @Transactional
    public RecordFood addRecordFood(DateTime datetime, Food food, Double totalAmount, Double amount, FoodUnit unit, FoodSeason season, String notice) {
        RecordFoodDO rec = new RecordFoodDO();
        rec.setDate(datetime);
        rec.setFood((FoodDO) getSession().load(FoodDO.class, food.getId()));
        rec.setUnit((FoodUnitDO) getSession().load(FoodUnitDO.class, unit.getId()));
        rec.setNotice(notice);
        rec.setPatient((PatientDO) getSession().load(PatientDO.class, id));
        rec.setSeason(season);
        rec.setAmount(amount);
        rec.setTotalAmount(totalAmount);
        getSession().save(rec);
        return new RecordFood(rec);
    }

    @Transactional
    public RecordActivity addRecordActivity(DateTime datetime, Activity activity, Integer duration, String notice) {
        RecordActivityDO rec = new RecordActivityDO();
        rec.setDate(datetime);
        rec.setActivity((ActivityDO) getSession().load(ActivityDO.class, activity.getId()));
        rec.setNotice(notice);
        rec.setPatient((PatientDO) getSession().load(PatientDO.class, id));
        rec.setDuration(duration);
        getSession().save(rec);
        return new RecordActivity(rec);
    }

    @Transactional
    public RecordInsulin addRecordInsulin(DateTime datetime, boolean basal, Insulin insulin, Double amount, InsulinSeason season, String notice) {
        RecordInsulinDO rec = new RecordInsulinDO();
        rec.setDate(datetime);
        rec.setInsulin((InsulinDO) getSession().load(InsulinDO.class, insulin.getId()));
        rec.setNotice(notice);
        rec.setPatient((PatientDO) getSession().load(PatientDO.class, id));
        rec.setBasal(basal);
        rec.setAmount(amount);
        rec.setSeason(season);
        rec.setPump(pumpUsage);
        getSession().save(rec);
        return new RecordInsulin(rec);
    }

    @Transactional
    public RecordInsulin addRecordInsulin(DateTime datetime, boolean basal, Double amount, InsulinSeason season, String notice) {
        return addRecordInsulin(datetime, basal, basal ? basalInsulin : bolusInsulin, amount, season, notice);
    }

    @Transactional(readOnly=true)
    public List<RecordFood> getRecordFoods(DateTime date) {
        List<RecordFoodDO> result = getSession().createCriteria(RecordFoodDO.class)
                .add(Restrictions.eq("datetime", date))
                .add(Restrictions.eq("patient.id", id)).list();
        return Lists.newArrayList(Lists.transform(result, RecordFood.CREATE_FUNCTION));
    }

    @Transactional(readOnly=true)
    public RecordFood getRecordFood(DateTime date, Food food) {
        RecordFoodDO res = (RecordFoodDO) getSession().createCriteria(RecordFoodDO.class)
                .add(Restrictions.eq("datetime", date))
                .add(Restrictions.eq("food.id", food.getId()))
                .add(Restrictions.eq("patient.id", id)).uniqueResult();
        return res == null ? null : new RecordFood(res);
    }

    @Transactional(readOnly=true)
    public RecordInsulin getRecordInsulin(DateTime date, Insulin ins) {
        RecordInsulinDO res = (RecordInsulinDO) getSession().createCriteria(RecordInsulinDO.class)
                .add(Restrictions.eq("datetime", date))
                .add(Restrictions.eq("insulin.id", ins.getId()))
                .add(Restrictions.eq("patient.id", id)).uniqueResult();
        return res == null ? null : new RecordInsulin(res);
    }

    @Transactional(readOnly=true)
    public RecordInvest getRecordInvest(DateTime date, Investigation inv) {
        RecordInvestDO res = (RecordInvestDO) getSession().createCriteria(RecordInvestDO.class)
                .add(Restrictions.eq("datetime", date))
                .add(Restrictions.eq("invest.id", inv.getId()))
                .add(Restrictions.eq("patient.id", id)).uniqueResult();
        return res == null ? null : new RecordInvest(res);
    }

    @Transactional(readOnly=true)
    public RecordActivity getRecordActivity(DateTime date, Activity act) {
        RecordActivityDO res = (RecordActivityDO) getSession().createCriteria(RecordActivityDO.class)
                .add(Restrictions.eq("datetime", date))
                .add(Restrictions.eq("activity.id", act.getId()))
                .add(Restrictions.eq("patient.id", id)).uniqueResult();
        return res == null ? null : new RecordActivity(res);
    }

    public static Function<PatientDO, Patient> CREATE_FUNCTION = new Function<PatientDO, Patient>() {

        @Override
        public Patient apply(PatientDO activityDO) {
            return new Patient(activityDO);
        }
    };

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public boolean isMale() {
        return male;
    }

    public LocalDate getBorn() {
        return born;
    }

    public LocalDate getSufferFrom() {
        return sufferFrom;
    }

    public boolean isPumpUsage() {
        return pumpUsage;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAdress() {
        return adress;
    }

    public Insulin getBasalInsulin() {
        return basalInsulin;
    }

    public Insulin getBolusInsulin() {
        return bolusInsulin;
    }

    public Double getPerSensitivity() {
        return perSensitivity;
    }

    public Double getHepSensitivity() {
        return hepSensitivity;
    }

    public Double getFiltrationRate() {
        return filtrationRate;
    }

    public Double getRenalThreshold() {
        return renalThreshold;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%s)", name, surname, born.toString(DateTimeFormat.forStyle("M-")));
    }

}
