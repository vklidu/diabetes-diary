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
package org.diabetesdiary.print.pdf;

import com.google.common.collect.Lists;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import java.util.List;

/**
 *
 * @author Jirka Majer
 */
public class GeneratorHelper {

    private GeneratorHelper() {
    }

    public static HeaderBuilder headerBuilder(String base) {
        return new HeaderBuilder(base);
    }

    public static DataBuilder dataBuilder(int columnCount) {
        return new DataBuilder(columnCount);
    }

    public static abstract class AbstractBuilder {

        protected final TreeContainer<String> tree;
        private TreeContainer<String> current;

        private AbstractBuilder(String base) {
            tree = new TreeContainer<String>(base);
            current = tree;
        }

        public AbstractBuilder addColumn(String name) {
            current = current.addChild(name);
            return this;
        }

        public AbstractBuilder addSister(String name) {
            current = current.getParent().addChild(name);
            return this;
        }

        public AbstractBuilder getParent() {
            current = current.getParent();
            return this;
        }

        public abstract PdfPTable build();
    }

    public static class HeaderBuilder extends AbstractBuilder {

        private HeaderBuilder(String base) {
            super(base);
        }

        @Override
        public PdfPTable build() {
            final PdfPTable table = new PdfPTable(tree.getMaxWidth());
            table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            tree.walkRecursive(new TreeContainer.RecursiveWalker<String>() {

                @Override
                public void onNode(TreeContainer<String> node) {
                    PdfPCell cell = new PdfPCell();
                    if (node.getMaxWidth() > 0) {
                        cell.setColspan(node.getMaxWidth());
                    }
                    Font font = PDFGenerator.DEJAVU;
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setBorder(Rectangle.BOX);
                    cell.setBorderWidth(1);
                    cell.setPhrase(new Phrase(node.getValue(), font));
                    table.addCell(cell);
                }
            });
            return table;
        }
    }

    public static class DataBuilder {

        private final List<String> cols = Lists.newArrayList();
        private final int columnCount;

        private DataBuilder(int columnCount) {
            this.columnCount = columnCount;
        }

        public void addCell(String value) {
            cols.add(value);
        }

        public PdfPTable build() {
            final PdfPTable table = new PdfPTable(columnCount);
            for (String col : cols) {
                PdfPCell cell = new PdfPCell();
                Font font = PDFGenerator.DEJAVU;
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.BOX);
                cell.setBorderWidth(1);
                cell.setPhrase(new Phrase(col, font));
                table.addCell(cell);

            }
            return table;
        }
    }
}
