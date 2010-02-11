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
import org.diabetesdiary.calendar.table.header.ColumnGroup;
import org.diabetesdiary.calendar.table.Energy;
import org.diabetesdiary.calendar.table.editor.NumberEditor;
import org.diabetesdiary.calendar.table.renderer.ActivityCellRenderer;
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
public class ActivityModel extends AbstractRecordSubModel {

    private RecordActivity[][][] dataActivity;
    private List<RecordInvest> weights;
    private List<RecordInvest> heighes;
    private List<RecordFood> foods;

    public ActivityModel(DateTime month) {
        super(month);
    }

    @Override
    public ColumnGroup getColumnHeader(List<TableColumn> cols) {
        ColumnGroup gSum = new ColumnGroup(NbBundle.getMessage(ActivityModel.class, "Column.energy"));
        ColumnGroup gOut = new ColumnGroup(NbBundle.getMessage(ActivityModel.class, "Column.energy.out"));

        gSum.add(gOut);
        gOut.add(cols.get(0));
        gOut.add(cols.get(1));
        gSum.add(cols.get(2));
        gSum.add(cols.get(3));
        return gSum;
    }

    private void setData() {
        Collection<RecordActivity> data = MyLookup.getCurrentPatient().getRecordActivities(getFrom(), getTo());
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
        foods = MyLookup.getCurrentPatient().getRecordFoods(getFrom(), getTo());
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
        if (weights == null) {
            weights = MyLookup.getCurrentPatient().getRecordInvests(getFrom(), getTo(), WKInvest.WEIGHT);
        }
        Double result = null;
        for (RecordInvest weight : weights) {
            if (dateTime.withDayOfMonth(rowIndex + 1).isBefore(weight.getDatetime())) {
                break;
            }
            result = weight.getValue();
        }
        return result == null && !weights.isEmpty() ? weights.get(0).getValue() : result;
    }

    private Double getHeight(int rowIndex) {
        if (heighes == null) {
            heighes = MyLookup.getCurrentPatient().getRecordInvests(getFrom(), getTo(), WKInvest.HEIGHT);
        }
        Double result = null;
        for (RecordInvest heigh : heighes) {
            if (dateTime.withDayOfMonth(rowIndex + 1).isBefore(heigh.getDatetime())) {
                break;
            }
            result = heigh.getValue();
        }
        return result == null && !heighes.isEmpty() ? heighes.get(0).getValue() : result;
    }

    private Integer getNumberOfYears(int rowIndex) {
        if (MyLookup.getCurrentPatient() != null) {
            return Years.yearsBetween(MyLookup.getCurrentPatient().getBorn(), dateTime.withDayOfMonth(rowIndex + 1).toLocalDate()).getYears();
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
        if (dataActivity == null || foods == null) {
            setData();
        }
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
                if (weight != null && tall != null && years != null) {
                    Energy energy = new Energy(Energy.Unit.kcal);
                    if (boy) {
                        energy.setValue(66.473 + (13.7516 * weight) + (5.0033 * tall) - (6.755 * years));
                    } else {
                        energy.setValue(655.0955 + (9.5634 * weight) + (1.8496 * tall) - (4.6756 * years));
                    }
                    return energy;
                }
                return NbBundle.getMessage(SumModel.class, "unknown.weight.tall");
            case 2:
                Energy energ = new Energy(Energy.Unit.kJ);
                DateTime rowDateFrom = dateTime.withDayOfMonth(rowIndex + 1).toDateMidnight().toDateTime();
                DateTime rowDateTo = rowDateFrom.plusDays(1);
                for (RecordFood rec : foods) {
                    if (!rec.getDatetime().isBefore(rowDateFrom) && !rec.getDatetime().isAfter(rowDateTo)) {
                        energ.plus(new Energy(Energy.Unit.kJ, rec.getEnergyInKJ()));
                    }
                }
                return energ.getValue() > 0 ? energ : null;
            case 3:
                Object foo = getValueAt(rowIndex, 2);
                Energy food = new Energy(Energy.Unit.kJ, 0);
                if (foo instanceof Energy) {
                    food = (Energy) foo;
                }
                //metabolismus
                Object metab = getValueAt(rowIndex, 1);
                if (metab instanceof Energy) {
                    food.minus((Energy) metab);
                } else {
                    return NbBundle.getMessage(SumModel.class, "unknown.weight.tall");
                }
                //aktivity
                Double weight2 = getWeight(rowIndex);
                if (weight2 == null) {
                    return NbBundle.getMessage(SumModel.class, "unknown.weight");
                }
                Energy en = countAktEnergy(getValueAt(rowIndex, 0), weight2);
                if (en != null) {
                    food.minus(en);
                }
                return food;
        }

        return null;
    }

    private static Energy countAktEnergy(Object value, double weight) {
        if (value instanceof RecordActivity[]) {
            RecordActivity[] values = (RecordActivity[]) value;
            if (values.length > 0 && values[0] != null && values[0].getActivity() != null) {
                Double sum = 0d;
                for (RecordActivity val : values) {
                    sum += val.getActivity().getPower() * val.getDuration() * weight;
                }
                Energy res = new Energy(Energy.Unit.kJ);
                res.setValue(sum);
            }
        } else if (value instanceof RecordActivity) {
            RecordActivity rec = (RecordActivity) value;
            if (rec.getDuration() != null && rec.getActivity() != null) {
                Energy res = new Energy(Energy.Unit.kJ);
                res.setValue(rec.getActivity().getPower() * rec.getDuration() * weight);
                return res;
            }
        }
        return null;
    }

    public DateTime getClickCellDate(int row, int column) {
        return dateTime.withDayOfMonth(row + 1).withTime(column == 0 ? 10 : 15, 0, 0, 0);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (columnIndex != 0) {
            return;
        }
        if (value instanceof Integer) {
            DateTime recDateTime = getClickCellDate(rowIndex, columnIndex);
            RecordActivity edited = dataActivity[recDateTime.getDayOfMonth() - 1][recDateTime.getHourOfDay() < 12 ? 0 : 1][0];
            if (edited != null) {
                edited = edited.update((Integer) value);
            } else {
                edited = MyLookup.getCurrentPatient().addRecordActivity(getClickCellDate(rowIndex, columnIndex),
                    MyLookup.getDiaryRepo().getRandomActivity(),
                    (Integer) value,
                    null);
            }
            dataActivity[recDateTime.getDayOfMonth() - 1][recDateTime.getHourOfDay() < 12 ? 0 : 1][0] = edited;
        }
    }

    @Override
    public void invalidateData() {
        dataActivity = null;
        weights = null;
        heighes = null;
        foods = null;
    }

    @Override
    public TableCellRenderer getCellRenderer(int columnIndex) {
        return new ActivityCellRenderer();
    }

    @Override
    public TableCellEditor getCellEditor(int columnIndex) {
        return new NumberEditor<Integer, Object>(0, 1000) {
            @Override
            public Integer getValue(Object object) {
                if (object instanceof RecordActivity) {
                    return ((RecordActivity) object).getDuration();
                } else if (object instanceof RecordActivity[]) {
                    return ((RecordActivity[]) object)[0].getDuration();
                }
                return null;
            }
        };
    }
}
