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

import com.google.common.base.Function;
import org.diabetesdiary.diary.service.db.InsulinDO;

/**
 *
 * @author Jirka Majer
 */
public class Insulin extends AbstractDomainObject {

    private final InsulinType type;
    private final String name;
    private final String source;
    private final String manufacturer;
    private final String description;

    public Insulin(InsulinDO insulinDO) {
        super(insulinDO.getId());
        this.type = new InsulinType(insulinDO.getType());
        this.name = insulinDO.getName();
        this.source = insulinDO.getSource();
        this.manufacturer = insulinDO.getManufacturer();
        this.description = insulinDO.getDescription();
    }
    public static Function<InsulinDO, Insulin> CREATE_FUNCTION = new Function<InsulinDO, Insulin>() {

        @Override
        public Insulin apply(InsulinDO activityDO) {
            return new Insulin(activityDO);
        }
    };

    public InsulinType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name;
    }
}
