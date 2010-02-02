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
 * @hibernate.class table="record_insulin"
 * @author Jiri Majer
 */
public class RecordInsulin implements Serializable {

    private RecordInsulinPK id;
    private Double amount;
    private String season;
    private Insulin insulin;
    private boolean pump;
    private String notice;

    /** Creates a new instance of RecordInsulin */
    public RecordInsulin() {
    }

    /**
     * @hibernate.composite-id
     * @return RecordInsulinPK
     */
    public RecordInsulinPK getId() {
        return id;
    }

    public void setId(RecordInsulinPK id) {
        this.id = id;
    }

    /**
     * @hibernate.property
     * @return Double Amount of insulin in 'units'.
     */
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RecordInsulin == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        RecordInsulin rhs = (RecordInsulin) obj;
        return id.equals(rhs.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Insulin getInsulin() {
        return insulin;
    }

    public void setInsulin(Insulin insulin) {
        this.insulin = insulin;
    }

    public boolean isBasal() {
        return id.isBasal();
    }

    public boolean isPump() {
        return pump;
    }

    public void setPump(boolean pump) {
        this.pump = pump;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
