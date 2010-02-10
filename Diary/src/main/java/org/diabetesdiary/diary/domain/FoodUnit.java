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
import org.diabetesdiary.diary.service.db.FoodUnitDO;

/**
 *
 * @author Jirka Majer
 */
public class FoodUnit extends AbstractDomainObject {

    private final Food food;
    private final String unit;
    private final Double koef;//gram * koef = unit in gram
    private final String name;
    private final String shortcut;

    public FoodUnit(FoodUnitDO unitDO) {
        super(unitDO.getId());
        this.food = new Food(unitDO.getFood());
        this.name = unitDO.getName();
        this.unit = unitDO.getUnit();
        this.shortcut = unitDO.getShortcut();
        this.koef = unitDO.getKoef();
    }
    public static Function<FoodUnitDO, FoodUnit> CREATE_FUNCTION = new Function<FoodUnitDO, FoodUnit>() {
        @Override
        public FoodUnit apply(FoodUnitDO activityDO) {
            return new FoodUnit(activityDO);
        }
    };

    public Food getFood() {
        return food;
    }

    public String getUnit() {
        return unit;
    }

    public Double getKoef() {
        return koef;
    }

    public String getName() {
        return name;
    }

    public String getShortcut() {
        return shortcut;
    }

    @Override
    public String toString() {
        return name;
    }

}
