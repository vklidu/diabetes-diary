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
package org.diabetesdiary.diary.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jirka Majer
 */
public abstract class AbstractDomainObject implements Serializable {

  protected transient Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private transient SessionFactory sessionFactory;

  protected final Long id;

  protected AbstractDomainObject(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  protected SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  protected Session getSession() {
    return sessionFactory.getCurrentSession();
  }

  private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    ois.defaultReadObject();
    log = LoggerFactory.getLogger(getClass());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AbstractDomainObject other = (AbstractDomainObject) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    }
    else if (!id.equals(other.id))
      return false;
    return true;
  }

}