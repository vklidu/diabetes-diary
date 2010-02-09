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
package org.diabetesdiary.calendar.table.model.recordeditor;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import org.diabetesdiary.calendar.ui.CalendarTopComponent;
import org.diabetesdiary.diary.utils.MyLookup;
import org.diabetesdiary.diary.api.DiaryRepository;
import org.diabetesdiary.diary.domain.RecordFood;
import org.joda.time.DateTime;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 *
 * @author Jiri Majer
 */
public class RecordFoodEditTableModel extends AbstractTableModel {

    private static NumberFormat format = NumberFormat.getInstance();
    private boolean detail = false;
    private DiaryRepository diary;
    private Date date;
    private Date dateFrom;
    private Date dateTo;
    private List<RecordFood> recordFoods;
    private static final String DELETE_ICO = "org/diabetesdiary/calendar/resources/delete16.png";

    /** Creates a new instance of CalendarTableModel */
    public RecordFoodEditTableModel(Date date) {
        diary = MyLookup.getDiaryRepo();
        this.date = date;
    }

    public List<RecordFood> getRecordFoods() {
        return recordFoods;
    }

    public void setDate(Date date) {
        this.date = date;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date.getTime());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            this.dateFrom = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            this.dateTo = cal.getTime();
        } else {
            this.dateFrom = null;
            this.dateTo = null;
        }
        fillData();
    }

    public void fillData() {
        //no data => end
        if (MyLookup.getCurrentPatient() == null || dateFrom == null || dateTo == null) {
            return;
        }
        recordFoods = MyLookup.getCurrentPatient().getRecordFoods(new DateTime(dateFrom), new DateTime(dateTo));
    }

    @Override
    public int getRowCount() {
        return (recordFoods == null ? 0 : recordFoods.size()) + 1;
    }

    @Override
    public int getColumnCount() {
        return isDetail() ? 12 : 7;
    }

    public double getSumAmount(int column) {
        double sumT = 0d;
        for (int i = 0; i < getRowCount() - 1; i++) {
            sumT += (Double) getDoubleValueInGramAt(i, column);
        }
        return sumT;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == getColumnCount() - 1) {
            if (getRowCount() > 1) {
                return new ImageIcon(Utilities.loadImage(DELETE_ICO, true));
            } else {
                return null;
            }
        }
        if (rowIndex == getRowCount() - 1) {
            switch (columnIndex) {
                case 0:
                    return NbBundle.getMessage(RecordFoodEditTableModel.class, "Celkem:");
                case 1:
                    return "";
                case 2:
                    return "";
                default:
                    return format.format(getSumAmount(columnIndex)) + " " + getUnitAt(rowIndex, columnIndex);
            }
        } else {
            RecordFood rec = recordFoods.get(rowIndex);
            double grams = rec.getAmount() * rec.getUnit().getKoef();
            switch (columnIndex) {
                case 0:
                    return rec.getFood().getName() + " " + getUnitAt(rowIndex, columnIndex);
                case 1:
                    return DateFormat.getTimeInstance(DateFormat.SHORT).format(rec.getDatetime().toDate());
                case 2:
                    return rec.getSeason().toString();
                case 3:
                    return format.format(rec.getTotalAmount()) + " " + getUnitAt(rowIndex, columnIndex);
                case 4:
                    return format.format(rec.getAmount()) + " " + getUnitAt(rowIndex, columnIndex);
                case 5:
                    return format.format(rec.getFood().getSugar() * grams / 100);
                case 6:
                    return format.format(rec.getFood().getFat() * grams / 100);
                case 7:
                    return format.format(rec.getFood().getProtein() * grams / 100);
                case 8:
                    return format.format(rec.getFood().getEnergy() * grams / 100);
                case 9:
                    return rec.getFood().getCholesterol() == null ? "?" : format.format(rec.getFood().getCholesterol() * grams / 100);
                case 10:
                    return rec.getFood().getRoughage() == null ? "?" : format.format(rec.getFood().getRoughage() * grams / 100);
            }
        }
        return "";
    }

    public double getDoubleValueInGramAt(int rowIndex, int columnIndex) {
        if (rowIndex < getRowCount() - 1) {
            RecordFood rec = recordFoods.get(rowIndex);
            double koef = rec.getUnit().getKoef();
            double grams = rec.getAmount() * koef;
            switch (columnIndex) {
                case 0:
                    return 0d;
                case 1:
                    return 0d;
                case 2:
                    return 0d;
                case 3:
                    return rec.getTotalAmount() * koef;
                case 4:
                    return rec.getAmount() * koef;
                case 5:
                    return rec.getFood().getSugar() * grams / 100;
                case 6:
                    return rec.getFood().getFat() * grams / 100;
                case 7:
                    return rec.getFood().getProtein() * grams / 100;
                case 8:
                    return rec.getFood().getEnergy() * grams / 100;
                case 9:
                    return rec.getFood().getCholesterol() * grams / 100;
                case 10:
                    return rec.getFood().getRoughage() * grams / 100;
            }
        }
        return 0d;
    }

    public String getUnitAt(int rowIndex, int columnIndex) {
        if (rowIndex == getRowCount() - 1) {
            switch (columnIndex) {
                case 8:
                    return "kJ";
                case 9:
                    return "mg";
                default:
                    return "g";
            }
        } else {
            RecordFood rec = recordFoods.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return "";
                case 1:
                    return "";
                case 2:
                    return "";
                case 3:
                    return rec.getUnit().getShortcut();
                case 4:
                    return rec.getUnit().getShortcut();
                case 8:
                    return "kJ";
                case 9:
                    return "mg";
                default:
                    return "g";
            }
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 4;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (MyLookup.getCurrentPatient() == null || columnIndex != 4 || value == null) {
            return;
        }
        if (rowIndex == getRowCount() - 1) {
            //posledni radek meni vse
            double oldVal = getSumAmount(columnIndex);
            try {
                double newVal = format.parse(value.toString()).doubleValue();
                double koef = newVal / oldVal;
                for (RecordFood rec : recordFoods) {
                    rec.update(rec.getAmount() * koef);
                    rec = diary.getRecordFood(rec.getId());//todo tohle asi nebude fungovat
                    CalendarTopComponent.getDefault().getModel().reloadData();
                    CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
                }
            } catch (ParseException ex) {
                return;
            }
        } else {
            //menim konkretni jidlo
            try {
                RecordFood rec = recordFoods.get(rowIndex);
                double koef = rec.getUnit().getKoef();
                double grams = rec.getAmount() * koef;
                rec.update(format.parse(value.toString()).doubleValue());
                rec = diary.getRecordFood(rec.getId());//todo tohle asi nebude fungovat
                CalendarTopComponent.getDefault().getModel().reloadData();
                CalendarTopComponent.getDefault().getModel().fireTableDataChanged();
            } catch (ParseException e) {
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        if (column == getColumnCount() - 1) {
            return "";
        }

        switch (column) {
            case 0:
                return NbBundle.getMessage(RecordFoodEditTableModel.class, "Jidlo");
            case 1:
                return NbBundle.getMessage(RecordFoodEditTableModel.class, "Time");
            case 2:
                return NbBundle.getMessage(RecordInsulinEditTableModel.class, "Season");               
            case 3:
                return NbBundle.getMessage(RecordFoodEditTableModel.class, "Celkem");
            case 4:
                return NbBundle.getMessage(RecordFoodEditTableModel.class, "Konzumace");
            case 5:
                return NbBundle.getMessage(RecordFoodEditTableModel.class, "Sacharidy_(g)");
            case 6:
                return NbBundle.getMessage(RecordFoodEditTableModel.class, "Tuky_(g)");
            case 7:
                return NbBundle.getMessage(RecordFoodEditTableModel.class, "Bilkoviny_(g)");
            case 8:
                return NbBundle.getMessage(RecordFoodEditTableModel.class, "Energie_(kJ)");
            case 9:
                return NbBundle.getMessage(RecordFoodEditTableModel.class, "Cholesterol_(mg)");
            case 10:
                return NbBundle.getMessage(RecordFoodEditTableModel.class, "Vlaknina_(g)");
            default:
                return "";
        }
    }

    public boolean isDetail() {
        return detail;
    }

    public void setDetail(boolean detail) {
        this.detail = detail;
        fireTableDataChanged();
        fireTableStructureChanged();
    }

    public Date getDate() {
        return date;
    }

    public RecordFood getRecordAt(int row) {
        if (row > -1 && row < getRowCount() - 1 && recordFoods != null) {
            return recordFoods.get(row);
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == getColumnCount() - 1) {
            return ImageIcon.class;
        }

        return super.getColumnClass(columnIndex);
    }
}
