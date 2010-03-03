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
import java.util.List;
import java.util.Map;
import org.diabetesdiary.commons.utils.StringUtils;
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
public class InvestTable extends AbstractPdfSubTable {

    private Map<Tuple2<LocalDate, Integer>, List<RecordInvest>> data;

    public InvestTable(DateTime from, DateTime to, Patient patient) {
        super(from, to, patient);
    }

    @Override
    public int getColumnCount() {
        return patient != null && !patient.isMale() ? 3 : 2;
    }

    @Override
    public float getWidth(int column) {
        return 0.5f;
    }

    @Override
    public HeaderBuilder getHeader() {
        if (patient != null && !patient.isMale()) {
        return (HeaderBuilder) GeneratorHelper.headerBuilder("Ostatní")
                .addColumn("Moč")
                .addColumn("Cukr")
                .addSister("Ketolátky").getParent()
                .addSister("Menstruace");
        }
        return (HeaderBuilder) GeneratorHelper.headerBuilder("Moč")
                .addColumn("Cukr")
                .addSister("Ketolátky");
    }

    @Override
    protected String getValue(LocalDate date, int col) {
        List<Double> values = getRecords(date, col);
        return values == null ? "" : StringUtils.collectionToDelimitedString(Lists.transform(values, FORMAT_FUNCTION), "; ");
    }

    private List<Double> getRecords(LocalDate date, final int column) {
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
            for (RecordInvest rec : patient.getRecordInvests(from, to, WKInvest.ACETON, WKInvest.URINE_SUGAR, WKInvest.MENSES)) {
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
        switch (rec.getInvest().getWKInvest()) {
            case URINE_SUGAR:
                return 0;
            case ACETON:
                return 1;
            case MENSES:
                return 2;
            default:
                throw new IllegalArgumentException();
        }
    }

}
