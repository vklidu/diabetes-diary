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
package org.diabetesdiary.datamodel.util;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 *
 * @author Jiri Majer
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static Logger log = Logger.getLogger("org.diabetesdiary.HibernateUtil");

    private static String getVersion() {
        return "1.01";
    }

    /** only static methods */
    private HibernateUtil() {
    }
    

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            String dbFile = System.getProperty("user.home") + File.separator + ".diabetesdiary" + File.separator + getVersion() + File.separator + "data";
            Configuration conf = new AnnotationConfiguration();
            conf.configure(HibernateUtil.class.getResource("hibernate.cfg.xml"));
            conf.setProperty("hibernate.connection.url", "jdbc:hsqldb:file:" + dbFile);
            sessionFactory = conf.buildSessionFactory();

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            log.log(Level.SEVERE, "Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void hsqlCleanup(Session s) {
        try {
            s.connection().createStatement().execute("SHUTDOWN");
        } catch (Exception e) {
        }
    }
    public static final ThreadLocal session = new ThreadLocal();

    public static Session currentSession() throws HibernateException {
        Session s = (Session) session.get();
        // Open a new Session, if this Thread has none yet
        if (s == null) {
            s = sessionFactory.openSession();
            session.set(s);
        }
        return s;
    }

    public static void closeSession() throws HibernateException {
        Session s = (Session) session.get();
        session.set(null);
        if (s != null) {
            s.close();
        }
    }

    public static void updateObject(Object obj, String errMessage) {
        Session session = null;
        Transaction tx = null;
        try {
            session = currentSession();
            tx = session.beginTransaction();
            session.update(obj);
            tx.commit();
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, errMessage, ex);
            throw ex;
        } finally {
            closeSession();
        }
    }

    public static void newObject(Object obj, String errMessage) {
        Session session = null;
        Transaction tx = null;
        try {
            session = currentSession();
            tx = session.beginTransaction();
            session.save(obj);
            tx.commit();
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, errMessage, ex);
            throw ex;
        } finally {
            closeSession();
        }
    }

    public static void saveOrUpdateObject(Object obj, String errMessage) {
        Session session = null;
        Transaction tx = null;
        try {
            session = currentSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(obj);
            tx.commit();
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, errMessage, ex);
            throw ex;
        } finally {
            closeSession();
        }
    }

    public static void deleteObject(Object obj, String errMessage) {
        Session session = null;
        Transaction tx = null;
        try {
            session = currentSession();
            tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, errMessage, ex);
            throw ex;
        } finally {
            closeSession();
        }
    }

    public static Object getObject(Class clazz, Serializable id, String errMessage) {
        Session session = null;
        try {
            session = currentSession();
            return session.get(clazz, id);
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, errMessage, ex);
            throw ex;
        } finally {
            closeSession();
        }
    }

    public static List getAllObjects(Class clazz, String errMessage) {
        Session session = null;
        try {
            session = currentSession();
            List result = session.createQuery("from " + clazz.getName() + " as c").list();
            return result;
        } catch (RuntimeException ex) {
            log.log(Level.WARNING, errMessage, ex);
            throw ex;
        } finally {
            closeSession();
        }
    }
}
