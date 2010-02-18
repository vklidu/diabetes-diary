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
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.diabetesdiary.diary.api.DiaryRepository;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.utils.MyLookup;
import org.diabetesdiary.print.pdf.GeneratorHelper.HeaderBuilder;
import org.joda.time.LocalDate;

/**
 *
 * @author Jirka Majer
 */
public abstract class AbstractPdfSubTable {

    protected final LocalDate from;
    protected final LocalDate to;
    protected final DiaryRepository diary;
    protected final Patient patient;
    private boolean visible = true;

    public AbstractPdfSubTable(LocalDate from, LocalDate to, Patient patient) {
        this.from = from;
        this.to = to;
        this.patient = patient;
        diary = MyLookup.getDiaryRepo();
    }

    public abstract int getColumnCount();

    public abstract float getWidth();

    protected abstract HeaderBuilder getHeader();

    public PdfPTable getHeader(boolean blackWhite, Font font) {
        return getHeader().setFont(font).build();
    }

    public PdfPTable getData(boolean blackWhite, Font font) {
        PdfPTable table = new PdfPTable(getColumnCount());
        LocalDate pom = from;
        while (!pom.isAfter(to)) {
            for (int column = 0; column < getColumnCount(); column++) {
                PdfPCell cell = new PdfPCell();
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

    protected BaseColor getBackGroundColor(LocalDate date, int col, boolean onlyBlackWhite) {
        return BaseColor.WHITE;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
