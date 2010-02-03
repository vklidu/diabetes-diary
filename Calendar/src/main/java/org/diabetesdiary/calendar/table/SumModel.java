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

import java.util.Collection;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import org.diabetesdiary.calendar.ColumnGroup;
import org.diabetesdiary.calendar.FormatUtils;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.calendar.utils.DbLookUp;
import org.diabetesdiary.datamodel.pojo.FoodUnitDO;
import org.diabetesdiary.datamodel.pojo.InvestigationDO;
import org.diabetesdiary.datamodel.pojo.RecordFoodDO;
import org.diabetesdiary.datamodel.pojo.RecordInsulinDO;
import org.diabetesdiary.datamodel.pojo.RecordInvestDO;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class SumModel implements TableSubModel, Comparable<TableSubModel> {

    private int baseIndex;
    private TableSubModel foodModel;
    private TableSubModel insulinModel;
    private TableSubModel otherInvestModel;

    /** Creates a new instance of SumTableModel */
    public SumModel(int baseIndex, TableSubModel foodModel, TableSubModel insulinModel, TableSubModel otherInvestModel) {
        this.baseIndex = baseIndex;
        this.foodModel = foodModel;
        this.insulinModel = insulinModel;
        this.otherInvestModel = otherInvestModel;
    }

    public int getColumnCount() {
        return 4;
    }

    public ColumnGroup getColumnHeader(TableColumnModel columnModel) {
        ColumnGroup gSum = new ColumnGroup(NbBundle.getMessage(SumModel.class, "Column.suma"));
        gSum.add(columnModel.getColumn(baseIndex));
        gSum.add(columnModel.getColumn(baseIndex + 1));
        gSum.add(columnModel.getColumn(baseIndex + 2));
        gSum.add(columnModel.getColumn(baseIndex + 3));

        return gSum;
    }

    private static Double countSachUnits(RecordFoodDO rec, FoodUnitDO sachUnit) {
        if (rec.getAmount() == null) {
            return null;
        }
        FoodUnitDO unit = null;
        if (rec.getFood() != null && rec.getFood().getUnits() != null) {
            for (Object un : rec.getFood().getUnits()) {
                unit = (FoodUnitDO) un;
                if (unit.getId().getUnit().equals(rec.getUnit())) {
                    break;
                }
            }
        }
        if (unit == null) {
            unit = DbLookUp.getFoodAdmin().getFoodUnit(rec.getId().getIdFood(), rec.getUnit());
        }
        double sachUnits = unit.getKoef() * rec.getAmount() * rec.getFood().getSugar() / (100 * sachUnit.getKoef());
        return sachUnits;
    }

    public Object getValueAt(int row, int col) {
        double suma = 0;
        //suma insulin
        if (col == 0 && insulinModel != null) {
            for (int i = 0; i < insulinModel.getColumnCount(); i++) {
                Object obj = insulinModel.getValueAt(row, i);
                if (obj instanceof RecordInsulinDO) {
                    RecordInsulinDO rec = (RecordInsulinDO) obj;
                    suma += rec.getAmount();
                } else if (obj instanceof RecordInsulinDO[]) {
                    RecordInsulinDO[] recs = (RecordInsulinDO[]) obj;
                    for (RecordInsulinDO rec : recs) {
                        suma += rec.getAmount();
                    }
                } else if (obj instanceof RecordInsulinPumpBasal) {
                    RecordInsulinPumpBasal basal = (RecordInsulinPumpBasal) obj;
                    for (RecordInsulinDO rec : basal.getData()) {
                        if (rec != null) {
                            suma += rec.getAmount();
                        }
                    }
                }
            }
        //suma food units
        } else if (col == 1 && foodModel != null) {
            FoodUnitDO sachUnit = DbLookUp.getFoodAdmin().getFoodUnit(1, CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT));
            for (int i = 0; i < foodModel.getColumnCount(); i++) {
                Object obj = foodModel.getValueAt(row, i);
                if (obj instanceof RecordFoodDO) {
                    RecordFoodDO rec = (RecordFoodDO) obj;
                    if (rec != null) {
                        suma += countSachUnits(rec, sachUnit);
                    }
                } else if (obj instanceof RecordFoodDO[]) {
                    RecordFoodDO[] recs = (RecordFoodDO[]) obj;
                    for (RecordFoodDO rec : recs) {
                        if (rec != null) {
                            suma += countSachUnits(rec, sachUnit);
                        }
                    }
                }
            }
        //bolus/food
        } else if (col == 2 && foodModel != null && insulinModel != null) {
            double sumFood = 0;
            FoodUnitDO sachUnit = DbLookUp.getFoodAdmin().getFoodUnit(1, CalendarSettings.getSettings().getValue(CalendarSettings.KEY_CARBOHYDRATE_UNIT));
            for (int i = 0; i < foodModel.getColumnCount(); i++) {
                Object obj = foodModel.getValueAt(row, i);
                if (obj instanceof RecordFoodDO) {
                    RecordFoodDO rec = (RecordFoodDO) obj;
                    if (rec != null) {
                        sumFood += countSachUnits(rec, sachUnit);
                    }
                } else if (obj instanceof RecordFoodDO[]) {
                    RecordFoodDO[] recs = (RecordFoodDO[]) obj;
                    for (RecordFoodDO rec : recs) {
                        if (rec != null) {
                            sumFood += countSachUnits(rec, sachUnit);
                        }
                    }
                }
            }
            double sumBolus = 0;
            for (int i = 0; i < insulinModel.getColumnCount(); i++) {
                Object obj = insulinModel.getValueAt(row, i);
                if (obj instanceof RecordInsulinDO) {
                    RecordInsulinDO rec = (RecordInsulinDO) obj;
                    if (!rec.isBasal()) {
                        sumBolus += rec.getAmount();
                    }
                } else if (obj instanceof RecordInsulinDO[]) {
                    RecordInsulinDO[] recs = (RecordInsulinDO[]) obj;
                    for (RecordInsulinDO rec : recs) {
                        if (!rec.isBasal()) {
                            sumBolus += rec.getAmount();
                        }
                    }
                }
            }
            if (sumBolus != 0 && sumFood != 0) {
                suma = sumBolus / sumFood;
            } else {
                suma = 0;
            }
        //ins./kg
        } else if (col == 3 && otherInvestModel != null && insulinModel != null) {
            double sumIns = 0;
            for (int i = 0; i < insulinModel.getColumnCount(); i++) {
                Object obj = insulinModel.getValueAt(row, i);
                if (obj instanceof RecordInsulinDO) {
                    RecordInsulinDO rec = (RecordInsulinDO) obj;
                    sumIns += rec.getAmount();
                } else if (obj instanceof RecordInsulinDO[]) {
                    RecordInsulinDO[] recs = (RecordInsulinDO[]) obj;
                    for (RecordInsulinDO rec : recs) {
                        sumIns += rec.getAmount();
                    }
                } else if (obj instanceof RecordInsulinPumpBasal) {
                    RecordInsulinPumpBasal rec = (RecordInsulinPumpBasal) obj;
                    for (RecordInsulinDO ins : rec.getData()) {
                        if (ins != null) {
                            sumIns += ins.getAmount();
                        }
                    }
                }
            }
            double weight = -1d;
            for (int actrow = row; actrow > -1; actrow--) {
                Object obj = otherInvestModel.getValueAt(actrow, 0);
                if (obj instanceof RecordInvestDO) {
                    RecordInvestDO inv = (RecordInvestDO) obj;
                    if (inv.getId().getIdInvest() == InvestigationDO.Instances.WEIGHT.getID()) {
                        weight = inv.getValue();
                        break;
                    }
                } else if (obj instanceof RecordInvestDO[]) {
                    RecordInvestDO[] invs = (RecordInvestDO[]) obj;
                    for (RecordInvestDO inv : invs) {
                        if (inv.getId().getIdInvest() == InvestigationDO.Instances.WEIGHT.getID()) {
                            weight = inv.getValue();
                            break;
                        }
                    }
                }
            }
            if (weight == -1d) {
                return NbBundle.getMessage(SumModel.class, "unknown.weight");
            }
            if (sumIns != 0 && weight != 0) {
                suma = sumIns / weight;
            } else {
                suma = 0;
            }
        }
        return suma == 0 ? null : FormatUtils.getDoubleFormat().format(suma);
    }

    public Object getNewRecordValueAt(int rowIndex, int columnIndex) {
        return null;
    }

    public void setValueAt(Object value, int row, int col) {
        //nothing
    }

    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return NbBundle.getMessage(SumModel.class, "Column.insulin");
            case 1:
                return NbBundle.getMessage(SumModel.class, "Column.food");
            case 2:
                return NbBundle.getMessage(SumModel.class, "bulus.sacharid");
            case 3:
                return NbBundle.getMessage(SumModel.class, "insulin.per.kilogram");
            default:
                return "";
        }
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setData(Collection<?> data) {
        //nothing to do
    }

    public int getBaseIndex() {
        return baseIndex;
    }

    public void setBaseIndex(int baseIndex) {
        this.baseIndex = baseIndex;
    }

    public int compareTo(TableSubModel o) {
        return Integer.valueOf(getBaseIndex()).compareTo(o.getBaseIndex());
    }

    public int getRowCount() {
        throw new IllegalStateException("Don't use it.");
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }
}
