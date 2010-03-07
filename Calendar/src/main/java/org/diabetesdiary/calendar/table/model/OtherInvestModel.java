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
import org.diabetesdiary.calendar.table.renderer.GlycaemiaCellRenderer;
import org.diabetesdiary.calendar.utils.DataChangeEvent;
import org.diabetesdiary.diary.domain.InvSeason;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.domain.RecordInvest;
import org.diabetesdiary.diary.domain.WKInvest;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;
import org.openide.util.NbBundle;

/**
 *
 * @author Jirka Majer
 */
public class OtherInvestModel extends AbstractRecordSubModel {

    private boolean male;
    private RecordInvest[][][] dataOtherInvest;

    public OtherInvestModel(DateTime month, Patient patient) {
        super(month, patient);
        this.male = patient.isMale();
    }

    @Override
    public ColumnGroup getColumnHeader(List<TableColumn> cols) {
        ColumnGroup gSum = new ColumnGroup(NbBundle.getMessage(OtherInvestModel.class, "Column.otherInvest"));
        gSum.add(cols.get(0));
        gSum.add(cols.get(1));
        gSum.add(cols.get(2));
        if (!male) {
            gSum.add(cols.get(3));
        }

        return gSum;
    }

    private void setData() {
        Collection<RecordInvest> data = MyLookup.getCurrentPatient().getRecordInvests(getFrom(), getTo());
        dataOtherInvest = new RecordInvest[31][4][1];
        Calendar cal = Calendar.getInstance();
        for (RecordInvest rec : data) {
            if (!rec.getInvest().anyType(WKInvest.GLYCAEMIA)) {
                cal.setTimeInMillis(rec.getDatetime().getMillis());
                WKInvest inst = rec.getInvest().getWKInvest();

                if (dataOtherInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][getColumnIndexForInvest(inst)][0] == null) {
                    dataOtherInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][getColumnIndexForInvest(inst)][0] = rec;
                } else {
                    RecordInvest[] pom = dataOtherInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][getColumnIndexForInvest(inst)];
                    dataOtherInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][getColumnIndexForInvest(inst)] = new RecordInvest[pom.length + 1];
                    for (int i = 0; i < pom.length; i++) {
                        dataOtherInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][getColumnIndexForInvest(inst)][i] = pom[i];
                    }
                    dataOtherInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][getColumnIndexForInvest(inst)][pom.length] = rec;
                }
            }
        }
    }

    @Override
    public int getColumnCount() {
        return isMale() ? 3 : 4;
    }

    @Override
    public String getColumnName(int index) {
        switch (index) {
            case 0:
                return NbBundle.getMessage(OtherInvestModel.class, "Column.weight");
            case 1:
                return NbBundle.getMessage(OtherInvestModel.class, "Column.sugar");
            case 2:
                return NbBundle.getMessage(OtherInvestModel.class, "Column.aceton");
            case 3:
                return NbBundle.getMessage(OtherInvestModel.class, "Column.menses");
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Class<?> getColumnClass(int index) {
        switch (index) {
            case 0:
                return RecordInvest.class;
            case 1:
                return RecordInvest.class;
            case 2:
                return RecordInvest.class;
            case 3:
                return RecordInvest.class;
            default:
                throw new IndexOutOfBoundsException();

        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (dataOtherInvest == null) {
            setData();
        }
        if (dataOtherInvest != null && rowIndex > -1) {
            if (dataOtherInvest[rowIndex][columnIndex] != null && dataOtherInvest[rowIndex][columnIndex].length == 1) {
                return dataOtherInvest[rowIndex][columnIndex][0];
            } else {
                return dataOtherInvest[rowIndex][columnIndex];
            }
        }
        return null;
    }

    public DateTime getClickCellDate(int row, int column) {
        return dateTime.withDayOfMonth(row + 1).withTime(12, 0, 0, 0);
    }

    private Integer getColumnIndexForInvest(WKInvest inst) {
        switch (inst) {
            case WEIGHT:
                return 0;
            case URINE_SUGAR:
                return 1;
            case KETONES:
                return 2;
            case MENSES:
                return 3;
            case HEIGHT:
                return 0;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    public WKInvest getClickCellInvestId(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return WKInvest.WEIGHT;//weight

            case 1:
                return WKInvest.URINE_SUGAR;//sugar

            case 2:
                return WKInvest.KETONES;//aceton

            case 3:
                return WKInvest.MENSES;//menses

            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value instanceof Integer) {
            value = ((Integer) value).doubleValue();
        }
        if (value instanceof Double) {
            DateTime recDateTime = getClickCellDate(rowIndex, columnIndex);
            RecordInvest edited = dataOtherInvest[recDateTime.getDayOfMonth() - 1][columnIndex][0];
            if (edited != null) {
                edited = edited.update((Double) value);
            } else {
                edited = MyLookup.getCurrentPatient().addRecordInvest(getClickCellDate(rowIndex, columnIndex),
                        (Double) value,
                        MyLookup.getDiaryRepo().getWellKnownInvestigation(getClickCellInvestId(rowIndex, columnIndex)),
                        InvSeason.BB,
                        null);
            }
            dataOtherInvest[recDateTime.getDayOfMonth() - 1][columnIndex][0] = edited;
        }
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    @Override
    public TableCellRenderer getCellRenderer(int columnIndex) {
        return new GlycaemiaCellRenderer();
    }

    @Override
    public TableCellEditor getCellEditor(int columnIndex) {
        if (columnIndex == 0) {
            return new NumberEditor<Integer, Object>(0, 400) {

                @Override
                public Integer getValue(Object object) {
                    if (object instanceof RecordInvest) {
                        return ((RecordInvest) object).getValue().intValue();
                    } else if (object instanceof RecordInvest[]) {
                        return ((RecordInvest[]) object)[0].getValue().intValue();
                    }
                    return null;
                }
            };
        }
        if (columnIndex == 3) {
            return new NumberEditor<Integer, Object>(0, 3) {

                @Override
                public Integer getValue(Object object) {
                    if (object instanceof RecordInvest) {
                        return ((RecordInvest) object).getValue().intValue();
                    } else if (object instanceof RecordInvest[]) {
                        return ((RecordInvest[]) object)[0].getValue().intValue();
                    }
                    return null;
                }
            };
        }
        return new NumberEditor<Integer, Object>(0, 4) {

            @Override
            public Integer getValue(Object object) {
                if (object instanceof RecordInvest) {
                    return ((RecordInvest) object).getValue().intValue();
                } else if (object instanceof RecordInvest[]) {
                    return ((RecordInvest[]) object)[0].getValue().intValue();
                }
                return null;
            }
        };
    }

    @Override
    public void onDataChange(DataChangeEvent evt) {
        if (evt.getDataChangedClazz() == null || evt.getDataChangedClazz().equals(RecordInvest.class)) {
            dataOtherInvest = null;
        }
    }
}
