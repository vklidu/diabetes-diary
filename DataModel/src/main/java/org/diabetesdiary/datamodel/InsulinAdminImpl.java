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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diabetesdiary.datamodel.api.InsulinAdministrator;
import org.diabetesdiary.datamodel.pojo.Insulin;
import org.diabetesdiary.datamodel.pojo.InsulinType;
import org.diabetesdiary.datamodel.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Jiri Majer
 */
public class InsulinAdminImpl implements InsulinAdministrator {

    private static final InsulinAdministrator admin = new InsulinAdminImpl();
    private static Logger log = Logger.getLogger("org.diabetesdiary.InsulinAdminImpl");

    public static InsulinAdministrator getInstance() {
        return admin;
    }

    public List<Insulin> getInsulines() {
        return HibernateUtil.getAllObjects(Insulin.class, "Cannot obtain collection of Insulines.");
    }

    public List<InsulinType> getInsulinTypes() {
        return HibernateUtil.getAllObjects(InsulinType.class, "Cannot obtain collection of InsulinTypes.");
    }

    public Insulin getInsulin(Integer idInsulin) {
        return (Insulin) HibernateUtil.getObject(Insulin.class, idInsulin, "Cannot obtain Insulin by id.");
    }

    public InsulinType getInsulinType(Integer idInsulin) {
        return (InsulinType) HibernateUtil.getObject(InsulinType.class, idInsulin, "Cannot obtain InsulinType by id.");
    }

    public void newInsulin(Insulin insulin) {
        HibernateUtil.newObject(insulin, "Cannot create new Insulin.");
    }

    public void newInsulinType(InsulinType type) {
        HibernateUtil.newObject(type, "Cannot create new InsulinType.");
    }

    public void updateInsulin(Insulin insulin) {
        HibernateUtil.updateObject(insulin, "Cannot update Insulin.");
    }

    public void updateInsulinType(InsulinType type) {
        HibernateUtil.updateObject(type, "Cannot update InsulinType.");
    }

    public void deleteInsulin(Insulin insulin) {
        HibernateUtil.deleteObject(insulin, "Cannot delete Insulin.");
    }

    public void deleteInsulinType(InsulinType type) {
        HibernateUtil.deleteObject(type, "Cannot delete InsulinType.");
    }

    public List<Insulin> getInsulines(Integer idType) {
        Session session = null;
        try {
            session = HibernateUtil.currentSession();
            List result = session.createCriteria(Insulin.class).add(Restrictions.eq("type.id", idType)).list();
            return result;
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, "Cannot obtain FoodUnits.", ex);
            throw ex;
        } finally {
            HibernateUtil.closeSession();
        }
    }
}
