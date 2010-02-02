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
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.composite-key class="RecordFoodPK"
 * @author Jiri Majer
 */
public class RecordActivityPK implements Serializable {

    private Integer idActivity;
    private String idPatient;
    private Date date;

    /** Creates a new instance of RecordActivityPK */
    public RecordActivityPK() {
    }

    public RecordActivityPK(Integer idActivity, String idPatient, Date date) {
        this.idActivity = idActivity;
        this.idPatient = idPatient;
        this.date = date;
    }

    /**
     * @hibernate.key-property
     * @return Integer
     */
    public Integer getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(Integer idActivity) {
        this.idActivity = idActivity;
    }

    /**
     * @hibernate.key-property
     * @return String
     */
    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    /**
     * @hibernate.key-property
     * @return Date
     */
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RecordActivityPK == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        RecordActivityPK rhs = (RecordActivityPK) obj;
        return new EqualsBuilder().append(getIdActivity(), rhs.getIdActivity()).append(idPatient, rhs.idPatient).append(date, rhs.date).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(57, 71).append(getIdActivity()).
                append(idPatient).
                append(date).
                toHashCode();
    }
}
