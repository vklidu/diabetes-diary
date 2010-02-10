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
import org.diabetesdiary.diary.api.DiaryRepository;
import org.diabetesdiary.diary.service.db.InvestigationDO;
import org.diabetesdiary.diary.service.db.RecordInvestDO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jirka Majer
 */
@Configurable
public class RecordInvest extends AbstractDomainObject {

    @Autowired
    private transient DiaryRepository repository;
    
    private Patient patient;
    private final Long idPatient;
    private final DateTime datetime;
    private final Double value;
    private final Investigation invest;
    private final InvSeason season;
    private final String notice;

    public RecordInvest(RecordInvestDO rec) {
        super(rec.getId());
        this.idPatient = rec.getPatient().getId();
        this.datetime = rec.getDate();
        this.value = rec.getValue();
        this.invest = new Investigation(rec.getInvest());
        this.season = rec.getSeason();
        this.notice = rec.getNotice();
    }

    @Transactional
    public void delete() {
        getSession().delete(getSession().load(RecordInvestDO.class, id));
    }

    @Transactional
    public RecordInvest update(DateTime datetime, Double value, Investigation invest, InvSeason season, String notice) {
        RecordInvestDO investDO = (RecordInvestDO) getSession().load(RecordInvestDO.class, id);
        investDO.setDate(datetime);
        investDO.setInvest((InvestigationDO) getSession().load(InvestigationDO.class, invest.getId()));
        investDO.setNotice(notice);
        investDO.setSeason(season);
        investDO.setValue(value);
        return new RecordInvest(investDO);
    }

    public RecordInvest update(Double value) {
        return update(datetime, value, invest, season, notice);
    }

    public static Function<RecordInvestDO, RecordInvest> CREATE_FUNCTION = new Function<RecordInvestDO, RecordInvest>() {
        @Override
        public RecordInvest apply(RecordInvestDO activityDO) {
            return new RecordInvest(activityDO);
        }
    };

    public Patient getPatient() {
        if (patient == null) {
            patient = repository.getPatient(idPatient);
        }
        return patient;
    }

    public DateTime getDatetime() {
        return datetime;
    }

    public Double getValue() {
        return value;
    }

    public Investigation getInvest() {
        return invest;
    }

    public InvSeason getSeason() {
        return season;
    }

    public String getNotice() {
        return notice;
    }

}
