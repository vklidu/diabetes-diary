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
package org.diabetesdiary.datamodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diabetesdiary.datamodel.api.Diary;
import org.diabetesdiary.datamodel.pojo.Food;
import org.diabetesdiary.datamodel.pojo.FoodGroup;
import org.diabetesdiary.datamodel.pojo.FoodUnit;
import org.diabetesdiary.datamodel.pojo.FoodUnitPK;
import org.diabetesdiary.datamodel.pojo.Investigation;
import org.diabetesdiary.datamodel.pojo.Patient;
import org.diabetesdiary.datamodel.pojo.RecordActivity;
import org.diabetesdiary.datamodel.pojo.RecordFood;
import org.diabetesdiary.datamodel.pojo.RecordFoodPK;
import org.diabetesdiary.datamodel.pojo.RecordInsulin;
import org.diabetesdiary.datamodel.pojo.RecordInvest;
import org.diabetesdiary.datamodel.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Jiri Majer
 */
public class DiaryImpl implements Diary {

    private static Logger log = Logger.getLogger("org.diabetesdiary.DiaryImpl");
    private static Diary diary = new DiaryImpl();
    private Patient curPat = null;

    /**
     * Singleton
     */
    public static Diary getInstance() {
        return diary;
    }

    public List<Patient> getPatients() {
        return HibernateUtil.getAllObjects(Patient.class, "Cannot obtain collection of Patients.");
    }

    public Patient getPatient(String idPatient) {
        return (Patient) HibernateUtil.getObject(Patient.class, idPatient, "Cannot obtain Patient by Id.");
    }

    public void newPatient(Patient patient) {
        HibernateUtil.saveOrUpdateObject(patient, "Cannot create new Patient.");
    }

    public void updatePatient(Patient patient) {
        HibernateUtil.updateObject(patient, "Cannot update Patient.");
    }

    public void deletePatient(Patient patient) {
        HibernateUtil.deleteObject(patient, "Cannot delete Patient.");
    }

    public List<RecordInvest> getRecordInvests(java.util.Date from, java.util.Date to, String idPatient) {
        Session session = null;
        try {
            session = HibernateUtil.currentSession();
            List result = session.createCriteria(RecordInvest.class).add(Restrictions.between("id.date", from, to)).add(Restrictions.eq("id.idPatient", idPatient)).addOrder(Order.asc("id.date")).list();
            return result;
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, "Cannot obtain Records from-to.", ex);
            throw ex;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public Patient getCurrentPatient() {
        return curPat;
    }

    public void setCurrentPatient(Patient patient) {
        this.curPat = patient;
    }

    public void addRecord(RecordInvest record) {
        HibernateUtil.saveOrUpdateObject(record, "Cannot create new Record.");
    }

    public void updateRecord(RecordInvest record) {
        HibernateUtil.updateObject(record, "Cannot update Record.");
    }

    public void deleteRecord(RecordInvest record) {
        HibernateUtil.deleteObject(record, "Cannot delete Record.");
    }

    public void addRecord(RecordFood record) {
        HibernateUtil.saveOrUpdateObject(record, "Cannot create new Record.");
    }

    public void updateRecord(RecordFood record) {
        HibernateUtil.updateObject(record, "Cannot update Record.");
    }

    public void deleteRecord(RecordFood record) {
        HibernateUtil.deleteObject(record, "Cannot delete Record.");
    }

    public void addRecord(RecordActivity record) {
        HibernateUtil.saveOrUpdateObject(record, "Cannot create new Record.");
    }

    public void updateRecord(RecordActivity record) {
        HibernateUtil.updateObject(record, "Cannot update Record.");
    }

    public void deleteRecord(RecordActivity record) {
        HibernateUtil.deleteObject(record, "Cannot delete Record.");
    }

    public void addRecord(RecordInsulin record) {
        HibernateUtil.saveOrUpdateObject(record, "Cannot create new Record.");
    }

    public void updateRecord(RecordInsulin record) {
        HibernateUtil.updateObject(record, "Cannot update Record.");
    }

    public void deleteRecord(RecordInsulin record) {
        HibernateUtil.deleteObject(record, "Cannot delete Record.");
    }

    public List<RecordFood> getRecordFoods(Date from, Date to, String idPatient) {
        Session session = null;
        try {
            session = HibernateUtil.currentSession();
            List<RecordFood> result = new LinkedList<RecordFood>();
            PreparedStatement ps = session.connection().prepareStatement("select food.name as name, idFood, energy, protein, fat, sugar, cholesterol, roughage," +
                    "id_food_group, unit, koef, food_unit.name as unitName, shortcut, amount, notice, season, totalAmount, idPatient, date " +
                    "from record_food left join food on food.idFood = record_food.idFood " +
                    "left join food_unit on food_unit.unit = record_food.unit and food.idFood = food_unit.idFood " +
                    "where idPatient=? and date>=? and date<=?");
            ps.setString(1, idPatient);
            ps.setTimestamp(2, new Timestamp(from.getTime()));
            ps.setTimestamp(3, new Timestamp(to.getTime()));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RecordFood rec = new RecordFood();
                Food food = new Food();
                food.setName(rs.getString("name"));
                food.setIdFood(rs.getInt("idFood"));
                food.setEnergy(rs.getDouble("energy"));
                food.setProtein(rs.getDouble("protein"));
                food.setFat(rs.getDouble("fat"));
                food.setSugar(rs.getDouble("sugar"));
                food.setCholesterol(rs.getDouble("cholesterol"));
                food.setRoughage(rs.getDouble("roughage"));
                food.setFoodGroup(new FoodGroup());
                food.getFoodGroup().setId(rs.getInt("id_food_group"));
                FoodUnit unit = new FoodUnit();
                unit.setId(new FoodUnitPK(rs.getInt("idFood"), rs.getString("unit")));
                unit.setKoef(rs.getDouble("koef"));
                unit.setName(rs.getString("unitName"));
                unit.setShortcut(rs.getString("shortcut"));
                food.setUnits(new HashSet(1));
                food.getUnits().add(unit);
                rec.setFood(food);
                rec.setAmount(rs.getDouble("amount"));
                rec.setNotice(rs.getString("notice"));
                rec.setSeason(rs.getString("season"));
                rec.setTotalAmount(rs.getDouble("totalAmount"));
                rec.setUnit(rs.getString("unit"));
                rec.setId(new RecordFoodPK(rs.getInt("idFood"), rs.getString("idPatient"), new Date(rs.getTimestamp("date").getTime())));
                result.add(rec);
            }
            return result;
        } catch (Exception ex) {
            log.log(Level.WARNING, "Cannot obtain RecordFoods from to.", ex);
            throw new RuntimeException(ex);
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public List<RecordInsulin> getRecordInsulins(Date from, Date to, String idPatient) {
        Session session = null;
        try {
            session = HibernateUtil.currentSession();
            List result = session.createCriteria(RecordInsulin.class).add(Restrictions.between("id.date", from, to)).add(Restrictions.eq("id.idPatient", idPatient)).list();
            return result;
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, "Cannot obtain RecordInsulins from-to.", ex);
            throw ex;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public List<RecordActivity> getRecordActivities(Date from, Date to, String idPatient) {
        Session session = null;
        try {
            session = HibernateUtil.currentSession();
            List result = session.createCriteria(RecordActivity.class).add(Restrictions.between("id.date", from, to)).add(Restrictions.eq("id.idPatient", idPatient)).list();
            return result;
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, "Cannot obtain RecordActivity from-to.", ex);
            throw ex;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public void deleteRecord(Object value) {
        if (value instanceof RecordInvest) {
            deleteRecord((RecordInvest) value);
        } else if (value instanceof RecordFood) {
            deleteRecord((RecordFood) value);
        } else if (value instanceof RecordActivity) {
            deleteRecord((RecordActivity) value);
        } else if (value instanceof RecordInsulin) {
            deleteRecord((RecordInsulin) value);
        } else if (value instanceof RecordInvest[]) {
            for (RecordInvest rec : (RecordInvest[]) value) {
                deleteRecord(rec);
            }
        } else if (value instanceof RecordFood[]) {
            for (RecordFood rec : (RecordFood[]) value) {
                deleteRecord(rec);
            }
        } else if (value instanceof RecordActivity[]) {
            for (RecordActivity rec : (RecordActivity[]) value) {
                deleteRecord(rec);
            }
        } else if (value instanceof RecordInsulin[]) {
            for (RecordInsulin rec : (RecordInsulin[]) value) {
                deleteRecord(rec);
            }
        }
    }

    public List<RecordFood> getRecordFoods(Date date, String idPatient) {
        Session session = null;
        try {
            session = HibernateUtil.currentSession();
            List<RecordFood> result = new LinkedList<RecordFood>();
            PreparedStatement ps = session.connection().prepareStatement("select * from record_food left join food on food.idFood=record_food.idFood where idPatient=? and date=?");
            ps.setString(1, idPatient);
            ps.setTimestamp(2, new Timestamp(date.getTime()));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RecordFood rec = new RecordFood();
                Food food = new Food();
                food.setName(rs.getString("name"));
                food.setIdFood(rs.getInt("idFood"));
                food.setEnergy(rs.getDouble("energy"));
                food.setProtein(rs.getDouble("protein"));
                food.setFat(rs.getDouble("fat"));
                food.setSugar(rs.getDouble("sugar"));
                food.setCholesterol(rs.getDouble("cholesterol"));
                food.setRoughage(rs.getDouble("roughage"));
                food.setFoodGroup(new FoodGroup());
                food.getFoodGroup().setId(rs.getInt("id_food_group"));
                rec.setFood(food);
                rec.setAmount(rs.getDouble("amount"));
                rec.setNotice(rs.getString("notice"));
                rec.setSeason(rs.getString("season"));
                rec.setTotalAmount(rs.getDouble("totalAmount"));
                rec.setUnit(rs.getString("unit"));
                rec.setId(new RecordFoodPK(rs.getInt("idFood"), rs.getString("idPatient"), new Date(rs.getTimestamp("date").getTime())));
                result.add(rec);
            }
            return result;
        } catch (Exception ex) {
            log.log(Level.WARNING, "Cannot obtain RecordFoods by date.", ex);
            throw new RuntimeException(ex);
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public void deleteRecordFood(String idPatient, Date date) {
        List<RecordFood> foods = getRecordFoods(date, idPatient);
        for (RecordFood food : foods) {
            deleteRecord(food);
        }
    }

    public Double getWeight(Date date, String idPatient) {
                Session session = null;
        try {
            session = HibernateUtil.currentSession();
            List result = session.createCriteria(RecordInvest.class).add(Restrictions.lt("id.date", date)).add(Restrictions.eq("id.idInvest", Investigation.Instances.WEIGHT.getID())).add(Restrictions.eq("id.idPatient", idPatient)).addOrder(Order.desc("id.date")).list();
            if(result != null && result.size() > 0){
                return ((RecordInvest)result.get(0)).getValue();
            }
            return null;
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, "Cannot obtain Records from-to.", ex);
            throw ex;
        } finally {
            HibernateUtil.closeSession();
        }
    }
}
