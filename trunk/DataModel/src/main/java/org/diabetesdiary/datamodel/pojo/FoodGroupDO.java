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
@Table(name = "food_group")
public class FoodGroupDO extends AbstractDO implements Comparable<FoodGroupDO> {

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private FoodGroupDO parent;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "foodGroup")
    private Set<FoodDO> foods;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    private Set<FoodGroupDO> groups;

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(FoodGroupDO o) {
        Collator myCollator = Collator.getInstance();
        return myCollator.compare(name, o.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodGroupDO getParent() {
        return parent;
    }

    public void setParent(FoodGroupDO parent) {
        this.parent = parent;
    }

    public Set<FoodDO> getFoods() {
        return foods;
    }

    public void setFoods(Set<FoodDO> foods) {
        this.foods = foods;
    }

    public Set<FoodGroupDO> getGroups() {
        return groups;
    }

    public void setGroups(Set<FoodGroupDO> groups) {
        this.groups = groups;
    }
    
}
