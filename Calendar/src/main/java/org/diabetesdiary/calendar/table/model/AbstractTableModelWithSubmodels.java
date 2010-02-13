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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.diabetesdiary.calendar.table.header.ColumnGroup;
import org.diabetesdiary.calendar.table.header.GroupableTableHeader;
import org.diabetesdiary.calendar.utils.DataChangeEvent;

/**
 *
 * @author Jirka Majer
 */
public abstract class AbstractTableModelWithSubmodels extends AbstractTableModel {

    private LinkedHashSet<TableSubModel> submodels;
    private final JTable jTable;

    public AbstractTableModelWithSubmodels(JTable jTable) {
        submodels = new LinkedHashSet<TableSubModel>();
        this.jTable = jTable;
        
        addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
                    recreateTableHeader();
                }
            }
        });
    }

    public boolean addModel(TableSubModel model) {
        return submodels.add(model);
    }

    public boolean replaceModel(TableSubModel oldModel, TableSubModel newModel) {
        boolean res = false;
        LinkedHashSet<TableSubModel> newSubModels = new LinkedHashSet<TableSubModel>();
        Iterator<TableSubModel> it = submodels.iterator();
        while (it.hasNext()) {
            TableSubModel cur = it.next();
            if (oldModel.equals(cur)) {
                res = true;
                newSubModels.add(newModel);
            } else {
                newSubModels.add(cur);
            }
        }
        submodels = newSubModels;
        return res;
    }

    public boolean removeModel(TableSubModel model) {
        return submodels.remove(model);
    }

    private List<TableSubModel> getVisibleModels() {
        return Lists.newArrayList(Iterables.filter(submodels, new Predicate<TableSubModel>() {
            @Override
            public boolean apply(TableSubModel input) {
                return input.isVisible();
            }
        }));
    }

    public void invalidateData(DataChangeEvent evt) {
        for (TableSubModel model : submodels) {
            model.onDataChange(evt);
        }
    }

    @Override
    public int getColumnCount() {
        int sum = 0;
        for (TableSubModel model : getVisibleModels()) {
            sum += model.getColumnCount();
        }
        return sum;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || columnIndex < 0) {
            return null;
        }
        TableSubModel sub = getSubModel(columnIndex);
        if (sub != null) {
            return sub.getValueAt(rowIndex, getIndexInSubModel(columnIndex));
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        TableSubModel sub = getSubModel(columnIndex);
        if (sub != null) {
            return sub.isCellEditable(rowIndex, getIndexInSubModel(columnIndex));
        }
        return false;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        TableSubModel sub = getSubModel(columnIndex);
        if (sub != null) {
            sub.setValueAt(value, rowIndex, getIndexInSubModel(columnIndex));
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        TableSubModel sub = getSubModel(columnIndex);
        if (sub != null) {
            return sub.getColumnName(getIndexInSubModel(columnIndex));
        }
        return "";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        TableSubModel sub = getSubModel(columnIndex);
        if (sub != null) {
            return sub.getColumnClass(getIndexInSubModel(columnIndex));
        }
        return Object.class;
    }

    public int getIndexInSubModel(int column) {
        int index = 0;
        for (TableSubModel model : getVisibleModels()) {
            if (column >= index && column <= model.getColumnCount() + index - 1) {
                return column - index;
            }
            index = index + model.getColumnCount();
        }
        throw new IllegalStateException();
    }

    public TableSubModel getSubModel(int column) {
        int index = 0;
        for (TableSubModel model : getVisibleModels()) {
            if (column >= index && column <= model.getColumnCount() + index - 1) {
                return model;
            }
            index = index + model.getColumnCount();
        }
        return null;
    }

    protected JTableHeader recreateTableHeader() {
        GroupableTableHeader header = (GroupableTableHeader) jTable.getTableHeader();
        TableColumnModel cm = header.getTable().getColumnModel();
        int index = 0;
        header.removeAll();
        for (TableSubModel subModel : getVisibleModels()) {
            List<TableColumn> cols = Lists.newArrayList();
            for (int i = 0; i < subModel.getColumnCount(); i++) {
                cols.add(cm.getColumn(i + index));
                cm.getColumn(i + index).setCellRenderer(subModel.getCellRenderer(i));
                cm.getColumn(i + index).setCellEditor(subModel.getCellEditor(i));
            }
            ColumnGroup group = subModel.getColumnHeader(cols);
            if (group != null) {
                header.addColumnGroup(group);
            }
            index = index + subModel.getColumnCount();
        }
        return header;
    }
}
