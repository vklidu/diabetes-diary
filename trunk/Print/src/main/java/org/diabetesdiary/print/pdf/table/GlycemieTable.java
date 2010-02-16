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
import java.awt.Color;
import org.diabetesdiary.print.pdf.GeneratorHelper;
import org.joda.time.LocalDate;

/**
 *
 * @author Jirka Majer
 */
public class GlycemieTable extends AbstractPdfSubTable {

    public GlycemieTable(LocalDate from, LocalDate to) {
        super(from, to);
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public float getWidth() {
        return 20;
    }

    @Override
    public PdfPTable getHeader() {
        return GeneratorHelper.headerBuilder("Glykémie").addColumn("ráno").addSister("v poledne").addSister("odpoledne").addSister("večer").build();
    }

    @Override
    protected String getValue(LocalDate date, int col) {
        return String.valueOf(date.getDayOfWeek());
    }

    @Override
    protected Color getBackGroundColor(LocalDate date, int col, boolean onlyBlackWhite) {
        return date.getDayOfWeek() > 5 ? Color.RED : Color.GREEN;
    }

}
