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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.BatchSize;

@Entity
@BatchSize(size = AbstractDO.BATCH_SIZE)
@Table(name = "activity")
public class ActivityDO extends AbstractDO implements Comparable<ActivityDO> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ActivityGroupDO activityGroup;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Double power;// kJ / Min / Kg

    @Override
    public int compareTo(ActivityDO o) {
        Collator myCollator = Collator.getInstance();
        return myCollator.compare(name, o.name);
    }

    public ActivityGroupDO getActivityGroup() {
        return activityGroup;
    }

    public void setActivityGroup(ActivityGroupDO activityGroup) {
        this.activityGroup = activityGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
    }
 
}
