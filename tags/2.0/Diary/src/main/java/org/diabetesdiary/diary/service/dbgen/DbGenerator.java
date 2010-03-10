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
package org.diabetesdiary.diary.service.dbgen;

import org.diabetesdiary.diary.service.db.DbVersionDO;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Pavel Cernocky
 */
@Component
public class DbGenerator {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private transient SessionFactory sessionFactory;

    @Transactional
    public void generate() {
        DbVersionDO dbVersionDO = (DbVersionDO) sessionFactory.getCurrentSession().createQuery("from DbVersionDO").uniqueResult();
        if (dbVersionDO == null) {
            dbVersionDO = new DbVersionDO();
            dbVersionDO.setDbVersion(0);
            sessionFactory.getCurrentSession().save(dbVersionDO);
        }

        for (int i = dbVersionDO.getDbVersion() + 1; i <= DbVersionDO.CURRENT_DB_VERSION; i++) {
            log.info("Updating DB to version " + i);
            updateToVersion(i);
        }

        if (DbVersionDO.CURRENT_DB_VERSION != dbVersionDO.getDbVersion()) {
            dbVersionDO.setDbVersion(DbVersionDO.CURRENT_DB_VERSION);
            sessionFactory.getCurrentSession().update(dbVersionDO);
        }

        log.info("Database version: " + DbVersionDO.CURRENT_DB_VERSION);
    }

    @SuppressWarnings("unchecked")
    private void updateToVersion(int version) {
        String className = getClass().getPackage().getName() + ".DbUpdate" + version;

        Class<AbstractDbUpdate> clazz;
        try {
            clazz = (Class<AbstractDbUpdate>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Can not find class: " + className, e);
        }

        AbstractDbUpdate dbUpdate;
        try {
            dbUpdate = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        dbUpdate.update();
    }
}
