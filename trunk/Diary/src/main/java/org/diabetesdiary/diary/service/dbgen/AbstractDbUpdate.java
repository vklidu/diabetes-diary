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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import org.diabetesdiary.diary.utils.IOUtils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;


/**
 * @author Pavel Cernocky
 */
@Configurable
public abstract class AbstractDbUpdate {

  protected final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private transient SessionFactory sessionFactory;

  public abstract void update();

  protected Session getSession() {
    return sessionFactory.getCurrentSession();
  }

  protected void executeHibernateSqlScript(final String resourceName) {
    log.info("Executing hibernate SQL script from " + resourceName);

    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resourceName)));
      String line;
      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.equals("")) {
          continue;
        }
        if (line.endsWith(";")) {
          line = line.substring(0, line.length() - 1);
        }
        getSession().createQuery(line).executeUpdate();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected void executeNativeSqlScript(final String resourceName) {
    log.info("Executing native SQL script from " + resourceName);

    InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream(resourceName));
    StringWriter sw = new StringWriter();
    try {
      IOUtils.readerToWriter(isr, sw, 1024, true, true);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    final String sql = sw.getBuffer().toString();

    getSession().createSQLQuery(sql).executeUpdate();
  }

}
