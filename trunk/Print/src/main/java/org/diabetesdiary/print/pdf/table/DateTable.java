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

import com.itextpdf.text.BaseColor;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.print.pdf.GeneratorHelper;
import org.diabetesdiary.print.pdf.GeneratorHelper.HeaderBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.openide.util.NbBundle;

/**
 *
 * @author Jirka Majer
 */
public class DateTable extends AbstractPdfSubTable {

    public DateTable(DateTime from, DateTime to, Patient patient) {
        super(from, to, patient);
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public float getWidth(int column) {
        return 7f;
    }

    @Override
    protected String getValue(LocalDate date, int col) {
        return date.toString(DateTimeFormat.shortDate());
    }

    @Override
    protected BaseColor getBackGroundColor(LocalDate date, int col, boolean onlyBlackWhite) {
        return date.getDayOfWeek() == DateTimeConstants.SUNDAY ? BaseColor.LIGHT_GRAY : super.getBackGroundColor(date, col, onlyBlackWhite);
    }

    @Override
    public HeaderBuilder getHeader() {
        return GeneratorHelper.headerBuilder(NbBundle.getMessage(DateTable.class, "DATUM"));
    }

}
