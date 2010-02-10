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
import org.diabetesdiary.diary.service.db.ActivityDO;
import org.diabetesdiary.diary.service.db.ActivityGroupDO;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jirka Majer
 */
@Configurable
public class ActivityGroup extends AbstractDomainObject implements Comparable<ActivityGroup> {

    private final String name;
    private final String description;

    public ActivityGroup(ActivityGroupDO groupDO) {
        super(groupDO.getId());
        this.name = groupDO.getName();
        this.description = groupDO.getDescription();
    }

    @Transactional(readOnly = true)
    public List<Activity> getActivities() {
        return Lists.newArrayList(Lists.transform(getSession()
                .createCriteria(ActivityDO.class)
                .add(Restrictions.eq("activityGroup.id", id))
                .list(), Activity.CREATE_FUNCTION));

    }
    public static final Function<ActivityGroupDO, ActivityGroup> CREATE_FUNCTION = new Function<ActivityGroupDO, ActivityGroup>() {

        @Override
        public ActivityGroup apply(ActivityGroupDO activityDO) {
            return new ActivityGroup(activityDO);
        }
    };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(ActivityGroup o) {
        Collator myCollator = Collator.getInstance();
        return myCollator.compare(name, o.getName());
    }

    @Override
    public String toString() {
        return name;
    }

}
