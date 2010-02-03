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
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

/**
 * @author Jiri Majer
 */
@Entity
@BatchSize(size = AbstractDO.BATCH_SIZE)
@Table(name="patient")
public class PatientDO extends AbstractDO {

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String surname;

    @Column(nullable=false)
    private boolean male;

    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
    @Column(nullable=false)
    private LocalDate born;

    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
    @Column
    private LocalDate sufferFrom;

    @Column(nullable=false)
    private boolean pumpUsage;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String adress;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private InsulinDO basalInsulin;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private InsulinDO bolusInsulin;

    @Column(nullable=false)
    private Double perSensitivity;

    @Column(nullable=false)
    private Double hepSensitivity;

    @Column(nullable=false)
    private Double filtrationRate;

    @Column(nullable=false)
    private Double renalThreshold;

    @Override
    public String toString() {
        return String.format("%s %s (%s)", name, surname, born.toString("dd.MM.yyyy"));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public LocalDate getBorn() {
        return born;
    }

    public void setBorn(LocalDate born) {
        this.born = born;
    }

    public LocalDate getSufferFrom() {
        return sufferFrom;
    }

    public void setSufferFrom(LocalDate sufferFrom) {
        this.sufferFrom = sufferFrom;
    }

    public boolean isPumpUsage() {
        return pumpUsage;
    }

    public void setPumpUsage(boolean pumpUsage) {
        this.pumpUsage = pumpUsage;
    }

    public InsulinDO getBasalInsulin() {
        return basalInsulin;
    }

    public void setBasalInsulin(InsulinDO basalInsulin) {
        this.basalInsulin = basalInsulin;
    }

    public InsulinDO getBolusInsulin() {
        return bolusInsulin;
    }

    public void setBolusInsulin(InsulinDO bolusInsulin) {
        this.bolusInsulin = bolusInsulin;
    }

    public Double getPerSensitivity() {
        return perSensitivity;
    }

    public void setPerSensitivity(Double perSensitivity) {
        this.perSensitivity = perSensitivity;
    }

    public Double getHepSensitivity() {
        return hepSensitivity;
    }

    public void setHepSensitivity(Double hepSensitivity) {
        this.hepSensitivity = hepSensitivity;
    }

    public Double getFiltrationRate() {
        return filtrationRate;
    }

    public void setFiltrationRate(Double filtrationRate) {
        this.filtrationRate = filtrationRate;
    }

    public Double getRenalThreshold() {
        return renalThreshold;
    }

    public void setRenalThreshold(Double renalThreshold) {
        this.renalThreshold = renalThreshold;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

}
