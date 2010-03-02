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

import org.diabetesdiary.diary.service.db.PatientDO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jirka Majer
 */
@Configurable
public abstract class AbstractRecord extends AbstractDomainObject {

    protected final DateTime datetime;
    protected final String notice;
    protected final Long patientId;
    private Patient patient;

    public AbstractRecord(Long id, PatientDO patientDO, DateTime dateTime, String notice) {
        super(id);
        this.datetime = dateTime;
        this.notice = notice;
        this.patientId = patientDO.getId();
    }

    protected abstract Class getPersistentClass();

    @Transactional
    public void delete() {
        getSession().delete(getSession().load(getPersistentClass(), id));
    }

    @Transactional(readOnly=true)
    public boolean isStillPersistent() {
        return getSession().get(getPersistentClass(), id) != null;
    }

    @Transactional(readOnly=true)
    protected Patient getPatient() {
        if (patient == null) {
            patient = new Patient((PatientDO) getSession().get(PatientDO.class, patientId));
        }
        return patient;
    }

    public DateTime getDatetime() {
        return datetime;
    }

    public String getNotice() {
        return notice;
    }
}
