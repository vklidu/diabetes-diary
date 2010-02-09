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
import java.util.List;
import javax.swing.table.TableColumnModel;
import org.diabetesdiary.calendar.table.header.ColumnGroup;
import org.diabetesdiary.diary.domain.RecordInvest;
import org.diabetesdiary.diary.domain.InvSeason;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.diary.domain.WKInvest;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class RecordInvestModel extends AbstractRecordSubModel {

    private RecordInvest dataInvest[][][];

    public RecordInvestModel(DateTime dateTime) {
        super(dateTime);
    }

    @Override
    public int getColumnCount() {
        return 9;
    }

    @Override
    public ColumnGroup getColumnHeader(TableColumnModel cm, int baseIndex) {
        ColumnGroup gGlyk = new ColumnGroup(NbBundle.getMessage(RecordInvestModel.class, "Column.glykemie"));
        ColumnGroup gBreak = new ColumnGroup(NbBundle.getMessage(RecordInvestModel.class, "Column.breakfest"));
        ColumnGroup gDinner = new ColumnGroup(NbBundle.getMessage(RecordInvestModel.class, "Column.dinner"));
        ColumnGroup gLaunch = new ColumnGroup(NbBundle.getMessage(RecordInvestModel.class, "Column.launch"));

        gGlyk.add(gBreak);
        gGlyk.add(gDinner);
        gGlyk.add(gLaunch);
        gGlyk.add(cm.getColumn(baseIndex));
        gGlyk.add(cm.getColumn(baseIndex + 7));
        gGlyk.add(cm.getColumn(baseIndex + 8));

        gBreak.add(cm.getColumn(baseIndex + 1));
        gBreak.add(cm.getColumn(baseIndex + 2));

        gDinner.add(cm.getColumn(baseIndex + 3));
        gDinner.add(cm.getColumn(baseIndex + 4));

        gLaunch.add(cm.getColumn(baseIndex + 5));
        gLaunch.add(cm.getColumn(baseIndex + 6));

        return gGlyk;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (dataInvest == null) {
            setData();
        }
        if (dataInvest != null && rowIndex > -1) {
            if (dataInvest[rowIndex][columnIndex] != null && dataInvest[rowIndex][columnIndex].length == 1) {
                return dataInvest[rowIndex][columnIndex][0];
            } else {
                return dataInvest[rowIndex][columnIndex];
            }
        }
        return null;
    }

    public Object getNewRecordValueAt(int rowIndex, int columnIndex) {
        /**
        RecordInvest gl = new RecordInvest();
        RecordInvestPK pk = new RecordInvestPK();
        pk.setIdPatient(MyLookup.getDiaryRepo().getCurrentPatient().getIdPatient());
        pk.setIdInvest(InvestigationDO.Instances.GLYCEMIE.getID());
        pk.setDate(getClickCellDate(rowIndex, columnIndex));
        InvestigationAdministrator invAdmin = MyLookup.getInvesAdmin();
        gl.setInvest(invAdmin.getInvestigation(InvestigationDO.Instances.GLYCEMIE.getID()));
        gl.setValue(null);
        gl.setId(pk);
        if (columnIndex == 0) {
        gl.setSeason(InvSeason.M.name());
        } else {
        gl.setSeason(InvSeason.values()[columnIndex - 1].name());
        }
        return gl;
         */
        return null;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value instanceof Double) {
            Patient pat = MyLookup.getCurrentPatient();
            InvSeason seas;
            if (columnIndex == 0) {
                seas = InvSeason.M;
            } else {
                seas = InvSeason.values()[columnIndex - 1];
            }

            RecordInvest rec = pat.addRecordInvest(
                    getClickCellDate(rowIndex, columnIndex),
                    (Double) value,
                    MyLookup.getDiaryRepo().getWellKnownInvestigation(WKInvest.GLYCEMIE),
                    seas,
                    null);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(rec.getDatetime().getMillis());
            dataInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][columnIndex][0] = rec;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return RecordInvest.class;
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return NbBundle.getMessage(RecordInvestModel.class, "Column.night");
            case 1:
                return NbBundle.getMessage(RecordInvestModel.class, "Column.before");
            case 2:
                return NbBundle.getMessage(RecordInvestModel.class, "Column.After");
            case 3:
                return NbBundle.getMessage(RecordInvestModel.class, "Column.before");
            case 4:
                return NbBundle.getMessage(RecordInvestModel.class, "Column.After");
            case 5:
                return NbBundle.getMessage(RecordInvestModel.class, "Column.before");
            case 6:
                return NbBundle.getMessage(RecordInvestModel.class, "Column.After");
            case 7:
                return NbBundle.getMessage(RecordInvestModel.class, "Column.beforeSleep");
            case 8:
                return NbBundle.getMessage(RecordInvestModel.class, "Column.night");
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    private void setData() {
        List<RecordInvest> data = MyLookup.getCurrentPatient().getRecordInvests(getFrom(), getTo());
        dataInvest = new RecordInvest[31][getColumnCount()][1];
        Calendar cal = Calendar.getInstance();
        for (RecordInvest rec : data) {
            if (rec.getInvest().anyType(WKInvest.GLYCEMIE)) {
                cal.setTimeInMillis(rec.getDatetime().getMillis());
                int column = rec.getSeason().ordinal() + 1;
                if (rec.getSeason() == InvSeason.M) {
                    if (cal.get(Calendar.HOUR_OF_DAY) < 12) {
                        column = 0;
                    } else {
                        column = 8;
                    }
                }
                if (dataInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][column][0] == null) {
                    dataInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][column][0] = rec;
                } else {
                    RecordInvest[] pom = dataInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][column];
                    dataInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][column] = new RecordInvest[pom.length + 1];
                    for (int i = 0; i < pom.length; i++) {
                        dataInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][column][i] = pom[i];
                    }
                    dataInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][column][pom.length] = rec;
                }
            }
        }
    }

    private DateTime getClickCellDate(int row, int column) {
        int hourOfDay;
        switch (column) {
            case 0:
                hourOfDay = 3;
                break;
            case 1:
                hourOfDay = 7;
                break;
            case 2:
                hourOfDay = 9;
                break;
            case 3:
                hourOfDay = 11;
                break;
            case 4:
                hourOfDay = 14;
                break;
            case 5:
                hourOfDay = 16;
                break;
            case 6:
                hourOfDay = 18;
                break;
            case 7:
                hourOfDay = 21;
                break;
            case 8:
                hourOfDay = 23;
                break;
            default:
                throw new IllegalStateException();
        }

        return dateTime.withDayOfMonth(row + 1).withTime(hourOfDay, 0, 0, 0);
    }

    @Override
    public void invalidateData() {
        dataInvest = null;
    }
}
