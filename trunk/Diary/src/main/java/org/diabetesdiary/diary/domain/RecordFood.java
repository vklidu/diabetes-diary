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
import org.diabetesdiary.diary.service.db.FoodDO;
import org.diabetesdiary.diary.service.db.FoodUnitDO;
import org.diabetesdiary.diary.service.db.RecordFoodDO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jirka Majer
 */
@Configurable
public class RecordFood extends AbstractDomainObject {

    private final Patient patient;
    private final DateTime datetime;
    private final Food food;
    private final Double totalAmount;
    private final Double amount;
    private final FoodUnit unit;
    private final FoodSeason season;
    private final String notice;


    public RecordFood(RecordFoodDO rec) {
        super(rec.getId());
        this.patient = new Patient(rec.getPatient());
        this.datetime = rec.getDate();
        this.food = new Food(rec.getFood());
        this.totalAmount = rec.getTotalAmount();
        this.amount = rec.getAmount();
        this.unit = new FoodUnit(rec.getUnit());
        this.season = rec.getSeason();
        this.notice = rec.getNotice();
    }

    @Transactional
    public void delete() {
        getSession().delete(getSession().load(RecordFoodDO.class, id));
    }

    @Transactional
    public RecordFood update(DateTime datetime, Food food, Double totalAmount, Double amount, FoodUnit unit, FoodSeason season, String notice) {
        RecordFoodDO rec = (RecordFoodDO) getSession().load(RecordFoodDO.class, id);
        rec.setAmount(amount);
        rec.setDate(datetime);
        rec.setFood((FoodDO) getSession().load(FoodDO.class, food.getId()));
        rec.setNotice(notice);
        rec.setSeason(season);
        rec.setTotalAmount(totalAmount);
        rec.setUnit((FoodUnitDO) getSession().load(FoodUnitDO.class, unit.getId()));
        return new RecordFood(rec);
    }

    @Transactional
    public RecordFood update(Double amount) {
        return update(datetime, food, totalAmount, amount, unit, season, notice);
    }

    public double getEnergyInKJ() {
        return unit.getKoef() * amount * food.getEnergy() / 100;
    }

    public double getSachUnits(FoodUnit sachUnit) {
        return unit.getKoef() * amount * food.getSugar() / (100 * sachUnit.getKoef());
    }

    public static Function<RecordFoodDO, RecordFood> CREATE_FUNCTION = new Function<RecordFoodDO, RecordFood>() {
        @Override
        public RecordFood apply(RecordFoodDO activityDO) {
            return new RecordFood(activityDO);
        }
    };

    public Patient getPatient() {
        return patient;
    }

    public DateTime getDatetime() {
        return datetime;
    }

    public Food getFood() {
        return food;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public Double getAmount() {
        return amount;
    }

    public FoodUnit getUnit() {
        return unit;
    }

    public FoodSeason getSeason() {
        return season;
    }

    public String getNotice() {
        return notice;
    }

}
