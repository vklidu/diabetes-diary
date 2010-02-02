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
 * @hibernate.composite-key class="RecordFoodPK"
 * @author Jiri Majer
 */
public class RecordFoodPK implements Serializable{
    private Integer idFood;
    private String idPatient;
    private Date date;
    
    /** Creates a new instance of RecordFoodPK */
    public RecordFoodPK() {
    }
    
    public RecordFoodPK(Integer idFood, String idPatient, Date date) {
        this.setIdFood(idFood);
        this.setIdPatient(idPatient);
        this.setDate(date);
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
    /**
     * @hibernate.key-property
     * @return Integer
     */
    public Integer getIdFood() {
        return idFood;
    }
    
    public void setIdFood(Integer idFood) {
        this.idFood = idFood;
    }
    
    
    public boolean equals(Object obj) {
        if (obj instanceof RecordFoodPK == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        RecordFoodPK rhs = (RecordFoodPK) obj;
        return new EqualsBuilder()
        .append(getIdFood(), rhs.getIdFood())
        .append(idPatient, rhs.idPatient)
        .append(date,rhs.date)
        .isEquals();
    }
    
    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(45, 61).
                append(getIdFood()).
                append(idPatient).
                append(date).
                toHashCode();
    }
    
}
