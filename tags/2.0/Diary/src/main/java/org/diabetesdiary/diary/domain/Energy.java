/*
 *   Copyright (C) 2006-2008 Jiri Majer. All Rights Reserved.
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

/**
 *
 * @author Jirka Majer
 */
public class Energy {

    private final double value;
    private final Unit unit;

    public Energy(Unit unit) {
        this(unit, 0);
    }

    public Energy(Unit unit, double value) {
        this.unit = unit;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public Energy withValue(double value) {
        return new Energy(unit, value);
    }

    public Energy minus(Energy energy) {
        return new Energy(unit, value - energy.getValue(unit));
    }

    public Energy plus(Energy energy) {
        return new Energy(unit, value + energy.getValue(unit));
    }

    public double getValue(Unit newUnit) {
        return this.unit.toUnit(newUnit, value);
    }

    public static enum Unit {
        kJ, kcal;

        public double toUnit(Unit newUnit, double value) {
            if (newUnit == this) {
                return value;
            }
            if (this == kJ && newUnit == kcal) {
                return value / 4.1868;
            }
            if (this == kcal && newUnit == kJ) {
                return value * 4.1868;
            }
            throw new IllegalArgumentException();
        }
    }
}
