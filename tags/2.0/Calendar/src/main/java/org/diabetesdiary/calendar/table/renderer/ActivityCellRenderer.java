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

import org.diabetesdiary.diary.domain.Energy;
import org.diabetesdiary.calendar.table.model.SumModel;
import java.text.DateFormat;
import java.text.NumberFormat;
import org.diabetesdiary.diary.api.UnknownWeightException;
import org.diabetesdiary.diary.domain.RecordActivity;
import org.openide.util.NbBundle;

/**
 *
 * @author Jiri Majer
 */
public class ActivityCellRenderer extends AbstractDiaryCellRenderer<Object> {

    private static NumberFormat format = NumberFormat.getIntegerInstance();

    @Override
    protected String getText(Object value) {
        if (value instanceof Energy) {
            Energy rec = (Energy) value;
            return format.format(rec.getValue(Energy.Unit.kJ));
        } else if (value instanceof RecordActivity) {
            RecordActivity rec = (RecordActivity) value;
            try {
                return format.format(rec.getEnergy().getValue(Energy.Unit.kJ)) + (rec.getNotice() != null && rec.getNotice().length() > 0 ? "!" : "");
            } catch (UnknownWeightException e) {
                return NbBundle.getMessage(SumModel.class, "unknown.weight");
            }
        } else if (value instanceof RecordActivity[]) {
            RecordActivity[] values = (RecordActivity[]) value;
            if (values.length > 0 && values[0] != null && values[0].getActivity() != null) {
                boolean note = false;
                Double sum = 0d;
                try {
                    for (RecordActivity val : values) {
                        sum += val.getEnergy().getValue(Energy.Unit.kJ);
                        if (val.getNotice() != null && val.getNotice().length() > 0) {
                            note = true;
                        }
                    }
                    return format.format(sum) + (note ? "!" : "");
                } catch (UnknownWeightException ex) {
                    return NbBundle.getMessage(SumModel.class, "unknown.weight");
                }
            }
        } else if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    @Override
    protected String getToolTip(Object rec) {
        if (rec instanceof RecordActivity) {
            return createToolTip((RecordActivity) rec);
        } else if (rec instanceof RecordActivity[]) {
            return createToolTip((RecordActivity[]) rec);
        }
        return null;
    }

    private static String createToolTip(RecordActivity rec) {
        if (rec == null || rec.getDuration() == null || rec.getActivity() == null) {
            return null;
        }
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        String result = timeFormat.format(rec.getDatetime().toDate()) + "\n";
        result += rec.getActivity().getName() + ": " + format.format(rec.getDuration()) + " min";
        if (rec.getNotice() != null && rec.getNotice().length() > 0) {
            return result + "\n(" + rec.getNotice() + ")";
        }
        return result;
    }

    private static String createToolTip(RecordActivity[] values) {
        if (values == null || values.length < 1 || values[0] == null || values[0].getDuration() == null || values[0].getActivity() == null) {
            return null;
        }
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        StringBuffer result = new StringBuffer();
        for (RecordActivity rec : values) {
            result.append(timeFormat.format(rec.getDatetime().toDate())).append('\n');
            result.append(rec.getActivity().getName()).append(": ").append(format.format(rec.getDuration())).append(' ').append(" min");
            if (rec.getNotice() != null && rec.getNotice().length() > 0) {
                result.append("\n(").append(rec.getNotice()).append(')');
            }
            result.append('\n');
        }
        return result.toString();
    }
}
