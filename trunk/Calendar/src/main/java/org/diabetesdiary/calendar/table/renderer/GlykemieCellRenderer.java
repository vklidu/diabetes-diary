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
package org.diabetesdiary.calendar.table.renderer;

import java.awt.Color;
import java.text.DateFormat;
import java.text.NumberFormat;
import org.diabetesdiary.calendar.option.CalendarSettings;
import org.diabetesdiary.diary.domain.RecordInvest;
import org.diabetesdiary.diary.domain.WKInvest;

/**
 *
 * @author Jiri Majer
 */
public class GlykemieCellRenderer extends AbstractDiaryCellRenderer<Object> {

    private static NumberFormat format = NumberFormat.getInstance();
    private static Color lowGlyColor = new Color(55, 110, 200);
    private static Color normalGlyColor = new Color(10, 200, 100);
    private static Color highGlyColor = new Color(240, 40, 40);
    private static Color lowGlySelColor = new Color(30, 60, 110);
    private static Color normalGlySelColor = new Color(4, 80, 40);
    private static Color highGlySelColor = new Color(150, 20, 20);
    private static final String VALUES_SEPARATOR = ";";

    @Override
    protected String getText(Object value) {
        if (value instanceof RecordInvest) {
            RecordInvest rec = (RecordInvest) value;
            String res;
            if (rec.getValue() != null && rec.getInvest() != null) {
                if (rec.getInvest().anyType(WKInvest.ACETON, WKInvest.URINE_SUGAR)) {
                    res = getCharForValue(rec.getValue());
                } else if (rec.getInvest().anyType(WKInvest.MENSES)) {
                    res = getCharForMenzesValue(rec.getValue());
                } else {
                    res = format.format(rec.getValue());
                }
                return rec.getNotice() != null && rec.getNotice().length() > 0 ? res + "!" : res;
            }
        } else if (value instanceof RecordInvest[]) {
            RecordInvest[] values = (RecordInvest[]) value;
            StringBuilder res = new StringBuilder();
            if (values.length > 0 && values[0] != null && values[0].getInvest() != null) {
                for (RecordInvest val : values) {
                    if (val.getInvest().anyType(WKInvest.ACETON, WKInvest.URINE_SUGAR)) {
                        res.append(" " + getCharForValue(val.getValue()));
                    } else if (val.getInvest().anyType(WKInvest.MENSES)) {
                        res.append(" " + getCharForMenzesValue(val.getValue()));
                    } else {
                        res.append(VALUES_SEPARATOR + format.format(val.getValue()));
                    }
                    if (val.getNotice() != null && val.getNotice().length() > 0) {
                        res.append("!");
                    }
                }
                return res.substring(1);
            }
        }
        return null;
    }

    @Override
    protected String getToolTip(Object value) {
        if (value instanceof RecordInvest) {
            return createToolTip((RecordInvest) value);
        } else if (value instanceof RecordInvest[]) {
            return createToolTip((RecordInvest[]) value);
        }
        return null;
    }

    private static String getCharForValue(Double val) {
        if (val < 1) {
            return "-";
        } else if (val < 2) {
            return "\u2591";
        } else if (val < 3) {
            return "\u2592";
        } else if (val < 4) {
            return "\u2593";
        } else {
            return "\u2588";
        }
    }

    private static String getCharForMenzesValue(Double val) {
        if (val < 1) {
            return "";
        } else if (val < 2) {
            return "x";
        } else if (val < 3) {
            return "xx";
        } else {
            return "xxx";
        }
    }

    @Override
    protected Color getBackgroundColor(Object value, boolean selected) {
        if (value instanceof RecordInvest) {
            RecordInvest rec = (RecordInvest) value;
            if (rec.getInvest().getWKInvest() == WKInvest.GLYCEMIE) {
                return getGlycemieBackColor(rec.getValue(), selected);
            }
        } else if (value instanceof RecordInvest[]) {
            RecordInvest[] values = (RecordInvest[]) value;
            if (values[0].getInvest().anyType(WKInvest.GLYCEMIE)) {
                //average glycemie is count and the background color is set
                double sum = 0;
                for (RecordInvest val : values) {
                    sum += val.getValue();
                }
                return getGlycemieBackColor(sum / values.length, selected);
            }
        }
        return super.getBackgroundColor(value, selected);
    }

    private Color getGlycemieBackColor(Double value, boolean isSelected) {
        if (value < Double.valueOf(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_GLYKEMIE_LOW_NORMAL))) {
            return isSelected ? lowGlySelColor : lowGlyColor;
        } else if (value <= Double.valueOf(CalendarSettings.getSettings().getValue(CalendarSettings.KEY_GLYKEMIE_HIGH_NORMAL))) {
            return isSelected ? normalGlySelColor : normalGlyColor;
        } else {
            return isSelected ? highGlySelColor : highGlyColor;
        }
    }

    private static String createToolTip(RecordInvest rec) {
        if (rec == null || rec.getValue() == null || rec.getInvest() == null) {
            return null;
        }
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        String result = timeFormat.format(rec.getDatetime().toDate()) + "\n";
        result += rec.getInvest().getName() + ": " + format.format(rec.getValue()) + " " + rec.getInvest().getUnit();
        if (rec.getNotice() != null && rec.getNotice().length() > 0) {
            return result + "\n(" + rec.getNotice() + ")";
        }
        return result;
    }

    private static String createToolTip(RecordInvest[] values) {
        if (values == null || values.length < 1 || values[0] == null || values[0].getValue() == null || values[0].getInvest() == null) {
            return null;
        }
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        StringBuffer result = new StringBuffer();
        for (RecordInvest rec : values) {
            result.append(timeFormat.format(rec.getDatetime().toDate())).append('\n');
            result.append(rec.getInvest().getName()).append(": ").append(format.format(rec.getValue())).append(' ').append(rec.getInvest().getUnit());
            if (rec.getNotice() != null && rec.getNotice().length() > 0) {
                result.append("\n(").append(rec.getNotice()).append(')');
            }
            result.append('\n');
        }
        return result.toString();
    }
}
