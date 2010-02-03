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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @author Jiri Majer
 */
@Entity
@BatchSize(size = AbstractDO.BATCH_SIZE)
@Table(name = "record_activity",
    uniqueConstraints = @UniqueConstraint(columnNames={"patient_id", "datetime", "activity_id"})
)
public class RecordActivityDO extends AbstractDO {

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    private PatientDO patient;

    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(nullable = false)
    private DateTime datetime;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    private ActivityDO activity;

    @Column(nullable=false)
    private Integer duration;//minutes

    @Column
    private String notice;
    
    public PatientDO getPatient() {
        return patient;
    }

    public void setPatient(PatientDO patient) {
        this.patient = patient;
    }

    public DateTime getDate() {
        return datetime;
    }

    public void setDate(DateTime date) {
        this.datetime = date;
    }

    public ActivityDO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDO activity) {
        this.activity = activity;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

}
