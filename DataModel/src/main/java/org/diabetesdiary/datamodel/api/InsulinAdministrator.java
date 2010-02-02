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

import java.util.List;
import org.diabetesdiary.datamodel.pojo.Insulin;
import org.diabetesdiary.datamodel.pojo.InsulinType;

/**
 *
 * @author Jiri Majer
 */
public interface InsulinAdministrator {

    public List<Insulin> getInsulines(Integer idType);

    public List<Insulin> getInsulines();

    public List<InsulinType> getInsulinTypes();

    public Insulin getInsulin(Integer idInsulin);

    public InsulinType getInsulinType(Integer idInsulin);

    public void newInsulin(Insulin insulin);

    public void newInsulinType(InsulinType type);

    public void updateInsulin(Insulin insulin);

    public void updateInsulinType(InsulinType type);

    public void deleteInsulin(Insulin insulin);

    public void deleteInsulinType(InsulinType type);
}
