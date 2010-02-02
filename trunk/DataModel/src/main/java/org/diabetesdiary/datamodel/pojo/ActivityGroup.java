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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.BatchSize;

/**
 * @author Jiri Majer
 */
@Entity
@Table(name="activity_group")
public class ActivityGroup extends AbstractDO implements Comparable<ActivityGroup> {

    @Column(nullable=false)
    private String name;

    @Column
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activityGroup")
    @BatchSize(size = BATCH_SIZE)
    private Set<Activity> activities;

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(ActivityGroup o) {
        Collator myCollator = Collator.getInstance();
        return myCollator.compare(name, o.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }
}
