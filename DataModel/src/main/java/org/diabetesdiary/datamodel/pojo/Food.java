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
 * @hibernate.class table="food"
 * @author Jiri Majer
 */
public class Food implements Serializable, Comparable<Food>{
    private Integer idFood;    
    private String name;
    private Double energy;
    private Double protein;
    private Double fat;
    private Double sugar;
    private Double cholesterol;
    private Double roughage;
    private Set units;
    private FoodGroup foodGroup;
    
    
    /** Creates a new instance of Food */
    public Food() {
    }
    
    /**        
     * @hibernate.id generator-class="native"
     * @return Integer
     */
    public Integer getIdFood() {
        return idFood;
    }
    
    public void setIdFood(Integer idFood) {
        this.idFood = idFood;
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
    
    /**
     * @hibernate.property
     * @return Double
     */
    public Double getEnergy() {
        return energy;
    }
    
    public void setEnergy(Double energy) {
        this.energy = energy;
    }
    /**
     * @hibernate.property
     * @return Double
     */
    public Double getProtein() {
        return protein;
    }
    
    public void setProtein(Double protein) {
        this.protein = protein;
    }
    
    /**
     * @hibernate.property
     * @return Double
     */
    public Double getFat() {
        return fat;
    }
    
    public void setFat(Double fat) {
        this.fat = fat;
    }
    
    /**
     * @hibernate.property
     * @return Double
     */
    public Double getSugar() {
        return sugar;
    }
    
    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }
    
    /**
     * @hibernate.set cascade="all" inverse="true" lazy="false"
     * @hibernate.key column="id_food" on-delete="cascade"
     * @hibernate.key column="unit"
     * @hibernate.one-to-many class="org.diabetesdiary.pojo.FoodUnit"
     * @return java.util.Set
     */
    public Set getUnits() {
        return units;
    }

    public void setUnits(Set units) {
        this.units = units;
    }

    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Food == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Food rhs = (Food) obj;
        return new EqualsBuilder().append(getIdFood(), rhs.getIdFood()).isEquals();
    }
    
    @Override
    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(19, 37).append(getIdFood()).toHashCode();
    }

  
    /**
     * @hibernate.many-to-one not-null="true" insert="false" update="false" column="idGroup"
     * @return FoodGroup
     */
    public FoodGroup getFoodGroup() {
        return foodGroup;
    }

    public void setFoodGroup(FoodGroup foodGroup) {
        this.foodGroup = foodGroup;
    }

    @Override
    public String toString() {
       return name;
    }

    public Double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Double getRoughage() {
        return roughage;
    }

    public void setRoughage(Double roughage) {
        this.roughage = roughage;
    }

    public int compareTo(Food o) {
        Collator myCollator = Collator.getInstance();
        return myCollator.compare(name,o.getName());
    }

    
}
