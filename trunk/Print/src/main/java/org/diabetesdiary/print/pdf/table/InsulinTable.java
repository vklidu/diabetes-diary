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
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.print.pdf.GeneratorHelper;
import org.diabetesdiary.print.pdf.GeneratorHelper.HeaderBuilder;
import org.diabetesdiary.print.pdf.PDFGenerator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 *
 * @author Jirka Majer
 */
public class InsulinTable extends AbstractPdfSubTable {

    public InsulinTable(DateTime from, DateTime to, Patient patient) {
        super(from, to, patient);
    }

    @Override
    public int getColumnCount() {
        return isPump() ? 5 : 6;
    }

    @Override
    public float getWidth(int column) {
        if (isPump() && column == 4) {
            return 4;
        }
        if ((isPump() && column == 3) || (!isPump() && column == 5)) {
            return 1f;
        }
        return 0.7f;
    }

    @Override
    public HeaderBuilder getHeader() {
        if (isPump()) {
            return (HeaderBuilder) GeneratorHelper.headerBuilder("Inzulín (U)")
                    .addColumn("Bolus").addColumn("snídaně").addSister("oběd").addSister("Večeře").addSister("Přídavek").getParent()
                    .addSister("Bazál");
        }
        return (HeaderBuilder) GeneratorHelper.headerBuilder("Inzulín (U)")
                .addColumn("snídaně").addColumn("rychlý").addSister("depotní").getParent()
                .addSister("oběd").addColumn("rychlý").getParent()
                .addSister("1. večeře").addColumn("rychlý").addSister("depotní").getParent()
                .addSister("přídavek");
    }

    @Override
    protected String getValue(LocalDate date, int column) {
        if (isPump() && column == 4) {
            return "00:00-04:00 1,1U 04:00-08:00 1,2U 08:00-15:00 1,5 U 15:00-20:00 1,1 U 20:00-23:00 1,0 U 23:00-24:00 1,1 U";
        }
        return String.valueOf(date.getDayOfWeek());
    }

    @Override
    public PdfPCell getData(boolean blackWhite, Font font, int column, LocalDate date) {
        PdfPCell cell = super.getData(blackWhite, font, column, date);
        if ((isPump() && column == 3) || (!isPump() && column == 5)) {
            Font pomfont = PDFGenerator.getFont();
            pomfont.setSize(font.getSize() > 6 ? font.getSize() - 4 : 2);
            Chunk chunk = new Chunk("12:30");
            chunk.setTextRise(5).setFont(pomfont);
            cell.getPhrase().add(chunk);
        }
        return cell;
    }

    @Override
    protected BaseColor getBackGroundColor(LocalDate date, int col, boolean onlyBlackWhite) {
        if (isPump()) {
            return col == 4 || col == 5 ? BaseColor.LIGHT_GRAY : super.getBackGroundColor(date, col, onlyBlackWhite);
        }
        return col == 1 || col == 4 ? BaseColor.LIGHT_GRAY : super.getBackGroundColor(date, col, onlyBlackWhite);
    }

    private boolean isPump() {
        return patient != null && patient.isPumpUsage();
    }

}
