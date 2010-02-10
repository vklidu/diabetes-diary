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
import org.diabetesdiary.diary.api.UnknownWeightException;
import org.diabetesdiary.diary.service.db.ActivityDO;
import org.diabetesdiary.diary.service.db.RecordActivityDO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jirka Majer
 */
@Configurable
public class RecordActivity extends AbstractDomainObject {

    private final Patient patient;
    private final DateTime datetime;
    private final Activity activity;
    private final Integer duration;//minutes
    private final String notice;

    public RecordActivity(RecordActivityDO activityDO) {
        super(activityDO.getId());
        this.patient = new Patient(activityDO.getPatient());
        this.datetime = activityDO.getDate();
        this.activity = new Activity(activityDO.getActivity());
        this.duration = activityDO.getDuration();
        this.notice = activityDO.getNotice();
    }
    
    @Transactional
    public void delete() {
        getSession().delete(getSession().load(RecordActivityDO.class, id));
    }

    @Transactional
    public RecordActivity update(DateTime actDate, Activity activity, Integer duration, String actNote) {
        RecordActivityDO rec = (RecordActivityDO) getSession().load(RecordActivityDO.class, id);
        rec.setDate(actDate);
        rec.setActivity((ActivityDO) getSession().load(ActivityDO.class, activity.getId()));
        rec.setNotice(actNote);
        rec.setDuration(duration);
        return new RecordActivity(rec);
    }
    
    @Transactional
    public RecordActivity update(Integer duration) {
        return update(datetime, activity, duration, notice);
    }


    private Double weight;
    public Double getEnergy() throws UnknownWeightException {
        if (weight == null) {
            weight = patient.getWeightBefore(datetime);
        }
        if (weight == null) {
            throw new UnknownWeightException();
        }
        return activity.getPower() * duration * weight;
    }

    public static Function<RecordActivityDO, RecordActivity> CREATE_FUNCTION = new Function<RecordActivityDO, RecordActivity>() {
        @Override
        public RecordActivity apply(RecordActivityDO activityDO) {
            return new RecordActivity(activityDO);
        }
    };

    public Patient getPatient() {
        return patient;
    }

    public DateTime getDatetime() {
        return datetime;
    }

    public Activity getActivity() {
        return activity;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getNotice() {
        return notice;
    }

}
