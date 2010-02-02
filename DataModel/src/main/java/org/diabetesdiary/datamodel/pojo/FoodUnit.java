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
 * @hibernate.class table="food_unit"
 * @author Jiri Majer
 */
public class FoodUnit implements Serializable{
    private FoodUnitPK id;
    private Double koef;
    private String name;
    private String shortcut;
    
    
    /** Creates a new instance of FoodUnit */
    public FoodUnit() {
    }
    
    /**
     * @hibernate.property type="double" column="koef"
     * @return Double
     */
    public Double getKoef() {
        return koef;
    }
    
    public void setKoef(Double koef) {
        this.koef = koef;
    }
    
    /**
     * @hibernate.composite-id
     * @return FoodUnitPK
     */
    public FoodUnitPK getId() {
        return id;
    }
    
    public void setId(FoodUnitPK id) {
        this.id = id;
    }
    
    
    public boolean equals(Object obj) {
        if (obj instanceof FoodUnit == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        FoodUnit rhs = (FoodUnit) obj;
        return new EqualsBuilder().append(getId(), rhs.getId()).isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder(7, 27).append(getId()).toHashCode();
    }

    public String toString() {
       return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }
    
    
}
