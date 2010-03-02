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

import com.google.common.base.Function;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import java.text.NumberFormat;
import org.diabetesdiary.diary.api.DiaryRepository;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.utils.MyLookup;
import org.diabetesdiary.print.pdf.GeneratorHelper.HeaderBuilder;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 *
 * @author Jirka Majer
 */
public abstract class AbstractPdfSubTable {
    protected DateTime from;
    protected DateTime to;
    protected final DiaryRepository diary;
    protected Patient patient;
    private boolean visible = true;
    protected boolean dirty = true;
    protected NumberFormat format = NumberFormat.getInstance();

    public AbstractPdfSubTable(DateTime from, DateTime to, Patient patient) {
        this.from = from;
        this.to = to;
        this.patient = patient;
        diary = MyLookup.getDiaryRepo();
        format.setMaximumFractionDigits(1);
    }

    protected final Function<Number, String> FORMAT_FUNCTION = new Function<Number, String>() {
        @Override
        public String apply(Number from) {
            return format.format(from);
        }
    };

    public abstract int getColumnCount();

    /**
     * It is called before  getData only if data is dirty
     */
    protected void loadData() {
    }

    public float getWidth(int column) {
        return 1;
    }

    public abstract HeaderBuilder getHeader();

    public void dirtyData() {
        dirty = true;
    }

    public final PdfPCell getData(boolean blackWhite, Font font, int column, LocalDate date) {
        if (dirty) {
            loadData();
            dirty = false;
        }
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(getBackGroundColor(date, column, blackWhite));
        cell.setBorder(Rectangle.BOX);
        cell.setBorderWidth(1);
        cell.setPhrase(patient == null ? new Phrase() : getPhrase(font, column, date));
        return cell;
    }

    protected Phrase getPhrase(Font font, int column, LocalDate date) {
        return new Phrase(getValue(date, column), font);
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

    public void setPatient(Patient patient) {
        dirtyData();
        this.patient = patient;
    }

    public void setFrom(DateTime from) {
        dirtyData();
        this.from = from;
    }

    public void setTo(DateTime to) {
        dirtyData();
        this.to = to;
    }
}
