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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import org.hibernate.Hibernate;

/**
 *
 * @author Jirka
 */
@MappedSuperclass
public abstract class AbstractDO implements Serializable {

  protected static final int BATCH_SIZE = 100;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="idSequenceGenerator")
  @SequenceGenerator(name="idSequenceGenerator", sequenceName="ID_SEQ", allocationSize=1000)
  private Long id;

  public Long getId() {
    return id;
  }

  @Override
  public int hashCode() {
    if (id == null) {
      throw new IllegalStateException("hashCode() not supported for nonpersisted objects");
    }
    else {
      return id.hashCode();
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (Hibernate.getClass(this) != Hibernate.getClass(obj)) {
      return false;
    }

    final AbstractDO other = (AbstractDO) obj;
    if (id == null || other.getId() == null) {
      throw new IllegalStateException("equals() not supported for nonpersisted objects");
    }
    else if (!id.equals(other.getId())) {
      return false;
    }
    return true;
  }

}
