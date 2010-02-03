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

import java.text.Collator;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.BatchSize;

/**
 * @author Jiri Majer
 */
@Entity
@BatchSize(size = AbstractDO.BATCH_SIZE)
@Table(name = "food")
public class FoodDO extends AbstractDO implements Comparable<FoodDO> {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double energy;

    @Column(nullable = false)
    private Double protein;

    @Column(nullable = false)
    private Double fat;

    @Column(nullable = false)
    private Double sugar;

    @Column
    private Double cholesterol;

    @Column
    private Double roughage;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="food")
    private Set<FoodUnitDO> units;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private FoodGroupDO foodGroup;

    @Override
    public int compareTo(FoodDO o) {
        Collator myCollator = Collator.getInstance();
        return myCollator.compare(name, o.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getEnergy() {
        return energy;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getSugar() {
        return sugar;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
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

    public Set<FoodUnitDO> getUnits() {
        return units;
    }

    public void setUnits(Set<FoodUnitDO> units) {
        this.units = units;
    }

    public FoodGroupDO getFoodGroup() {
        return foodGroup;
    }

    public void setFoodGroup(FoodGroupDO foodGroup) {
        this.foodGroup = foodGroup;
    }
    
}
