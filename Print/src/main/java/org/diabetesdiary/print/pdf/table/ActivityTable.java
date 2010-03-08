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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.diabetesdiary.diary.domain.Energy;
import org.diabetesdiary.diary.api.DiaryException;
import org.diabetesdiary.diary.api.UnknownWeightException;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.domain.RecordActivity;
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
public class ActivityTable extends AbstractPdfSubTable {

    private Map<LocalDate, List<RecordActivity>> data;
    private List<RecordFood> foods;

    public ActivityTable(DateTime from, DateTime to, Patient patient) {
        super(from, to, patient);
        format.setMaximumFractionDigits(0);
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public float getWidth(int column) {
        return 1.5f;
    }

    @Override
    public HeaderBuilder getHeader() {
        return (HeaderBuilder) GeneratorHelper.headerBuilder(NbBundle.getMessage(ActivityTable.class, "ENERGIE (KJ)"))
                .addColumn(NbBundle.getMessage(ActivityTable.class, "VÝDEJ"))
                .addColumn(NbBundle.getMessage(ActivityTable.class, "AKTIVITY"))
                .addSister(NbBundle.getMessage(ActivityTable.class, "METABOLISMUS"))
                .getParent().addSister(NbBundle.getMessage(ActivityTable.class, "PŘÍJEM"))
                .addSister(NbBundle.getMessage(ActivityTable.class, "BILANCE"));
    }

    @Override
    protected String getValue(LocalDate date, int col) {
        switch (col) {
            case 0:
                try {
                    return FORMAT_FUNCTION.apply(getEnergyActivity(date).getValue(Energy.Unit.kJ));
                } catch (UnknownWeightException ex) {
                    return "";
                }
            case 1:
                try {
                    return FORMAT_FUNCTION.apply(patient.getMetabolismus(date).getValue(Energy.Unit.kJ));
                } catch (DiaryException ex) {
                    return "";
                }
            case 2:
                Energy en = getEnergyIncome(date);
                return en.getValue() > 0 ? FORMAT_FUNCTION.apply(en.getValue(Energy.Unit.kJ)) : "";
            case 3:
                try {
                    Energy sum = getEnergyIncome(date).minus(patient.getMetabolismus(date)).minus(getEnergyActivity(date));
                    return FORMAT_FUNCTION.apply(sum.getValue(Energy.Unit.kJ));
                } catch (DiaryException e) {
                    return "";
                }
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    protected void loadData() {
        data = Maps.newHashMap();
        if (patient != null) {
            for (RecordActivity rec : patient.getRecordActivities(from, to)) {
                LocalDate key = rec.getDatetime().toLocalDate();
                List<RecordActivity> list = data.get(key);
                if (list == null) {
                    list = Lists.newArrayList();
                    data.put(key, list);
                }
                list.add(rec);
            }
            foods = patient.getRecordFoods(from, to);
        }
    }

    private Energy getEnergyIncome(LocalDate date) {
        Energy energ = new Energy(Energy.Unit.kJ);
        DateTime rowDateFrom = date.toDateTimeAtStartOfDay();
        DateTime rowDateTo = date.toDateTimeAtStartOfDay().plusDays(1);
        for (RecordFood rec : foods) {
            if (!rec.getDatetime().isBefore(rowDateFrom) && rec.getDatetime().isBefore(rowDateTo)) {
                energ = energ.plus(rec.getEnergy());
            }
        }
        return energ;
    }

    private Energy getEnergyActivity(LocalDate date) throws UnknownWeightException {
        Energy energ = new Energy(Energy.Unit.kJ);
        if (data.get(date) == null) {
            return energ;
        }
        for (RecordActivity rec : data.get(date)) {
            energ = energ.plus(rec.getEnergy());
        }
        return energ;
    }
}
