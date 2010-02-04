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
package org.diabetesdiary.calendar.table;

import java.text.NumberFormat;
import org.diabetesdiary.diary.service.db.RecordInsulinDO;

/**
 *
 * @author Jiri Majer
 */
public class RecordInsulinPumpBasal {

    private RecordInsulinDO[] data;

    /** Creates a new instance of RecordInsulinPumpBasal */
    public RecordInsulinPumpBasal() {
        data = new RecordInsulinDO[12];
    }

    @Override
    public String toString() {
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(0);
        format.setMinimumIntegerDigits(2);

        String result = "";
        for (RecordInsulinDO rec : data) {
            if (rec != null) {
                result += format.format(rec.getAmount() * 10);
            } else {
                result += "00";
            }
            result += "-";
        }
        return result.substring(0, result.length() - 1);
    }

    public RecordInsulinDO[] getData() {
        return data;
    }
}
