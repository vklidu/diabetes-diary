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
import org.diabetesdiary.diary.service.db.FoodGroupDO;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jirka Majer
 */
@Configurable
public class FoodGroup extends AbstractDomainObject implements Comparable<FoodGroup> {

    private final String name;
    private final FoodGroup parent;

    public FoodGroup(FoodGroupDO groupDO) {
        super(groupDO.getId());
        this.name = groupDO.getName();
        this.parent = groupDO.getParent() != null ? new FoodGroup(groupDO.getParent()) : null;
    }

    @Transactional(readOnly=true)
    public List<FoodGroup> getFoodGroups() {
        return Lists.newArrayList(Lists.transform(getSession()
                .createCriteria(FoodGroupDO.class)
                .add(Restrictions.eq("parent.id", id))
                .list(), FoodGroup.CREATE_FUNCTION));
    }

    @Transactional(readOnly=true)
    public List<Food> getFoods() {
        return Lists.newArrayList(Lists.transform(getSession()
                .createCriteria(FoodDO.class)
                .add(Restrictions.eq("foodGroup.id", id))
                .list(), Food.CREATE_FUNCTION));
    }

    public static Function<FoodGroupDO, FoodGroup> CREATE_FUNCTION = new Function<FoodGroupDO, FoodGroup>() {
        @Override
        public FoodGroup apply(FoodGroupDO activityDO) {
            return new FoodGroup(activityDO);
        }
    };

    public String getName() {
        return name;
    }

    public FoodGroup getParent() {
        return parent;
    }

    @Override
    public int compareTo(FoodGroup o) {
        Collator myCollator = Collator.getInstance();
        return myCollator.compare(name, o.getName());
    }

    @Override
    public String toString() {
        return name;
    }

}
