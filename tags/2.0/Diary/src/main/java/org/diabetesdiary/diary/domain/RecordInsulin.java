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
import org.diabetesdiary.diary.service.db.InsulinDO;
import org.diabetesdiary.diary.service.db.RecordInsulinDO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jirka Majer
 */
@Configurable
public class RecordInsulin extends AbstractRecord {

    private final Boolean basal;
    private final Insulin insulin;
    private final Double amount;
    private final InsulinSeason season;
    private final Boolean pump;

    public RecordInsulin(RecordInsulinDO rec) {
        super(rec.getId(), rec.getPatient(), rec.getDate(), rec.getNotice());
        this.basal = rec.getBasal();
        this.insulin = new Insulin(rec.getInsulin());
        this.amount = rec.getAmount();
        this.season = rec.getSeason();
        this.pump = rec.getPump();
    }

    @Transactional
    public RecordInsulin update(DateTime datetime, boolean basal, Insulin insulin, Double amount, InsulinSeason season, String notice) {
        RecordInsulinDO rec = (RecordInsulinDO) getSession().load(RecordInsulinDO.class, id);
        rec.setDate(datetime);
        rec.setInsulin((InsulinDO) getSession().load(InsulinDO.class, insulin.getId()));
        rec.setNotice(notice);
        rec.setBasal(basal);
        rec.setAmount(amount);
        rec.setSeason(season);
        return new RecordInsulin(rec);
    }

    public RecordInsulin update(Double amount) {
        return update(datetime, basal, insulin, amount, season, notice);
    }
    public static Function<RecordInsulinDO, RecordInsulin> CREATE_FUNCTION = new Function<RecordInsulinDO, RecordInsulin>() {

        @Override
        public RecordInsulin apply(RecordInsulinDO activityDO) {
            return new RecordInsulin(activityDO);
        }
    };

    public boolean isBasal() {
        return basal;
    }

    public Insulin getInsulin() {
        return insulin;
    }

    public Double getAmount() {
        return amount;
    }

    public InsulinSeason getSeason() {
        return season;
    }

    public Boolean getPump() {
        return pump;
    }

    @Override
    protected Class getPersistentClass() {
        return RecordInsulinDO.class;
    }
}
