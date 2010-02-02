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
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.composite-key class="RecordInvestPK"
 * @author Jiri Majer
 */
public class RecordInvestPK implements Serializable{
    private Integer idInvest;
    private String idPatient;
    private Date date;
    
    /** Creates a new instance of RecordInsulinPK */
    public RecordInvestPK() {
    }

    public RecordInvestPK(Integer idInvest, String idPatient, Date date) {
        this.idInvest = idInvest;
        this.idPatient = idPatient;
        this.date = date;
    }

    /**
     * @hibernate.key-property
     * @return Integer
     */
    public Integer getIdInvest() {
        return idInvest;
    }
    
    public void setIdInvest(Integer idInvest) {
        this.idInvest = idInvest;
    }
    
    /**
     * @hibernate.key-property
     * @return String
     */
    public String getIdPatient() {
        return idPatient;
    }
    
    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }
    
    /**
     * @hibernate.key-property
     * @return Date
     */
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof RecordInvestPK == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        RecordInvestPK rhs = (RecordInvestPK) obj;
        return new EqualsBuilder()
        .append(getIdInvest(), rhs.getIdInvest())
        .append(idPatient, rhs.idPatient)
        .append(date,rhs.date)
        .isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder(81, 101).
                append(getIdInvest()).
                append(idPatient).
                append(date).
                toHashCode();
    }
    
}
