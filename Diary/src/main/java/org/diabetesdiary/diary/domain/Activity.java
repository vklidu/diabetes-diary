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
import java.text.Collator;
import org.diabetesdiary.diary.service.db.ActivityDO;

/**
 *
 * @author Jirka Majer
 */
public class Activity extends AbstractDomainObject implements Comparable<Activity> {

    private final ActivityGroup activityGroup;// kJ / Min / Kg
    private final String name;
    private final Double power;

    public Activity(ActivityDO activityDO) {
        super(activityDO.getId());
        this.activityGroup = new ActivityGroup(activityDO.getActivityGroup());
        this.name = activityDO.getName();
        this.power = activityDO.getPower();
    }
    public static final Function<ActivityDO, Activity> CREATE_FUNCTION = new Function<ActivityDO, Activity>() {

        @Override
        public Activity apply(ActivityDO activityDO) {
            return new Activity(activityDO);
        }
    };

    public ActivityGroup getActivityGroup() {
        return activityGroup;
    }

    public String getName() {
        return name;
    }

    public Double getPower() {
        return power;
    }

    @Override
    public int compareTo(Activity o) {
        Collator myCollator = Collator.getInstance();
        return myCollator.compare(name, o.getName());
    }

    @Override
    public String toString() {
        return name;
    }
}
