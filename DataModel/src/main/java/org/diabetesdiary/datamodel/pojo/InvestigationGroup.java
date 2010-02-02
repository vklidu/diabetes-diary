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
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="investigation_group"
 * @author Jiri Majer
 */
public class InvestigationGroup implements Serializable{
    private Integer id;
    private String name;
    private List investigations;
    
    
    /** Creates a new instance of InvestigationGroup */
    public InvestigationGroup() {
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
    
    public boolean equals(Object obj) {
        if (obj instanceof InvestigationGroup == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        InvestigationGroup rhs = (InvestigationGroup) obj;
        return new EqualsBuilder()
        .append(getId(), rhs.getId())
        .isEquals();
    }
    
    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(49, 53).
                append(getId()).
                toHashCode();
    }
    
    /**
     * @hibernate.set table="investigation" cascade="all" lazy="false" inverse="true"
     * @hibernate.key column="idGroup"
     * @hibernate.one-to-many class="org.diabetesdiary.pojo.Investigation"
     * @return java.util.Set
     */    
    public List getInvestigations() {
        return investigations;
    }
    
    public void setInvestigations(List investigations) {
        this.investigations = investigations;
    }
    
}
