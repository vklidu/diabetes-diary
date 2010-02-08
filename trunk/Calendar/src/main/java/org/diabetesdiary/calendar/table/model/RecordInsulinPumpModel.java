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
import java.util.StringTokenizer;
import javax.swing.table.TableColumnModel;
import org.diabetesdiary.calendar.table.header.ColumnGroup;
import org.diabetesdiary.diary.domain.RecordInsulinPumpBasal;
import org.diabetesdiary.diary.domain.InsulinSeason;
import org.diabetesdiary.diary.domain.RecordInsulin;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class RecordInsulinPumpModel extends AbstractRecordSubModel<RecordInsulin> {

    private RecordInsulin dataIns[][][];
    private RecordInsulinPumpBasal dataBasal[][];

    /** Creates a new instance of RecordInsulinModel */
    public RecordInsulinPumpModel(int baseIndex, DateTime month) {
        super(baseIndex, month);
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public ColumnGroup getColumnHeader(TableColumnModel cm) {
        ColumnGroup gInsulin = new ColumnGroup(NbBundle.getMessage(RecordInsulinPumpModel.class, "Column.insulin"));
        ColumnGroup gBazal = new ColumnGroup(NbBundle.getMessage(RecordInsulinPumpModel.class, "Bazal_(v_desetinach_U)"));
        ColumnGroup gBolus = new ColumnGroup(NbBundle.getMessage(RecordInsulinPumpModel.class, "Bolus_(U)"));

        gInsulin.add(gBolus);
        gInsulin.add(gBazal);

        gBolus.add(cm.getColumn(baseIndex));
        gBolus.add(cm.getColumn(baseIndex + 1));
        gBolus.add(cm.getColumn(baseIndex + 2));
        gBolus.add(cm.getColumn(baseIndex + 3));

        cm.getColumn(baseIndex + 4).setPreferredWidth(200);
        cm.getColumn(baseIndex + 5).setPreferredWidth(200);
        gBazal.add(cm.getColumn(baseIndex + 4));
        gBazal.add(cm.getColumn(baseIndex + 5));

        return gInsulin;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex < 4) {
            if (dataIns != null && rowIndex > -1) {
                if (dataIns[rowIndex][columnIndex] != null && dataIns[rowIndex][columnIndex].length == 1) {
                    return dataIns[rowIndex][columnIndex][0];
                } else {
                    return dataIns[rowIndex][columnIndex];
                }
            }
        } else {
            if (dataBasal != null && rowIndex > -1) {
                return dataBasal[rowIndex][columnIndex - 4];
            }
        }
        return null;
    }

    public Object getNewRecordValueAt(int rowIndex, int columnIndex) {
        /*
        RecordInsulin rec = new RecordInsulin();
        RecordInsulinPK pk = new RecordInsulinPK();
        pk.setIdPatient(MyLookup.getDiaryRepo().getCurrentPatient().getIdPatient());

        InsulinSeason seas;
        switch (columnIndex) {
        case 0:
        seas = InsulinSeason.B;
        break;
        case 1:
        seas = InsulinSeason.D;
        break;
        case 2:
        seas = InsulinSeason.L;
        break;
        case 3:
        seas = InsulinSeason.ADD;
        break;
        default:
        seas = InsulinSeason.ADD;
        break;
        }
        pk.setIdInsulin(MyLookup.getDiaryRepo().getCurrentPatient().getBasalInsulin().getId());
        rec.setInsulin(MyLookup.getDiaryRepo().getCurrentPatient().getBasalInsulin());
        pk.setDate(getClickCellDate(rowIndex, columnIndex));
        rec.setId(pk);
        rec.setAmount(null);
        rec.setSeason(seas.name());
        return rec;
         *
         */
        return null;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value instanceof Double && columnIndex < 4) {
            InsulinSeason seas;
            switch (columnIndex) {
                case 0:
                    seas = InsulinSeason.B;
                    break;
                case 1:
                    seas = InsulinSeason.D;
                    break;
                case 2:
                    seas = InsulinSeason.L;
                    break;
                case 3:
                    seas = InsulinSeason.ADD;
                    break;
                default:
                    seas = InsulinSeason.ADD;
                    break;
            }
            RecordInsulin rec = MyLookup.getCurrentPatient().addRecordInsulin(
                    getClickCellDate(rowIndex, columnIndex),
                    false,
                    (Double) value,
                    seas,
                    null);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(rec.getDatetime().getMillis());
            dataIns[cal.get(Calendar.DAY_OF_MONTH) - 1][columnIndex][0] = rec;
        } else if (value instanceof String && columnIndex > 3) {
            StringTokenizer tok = new StringTokenizer(value.toString(), "-");
            int i = 0;
            while (tok.hasMoreTokens()) {
                String val = tok.nextToken().trim();
                double amount;
                if (val != null && val.length() > 0 && Character.isDigit(val.charAt(0))) {
                    amount = Double.valueOf(val) / 10;
                    if (amount > 0) {
                        RecordInsulin rec = MyLookup.getCurrentPatient().addRecordInsulin(
                                dateTime.withHourOfDay((columnIndex - 4) * 12 + i),
                                true,
                                amount,
                                InsulinSeason.BASAL,
                                null);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(getClickCellDate(rowIndex, columnIndex).getMillis());
                        RecordInsulinPumpBasal basal = dataBasal[cal.get(Calendar.DAY_OF_MONTH) - 1][columnIndex - 4];
                        if (basal == null) {
                            basal = new RecordInsulinPumpBasal();
                            dataBasal[cal.get(Calendar.DAY_OF_MONTH) - 1][columnIndex - 4] = basal;
                        }
                        basal.getData()[i] = rec;

                    }
                }
                i++;
            }
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex < 4) {
            return RecordInsulin.class;
        }
        return RecordInsulinPumpBasal.class;
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return NbBundle.getMessage(RecordInsulinPumpModel.class, "Column.breakfest");
            case 1:
                return NbBundle.getMessage(RecordInsulinPumpModel.class, "Column.dinner");
            case 2:
                return NbBundle.getMessage(RecordInsulinPumpModel.class, "Column.launch");
            case 3:
                return NbBundle.getMessage(RecordInsulinPumpModel.class, "Column.AddInsulin");
            case 4:
                return "00:00 - 12:00";
            case 5:
                return "12:00 - 24:00";
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
        dataIns = new RecordInsulin[31][6][1];
        dataBasal = new RecordInsulinPumpBasal[31][2];
        for (RecordInsulin rec : data) {
            Calendar month = Calendar.getInstance();
            month.setTimeInMillis(rec.getDatetime().getMillis());
            if (!rec.isBasal()) {
                int col = rec.getSeason().ordinal();
                if (dataIns[month.get(Calendar.DAY_OF_MONTH) - 1][col][0] == null) {
                    dataIns[month.get(Calendar.DAY_OF_MONTH) - 1][col][0] = rec;
                } else {
                    RecordInsulin[] pom = dataIns[month.get(Calendar.DAY_OF_MONTH) - 1][col];
                    dataIns[month.get(Calendar.DAY_OF_MONTH) - 1][col] = new RecordInsulin[pom.length + 1];
                    for (int i = 0; i < pom.length; i++) {
                        dataIns[month.get(Calendar.DAY_OF_MONTH) - 1][col][i] = pom[i];
                    }
                    dataIns[month.get(Calendar.DAY_OF_MONTH) - 1][col][pom.length] = rec;
                }
            } else {
                if (month.get(Calendar.HOUR_OF_DAY) < 12) {
                    RecordInsulinPumpBasal dataDay = dataBasal[month.get(Calendar.DAY_OF_MONTH) - 1][0];
                    if (dataDay == null) {
                        dataDay = new RecordInsulinPumpBasal();
                        dataDay.getData()[month.get(Calendar.HOUR_OF_DAY)] = rec;
                        dataBasal[month.get(Calendar.DAY_OF_MONTH) - 1][0] = dataDay;
                    } else {
                        dataDay.getData()[month.get(Calendar.HOUR_OF_DAY)] = rec;
                    }
                } else {
                    RecordInsulinPumpBasal dataDay = dataBasal[month.get(Calendar.DAY_OF_MONTH) - 1][1];
                    if (dataDay == null) {
                        dataDay = new RecordInsulinPumpBasal();
                        dataDay.getData()[month.get(Calendar.HOUR_OF_DAY) - 12] = rec;
                        dataBasal[month.get(Calendar.DAY_OF_MONTH) - 1][1] = dataDay;
                    } else {
                        dataDay.getData()[month.get(Calendar.HOUR_OF_DAY) - 12] = rec;
                    }
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
                hourOfDay = 12;
                break;
            case 2:
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
