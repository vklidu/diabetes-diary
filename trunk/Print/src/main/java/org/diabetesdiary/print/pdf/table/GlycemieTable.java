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
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itextpdf.text.BaseColor;
import java.util.List;
import java.util.Map;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.commons.utils.MathUtils;
import org.diabetesdiary.commons.utils.StringUtils;
import org.diabetesdiary.diary.domain.InvSeason;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.domain.RecordInvest;
import org.diabetesdiary.diary.domain.WKInvest;
import org.diabetesdiary.print.pdf.GeneratorHelper;
import org.diabetesdiary.print.pdf.GeneratorHelper.HeaderBuilder;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 *
 * @author Jirka Majer
 */
public class GlycemieTable extends AbstractPdfSubTable {

    private static BaseColor lowGlyColor = new BaseColor(55, 110, 200);
    private static BaseColor normalGlyColor = new BaseColor(10, 200, 100);
    private static BaseColor highGlyColor = new BaseColor(240, 40, 40);
    private Map<Tuple2<LocalDate, Integer>, List<RecordInvest>> data;

    public GlycemieTable(DateTime from, DateTime to, Patient patient) {
        super(from, to, patient);
    }

    @Override
    public int getColumnCount() {
        return 9;
    }

    @Override
    public HeaderBuilder getHeader() {
        return (HeaderBuilder) GeneratorHelper.headerBuilder("Glykémie (mmol/l)")
                .addColumn("v noci")
                .addSister("snídaně").addColumn("před").addSister("po").getParent()
                .addSister("oběd").addColumn("před").addSister("po").getParent()
                .addSister("1. večeře").addColumn("před").addSister("po").getParent()
                .addSister("před spaním")
                .addSister("v noci");
    }

    @Override
    protected String getValue(LocalDate date, int col) {
        List<Double> values = getGlycemies(date, col);
        return values == null ? "" : StringUtils.collectionToDelimitedString(Lists.transform(values, FORMAT_FUNCTION), "; ");
    }

    @Override
    protected BaseColor getBackGroundColor(LocalDate date, int col, boolean onlyBlackWhite) {
        List<Double> values = getGlycemies(date, col);
        if (onlyBlackWhite || values == null || values.size() == 0) {
            return super.getBackGroundColor(date, col, onlyBlackWhite);
        }
        double average = MathUtils.average(values, col);
        if (average < Double.valueOf(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_GLYKEMIE_LOW_NORMAL))) {
            return lowGlyColor;
        } else if (average <= Double.valueOf(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_GLYKEMIE_HIGH_NORMAL))) {
            return normalGlyColor;
        } else {
            return highGlyColor;
        }
    }

    private List<Double> getGlycemies(LocalDate date, final int column) {
        List<RecordInvest> list = data.get(new Tuple2<LocalDate, Integer>(date, column));
        return list == null ? null : Lists.transform(list, new Function<RecordInvest, Double>() {
            @Override
            public Double apply(RecordInvest from) {
                return from.getValue();
            }
        });
    }

    @Override
    protected void loadData() {
        data = Maps.newHashMap();
        if (patient != null) {
            for (RecordInvest rec : patient.getRecordInvests(from, to, WKInvest.GLYCEMIE)) {
                Tuple2<LocalDate, Integer> key = new Tuple2<LocalDate, Integer>(rec.getDatetime().toLocalDate(), getColumn(rec));
                List<RecordInvest> list = data.get(key);
                if (list == null) {
                    list = Lists.newArrayList();
                    data.put(key, list);
                }
                list.add(rec);
            }
        }
    }

    private int getColumn(RecordInvest rec) {
        if (rec.getSeason() == InvSeason.M) {
            return rec.getDatetime().getHourOfDay() < 12 ? 0 : 8;
        }
        return rec.getSeason().ordinal() + 1;
    }

}
