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
package org.diabetesdiary.print.pdf.table;

import com.google.common.base.Preconditions;

/**
 *
 * @author Jirka Majer
 */
public class Tuple2<T, S> {

    private final T value1;
    private final S value2;

    public Tuple2(T value1, S value2) {
        this.value1 = Preconditions.checkNotNull(value1);
        this.value2 = Preconditions.checkNotNull(value2);
    }

    public T getValue1() {
        return value1;
    }

    public S getValue2() {
        return value2;
    }

    @Override
    public int hashCode() {
        return value1.hashCode() + value2.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tuple2) {
            Tuple2 inst = (Tuple2) obj;
            return inst.value1.equals(value1) && inst.value2.equals(value2);
        }
        return false;
    }
}
