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
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.diabetesdiary.datamodel.api.InsulinParameters;

/**
 * Mame 4 zakladni druhy inzulinu. Deli se podle rychlosti vstrebavani do tela
 * a nastupem ucinku. (rapid, stredni, pomaly, analog)
 * @hibernate.class table="insulin_type"
 * @author Jiri Majer
 */
public class InsulinType implements Serializable, InsulinParameters {

    private Integer id;
    private String name;
    private Double parameterS;
    private Double parameterA;
    private Double parameterB;
    private String description;
    private Set insulins;

    /** Creates a new instance of InsulinType */
    public InsulinType() {
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
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InsulinType == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        InsulinType rhs = (InsulinType) obj;
        return new EqualsBuilder().append(getId(), rhs.getId()).isEquals();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(37, 51).append(getId()).
                toHashCode();
    }

    /**
     * @hibernate.set table="insulin" cascade="all" lazy="false" inverse="true"
     * @hibernate.key column="idType"
     * @hibernate.one-to-many class="org.diabetesdiary.pojo.Insulin"
     * @return java.util.Set
     */
    public Set getInsulins() {
        return insulins;
    }

    public void setInsulins(Set insulins) {
        this.insulins = insulins;
    }

    public Double getParameterS() {
        return parameterS;
    }

    public void setParameterS(Double parameterS) {
        this.parameterS = parameterS;
    }

    public Double getParameterA() {
        return parameterA;
    }

    public void setParameterA(Double parameterA) {
        this.parameterA = parameterA;
    }

    public Double getParameterB() {
        return parameterB;
    }

    public void setParameterB(Double parameterB) {
        this.parameterB = parameterB;
    }
}
