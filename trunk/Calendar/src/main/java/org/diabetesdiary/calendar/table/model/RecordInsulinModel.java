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
import java.util.List;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.diabetesdiary.calendar.table.editor.NumberEditor;
import org.diabetesdiary.calendar.table.header.ColumnGroup;
import org.diabetesdiary.calendar.table.renderer.InsulinCellRenderer;
import org.diabetesdiary.calendar.utils.DataChangeEvent;
import org.diabetesdiary.diary.domain.InsulinSeason;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.domain.RecordInsulin;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class RecordInsulinModel extends AbstractRecordSubModel {

    private RecordInsulin dataIns[][][];

    public RecordInsulinModel(DateTime month, Patient patient) {
        super(month, patient);
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public ColumnGroup getColumnHeader(List<TableColumn> cols) {
        ColumnGroup gInsulin = new ColumnGroup(NbBundle.getMessage(RecordInsulinModel.class, "Column.insulin"));
        ColumnGroup gInsBr = new ColumnGroup(NbBundle.getMessage(RecordInsulinModel.class, "Column.breakfest"));
        ColumnGroup gInsDin = new ColumnGroup(NbBundle.getMessage(RecordInsulinModel.class, "Column.dinner"));
        ColumnGroup gInsLaun = new ColumnGroup(NbBundle.getMessage(RecordInsulinModel.class, "Column.launch"));
        gInsulin.add(gInsBr);
        gInsulin.add(gInsDin);
        gInsulin.add(gInsLaun);

        gInsBr.add(cols.get(0));
        gInsBr.add(cols.get(1));

        gInsDin.add(cols.get(2));

        gInsLaun.add(cols.get(3));
        gInsLaun.add(cols.get(4));

        gInsulin.add(cols.get(5));

        return gInsulin;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (dataIns == null) {
            setData();
        }
        if (dataIns != null && rowIndex > -1) {
            if (dataIns[rowIndex][columnIndex] != null && dataIns[rowIndex][columnIndex].length == 1) {
                return dataIns[rowIndex][columnIndex][0];
            } else {
                return dataIns[rowIndex][columnIndex];
            }
        }
        return null;
    }

    public InsulinSeason getSeason(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return InsulinSeason.B;
            case 1:
                return InsulinSeason.B;
            case 2:
                return InsulinSeason.D;
            case 3:
                return InsulinSeason.L;
            case 4:
                return InsulinSeason.L;
            case 5:
                return InsulinSeason.ADD;
            default:
                return InsulinSeason.ADD;
        }
    }

    public boolean isBasal(int columnIndex) {
        return columnIndex == 1 || columnIndex == 4;
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

            DateTime recDateTime = getClickCellDate(rowIndex, columnIndex);
            RecordInsulin edited = dataIns[recDateTime.getDayOfMonth() - 1][columnIndex][0];
            if (edited != null) {
                edited = edited.update((Double) value);
            } else {
                edited = MyLookup.getCurrentPatient().addRecordInsulin(
                        getClickCellDate(rowIndex, columnIndex),
                        !bolus,
                        (Double) value,
                        seas,
                        null);
            }
            dataIns[recDateTime.getDayOfMonth() - 1][columnIndex][0] = edited;
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

    private void setData() {
        Collection<RecordInsulin> data = MyLookup.getCurrentPatient().getRecordInsulins(getFrom(), getTo());
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

    public DateTime getClickCellDate(int rowIndex, int columnIndex) {
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

    @Override
    public void onDataChange(DataChangeEvent evt) {
        if (evt.getDataChangedClazz() == null || evt.getDataChangedClazz().equals(RecordInsulin.class)) {
            dataIns = null;
        }
    }


    @Override
    public TableCellRenderer getCellRenderer(int columnIndex) {
        return new InsulinCellRenderer();
    }

    @Override
    public TableCellEditor getCellEditor(int columnIndex) {
        return new NumberEditor<Double, Object>(0d, 50d) {

            @Override
            public Double getValue(Object object) {
                if (object instanceof RecordInsulin) {
                    return ((RecordInsulin) object).getAmount();
                } else if (object instanceof RecordInsulin[]) {
                    return ((RecordInsulin[]) object)[0].getAmount();
                }
                return null;
            }
        };
    }
}
