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

import org.diabetesdiary.commons.utils.tuples.Tuple2;
import org.diabetesdiary.commons.utils.tuples.Tuples;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.diabetesdiary.commons.utils.StringUtils;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.domain.RecordInsulin;
import org.diabetesdiary.print.pdf.GeneratorHelper;
import org.diabetesdiary.print.pdf.GeneratorHelper.HeaderBuilder;
import org.diabetesdiary.print.pdf.PDFGenerator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openide.util.NbBundle;

/**
 *
 * @author Jirka Majer
 */
public class InsulinTable extends AbstractPdfSubTable {

    private Map<Tuple2<LocalDate, Integer>, List<RecordInsulin>> data;

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
            return 1.2f;
        }
        return 0.7f;
    }

    @Override
    public HeaderBuilder getHeader() {
        if (isPump()) {
            return (HeaderBuilder) GeneratorHelper.headerBuilder(NbBundle.getMessage(InsulinTable.class, "INZULÍN (U)")).addColumn(NbBundle.getMessage(InsulinTable.class, "BOLUS")).addColumn(NbBundle.getMessage(InsulinTable.class, "SNÍDANĚ")).addSister(NbBundle.getMessage(InsulinTable.class, "OBĚD")).addSister(NbBundle.getMessage(InsulinTable.class, "VEČEŘE")).addSister(NbBundle.getMessage(InsulinTable.class, "PŘÍDAVEK")).getParent().addSister(NbBundle.getMessage(InsulinTable.class, "BAZÁL"));
        }
        return (HeaderBuilder) GeneratorHelper.headerBuilder(NbBundle.getMessage(InsulinTable.class, "INZULÍN (U)")).addColumn(NbBundle.getMessage(InsulinTable.class, "SNÍDANĚ")).addColumn(NbBundle.getMessage(InsulinTable.class, "RYCHLÝ")).addSister(NbBundle.getMessage(InsulinTable.class, "DEPOTNÍ")).getParent().addSister(NbBundle.getMessage(InsulinTable.class, "OBĚD")).addColumn(NbBundle.getMessage(InsulinTable.class, "RYCHLÝ")).getParent().addSister(NbBundle.getMessage(InsulinTable.class, "1. VEČEŘE")).addColumn(NbBundle.getMessage(InsulinTable.class, "RYCHLÝ")).addSister(NbBundle.getMessage(InsulinTable.class, "DEPOTNÍ")).getParent().addSister(NbBundle.getMessage(InsulinTable.class, "PŘÍDAVEK"));
    }

    @Override
    protected String getValue(LocalDate date, int column) {
        throw new IllegalStateException("isn't used because is overriedn getPhrase");
    }

    @Override
    protected Phrase getPhrase(Font font, int column, LocalDate date) {
        List<RecordInsulin> recs = data.get(Tuples.of(date, column));
        if (recs == null || recs.isEmpty()) {
            return new Phrase();
        }

        //addition intake
        if ((isPump() && column == 3) || (!isPump() && column == 5)) {
            Phrase phrase = new Phrase();
            Font pomfont = PDFGenerator.getFont();
            pomfont.setSize(font.getSize() > 6 ? font.getSize() - 4 : 2);
            boolean first = true;
            for (RecordInsulin rec : recs) {
                if (!first) {
                    Chunk chunk = new Chunk(" ");
                    chunk.setFont(font);
                    phrase.add(chunk);
                    first = false;
                }
                Chunk chunk = new Chunk(format.format(rec.getAmount()));
                chunk.setFont(font);
                phrase.add(chunk);

                chunk = new Chunk(DateTimeFormat.forPattern("H:mm").print(rec.getDatetime()));
                chunk.setTextRise(5).setFont(pomfont);
                phrase.add(chunk);
            }
            return phrase;
        }

        if (isPump() && column == 4) {
            return new Phrase(formatBasales(recs), font);
        }

        return new Phrase(StringUtils.collectionToDelimitedString(Lists.transform(recs, Functions.compose(FORMAT_FUNCTION, VALUE_FUNC)), ";"), font);
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

    @Override
    protected void loadData() {
        data = Maps.newHashMap();
        if (patient != null) {
            for (RecordInsulin rec : patient.getRecordInsulins(from, to)) {
                Tuple2<LocalDate, Integer> key = new Tuple2<LocalDate, Integer>(rec.getDatetime().toLocalDate(), getColumn(rec));
                List<RecordInsulin> list = data.get(key);
                if (list == null) {
                    list = Lists.newArrayList();
                    data.put(key, list);
                }
                list.add(rec);
            }
        }
    }

    private int getColumn(RecordInsulin rec) {
        if (isPump()) {
            return rec.getSeason().ordinal();
        } else {
            switch (rec.getSeason()) {
                case B:
                    return 0 + (rec.isBasal() ? 1 : 0);
                case D:
                    return 2;
                case L:
                    return 3 + (rec.isBasal() ? 1 : 0);
                case ADD:
                    return 5;
                default:
                    return 5;
            }
        }
    }

    private String formatBasales(List<RecordInsulin> recs) {
        StringBuffer result = new StringBuffer();
        DateTimeFormatter dateFormat = DateTimeFormat.shortTime();
        RecordInsulin lastRec = null;
        Queue<RecordInsulin> queue = new ArrayDeque<RecordInsulin>();
        for (RecordInsulin rec : recs) {
            if (queue.isEmpty()) {
                queue.add(rec);
            } else if (!lastRec.getDatetime().plusHours(1).isEqual(rec.getDatetime()) || !lastRec.getAmount().equals(rec.getAmount())) {
                result.append(dateFormat.print(queue.peek().getDatetime()));
                result.append("-");
                result.append(dateFormat.print(lastRec.getDatetime().plusHours(1)));
                result.append(" ").append(queue.peek().getAmount()).append("U ");
                queue.poll();
                queue.add(rec);
            }
            lastRec = rec;
        }
        if (!queue.isEmpty()) {
            result.append(dateFormat.print(queue.peek().getDatetime()));
            result.append("-");
            result.append(dateFormat.print(lastRec.getDatetime().plusHours(1)));
            result.append(" ").append(queue.peek().getAmount()).append("U\n");

        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }
    
    private static final Function<RecordInsulin, Double> VALUE_FUNC = new Function<RecordInsulin, Double>() {

        @Override
        public Double apply(RecordInsulin from) {
            return from.getAmount();
        }
    };
}
