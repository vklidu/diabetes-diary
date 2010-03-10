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
import org.diabetesdiary.diary.service.db.InvestigationDO;

/**
 *
 * @author Jirka Majer
 */
public class Investigation extends AbstractDomainObject {

    private final InvestigationGroup group;
    private final String name;
    private final String unit;
    private final Double normalMin;
    private final Double normalMax;
    private final WKInvest wkinvest;

    public Investigation(InvestigationDO inv) {
        super(inv.getId());
        this.group = new InvestigationGroup(inv.getGroup());
        this.name = inv.getName();
        this.unit = inv.getUnit();
        this.normalMin = inv.getNormalMin();
        this.normalMax = inv.getNormalMax();
        this.wkinvest = inv.getWkinvest();
    }
    public static Function<InvestigationDO, Investigation> CREATE_FUNCTION = new Function<InvestigationDO, Investigation>() {

        @Override
        public Investigation apply(InvestigationDO activityDO) {
            return new Investigation(activityDO);
        }
    };

    public InvestigationGroup getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public Double getNormalMin() {
        return normalMin;
    }

    public Double getNormalMax() {
        return normalMax;
    }

    public boolean anyType(WKInvest... wkinvests) {
        if (wkinvest == null) {
            return false;
        }
        for (WKInvest wki : wkinvests) {
            if (wki == wkinvest) {
                return true;
            }
        }
        return false;
    }

    public WKInvest getWKInvest() {
        return wkinvest;
    }

    @Override
    public String toString() {
        return name;
    }
}
