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
import java.text.Collator;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="food_group"
 * @author Jiri Majer
 */
public class FoodGroup implements Serializable, Comparable<FoodGroup>{
    private Integer id;
    private String name;
    private FoodGroup parent;
    private Set foods;
    private Set groups;
    
    
    /** Creates a new instance of FoodGroup */
    public FoodGroup() {
    }
    
    /**
     * @hibernate.id generator-class="native"
     * @return Integer
     */
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * @hibernate.property
     * @return String
     */
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof FoodGroup == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        FoodGroup rhs = (FoodGroup) obj;
        return new EqualsBuilder().append(getId(), rhs.getId()).isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder(17, 41).append(getId()).toHashCode();
    }
    
    /**
     * @hibernate.list table="food" cascade="all" lazy="false" inverse="true"
     * @hibernate.key column="id_food_group"
     * @hibernate.list-index
     * @hibernate.one-to-many class="org.diabetesdiary.pojo.Food"
     * @return java.util.List
     */
    public Set getFoods() {
        return foods;
    }
    
    public void setFoods(Set foods) {
        this.foods = foods;
    }
    
    /**
     * @hibernate.many-to-one not-null="false" insert="true" update="true" column="idParent"
     * @return FoodGroup
     */
    public FoodGroup getParent() {
        return parent;
    }
    
    public void setParent(FoodGroup parent) {
        this.parent = parent;
    }
    
    /**
     * @hibernate.list table="food_group" cascade="all" lazy="false"
     * @hibernate.key column="idParent"
     * @hibernate.list-index
     * @hibernate.one-to-many class="org.diabetesdiary.pojo.FoodGroup"
     * @return java.util.List
     */
    public Set getGroups() {
        return groups;
    }
    
    public void setGroups(Set groups) {
        this.groups = groups;
    }
    
    public String toString() {
        return name;
    }
    
    public int compareTo(FoodGroup o) {
        Collator myCollator = Collator.getInstance();
        return myCollator.compare(name,o.getName());
    }
    
    
}
