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

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
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

    public static abstract class AbstractBuilder<T> {

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

        public AbstractBuilder add(AbstractBuilder<T> builder) {
            tree.addChild(builder.tree);
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

        public abstract T build();
    }

    public static class HeaderBuilder extends AbstractBuilder<List<PdfPCell>> {

        private Font font;

        private HeaderBuilder(String base) {
            super(base);
        }

        public HeaderBuilder setFont(Font font) {
            this.font = font;
            return this;
        }

        @Override
        public List<PdfPCell> build() {
            Preconditions.checkNotNull(font);
            final List<PdfPCell> table = Lists.newArrayList();
            tree.walkBreathFirst(new TreeContainer.RecursiveWalker<String>() {

                @Override
                public void onAction(TreeContainer<String> node) {
                    if (node.getValue() == null) {
                        return;
                    }
                    PdfPCell cell = new PdfPCell();
                    int colspan = node.getMaxWidth();
                    int rowspan = node.getMaxDepth() < node.getMaxDepthOfSisters() && node.getChildren().size() == 0 ? node.getMaxDepthOfSisters() - node.getMaxDepth() + 1 : 1;
                    cell.setColspan(colspan);
                    cell.setRowspan(rowspan);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setBorder(Rectangle.BOX);
                    cell.setBorderWidth(1);
                    cell.setPhrase(new Phrase(node.getValue(), font));
                    table.add(cell);
                }
            });
            return table;
        }
    }
}
