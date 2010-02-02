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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diabetesdiary.datamodel.api.FoodAdministrator;
import org.diabetesdiary.datamodel.pojo.Food;
import org.diabetesdiary.datamodel.pojo.FoodGroup;
import org.diabetesdiary.datamodel.pojo.FoodUnit;
import org.diabetesdiary.datamodel.pojo.FoodUnitPK;
import org.diabetesdiary.datamodel.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Jiri Majer
 */
public class FoodAdministratorImpl implements FoodAdministrator {

    private static Logger log = Logger.getLogger("org.diabetesdiary.FoodAdministratorImpl");
    private static final FoodAdministrator foodAdmin = new FoodAdministratorImpl();

    public static FoodAdministrator getInstance() {
        return foodAdmin;
    }

    public List<Food> getFoods() {
        Session session = null;
        try {
            session = HibernateUtil.currentSession();
            List result = session.createCriteria(Food.class).list();
            return result;
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, "Cannot obtain Foods.", ex);
            throw ex;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public List<Food> getFoodsByGroup(FoodGroup idGroup) {
        Session session = null;
        try {
            session = HibernateUtil.currentSession();
            List<Food> result = new LinkedList<Food>();
            ResultSet rs = session.connection().createStatement().executeQuery("select * from food where id_food_group = " + idGroup.getId());
            while (rs.next()) {
                Food food = new Food();
                food.setName(rs.getString("name"));
                food.setIdFood(rs.getInt("idFood"));
                food.setEnergy(rs.getDouble("energy"));
                food.setProtein(rs.getDouble("protein"));
                food.setFat(rs.getDouble("fat"));
                food.setSugar(rs.getDouble("sugar"));
                food.setCholesterol(loadDouble(rs, "cholesterol"));
                food.setRoughage(loadDouble(rs, "roughage"));
                result.add(food);
            }
            return result;
        } catch (Exception ex) {
            log.log(Level.WARNING, "Cannot obtain FoodGroups by parent.", ex);
            throw new RuntimeException(ex);
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public Food getFood(Integer idFood) {
        return (Food) HibernateUtil.getObject(Food.class, idFood, "Cannot get Food by id.");
    }

    public void newFood(Food food) {
        HibernateUtil.newObject(food, "Cannot insert new Food.");
    }

    public void newFoodGroup(FoodGroup group) {
        HibernateUtil.newObject(group, "Cannot insert new FoodGroup.");
    }

    public void updateFood(Food food) {
        HibernateUtil.updateObject(food, "Cannot update Food.");
    }

    public void updateGroup(FoodGroup group) {
        HibernateUtil.updateObject(group, "Cannot update FoodGroup.");
    }

    public void deleteFood(Food food) {
        HibernateUtil.deleteObject(food, "Cannot delete Food.");
    }

    public void deleteFoodGroup(FoodGroup group) {
        HibernateUtil.deleteObject(group, "Cannot delete FoodGroup.");
    }

    public List<FoodGroup> getFoodGroups() {
        return HibernateUtil.getAllObjects(FoodGroup.class, "Cannot obtain collection of FoodGroups.");
    }

    private Integer loadInteger(ResultSet rs, String field) throws SQLException {
        Integer result = rs.getInt(field);
        if (rs.wasNull()) {
            result = null;
        }
        return result;
    }

    private static Double loadDouble(ResultSet rs, String field) throws SQLException {
        Double result = rs.getDouble(field);
        if (rs.wasNull()) {
            return null;
        }
        return result;
    }

    public FoodGroup getFoodGroup(Integer idGroup) {
        Session session = null;
        try {
            session = HibernateUtil.currentSession();
            FoodGroup result = null;
            ResultSet rs = session.connection().createStatement().executeQuery("select * from food_group where id = " + idGroup);
            if (rs.next()) {
                result = new FoodGroup();
                result.setId(rs.getInt("id"));
                result.setName(rs.getString("name"));
                result.setParent(new FoodGroup());
                result.getParent().setId(loadInteger(rs, "idParent"));
            }
            return result;
        } catch (Exception ex) {
            log.log(Level.WARNING, "Cannot obtain FoodGroup by id.", ex);
            throw new RuntimeException(ex);
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public List<FoodUnit> getFoodUnits(Integer idFood) {
        Session session = null;
        try {
            session = HibernateUtil.currentSession();
            List result = session.createCriteria(FoodUnit.class).add(Restrictions.eq("id.idFood", idFood)).list();
            return result;
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, "Cannot obtain FoodUnits.", ex);
            throw ex;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public FoodUnit getFoodUnit(Integer idFood, String unit) {
        return (FoodUnit) HibernateUtil.getObject(FoodUnit.class, new FoodUnitPK(idFood, unit), "Cannot get FoodUnit by id.");
    }

    public List<FoodGroup> getFoodGroupByParent(FoodGroup idGroup) {
        Session session = null;
        try {
            session = HibernateUtil.currentSession();
            List<FoodGroup> result = new LinkedList<FoodGroup>();
            ResultSet rs = session.connection().createStatement().executeQuery("select * from food_group where idparent = " + idGroup.getId());
            while (rs.next()) {
                FoodGroup gr = new FoodGroup();
                gr.setId(rs.getInt(1));
                gr.setName(rs.getString(2));
                gr.setParent(new FoodGroup());
                gr.getParent().setId(loadInteger(rs, "idParent"));
                result.add(gr);
            }
            return result;
        } catch (Exception ex) {
            log.log(Level.WARNING, "Cannot obtain FoodGroups by parent.", ex);
            throw new RuntimeException(ex);
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public List<FoodGroup> getBaseFoodGroups() {
        Session session = null;
        try {
            session = HibernateUtil.currentSession();
            List result = session.createCriteria(FoodGroup.class).add(Restrictions.isNull("parent.id")).list();
            return result;
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, "Cannot obtain base FoodGroups.", ex);
            throw ex;
        } finally {
            HibernateUtil.closeSession();
        }
    }
}
