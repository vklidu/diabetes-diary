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
import org.diabetesdiary.datamodel.api.DbLookUp;
import org.diabetesdiary.datamodel.api.InvestigationAdministrator;
import org.diabetesdiary.datamodel.pojo.GlykSeason;
import org.diabetesdiary.datamodel.pojo.Investigation;
import org.diabetesdiary.datamodel.pojo.RecordInvest;
import org.diabetesdiary.datamodel.pojo.RecordInvestPK;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class RecordInvestModel implements TableSubModel, Comparable<TableSubModel> {

    private RecordInvest dataInvest[][][];
    private int baseIndex;
    private Calendar month;

    /** Creates a new instance of RecordInvestModel */
    public RecordInvestModel(int baseIndex, Calendar month) {
        this.baseIndex = baseIndex;
        this.month = month;
    }

    public int getColumnCount() {
        return 9;
    }

    public ColumnGroup getColumnHeader(TableColumnModel cm) {
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

    public Object getValueAt(int rowIndex, int columnIndex) {
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
        RecordInvest gl = new RecordInvest();
        RecordInvestPK pk = new RecordInvestPK();
        pk.setIdPatient(DbLookUp.getDiary().getCurrentPatient().getIdPatient());
        pk.setIdInvest(Investigation.Instances.GLYCEMIE.getID());
        pk.setDate(getClickCellDate(rowIndex, columnIndex));
        InvestigationAdministrator invAdmin = DbLookUp.getInvesAdmin();
        gl.setInvest(invAdmin.getInvestigation(Investigation.Instances.GLYCEMIE.getID()));
        gl.setValue(null);
        gl.setId(pk);
        if (columnIndex == 0) {
            gl.setSeason(GlykSeason.M.name());
        } else {
            gl.setSeason(GlykSeason.values()[columnIndex - 1].name());
        }
        return gl;
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value instanceof Double) {
            RecordInvest gl = new RecordInvest();
            RecordInvestPK pk = new RecordInvestPK();
            pk.setIdPatient(DbLookUp.getDiary().getCurrentPatient().getIdPatient());
            pk.setIdInvest(Investigation.Instances.GLYCEMIE.getID());
            pk.setDate(getClickCellDate(rowIndex, columnIndex));

            gl.setInvest(DbLookUp.getInvesAdmin().getInvestigation(Investigation.Instances.GLYCEMIE.getID()));

            gl.setId(pk);
            gl.setValue((Double) value);
            if (columnIndex == 0) {
                gl.setSeason(GlykSeason.M.name());
            } else {
                gl.setSeason(GlykSeason.values()[columnIndex - 1].name());
            }
            DbLookUp.getDiary().addRecord(gl);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(gl.getId().getDate().getTime());
            dataInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][columnIndex][0] = gl;
        }
    }

    public Class<?> getColumnClass(int columnIndex) {
        return RecordInvest.class;
    }

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

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public void setData(Collection<?> data) {
        dataInvest = new RecordInvest[31][getColumnCount()][1];
        Calendar cal = Calendar.getInstance();
        for (Object record : data) {
            if (record instanceof RecordInvest) {
                RecordInvest rec = (RecordInvest) record;
                if (rec.getId().getIdInvest().equals(Investigation.Instances.GLYCEMIE.getID())) {
                    cal.setTimeInMillis(rec.getId().getDate().getTime());
                    int column = GlykSeason.valueOf(rec.getSeason()).ordinal() + 1;
                    if (GlykSeason.valueOf(rec.getSeason()).equals(GlykSeason.M)) {
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

    public int getRowCount() {
        throw new IllegalStateException("Don't use it.");
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }

    private Date getClickCellDate(int row, int column) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(month.getTimeInMillis());
        cal.set(Calendar.DAY_OF_MONTH, row + 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        switch (column) {
            case 0:
                cal.set(Calendar.HOUR_OF_DAY, 3);
                break;
            case 1:
                cal.set(Calendar.HOUR_OF_DAY, 7);
                break;
            case 2:
                cal.set(Calendar.HOUR_OF_DAY, 9);
                break;
            case 3:
                cal.set(Calendar.HOUR_OF_DAY, 11);
                break;
            case 4:
                cal.set(Calendar.HOUR_OF_DAY, 14);
                break;
            case 5:
                cal.set(Calendar.HOUR_OF_DAY, 16);
                break;
            case 6:
                cal.set(Calendar.HOUR_OF_DAY, 18);
                break;
            case 7:
                cal.set(Calendar.HOUR_OF_DAY, 21);
                break;
            case 8:
                cal.set(Calendar.HOUR_OF_DAY, 23);
                break;
        }

        return cal.getTime();
    }
}
