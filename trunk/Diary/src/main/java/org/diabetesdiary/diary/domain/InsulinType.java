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
import com.google.common.collect.Lists;
import java.util.List;
import org.diabetesdiary.diary.api.InsulinParameters;
import org.diabetesdiary.diary.service.db.InsulinDO;
import org.diabetesdiary.diary.service.db.InsulinTypeDO;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jirka Majer
 */
@Configurable
public class InsulinType extends AbstractDomainObject implements InsulinParameters {

    private final String name;
    private final Double parameterS;
    private final Double parameterA;
    private final Double parameterB;
    private final String description;

    public InsulinType(InsulinTypeDO type) {
        super(type.getId());
        this.name = type.getName();
        this.parameterS = type.getParameterS();
        this.parameterA = type.getParameterA();
        this.parameterB = type.getParameterB();
        this.description = type.getDescription();
    }

    @Transactional(readOnly = true)
    public List<Insulin> getInsulines() {
        return Lists.newArrayList(Lists.transform(getSession().createCriteria(InsulinDO.class).add(Restrictions.eq("type.id", id)).list(), Insulin.CREATE_FUNCTION));
    }
    public static Function<InsulinTypeDO, InsulinType> CREATE_FUNCTION = new Function<InsulinTypeDO, InsulinType>() {

        @Override
        public InsulinType apply(InsulinTypeDO activityDO) {
            return new InsulinType(activityDO);
        }
    };

    public String getName() {
        return name;
    }

    @Override
    public Double getParameterS() {
        return parameterS;
    }

    @Override
    public Double getParameterA() {
        return parameterA;
    }

    @Override
    public Double getParameterB() {
        return parameterB;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name;
    }
}
