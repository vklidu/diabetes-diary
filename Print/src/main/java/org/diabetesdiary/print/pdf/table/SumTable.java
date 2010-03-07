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

import java.util.List;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.diary.domain.FoodUnit;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.domain.RecordFood;
import org.diabetesdiary.diary.domain.RecordInsulin;
import org.diabetesdiary.print.pdf.GeneratorHelper;
import org.diabetesdiary.print.pdf.GeneratorHelper.HeaderBuilder;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 *
 * @author Jirka Majer
 */
public class SumTable extends AbstractPdfSubTable {

    private List<RecordInsulin> insulines;
    private List<RecordFood> foods;
    private FoodUnit sachUnit;

    public SumTable(DateTime from, DateTime to, Patient patient) {
        super(from, to, patient);
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public HeaderBuilder getHeader() {
        return (HeaderBuilder) GeneratorHelper.headerBuilder("Celkem")
                .addColumn("Inzulín")
                .addSister("Jídlo")
                .addSister("bolus/sach.")
                .addSister("ins./kg");
    }

    @Override
    protected void loadData() {
        sachUnit = diary.getSacharidUnit(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT));
        insulines = patient.getRecordInsulins(from, to);
        foods = patient.getRecordFoods(from, to);
    }

    @Override
    protected String getValue(LocalDate date, int col) {
        DateTime rowDateFrom = date.toDateTimeAtStartOfDay();
        DateTime rowDateTo = rowDateFrom.plusDays(1);
        switch (col) {
            //suma insulin
            case 0:
                double sumaInsulines = 0;
                for (RecordInsulin rec : insulines) {
                    if (!rec.getDatetime().isBefore(rowDateFrom) && !rec.getDatetime().isAfter(rowDateTo)) {
                        sumaInsulines += rec.getAmount();
                    }
                }
                return format.format(sumaInsulines);
            //suma food units
            case 1:
                double sumaFoodUnits = 0;
                for (RecordFood rec : foods) {
                    if (!rec.getDatetime().isBefore(rowDateFrom) && !rec.getDatetime().isAfter(rowDateTo)) {
                        sumaFoodUnits += rec.getSachUnits(sachUnit);
                    }
                }
                return format.format(sumaFoodUnits);
            //bolus/food
            case 2:
                double sumFoodUnits = 0;
                for (RecordFood rec : foods) {
                    if (!rec.getDatetime().isBefore(rowDateFrom) && !rec.getDatetime().isAfter(rowDateTo)) {
                        sumFoodUnits += rec.getSachUnits(sachUnit);
                    }
                }
                double sumBolus = 0;
                for (RecordInsulin rec : insulines) {
                    if (!rec.isBasal() && !rec.getDatetime().isBefore(rowDateFrom) && !rec.getDatetime().isAfter(rowDateTo)) {
                        sumBolus += rec.getAmount();
                    }
                }

                return sumFoodUnits > 0 && sumBolus > 0 ? format.format(sumBolus / sumFoodUnits) : null;
            //ins./kg
            case 3:
                double sumaInsulin = 0;
                for (RecordInsulin rec : insulines) {
                    if (!rec.getDatetime().isBefore(rowDateFrom) && !rec.getDatetime().isAfter(rowDateTo)) {
                        sumaInsulin += rec.getAmount();
                    }
                }
                Double weight = patient.getWeightBefore(rowDateTo);
                return weight != null && weight > 0 && sumaInsulin > 0 ? format.format(sumaInsulin / weight) : null;
            default:
                throw new IllegalStateException();
        }
    }

}
