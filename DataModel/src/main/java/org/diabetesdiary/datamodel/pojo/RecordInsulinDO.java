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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "record_insulin",
    uniqueConstraints = @UniqueConstraint(columnNames={"patient_id", "datetime", "basal", "insulin_id"})
)
public class RecordInsulinDO extends AbstractDO {

    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(nullable = false)
    private DateTime datetime;

    @Column(nullable=false)
    private Boolean basal;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    private InsulinDO insulin;
    
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    private PatientDO patient;

    @Column(nullable=false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private InsulinSeason season;

    @Column(nullable=false)
    private Boolean pump;

    @Column
    private String notice;

    public DateTime getDate() {
        return datetime;
    }

    public void setDate(DateTime date) {
        this.datetime = date;
    }

    public Boolean getBasal() {
        return basal;
    }

    public void setBasal(Boolean basal) {
        this.basal = basal;
    }

    public PatientDO getPatient() {
        return patient;
    }

    public void setPatient(PatientDO patient) {
        this.patient = patient;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public InsulinSeason getSeason() {
        return season;
    }

    public void setSeason(InsulinSeason season) {
        this.season = season;
    }

    public InsulinDO getInsulin() {
        return insulin;
    }

    public void setInsulin(InsulinDO insulin) {
        this.insulin = insulin;
    }

    public Boolean getPump() {
        return pump;
    }

    public void setPump(Boolean pump) {
        this.pump = pump;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

}
