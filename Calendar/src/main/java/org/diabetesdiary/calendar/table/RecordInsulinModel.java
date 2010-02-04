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
package org.diabetesdiary.calendar.table;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import org.diabetesdiary.calendar.ColumnGroup;
import org.diabetesdiary.diary.utils.MyLookup;
import org.diabetesdiary.diary.service.db.InsulinSeason;
import org.diabetesdiary.diary.service.db.RecordInsulinDO;
import org.diabetesdiary.datamodel.pojo.RecordInsulinPK;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class RecordInsulinModel implements TableSubModel, Comparable<TableSubModel> {

    private RecordInsulinDO dataIns[][][];
    private int baseIndex;
    private Calendar month;

    /** Creates a new instance of RecordInsulinModel */
    public RecordInsulinModel(int baseIndex, Calendar month) {
        this.baseIndex = baseIndex;
        this.month = month;
    }

    public int getColumnCount() {
        return 6;
    }

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
        RecordInsulinDO rec = new RecordInsulinDO();
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
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value instanceof Double) {
            RecordInsulinDO rec = new RecordInsulinDO();
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

            Double units = (Double) value;
            rec.setAmount(units);
            rec.setSeason(seas.name());
            MyLookup.getDiaryRepo().addRecord(rec);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(rec.getId().getDate().getTime());
            dataIns[cal.get(Calendar.DAY_OF_MONTH) - 1][columnIndex][0] = rec;
        }
    }

    public Class<?> getColumnClass(int columnIndex) {
        return RecordInsulinDO.class;
    }

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

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public void setData(Collection<?> data) {
        dataIns = new RecordInsulinDO[month.getActualMaximum(Calendar.DAY_OF_MONTH)][6][1];
        Calendar cal = Calendar.getInstance();
        for (Object record : data) {
            if (record instanceof RecordInsulinDO) {
                RecordInsulinDO rec = (RecordInsulinDO) record;
                cal.setTimeInMillis(rec.getId().getDate().getTime());
                int col;
                boolean basal = rec.isBasal();
                switch (InsulinSeason.valueOf(rec.getSeason()).ordinal()) {
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
                    RecordInsulinDO[] pom = dataIns[cal.get(Calendar.DAY_OF_MONTH) - 1][col];
                    dataIns[cal.get(Calendar.DAY_OF_MONTH) - 1][col] = new RecordInsulinDO[pom.length + 1];
                    for (int i = 0; i < pom.length; i++) {
                        dataIns[cal.get(Calendar.DAY_OF_MONTH) - 1][col][i] = pom[i];
                    }
                    dataIns[cal.get(Calendar.DAY_OF_MONTH) - 1][col][pom.length] = rec;
                }
            }
        }
    }

    public void setBaseIndex(int baseIndex) {
        this.baseIndex = baseIndex;
    }

    public int getBaseIndex() {
        return baseIndex;
    }

    public int compareTo(TableSubModel o) {
        return Integer.valueOf(baseIndex).compareTo(o.getBaseIndex());
    }

    private Date getClickCellDate(int rowIndex, int columnIndex) {
        Date date = month.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(month.getTimeInMillis());
        cal.set(Calendar.DAY_OF_MONTH, rowIndex + 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        switch (columnIndex) {
            case 0:
                cal.set(Calendar.HOUR_OF_DAY, 8);
                break;
            case 1:
                cal.set(Calendar.HOUR_OF_DAY, 8);
                break;
            case 2:
                cal.set(Calendar.HOUR_OF_DAY, 12);
                break;
            case 3:
                cal.set(Calendar.HOUR_OF_DAY, 17);
                break;
            case 4:
                cal.set(Calendar.HOUR_OF_DAY, 17);
                break;
            //@todo jak cip
            default:
                cal.set(Calendar.HOUR_OF_DAY, 20);
                break;
        }
        return cal.getTime();
    }

    public int getRowCount() {
        throw new IllegalStateException("Don't use it.");
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }
}
