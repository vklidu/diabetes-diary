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


package org.diabetesdiary.datamodel.api;

import java.util.Date;
import java.util.List;
import org.diabetesdiary.datamodel.pojo.Patient;
import org.diabetesdiary.datamodel.pojo.RecordActivity;
import org.diabetesdiary.datamodel.pojo.RecordFood;
import org.diabetesdiary.datamodel.pojo.RecordInsulin;
import org.diabetesdiary.datamodel.pojo.RecordInvest;

/**
 *
 * @author Jiri Majer
 */
public interface Diary {
    
    public void setCurrentPatient(Patient patient);
    
    public Double getWeight(java.util.Date date, String idPatient);
    
    public List<RecordInvest> getRecordInvests(java.util.Date from, java.util.Date to, String idPatient);
    
    public List<RecordFood> getRecordFoods(java.util.Date from, java.util.Date to, String idPatient);
    
    public List<RecordActivity> getRecordActivities(java.util.Date from, java.util.Date to, String idPatient);
    
    public List<RecordInsulin> getRecordInsulins(java.util.Date from, java.util.Date to, String idPatient);
    
    public Patient getCurrentPatient();
    
    public List<Patient> getPatients();
    
    public Patient getPatient(String idPatient);
    
    public void newPatient(Patient patient);
    
    public void updatePatient(Patient patient);
    
    public void deletePatient(Patient patient);
    
    
    public void addRecord(RecordInvest record);
    
    public void updateRecord(RecordInvest record);
    
    public void deleteRecord(RecordInvest record);
    
    
    public void addRecord(RecordFood record);
    
    public void updateRecord(RecordFood record);
    
    public void deleteRecord(RecordFood record);
    
    
    public void addRecord(RecordActivity record);
    
    public void updateRecord(RecordActivity record);
    
    public void deleteRecord(RecordActivity record);
    
    
    public void addRecord(RecordInsulin record);
    
    public void updateRecord(RecordInsulin record);
    
    public void deleteRecord(RecordInsulin record);

    public void deleteRecord(Object value);

    public List<RecordFood> getRecordFoods(Date date, String idPatient);

    public void deleteRecordFood(String idPatient, Date date);
    
}
