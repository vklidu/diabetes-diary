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
package org.diabetesdiary.model;

import org.diabetesdiary.datamodel.api.InsulinParameters;

/**
 *
 * @author Jiri Majer
 */
public enum InsulinParam implements InsulinParameters {

    REGULAR, NPH, LENTE, ULTRALENTE, ANALOG_RAPID, ANALOG_LONG;

    public Double getParameterA() {
        switch (this) {
            case ANALOG_RAPID:
                return 0.04;
            case ANALOG_LONG:
                return 0.0;
            case REGULAR:
                return 0.05;
            case NPH:
                return 0.18;
            case LENTE:
                return 0.15;
            case ULTRALENTE:
                return 0d;
        }
        throw new IllegalStateException("Unknown type of insulin.");
    }

    public Double getParameterB() {
        switch (this) {
            case ANALOG_RAPID:
                return 1.0;
            case ANALOG_LONG:
                return 14d;
            case REGULAR:
                return 1.7;
            case NPH:
                return 4.9;
            case LENTE:
                return 6.2;
            case ULTRALENTE:
                return 13d;
        }
        throw new IllegalStateException("Unknown type of insulin.");
    }

    public Double getParameterS() {
        switch (this) {
            case ANALOG_RAPID:
                return 1.5;
            case ANALOG_LONG:
                return 1.5;
            case REGULAR:
                return 2.0;
            case NPH:
                return 2.0;
            case LENTE:
                return 2.4;
            case ULTRALENTE:
                return 2.5;
        }
        throw new IllegalStateException("Unknown type of insulin.");
    }
}
