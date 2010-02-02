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
 * @hibernate.class table="record_investigation"
 * @author Jiri Majer
 */
public class RecordInvest implements Serializable {

    private RecordInvestPK id;
    private Double value;
    private Investigation invest;
    private String season;
    private String notice;

    /** Creates a new instance of RecordInvest */
    public RecordInvest() {
    }

    /**
     * @hibernate.composite-id
     * @return RecordInvestPK
     */
    public RecordInvestPK getId() {
        return id;
    }

    public void setId(RecordInvestPK id) {
        this.id = id;
    }

    /**
     * @hibernate.property
     * @return Double
     */
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RecordInvest == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        RecordInvest rhs = (RecordInvest) obj;
        return id.equals(rhs.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Investigation getInvest() {
        return invest;
    }

    public void setInvest(Investigation invest) {
        this.invest = invest;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
