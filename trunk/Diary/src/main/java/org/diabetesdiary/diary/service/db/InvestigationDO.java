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
package org.diabetesdiary.diary.service.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.BatchSize;

/**
 * @author Jiri Majer
 */
@Entity
@BatchSize(size = AbstractDO.BATCH_SIZE)
@Table(name = "investigation")
public class InvestigationDO extends AbstractDO {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private InvestigationGroupDO group;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String unit;

    @Column
    private Double normalMin;
    
    @Column
    private Double normalMax;

    @Override
    public String toString() {
        return name;
    }

    public InvestigationGroupDO getGroup() {
        return group;
    }

    public void setGroup(InvestigationGroupDO group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getNormalMin() {
        return normalMin;
    }

    public void setNormalMin(Double normalMin) {
        this.normalMin = normalMin;
    }

    public Double getNormalMax() {
        return normalMax;
    }

    public void setNormalMax(Double normalMax) {
        this.normalMax = normalMax;
    }

    public enum Instances {

        MENZES, ACETON, WEIGHT, SUGAR, GLYCEMIE, TALL;

        public int getID() {
            switch (this) {
                case GLYCEMIE:
                    return 1;
                case WEIGHT:
                    return 2;
                case TALL:
                    return 3;
                case MENZES:
                    return 4;
                case SUGAR:
                    return 5;
                case ACETON:
                    return 6;
                default:
                    throw new IllegalArgumentException();
            }
        }

        public static Instances getInvestInstanceByID(int id) {
            switch (id) {
                case 1:
                    return InvestigationDO.Instances.GLYCEMIE;
                case 2:
                    return InvestigationDO.Instances.WEIGHT;
                case 3:
                    return InvestigationDO.Instances.TALL;
                case 4:
                    return InvestigationDO.Instances.MENZES;
                case 5:
                    return InvestigationDO.Instances.SUGAR;
                case 6:
                    return InvestigationDO.Instances.ACETON;
                default:
                    throw new ArrayIndexOutOfBoundsException();
            }
        }
    }

}
