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
package org.diabetesdiary.diary.domain;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.text.Collator;
import java.util.List;
import org.diabetesdiary.diary.service.db.FoodDO;
import org.diabetesdiary.diary.service.db.FoodUnitDO;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jirka Majer
 */
@Configurable
public class Food extends AbstractDomainObject implements Comparable<Food> {

    private final String name;
    private final Double energy;
    private final Double protein;
    private final Double fat;
    private final Double sugar;
    private final Double cholesterol;
    private final Double roughage;
    private final FoodGroup foodGroup;
    private final WKFood wkfood;

    public Food(FoodDO foodDO) {
        super(foodDO.getId());
        this.foodGroup = new FoodGroup(foodDO.getFoodGroup());
        this.name = foodDO.getName();
        this.energy = foodDO.getEnergy();
        this.protein = foodDO.getProtein();
        this.fat = foodDO.getFat();
        this.sugar = foodDO.getSugar();
        this.cholesterol = foodDO.getCholesterol();
        this.roughage = foodDO.getRoughage();
        this.wkfood = foodDO.getWkfood();
    }

    public String getName() {
        return name;
    }

    public Double getEnergy() {
        return energy;
    }

    public Double getProtein() {
        return protein;
    }

    public Double getFat() {
        return fat;
    }

    public Double getSugar() {
        return sugar;
    }

    public Double getCholesterol() {
        return cholesterol;
    }

    public Double getRoughage() {
        return roughage;
    }

    public FoodGroup getFoodGroup() {
        return foodGroup;
    }
    public static final Function<FoodDO, Food> CREATE_FUNCTION = new Function<FoodDO, Food>() {

        @Override
        public Food apply(FoodDO activityDO) {
            return new Food(activityDO);
        }
    };

    @Transactional(readOnly = true)
    public List<FoodUnit> getUnits() {
        return Lists.newArrayList(Lists.transform(getSession()
                .createCriteria(FoodUnitDO.class)
                .add(Restrictions.eq("food.id", id))
                .list(), FoodUnit.CREATE_FUNCTION));
    }

    public boolean isSacharidUnit() {
        return wkfood == WKFood.SACCHARIDE;
    }

    @Override
    public int compareTo(Food o) {
        Collator myCollator = Collator.getInstance();
        return myCollator.compare(name, o.getName());
    }

    @Override
    public String toString() {
        return name;
    }
}
