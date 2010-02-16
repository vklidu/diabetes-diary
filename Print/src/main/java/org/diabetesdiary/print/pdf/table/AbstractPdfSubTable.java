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

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import java.awt.Color;
import org.aspectj.apache.bcel.generic.TABLESWITCH;
import org.diabetesdiary.print.pdf.GeneratorHelper;
import org.diabetesdiary.print.pdf.GeneratorHelper.DataBuilder;
import org.diabetesdiary.print.pdf.PDFGenerator;
import org.joda.time.LocalDate;

/**
 *
 * @author Jirka Majer
 */
public abstract class AbstractPdfSubTable {

    protected final LocalDate from;
    protected final LocalDate to;

    public AbstractPdfSubTable(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public abstract int getColumnCount();

    public abstract float getWidth();

    public abstract PdfPTable getHeader();

    public PdfPTable getData(boolean blackWhite) {
        PdfPTable table = new PdfPTable(getColumnCount());
        LocalDate pom = from;
        while (!pom.isAfter(to)) {
            for (int column = 0; column < getColumnCount(); column++) {
                PdfPCell cell = new PdfPCell();
                Font font = PDFGenerator.DEJAVU;
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(getBackGroundColor(pom, column, blackWhite));
                cell.setBorder(Rectangle.BOX);
                cell.setBorderWidth(1);
                cell.setPhrase(new Phrase(getValue(pom, column), font));
                table.addCell(cell);
            }
            pom = pom.plusDays(1);
        }
        return table;
    }

    protected abstract String getValue(LocalDate date, int col);

    protected Color getBackGroundColor(LocalDate date, int col, boolean onlyBlackWhite) {
        return Color.WHITE;
    }
}
