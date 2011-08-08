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
package org.diabetesdiary.diary.service.db;

import org.diabetesdiary.diary.domain.FoodSeason;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @author Jiri Majer
 */
@Entity
@BatchSize(size = AbstractDO.BATCH_SIZE)
@Table(name = "record_food",
    uniqueConstraints = @UniqueConstraint(columnNames={"patient_id", "datetime", "food_id"})
)
public class RecordFoodDO extends AbstractDO {

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    private PatientDO patient;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(nullable = false)
    private DateTime datetime;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    private FoodDO food;

    @Column(nullable=false)
    private Double totalAmount;

    @Column(nullable=false)
    private Double amount;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    private FoodUnitDO unit;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private FoodSeason season;

    @Column
    private String notice;

    public PatientDO getPatient() {
        return patient;
    }

    public void setPatient(PatientDO patient) {
        this.patient = patient;
    }

    public DateTime getDate() {
        return datetime;
    }

    public void setDate(DateTime date) {
        this.datetime = date;
    }

    public FoodDO getFood() {
        return food;
    }

    public void setFood(FoodDO food) {
        this.food = food;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public FoodUnitDO getUnit() {
        return unit;
    }

    public void setUnit(FoodUnitDO unit) {
        this.unit = unit;
    }

    public FoodSeason getSeason() {
        return season;
    }

    public void setSeason(FoodSeason season) {
        this.season = season;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

}
