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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/** 
 * 
 * @author Jiri Majer
 */
public class FoodUnitPK implements Serializable{
    private Integer idFood;
    private String unit;
    
    
    /** Creates a new instance of FoodUnitPK */
    public FoodUnitPK() {
    }
    
    public FoodUnitPK(Integer idFood, String unit) {
        this.idFood = idFood;
        this.unit = unit;
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
    
    /**
     * @hibernate.key-property
     * @return String
     */
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof FoodUnitPK == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        FoodUnitPK rhs = (FoodUnitPK) obj;
        return new EqualsBuilder().append(getIdFood(), rhs.getIdFood())
                .append(getUnit(),rhs.getUnit())
                .isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder(3, 27)
                .append(getIdFood())
                .append(getUnit())
                .toHashCode();
    }
    
}
