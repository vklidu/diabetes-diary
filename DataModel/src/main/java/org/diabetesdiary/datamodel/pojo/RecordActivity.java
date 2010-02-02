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

/**
 * @hibernate.class table="record_activity"
 * @author Jiri Majer
 */
public class RecordActivity implements Serializable {

    private RecordActivityPK id;
    private Activity activity;
    private Integer duration;//minutes
    private String notice;
    
    //this atribute is not mapped into database, is used only in cell renderer
    private Double weight;

    /** Creates a new instance of RecordActivity */
    public RecordActivity() {
    }

    /**
     * @hibernate.composite-id
     * @return RecordActivityPK
     */
    public RecordActivityPK getId() {
        return id;
    }

    public void setId(RecordActivityPK id) {
        this.id = id;
    }

    /**
     * @hibernate.property
     * @return Integer Time in minutes.
     */
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RecordActivity == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        RecordActivity rhs = (RecordActivity) obj;
        return id.equals(rhs.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
