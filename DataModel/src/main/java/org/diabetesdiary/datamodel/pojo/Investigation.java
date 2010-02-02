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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="investigation"
 * @author Jiri Majer
 */
public class Investigation implements Serializable {

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
                    return Investigation.Instances.GLYCEMIE;
                case 2:
                    return Investigation.Instances.WEIGHT;
                case 3:
                    return Investigation.Instances.TALL;
                case 4:
                    return Investigation.Instances.MENZES;
                case 5:
                    return Investigation.Instances.SUGAR;
                case 6:
                    return Investigation.Instances.ACETON;
                default:
                    throw new ArrayIndexOutOfBoundsException();
            }
        }
    }
    private Integer id;
    private InvestigationGroup group;
    private String name;
    private String unit;
    private Double normalMin;
    private Double normalMax;

    /** Creates a new instance of Investigation */
    public Investigation() {
    }

    /**
     * @hibernate.id generator-class="native"
     * @return Integer
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @hibernate.property
     * @return String
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @hibernate.property
     * @return String
     */
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @hibernate.property
     * @return Double
     */
    public Double getNormalMin() {
        return normalMin;
    }

    public void setNormalMin(Double normalMin) {
        this.normalMin = normalMin;
    }

    /**
     * @hibernate.property
     * @return Double
     */
    public Double getNormalMax() {
        return normalMax;
    }

    public void setNormalMax(Double normalMax) {
        this.normalMax = normalMax;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Investigation == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Investigation rhs = (Investigation) obj;
        return new EqualsBuilder().append(id, rhs.id).isEquals();
    }

    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(53, 57).append(id).
                toHashCode();
    }

    public InvestigationGroup getGroup() {
        return group;
    }

    public void setGroup(InvestigationGroup group) {
        this.group = group;
    }

    public String toString() {
        return name;
    }
}
