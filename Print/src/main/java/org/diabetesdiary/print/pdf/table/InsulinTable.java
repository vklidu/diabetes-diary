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

import com.lowagie.text.pdf.PdfPTable;
import org.diabetesdiary.print.pdf.GeneratorHelper;
import org.joda.time.LocalDate;

/**
 *
 * @author Jirka Majer
 */
public class InsulinTable extends AbstractPdfSubTable {

    public InsulinTable(LocalDate from, LocalDate to) {
        super(from, to);
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public float getWidth() {
        return 20;
    }

    @Override
    public PdfPTable getHeader() {
        return GeneratorHelper.headerBuilder("Inzulín").addColumn("Bazál").addColumn("ráno").addSister("v poledne").addSister("večer").getParent().addSister("Bolus").addColumn("dopoledne").addSister("odpoledne").build();
    }

    @Override
    protected String getValue(LocalDate date, int col) {
        return String.valueOf(date.getDayOfWeek());
    }

}
