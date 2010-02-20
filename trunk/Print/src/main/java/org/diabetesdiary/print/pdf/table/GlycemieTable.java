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
import org.joda.time.LocalDate;

/**
 *
 * @author Jirka Majer
 */
public class GlycemieTable extends AbstractPdfSubTable {

    private static BaseColor lowGlyColor = new BaseColor(55, 110, 200);
    private static BaseColor normalGlyColor = new BaseColor(10, 200, 100);
    private static BaseColor highGlyColor = new BaseColor(240, 40, 40);
    private double normalMin = 4;
    private double normalMax = 9;

    public GlycemieTable(LocalDate from, LocalDate to, Patient patient) {
        super(from, to, patient);
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public float getWidth() {
        return 8;
    }

    @Override
    protected HeaderBuilder getHeader() {
        return (HeaderBuilder) GeneratorHelper.headerBuilder("Glykémie (mmol/l)").addColumn("snídaně").addColumn("před").addSister("po").getParent().addSister("oběd").addColumn("před").addSister("po").getParent().addSister("1. večeře").addColumn("před").addSister("po").getParent().addSister("před spaním").addSister("v noci");
    }

    @Override
    protected String getValue(LocalDate date, int col) {
        return String.valueOf(date.getDayOfWeek() * 2);
    }

    @Override
    protected BaseColor getBackGroundColor(LocalDate date, int col, boolean onlyBlackWhite) {
        Double value = date.getDayOfWeek() * 2.0;
        if (onlyBlackWhite || value == null) {
            return super.getBackGroundColor(date, col, onlyBlackWhite);
        }
        if (value < normalMin) {
            return lowGlyColor;
        } else if (value <= normalMax) {
            return normalGlyColor;
        } else {
            return highGlyColor;
        }
    }
}
