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
import org.diabetesdiary.diary.domain.RecordInsulin;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class RecordInsulinPumpModel implements TableSubModel, Comparable<TableSubModel> {

    private RecordInsulin dataIns[][][];
    private RecordInsulinPumpBasal dataBasal[][];
    private int baseIndex;
    private Calendar month;

    /** Creates a new instance of RecordInsulinModel */
    public RecordInsulinPumpModel(int baseIndex, Calendar month) {
        this.baseIndex = baseIndex;
        this.month = month;
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

    @Override
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
            /**
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
            pk.setIdInsulin(MyLookup.getDiaryRepo().getCurrentPatient().getBolusInsulin().getId());
            rec.setInsulin(MyLookup.getDiaryRepo().getCurrentPatient().getBolusInsulin());
            pk.setDate(getClickCellDate(rowIndex, columnIndex));
            rec.setId(pk);
            rec.getId().setBasal(false);
            rec.setPump(true);

            Double units = (Double) value;
            rec.setAmount(units);
            rec.setSeason(seas.name());
            MyLookup.getDiaryRepo().addRecord(rec);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(rec.getId().getDate().getTime());
            dataIns[cal.get(Calendar.DAY_OF_MONTH) - 1][columnIndex][0] = rec;
        } else if (value instanceof String && columnIndex > 3) {
            StringTokenizer tok = new StringTokenizer(value.toString(), "-");
            int i = 0;
            Calendar cal = Calendar.getInstance();
            cal.setTime(getClickCellDate(rowIndex, columnIndex));
            while (tok.hasMoreTokens()) {
                String val = tok.nextToken().trim();
                try {
                    if (val != null && val.length() > 0 && Character.isDigit(val.charAt(0))) {
                        double units = Double.valueOf(val) / 10;
                        RecordInsulin rec = new RecordInsulin();
                        RecordInsulinPK pk = new RecordInsulinPK();
                        pk.setIdPatient(MyLookup.getDiaryRepo().getCurrentPatient().getIdPatient());
                        InsulinSeason seas = InsulinSeason.BASAL;
                        pk.setIdInsulin(MyLookup.getDiaryRepo().getCurrentPatient().getBasalInsulin().getId());
                        rec.setInsulin(MyLookup.getDiaryRepo().getCurrentPatient().getBasalInsulin());
                        cal.set(Calendar.HOUR_OF_DAY, (columnIndex - 4) * 12 + i);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        pk.setDate(cal.getTime());

                        rec.setId(pk);
                        rec.setAmount(units);
                        rec.setSeason(seas.name());
                        rec.setPump(true);
                        rec.getId().setBasal(true);
                        MyLookup.getDiaryRepo().addRecord(rec);
                        RecordInsulinPumpBasal basal = dataBasal[cal.get(Calendar.DAY_OF_MONTH) - 1][columnIndex - 4];
                        if (basal == null) {
                            basal = new RecordInsulinPumpBasal();
                            dataBasal[cal.get(Calendar.DAY_OF_MONTH) - 1][columnIndex - 4] = basal;
                        }
                        basal.getData()[i] = rec;
                    }
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                } finally {
                    i++;
                }
            }
             */
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
    public void setData(Collection<?> data) {
        dataIns = new RecordInsulin[31][6][1];
        dataBasal = new RecordInsulinPumpBasal[31][2];
        for (Object record : data) {
            if (record instanceof RecordInsulin) {
                RecordInsulin rec = (RecordInsulin) record;
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
    }

    @Override
    public void setBaseIndex(int baseIndex) {
        this.baseIndex = baseIndex;
    }

    @Override
    public int getBaseIndex() {
        return baseIndex;
    }

    @Override
    public int compareTo(TableSubModel o) {
        return Integer.valueOf(baseIndex).compareTo(o.getBaseIndex());
    }

    private Date getClickCellDate(int rowIndex, int columnIndex) {
        Date date = month.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(month.getTimeInMillis());
        cal.set(Calendar.DAY_OF_MONTH, rowIndex + 1);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        switch (columnIndex) {
            case 0:
                cal.set(Calendar.HOUR_OF_DAY, 8);
                break;
            case 1:
                cal.set(Calendar.HOUR_OF_DAY, 12);
                break;
            case 2:
                cal.set(Calendar.HOUR_OF_DAY, 17);
                break;
            //@todo jak cip
            default:
                cal.set(Calendar.HOUR_OF_DAY, 20);
                break;
        }
        return cal.getTime();
    }

    @Override
    public int getRowCount() {
        throw new IllegalStateException("Don't use it.");
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }
}
