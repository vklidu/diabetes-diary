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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="patient"
 * @author Jiri Majer
 */
public class Patient implements Serializable{
    
    private String idPatient;
    private String name;
    private String surname;
    private boolean male;
    private Date born;
    private Date sufferFrom;
    private String email;
    private String address;
    private String phone;
    private boolean pumpUsage;
    
    private Insulin basalInsulin;
    private Insulin bolusInsulin;
    
    private Double perSensitivity;
    private Double hepSensitivity;
    private Double filtrationRate;
    private Double renalThreshold;
    
    /**
     * Creates a new instance of Patient
     */
    public Patient() {
    }
    
    public Patient(String idDiary, String name, String surname,
            Boolean male, Date born, Date sufferFrom, String email,
            String address, String phone){
        this.setIdPatient(idDiary);
        this.setName(name);
        this.setSurname(surname);
        this.setMale(male);
        this.setBorn(born);
        this.setSufferFrom(sufferFrom);
        this.setEmail(email);
        this.setAddress(address);
        this.setPhone(phone);
    }
    /**
     * @hibernate.id column="id_patient" generator-class="assigned"
     * @return String
     */
    public String getIdPatient() {
        return idPatient;
    }
    
    public void setIdPatient(String idDiary) {
        this.idPatient = idDiary;
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
    public String getSurname() {
        return surname;
    }
    
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    /**
     * @hibernate.property
     * @return Boolean
     */
    public boolean isMale() {
        return male;
    }
    
    public void setMale(boolean male) {
        this.male = male;
    }
    
    /**
     * @hibernate.property type="date"
     * @return Date
     */
    public Date getBorn() {
        return born;
    }
    
    public void setBorn(Date born) {
        this.born = born;
    }
    
    /**
     * @hibernate.property
     * @return Date
     */
    public Date getSufferFrom() {
        return sufferFrom;
    }
    
    public void setSufferFrom(Date sufferFrom) {
        this.sufferFrom = sufferFrom;
    }
    
    /**
     * @hibernate.property
     * @return String
     */
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * @hibernate.property
     * @return String
     */
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * @hibernate.property
     * @return String
     */
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    
    public boolean equals(Object obj) {
        if (obj instanceof Patient == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Patient rhs = (Patient) obj;
        return new EqualsBuilder()
        .append(idPatient, rhs.idPatient)        
        .isEquals();
    }
    
    public int hashCode() {        
        return new HashCodeBuilder(23, 41).append(idPatient).toHashCode();
    } 

    public String toString(){
        String result = getName() + " " + getSurname();
        if(getBorn() != null){
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            result += " (" + format.format(getBorn()) + ")";
        }
        return result;
    }

    public boolean isPumpUsage() {
        return pumpUsage;
    }

    public void setPumpUsage(boolean pumpUsage) {
        this.pumpUsage = pumpUsage;
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

    public Insulin getBasalInsulin() {
        return basalInsulin;
    }

    public void setBasalInsulin(Insulin basalInsulin) {
        this.basalInsulin = basalInsulin;
    }

    public Insulin getBolusInsulin() {
        return bolusInsulin;
    }

    public void setBolusInsulin(Insulin bolusInsulin) {
        this.bolusInsulin = bolusInsulin;
    }
}
