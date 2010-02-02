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
package org.diabetesdiary.datamodel.pojo;

import java.io.Serializable;

/**
 * @hibernate.class table="record_food"
 * @author Jiri Majer
 */
public class RecordFood implements Serializable {

    private RecordFoodPK id;
    private Double totalAmount;
    private Double amount;
    private String unit;
    private String season;
    private Food food;
    private String notice;

    /** Creates a new instance of RecordFood */
    public RecordFood() {
    }

    /**
     * @hibernate.composite-id
     * @return RecordFoodPK
     */
    public RecordFoodPK getId() {
        return id;
    }

    public void setId(RecordFoodPK id) {
        this.id = id;
    }

    /**
     * @hibernate.property
     * @return Double
     */
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * @hibernate.property
     * @return String
     */
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RecordFood == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        RecordFood rhs = (RecordFood) obj;
        return getId().equals(rhs.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * @hibernate.many-to-one not-null="true" insert="false" update="false" column="idFood"
     * @return Food
     */
    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    /**
     * @hibernate.property
     * @return Double
     */
    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
