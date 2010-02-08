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
import org.diabetesdiary.diary.domain.InvSeason;
import org.diabetesdiary.diary.domain.RecordInvest;
import org.diabetesdiary.diary.domain.WKInvest;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;
import org.openide.util.NbBundle;

/**
 *
 * @author Jirka Majer
 */
public class OtherInvestModel extends AbstractRecordSubModel<RecordInvest> {

    private boolean male;
    private RecordInvest[][][] dataOtherInvest;

    public OtherInvestModel(int baseIndex, boolean male, DateTime month) {
        super(baseIndex, month);
        this.male = male;
    }

    @Override
    public ColumnGroup getColumnHeader(TableColumnModel columnModel) {
        ColumnGroup gSum = new ColumnGroup(NbBundle.getMessage(OtherInvestModel.class, "Column.otherInvest"));
        gSum.add(columnModel.getColumn(baseIndex));
        gSum.add(columnModel.getColumn(baseIndex + 1));
        gSum.add(columnModel.getColumn(baseIndex + 2));
        if (!male) {
            gSum.add(columnModel.getColumn(baseIndex + 3));
        }

        return gSum;
    }

    public Object getNewRecordValueAt(int rowIndex, int columnIndex) {
        return null;
        /*
        RecordInvest gl = new RecordInvest();
        RecordInvestPK pk = new RecordInvestPK();
        pk.setIdPatient(MyLookup.getDiaryRepo().getCurrentPatient().getIdPatient());
        pk.setIdInvest(getClickCellInvestId(rowIndex, columnIndex));
        pk.setDate(getClickCellDate(rowIndex, columnIndex));
        InvestigationAdministrator invAdmin = MyLookup.getInvesAdmin();
        gl.setInvest(invAdmin.getInvestigation(getClickCellInvestId(rowIndex, columnIndex)));
        gl.setValue(null);
        gl.setId(pk);
        gl.setSeason(null);
        return gl;
         * 
         */
    }

    @Override
    public void setData(Collection<RecordInvest> data) {
        dataOtherInvest = new RecordInvest[31][4][1];
        Calendar cal = Calendar.getInstance();
        for (RecordInvest rec : data) {
            if (!rec.getInvest().anyType(WKInvest.GLYCEMIE)) {
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
        if (dataOtherInvest != null && rowIndex > -1) {
            if (dataOtherInvest[rowIndex][columnIndex] != null && dataOtherInvest[rowIndex][columnIndex].length == 1) {
                return dataOtherInvest[rowIndex][columnIndex][0];
            } else {
                return dataOtherInvest[rowIndex][columnIndex];
            }
        }
        return null;
    }

    private DateTime getClickCellDate(int row, int column) {
        return dateTime.withDayOfMonth(row+1).withTime(12, 0, 0, 0);
    }

    private Integer getColumnIndexForInvest(WKInvest inst) {
        switch (inst) {
            case WEIGHT:
                return 0;
            case URINE_SUGAR:
                return 1;
            case ACETON:
                return 2;
            case MENSES:
                return 3;
            case HEIGHT:
                return 0;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    private WKInvest getClickCellInvestId(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return WKInvest.WEIGHT;//weight

            case 1:
                return WKInvest.URINE_SUGAR;//sugar

            case 2:
                return WKInvest.ACETON;//aceton

            case 3:
                return WKInvest.MENSES;//menses

            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value instanceof Double) {
            RecordInvest rec = MyLookup.getCurrentPatient().addRecordInvest(getClickCellDate(rowIndex, columnIndex),
                    (Double)value,
                    MyLookup.getDiaryRepo().getWellKnownInvestigation(getClickCellInvestId(rowIndex, columnIndex)),
                    InvSeason.BB,
                    null);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(rec.getDatetime().getMillis());
            dataOtherInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][columnIndex][0] = rec;
        }
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

}
