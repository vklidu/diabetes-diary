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
import org.diabetesdiary.datamodel.api.ActivityAdministrator;
import org.diabetesdiary.datamodel.api.DbLookUp;
import org.diabetesdiary.datamodel.pojo.FoodUnit;
import org.diabetesdiary.datamodel.pojo.Investigation;
import org.diabetesdiary.datamodel.pojo.RecordActivity;
import org.diabetesdiary.datamodel.pojo.RecordActivityPK;
import org.diabetesdiary.datamodel.pojo.RecordFood;
import org.diabetesdiary.datamodel.pojo.RecordInvest;
import org.openide.util.NbBundle;

/**
 *
 * @author Jirka Majer
 */
public class ActivityModel implements TableSubModel, Comparable<TableSubModel> {

    private int baseIndex;
    private Calendar month;
    private RecordActivity[][][] dataActivity;
    private TableSubModel foodModel;
    private TableSubModel otherInvestModel;

    public ActivityModel(int baseIndex, Calendar month, TableSubModel foodModel, TableSubModel otherInvestModel) {
        this.baseIndex = baseIndex;
        this.foodModel = foodModel;
        this.otherInvestModel = otherInvestModel;
        this.month = month;
    }

    public ColumnGroup getColumnHeader(TableColumnModel columnModel) {
        ColumnGroup gSum = new ColumnGroup(NbBundle.getMessage(ActivityModel.class, "Column.energy"));
        ColumnGroup gOut = new ColumnGroup(NbBundle.getMessage(ActivityModel.class, "Column.energy.out"));

        gSum.add(gOut);
        gOut.add(columnModel.getColumn(baseIndex));
        gOut.add(columnModel.getColumn(baseIndex + 1));
        gSum.add(columnModel.getColumn(baseIndex + 2));
        gSum.add(columnModel.getColumn(baseIndex + 3));
        return gSum;
    }

    public Object getNewRecordValueAt(int rowIndex, int columnIndex) {
        if (columnIndex != 0) {
            return null;
        }
        RecordActivity gl = new RecordActivity();
        RecordActivityPK pk = new RecordActivityPK();
        pk.setIdPatient(DbLookUp.getDiary().getCurrentPatient().getIdPatient());
        ActivityAdministrator admin = DbLookUp.getActivityAdmin();
        pk.setIdActivity(1);
        gl.setActivity(admin.getActivity(1));
        pk.setDate(getClickCellDate(rowIndex, columnIndex));
        gl.setDuration(null);
        gl.setId(pk);
        return gl;
    }

    public void setData(Collection<?> data) {
        dataActivity = new RecordActivity[31][1][1];
        Calendar cal = Calendar.getInstance();
        for (Object record : data) {
            if (record instanceof RecordActivity) {
                RecordActivity rec = (RecordActivity) record;
                cal.setTimeInMillis(rec.getId().getDate().getTime());
                int col = 0;

                if (dataActivity[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] == null) {
                    dataActivity[cal.get(Calendar.DAY_OF_MONTH) - 1][col][0] = rec;
                } else {
                    RecordActivity[] pom = dataActivity[cal.get(Calendar.DAY_OF_MONTH) - 1][col];
                    dataActivity[cal.get(Calendar.DAY_OF_MONTH) - 1][col] = new RecordActivity[pom.length + 1];
                    for (int i = 0; i < pom.length; i++) {
                        dataActivity[cal.get(Calendar.DAY_OF_MONTH) - 1][col][i] = pom[i];
                    }
                    dataActivity[cal.get(Calendar.DAY_OF_MONTH) - 1][col][pom.length] = rec;
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
        return 4;
    }

    public String getColumnName(int index) {
        switch (index) {
            case 0:
                return NbBundle.getMessage(OtherInvestModel.class, "Column.energy.activity");
            case 1:
                return NbBundle.getMessage(OtherInvestModel.class, "Column.energy.metabolic");
            case 2:
                return NbBundle.getMessage(OtherInvestModel.class, "Column.energy.in");
            case 3:
                return NbBundle.getMessage(OtherInvestModel.class, "Column.energy.bilance");
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public Class<?> getColumnClass(int index) {
        return RecordActivity.class;
    }

    public boolean isCellEditable(int row, int col) {
        return col == 0;
    }

    private Double getWeight(int rowIndex) {
        Double weight = null;
        for (int actrow = rowIndex; actrow > -1; actrow--) {
            Object obj = otherInvestModel.getValueAt(actrow, 0);
            if (obj instanceof RecordInvest) {
                RecordInvest inv = (RecordInvest) obj;
                if (inv.getId().getIdInvest() == Investigation.Instances.WEIGHT.getID()) {
                    weight = inv.getValue();
                    break;
                }
            } else if (obj instanceof RecordInvest[]) {
                RecordInvest[] invs = (RecordInvest[]) obj;
                for (RecordInvest inv : invs) {
                    if (inv.getId().getIdInvest() == Investigation.Instances.WEIGHT.getID()) {
                        weight = inv.getValue();
                        break;
                    }
                }
            }
        }
        return weight;
    }

    private Double getTall(int rowIndex) {
        Double tall = null;
        for (int actrow = rowIndex; actrow > -1; actrow--) {
            Object obj = otherInvestModel.getValueAt(actrow, 0);
            if (obj instanceof RecordInvest) {
                RecordInvest inv = (RecordInvest) obj;
                if (inv.getId().getIdInvest() == Investigation.Instances.TALL.getID()) {
                    tall = inv.getValue();
                    break;
                }
            } else if (obj instanceof RecordInvest[]) {
                RecordInvest[] invs = (RecordInvest[]) obj;
                for (RecordInvest inv : invs) {
                    if (inv.getId().getIdInvest() == Investigation.Instances.TALL.getID()) {
                        tall = inv.getValue();
                        break;
                    }
                }
            }
        }
        return tall;
    }

    private Integer getNumberOfYears(int rowIndex) {
        if (DbLookUp.getDiary() != null && DbLookUp.getDiary().getCurrentPatient() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(month.getTimeInMillis());
            cal.set(Calendar.DAY_OF_MONTH, rowIndex + 1);

            Date born = DbLookUp.getDiary().getCurrentPatient().getBorn();
            Calendar cal2 = Calendar.getInstance();
            cal2.setTimeInMillis(born.getTime());
            cal.add(Calendar.YEAR, -(cal2.get(Calendar.YEAR)));
            return cal.get(Calendar.YEAR) - 1;
        }
        return null;
    }

    private Boolean isMale() {
        if (DbLookUp.getDiary() != null && DbLookUp.getDiary().getCurrentPatient() != null) {
            return DbLookUp.getDiary().getCurrentPatient().isMale();
        }
        return null;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                if (dataActivity != null && rowIndex > -1) {
                    if (dataActivity[rowIndex][columnIndex] != null && dataActivity[rowIndex][columnIndex].length == 1) {
                        RecordActivity res = dataActivity[rowIndex][columnIndex][0];
                        if (res != null) {
                            res.setWeight(getWeight(rowIndex));
                        }
                        return res;
                    } else {
                        RecordActivity[] res = dataActivity[rowIndex][columnIndex];
                        for (RecordActivity rec : res) {
                            rec.setWeight(getWeight(rowIndex));
                        }
                        return res;
                    }
                }
                break;
            case 1:
                Integer years = getNumberOfYears(rowIndex);
                Boolean boy = isMale();
                Double weight = getWeight(rowIndex);
                Double tall = getTall(rowIndex);
                //http://www.mte.cz/bmr.php
                //BMR(ženy) = 655,0955 + (9,5634 × váha v kg) + (1,8496 × výška v cm) - (4,6756 × věk v letech)
                //BMR(muži) = 66,473 + (13,7516 × váha v kg) + (5,0033 × výška v cm) - (6,755 × věk v letech)
                Energy energy = new Energy();
                if (weight != null && tall != null && years != null) {
                    energy.setUnit("kJ");
                    if (boy) {
                        energy.setValue(66.473 + (13.7516 * weight) + (5.0033 * tall) - (6.755 * years));
                        //prevod z kcal na kJ
                        energy.setValue(energy.getValue() * 4.1868);
                    } else {
                        energy.setValue(655.0955 + (9.5634 * weight) + (1.8496 * tall) - (4.6756 * years));
                        //prevod z kcal na kJ
                        energy.setValue(energy.getValue() * 4.1868);
                    }
                }
                return energy;
            case 2:
                Energy energ = null;
                for (int col = 0; col < foodModel.getColumnCount(); col++) {
                    Object values = foodModel.getValueAt(rowIndex, col);
                    if (values instanceof RecordFood) {
                        RecordFood rec = (RecordFood) values;
                        if (rec.getAmount() != null) {
                            energ = new Energy();
                            energ.setUnit("kJ");
                            energ.setValue(countFoodEnergy(rec));
                        }
                    } else if (values instanceof RecordFood[]) {
                        RecordFood[] recs = (RecordFood[]) values;
                        energ = new Energy();
                        energ.setUnit("kJ");
                        Double sum = 0d;
                        for (RecordFood rec : recs) {
                            if (rec.getAmount() != null) {

                                sum += countFoodEnergy(rec);
                            }
                        }
                        energ.setValue(sum);
                    }
                }
                return energ;
            case 3:
                Object foo = getValueAt(rowIndex, 2);
                Energy food = new Energy();
                food.setUnit("kJ");
                food.setValue(0d);
                if (foo instanceof Energy) {
                    food = (Energy) foo;
                }
                //metabolismus
                Object metab = getValueAt(rowIndex, 1);
                if (metab instanceof Energy) {
                    food.minus((Energy) metab);
                }
                //aktivity                
                food.minus(countAktEnergy(getValueAt(rowIndex, 0)));
                return food;
        }

        return null;
    }

    private static Energy countAktEnergy(Object value) {
        if (value instanceof RecordActivity[]) {
            RecordActivity[] values = (RecordActivity[]) value;
            if (values.length > 0 && values[0] != null && values[0].getActivity() != null) {
                Double sum = 0d;
                for (RecordActivity val : values) {
                    if (val.getWeight() != null) {
                        sum += val.getActivity().getPower() * val.getDuration() * val.getWeight();
                    } else {
                        sum = Double.NEGATIVE_INFINITY;
                    }
                }
                Energy res = new Energy();
                res.setUnit("kJ");
                res.setValue(sum);
            }
        } else if (value instanceof RecordActivity) {
            RecordActivity rec = (RecordActivity) value;
            if (rec.getDuration() != null && rec.getActivity() != null && rec.getWeight() != null) {
                Energy res = new Energy();
                res.setUnit("kJ");
                res.setValue(rec.getActivity().getPower() * rec.getDuration() * rec.getWeight());
                return res;
            }
        }
        return null;
    }

    private static Double countFoodEnergy(RecordFood rec) {
        if (rec.getAmount() == null) {
            return null;
        }
        FoodUnit unit = null;
        if (rec.getFood() != null && rec.getFood().getUnits() != null) {
            for (Object un : rec.getFood().getUnits()) {
                unit = (FoodUnit) un;
                if (unit.getId().getUnit().equals(rec.getUnit())) {
                    break;
                }
            }
        }
        if (unit == null) {
            unit = DbLookUp.getFoodAdmin().getFoodUnit(rec.getId().getIdFood(), rec.getUnit());
        }
        double sachUnits = unit.getKoef() * rec.getAmount() * rec.getFood().getEnergy() / 100;
        return sachUnits;
    }

    private Date getClickCellDate(int row, int column) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(month.getTimeInMillis());
        cal.set(Calendar.DAY_OF_MONTH, row + 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, column == 0 ? 10 : 15);
        return cal.getTime();
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (columnIndex != 0) {
            return;
        }
        if (value instanceof Integer) {
            RecordActivity rec = new RecordActivity();
            RecordActivityPK pk = new RecordActivityPK();
            pk.setIdPatient(DbLookUp.getDiary().getCurrentPatient().getIdPatient());
            pk.setIdActivity(1);
            rec.setActivity(DbLookUp.getActivityAdmin().getActivity(1));

            pk.setDate(getClickCellDate(rowIndex, columnIndex));
            rec.setId(pk);
            rec.setDuration((Integer) value);
            DbLookUp.getDiary().addRecord(rec);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(rec.getId().getDate().getTime());
            dataActivity[cal.get(Calendar.DAY_OF_MONTH) - 1][cal.get(Calendar.HOUR_OF_DAY) < 12 ? 0 : 1][0] = rec;
        }
    }

    public void addTableModelListener(TableModelListener arg0) {
    }

    public void removeTableModelListener(TableModelListener arg0) {
    }

    public int compareTo(TableSubModel o) {
        return Integer.valueOf(getBaseIndex()).compareTo(o.getBaseIndex());
    }
}
