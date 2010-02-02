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
 * @hibernate.class table="insulin"
 * @author Jiri Majer
 */
public class Insulin implements Serializable {

    private Integer id;
    private InsulinType type;
    private String name;
    private String source;
    private String manufacturer;
    private String description;

    /** Creates a new instance of Insulin */
    public Insulin() {
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
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @hibernate.property
     * @return String
     */
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
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
        if (obj instanceof Insulin == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Insulin rhs = (Insulin) obj;
        return new EqualsBuilder().append(id, rhs.id).isEquals();
    }

    @Override
    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(31, 49).append(id).
                toHashCode();
    }

    public InsulinType getType() {
        return type;
    }

    public void setType(InsulinType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }
}
