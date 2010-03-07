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
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.commons.utils.MathUtils;
import org.diabetesdiary.diary.domain.FoodUnit;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.domain.RecordFood;
import org.diabetesdiary.print.pdf.GeneratorHelper;
import org.diabetesdiary.print.pdf.GeneratorHelper.HeaderBuilder;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.openide.util.NbBundle;

/**
 *
 * @author Jirka Majer
 */
public class FoodTable extends AbstractPdfSubTable {

    private Map<Tuple2<LocalDate, Integer>, List<RecordFood>> data;
    private FoodUnit sachUnit;

    public FoodTable(DateTime from, DateTime to, Patient patient) {
        super(from, to, patient);
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public HeaderBuilder getHeader() {
        FoodUnit unit = diary.getSacharidUnit(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT));
        return (HeaderBuilder) GeneratorHelper.headerBuilder(String.format(NbBundle.getMessage(FoodTable.class, "JÍDLO (~%.0F G SACH.)"), unit.getKoef()))
                .addColumn(NbBundle.getMessage(FoodTable.class, "SNÍDANĚ"))
                .addSister(NbBundle.getMessage(FoodTable.class, "SVAČINA"))
                .addSister(NbBundle.getMessage(FoodTable.class, "OBĚD"))
                .addSister(NbBundle.getMessage(FoodTable.class, "SVAČINA"))
                .addSister(NbBundle.getMessage(FoodTable.class, "1. VEČEŘE"))
                .addSister(NbBundle.getMessage(FoodTable.class, "2. VEČEŘE"));
    }

    @Override
    protected String getValue(LocalDate date, int col) {
        List<Double> values = getRecords(date, col);        
        return values == null ? "" : FORMAT_FUNCTION.apply(MathUtils.sum(values));
    }

   private List<Double> getRecords(LocalDate date, final int column) {
        List<RecordFood> list = data.get(new Tuple2<LocalDate, Integer>(date, column));
        return list == null ? null : Lists.transform(list, new Function<RecordFood, Double>() {
            @Override
            public Double apply(RecordFood from) {
                return from.getSachUnits(sachUnit);
            }
        });
    }

    @Override
    protected void loadData() {
        data = Maps.newHashMap();
        sachUnit = diary.getSacharidUnit(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT));
        if (patient != null) {
            for (RecordFood rec : patient.getRecordFoods(from, to)) {
                Tuple2<LocalDate, Integer> key = new Tuple2<LocalDate, Integer>(rec.getDatetime().toLocalDate(), getColumn(rec));
                List<RecordFood> list = data.get(key);
                if (list == null) {
                    list = Lists.newArrayList();
                    data.put(key, list);
                }
                list.add(rec);
            }
        }
    }

    private int getColumn(RecordFood rec) {
        return rec.getSeason().ordinal();
    }

}
