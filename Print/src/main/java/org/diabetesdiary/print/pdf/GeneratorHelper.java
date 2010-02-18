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

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

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

        private Font font;

        private HeaderBuilder(String base) {
            super(base);
        }

        public HeaderBuilder setFont(Font font) {
            this.font = font;
            return this;
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
                    if (node.getMaxDepth() < node.getMaxDepthOfSisters()) {
                        cell.setRowspan(node.getMaxDepthOfSisters() - node.getMaxDepth() + 1);
                    }
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setBorder(Rectangle.BOX);
                    cell.setBorderWidth(2);
                    cell.setPhrase(new Phrase(node.getValue(), font));
                    table.addCell(cell);
                }
            });
            return table;
        }
    }
}
