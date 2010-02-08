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
import org.diabetesdiary.calendar.table.Energy;
import org.diabetesdiary.diary.domain.RecordActivity;
import org.diabetesdiary.diary.domain.RecordFood;
import org.diabetesdiary.diary.domain.RecordInvest;
import org.diabetesdiary.diary.domain.WKInvest;
import org.diabetesdiary.diary.utils.MyLookup;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.openide.util.NbBundle;

/**
 *
 * @author Jirka Majer
 */
public class ActivityModel extends AbstractRecordSubModel<RecordActivity> {

    private RecordActivity[][][] dataActivity;
    private TableSubModel foodModel;
    private TableSubModel otherInvestModel;

    public ActivityModel(int baseIndex, DateTime month, TableSubModel foodModel, TableSubModel otherInvestModel) {
        super(baseIndex, month);
        this.foodModel = foodModel;
        this.otherInvestModel = otherInvestModel;
    }

    @Override
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
        /*
        RecordActivity gl = new RecordActivity();
        RecordActivityPK pk = new RecordActivityPK();
        pk.setIdPatient(MyLookup.getDiaryRepo().getCurrentPatient().getIdPatient());
        ActivityAdministrator admin = MyLookup.getActivityAdmin();
        pk.setIdActivity(1);
        gl.setActivity(admin.getActivity(1));
        pk.setDate(getClickCellDate(rowIndex, columnIndex));
        gl.setDuration(null);
        gl.setId(pk);
         *
         */
        return null;
    }

    @Override
    public void setData(Collection<RecordActivity> data) {
        dataActivity = new RecordActivity[31][1][1];
        Calendar cal = Calendar.getInstance();
        for (RecordActivity rec : data) {
            cal.setTimeInMillis(rec.getDatetime().getMillis());
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

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
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

    @Override
    public Class<?> getColumnClass(int index) {
        return RecordActivity.class;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 0;
    }

    private Double getWeight(int rowIndex) {
        for (int actrow = rowIndex; actrow > -1; actrow--) {
            Object obj = otherInvestModel.getValueAt(actrow, 0);
            if (obj instanceof RecordInvest) {
                RecordInvest inv = (RecordInvest) obj;
                if (inv.getInvest().anyType(WKInvest.WEIGHT)) {
                    return inv.getValue();
                }
            } else if (obj instanceof RecordInvest[]) {
                RecordInvest[] invs = (RecordInvest[]) obj;
                for (RecordInvest inv : invs) {
                    if (inv.getInvest().anyType(WKInvest.WEIGHT)) {
                        return inv.getValue();
                    }
                }
            }
        }
        return null;
    }

    private Double getHeight(int rowIndex) {
        for (int actrow = rowIndex; actrow > -1; actrow--) {
            Object obj = otherInvestModel.getValueAt(actrow, 0);
            if (obj instanceof RecordInvest) {
                RecordInvest inv = (RecordInvest) obj;
                if (inv.getInvest().anyType(WKInvest.HEIGHT)) {
                    return inv.getValue();
                }
            } else if (obj instanceof RecordInvest[]) {
                RecordInvest[] invs = (RecordInvest[]) obj;
                for (RecordInvest inv : invs) {
                    if (inv.getInvest().anyType(WKInvest.HEIGHT)) {
                        return inv.getValue();
                    }
                }
            }
        }
        return null;
    }

    private Integer getNumberOfYears(int rowIndex) {
        if (MyLookup.getCurrentPatient() != null) {
            return Years.yearsBetween(MyLookup.getCurrentPatient().getBorn(), dateTime.toLocalDate()).getYears();
        }
        return null;
    }

    private Boolean isMale() {
        if (MyLookup.getCurrentPatient() != null) {
            return MyLookup.getCurrentPatient().isMale();
        }
        return null;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                if (dataActivity != null && rowIndex > -1) {
                    if (dataActivity[rowIndex][columnIndex] != null && dataActivity[rowIndex][columnIndex].length == 1) {
                        return dataActivity[rowIndex][columnIndex][0];
                    } else {
                        return dataActivity[rowIndex][columnIndex];
                    }
                }
                break;
            case 1:
                Integer years = getNumberOfYears(rowIndex);
                Boolean boy = isMale();
                Double weight = getWeight(rowIndex);
                Double tall = getHeight(rowIndex);
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
                food.minus(countAktEnergy(getValueAt(rowIndex, 0), getWeight(rowIndex)));
                return food;
        }

        return null;
    }

    private static Energy countAktEnergy(Object value, Double weight) {
        if (value instanceof RecordActivity[]) {
            RecordActivity[] values = (RecordActivity[]) value;
            if (values.length > 0 && values[0] != null && values[0].getActivity() != null) {
                Double sum = 0d;
                for (RecordActivity val : values) {
                    if (weight != null) {
                        sum += val.getActivity().getPower() * val.getDuration() * weight;
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
            if (rec.getDuration() != null && rec.getActivity() != null && weight != null) {
                Energy res = new Energy();
                res.setUnit("kJ");
                res.setValue(rec.getActivity().getPower() * rec.getDuration() * weight);
                return res;
            }
        }
        return null;
    }

    private static Double countFoodEnergy(RecordFood rec) {
        if (rec.getAmount() == null) {
            return null;
        }
        return rec.getUnit().getKoef() * rec.getAmount() * rec.getFood().getEnergy() / 100;
    }

    private DateTime getClickCellDate(int row, int column) {
        return dateTime.withDayOfMonth(row + 1).withTime(column == 0 ? 10 : 15, 0, 0, 0);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (columnIndex != 0) {
            return;
        }
        /*
        if (value instanceof Integer) {
            RecordActivity rec = MyLookup.getCurrentPatient().addRecordActivity(getClickCellDate(rowIndex, columnIndex),
                    null,//todo
                    value,
                    null);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(rec.getDatetime().getMillis());
            dataActivity[cal.get(Calendar.DAY_OF_MONTH) - 1][cal.get(Calendar.HOUR_OF_DAY) < 12 ? 0 : 1][0] = rec;
        }
         * 
         */
    }
}
