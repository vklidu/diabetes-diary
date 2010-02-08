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
package org.diabetesdiary.calendar.table.model;

import java.util.Calendar;
import java.util.Collection;
import javax.swing.table.TableColumnModel;
import org.diabetesdiary.calendar.table.header.ColumnGroup;
import org.diabetesdiary.diary.domain.InsulinSeason;
import org.diabetesdiary.diary.domain.RecordInsulin;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class RecordInsulinModel extends AbstractRecordSubModel<RecordInsulin> {

    private RecordInsulin dataIns[][][];

    /** Creates a new instance of RecordInsulinModel */
    public RecordInsulinModel(int baseIndex, DateTime month) {
        super(baseIndex, month);
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public ColumnGroup getColumnHeader(TableColumnModel cm) {
        ColumnGroup gInsulin = new ColumnGroup(NbBundle.getMessage(RecordInsulinModel.class, "Column.insulin"));
        ColumnGroup gInsBr = new ColumnGroup(NbBundle.getMessage(RecordInsulinModel.class, "Column.breakfest"));
        ColumnGroup gInsDin = new ColumnGroup(NbBundle.getMessage(RecordInsulinModel.class, "Column.dinner"));
        ColumnGroup gInsLaun = new ColumnGroup(NbBundle.getMessage(RecordInsulinModel.class, "Column.launch"));
        gInsulin.add(gInsBr);
        gInsulin.add(gInsDin);
        gInsulin.add(gInsLaun);

        gInsBr.add(cm.getColumn(baseIndex));
        gInsBr.add(cm.getColumn(baseIndex + 1));

        gInsDin.add(cm.getColumn(baseIndex + 2));

        gInsLaun.add(cm.getColumn(baseIndex + 3));
        gInsLaun.add(cm.getColumn(baseIndex + 4));

        gInsulin.add(cm.getColumn(baseIndex + 5));

        return gInsulin;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (dataIns != null && rowIndex > -1) {
            if (dataIns[rowIndex][columnIndex] != null && dataIns[rowIndex][columnIndex].length == 1) {
                return dataIns[rowIndex][columnIndex][0];
            } else {
                return dataIns[rowIndex][columnIndex];
            }
        }
        return null;
    }

    /**
     * @para columnIndex
     * 0 - breakfest bolus
     * 1 - breakfest basal
     * 2 - dinner bolus
     * 3 - launch bolus
     * 4 - launch basal
     * 5 - bonus bolus
     */
    public Object getNewRecordValueAt(int rowIndex, int columnIndex) {
        /*
        RecordInsulin rec = new RecordInsulin();
        RecordInsulinPK pk = new RecordInsulinPK();
        pk.setIdPatient(MyLookup.getDiaryRepo().getCurrentPatient().getIdPatient());
        boolean bolus = true;
        InsulinSeason seas;
        switch (columnIndex) {
            case 0:
                seas = InsulinSeason.B;
                break;
            case 1:
                bolus = false;
                seas = InsulinSeason.B;
                break;
            case 2:
                seas = InsulinSeason.D;
                break;
            case 3:
                seas = InsulinSeason.L;
                break;
            case 4:
                bolus = false;
                seas = InsulinSeason.L;
                break;
            case 5:
                seas = InsulinSeason.ADD;
                break;
            default:
                seas = InsulinSeason.ADD;
                break;
        }
        if (bolus) {
            pk.setIdInsulin(MyLookup.getDiaryRepo().getCurrentPatient().getBolusInsulin().getId());
            rec.setInsulin(MyLookup.getDiaryRepo().getCurrentPatient().getBolusInsulin());
        } else {
            pk.setIdInsulin(MyLookup.getDiaryRepo().getCurrentPatient().getBasalInsulin().getId());
            rec.setInsulin(MyLookup.getDiaryRepo().getCurrentPatient().getBasalInsulin());
        }
        pk.setDate(getClickCellDate(rowIndex, columnIndex));
        pk.setBasal(!bolus);
        rec.setId(pk);
        rec.setPump(false);
        rec.setAmount(null);
        rec.setSeason(seas.name());
        return rec;
         *
         */
        return null;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value instanceof Double) {
            boolean bolus = true;
            InsulinSeason seas;
            switch (columnIndex) {
                case 0:
                    seas = InsulinSeason.B;
                    break;
                case 1:
                    bolus = false;
                    seas = InsulinSeason.B;
                    break;
                case 2:
                    seas = InsulinSeason.D;
                    break;
                case 3:
                    seas = InsulinSeason.L;
                    break;
                case 4:
                    bolus = false;
                    seas = InsulinSeason.L;
                    break;
                case 5:
                    seas = InsulinSeason.ADD;
                    break;
                default:
                    seas = InsulinSeason.ADD;
                    break;
            }

            RecordInsulin rec = MyLookup.getCurrentPatient().addRecordInsulin(
                    getClickCellDate(rowIndex, columnIndex),
                    !bolus,
                    (Double)value,
                    seas,
                    null);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(rec.getDatetime().getMillis());
            dataIns[cal.get(Calendar.DAY_OF_MONTH) - 1][columnIndex][0] = rec;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return RecordInsulin.class;
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return NbBundle.getMessage(RecordInsulinModel.class, "Column.fastInsulin");
            case 1:
                return NbBundle.getMessage(RecordInsulinModel.class, "Column.slowInsulin");
            case 2:
                return NbBundle.getMessage(RecordInsulinModel.class, "Column.fastInsulin");
            case 3:
                return NbBundle.getMessage(RecordInsulinModel.class, "Column.fastInsulin");
            case 4:
                return NbBundle.getMessage(RecordInsulinModel.class, "Column.slowInsulin");
            case 5:
                return NbBundle.getMessage(RecordInsulinModel.class, "Column.AddInsulin");
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void setData(Collection<RecordInsulin> data) {
        dataIns = new RecordInsulin[dateTime.dayOfMonth().withMaximumValue().getDayOfMonth()][6][1];
        Calendar cal = Calendar.getInstance();
        for (Object record : data) {
            if (record instanceof RecordInsulin) {
                RecordInsulin rec = (RecordInsulin) record;
                cal.setTimeInMillis(rec.getDatetime().getMillis());
                int col;
                boolean basal = rec.isBasal();
                switch (rec.getSeason().ordinal()) {
                    case 0:
                        col = 0 + (basal ? 1 : 0);
                        break;
                    case 1:
                        col = 2;
                        break;
                    case 2:
                        col = 3 + (basal ? 1 : 0);
                        break;
                    case 3:
                        col = 5;
                        break;
                    default:
                        col = 5;
                        break;
                }
                if (dataIns[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] == null) {
                    dataIns[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] = rec;
                } else {
                    RecordInsulin[] pom = dataIns[cal.get(Calendar.DAY_OF_MONTH) - 1][col];
                    dataIns[cal.get(Calendar.DAY_OF_MONTH) - 1][col] = new RecordInsulin[pom.length + 1];
                    for (int i = 0; i < pom.length; i++) {
                        dataIns[cal.get(Calendar.DAY_OF_MONTH) - 1][col][i] = pom[i];
                    }
                    dataIns[cal.get(Calendar.DAY_OF_MONTH) - 1][col][pom.length] = rec;
                }
            }
        }
    }

    private DateTime getClickCellDate(int rowIndex, int columnIndex) {
        int hourOfDay;
        switch (columnIndex) {
            case 0:
                hourOfDay = 8;
                break;
            case 1:
                hourOfDay = 8;
                break;
            case 2:
                hourOfDay = 12;
                break;
            case 3:
                hourOfDay = 17;
                break;
            case 4:
                hourOfDay = 17;
                break;
            //@todo jak cip
            default:
                hourOfDay = 20;
                break;
        }
        return dateTime.withDayOfMonth(rowIndex + 1).withTime(hourOfDay, 0, 0, 0);
    }

}
