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
import org.diabetesdiary.calendar.utils.DbLookUp;
import org.diabetesdiary.datamodel.api.InvestigationAdministrator;
import org.diabetesdiary.datamodel.pojo.InvSeason;
import org.diabetesdiary.datamodel.pojo.InvestigationDO;
import org.diabetesdiary.datamodel.pojo.RecordInvestDO;
import org.diabetesdiary.datamodel.pojo.RecordInvestPK;
import org.openide.util.NbBundle;

/**
 *
 * @author Jirka Majer
 */
public class OtherInvestModel implements TableSubModel, Comparable<TableSubModel> {

    private int baseIndex;
    private boolean male;
    private Calendar month;
    private RecordInvestDO[][][] dataOtherInvest;

    public OtherInvestModel(int baseIndex, boolean male, Calendar month) {
        this.baseIndex = baseIndex;
        this.male = male;
        this.month = month;
    }

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
        RecordInvestDO gl = new RecordInvestDO();
        RecordInvestPK pk = new RecordInvestPK();
        pk.setIdPatient(DbLookUp.getDiaryRepo().getCurrentPatient().getIdPatient());
        pk.setIdInvest(getClickCellInvestId(rowIndex, columnIndex));
        pk.setDate(getClickCellDate(rowIndex, columnIndex));
        InvestigationAdministrator invAdmin = DbLookUp.getInvesAdmin();
        gl.setInvest(invAdmin.getInvestigation(getClickCellInvestId(rowIndex, columnIndex)));
        gl.setValue(null);
        gl.setId(pk);
        gl.setSeason(null);
        return gl;
    }

    public void setData(Collection<?> data) {
        dataOtherInvest = new RecordInvestDO[31][4][1];
        Calendar cal = Calendar.getInstance();
        for (Object record : data) {
            if (record instanceof RecordInvestDO) {
                RecordInvestDO rec = (RecordInvestDO) record;
                int id = rec.getId().getIdInvest();
                if (id != InvestigationDO.Instances.GLYCEMIE.getID()) {
                    cal.setTimeInMillis(rec.getId().getDate().getTime());
                    InvestigationDO.Instances inst = InvestigationDO.Instances.getInvestInstanceByID(id);

                    if (dataOtherInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][getColumnIndexForInvest(inst)][0] == null) {
                        dataOtherInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][getColumnIndexForInvest(inst)][0] = rec;
                    } else {
                        RecordInvestDO[] pom = dataOtherInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][getColumnIndexForInvest(inst)];
                        dataOtherInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][getColumnIndexForInvest(inst)] = new RecordInvestDO[pom.length + 1];
                        for (int i = 0; i < pom.length; i++) {
                            dataOtherInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][getColumnIndexForInvest(inst)][i] = pom[i];
                        }
                        dataOtherInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][getColumnIndexForInvest(inst)][pom.length] = rec;
                    }
                }
            }
        }
    }

    public int getBaseIndex() {
        return baseIndex;
    }

    public void setBaseIndex(int baseIndex) {
        this.baseIndex = baseIndex;
    }

    public int getRowCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getColumnCount() {
        return isMale() ? 3 : 4;
    }

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

    public Class<?> getColumnClass(int index) {
        switch (index) {
            case 0:
                return RecordInvestDO.class;
            case 1:
                return RecordInvestDO.class;
            case 2:
                return RecordInvestDO.class;
            case 3:
                return RecordInvestDO.class;
            default:
                throw new IndexOutOfBoundsException();

        }
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

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

    private Date getClickCellDate(int row, int column) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(month.getTimeInMillis());
        cal.set(Calendar.DAY_OF_MONTH, row + 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        return cal.getTime();
    }

    private Integer getColumnIndexForInvest(InvestigationDO.Instances inst) {
        switch (inst) {
            case WEIGHT:
                return 0;
            case SUGAR:
                return 1;
            case ACETON:
                return 2;
            case MENZES:
                return 3;
            case TALL:
                return 0;                
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    private Integer getClickCellInvestId(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return InvestigationDO.Instances.WEIGHT.getID();//weight

            case 1:
                return InvestigationDO.Instances.SUGAR.getID();//sugar

            case 2:
                return InvestigationDO.Instances.ACETON.getID();//aceton

            case 3:
                return InvestigationDO.Instances.MENZES.getID();//menses

            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value instanceof Double) {
            RecordInvestDO gl = new RecordInvestDO();
            RecordInvestPK pk = new RecordInvestPK();
            pk.setIdPatient(DbLookUp.getDiaryRepo().getCurrentPatient().getIdPatient());
            pk.setIdInvest(getClickCellInvestId(rowIndex, columnIndex));
            pk.setDate(getClickCellDate(rowIndex, columnIndex));

            gl.setInvest(DbLookUp.getInvesAdmin().getInvestigation(getClickCellInvestId(rowIndex, columnIndex)));

            gl.setId(pk);
            gl.setValue((Double) value);
            gl.setSeason(InvSeason.BB.name());
            DbLookUp.getDiaryRepo().addRecord(gl);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(gl.getId().getDate().getTime());
            dataOtherInvest[cal.get(Calendar.DAY_OF_MONTH) - 1][columnIndex][0] = gl;
        }
    }

    public void addTableModelListener(TableModelListener arg0) {
    }

    public void removeTableModelListener(TableModelListener arg0) {
    }

    public int compareTo(TableSubModel o) {
        return Integer.valueOf(getBaseIndex()).compareTo(o.getBaseIndex());
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }
}
