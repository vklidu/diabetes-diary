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

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PagePanel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.print.pdf.table.AbstractPdfSubTable;
import org.diabetesdiary.print.pdf.table.ActivityTable;
import org.diabetesdiary.print.pdf.table.DateTable;
import org.diabetesdiary.print.pdf.table.FoodTable;
import org.diabetesdiary.print.pdf.table.GlycemieTable;
import org.diabetesdiary.print.pdf.table.InsulinTable;
import org.diabetesdiary.print.pdf.table.InvestTable;
import org.diabetesdiary.print.pdf.table.SumTable;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author Jirka Majer
 */
public class PDFGenerator {

    private Rectangle pageSize = PageSize.A4;
    private static final int MARGIN = 10;
    private Patient patient;
    private boolean horizontal = true;
    private DateTime from = new DateTime().dayOfMonth().withMinimumValue();
    private DateTime to = new DateTime().dayOfMonth().withMaximumValue();
    private boolean blackWhite = false;
    private int fontSize = 9;
    private final List<AbstractPdfSubTable> subTables = Lists.newArrayList();

    static {
        FontFactory.register("org/diabetesdiary/print/pdf/fonts/DejaVuSans.ttf");
    }

    public PDFGenerator(Patient patient) {
        this.patient = patient;
        subTables.add(new DateTable(from, to, patient));
        subTables.add(new InsulinTable(from, to, patient));
        subTables.add(new GlycemieTable(from, to, patient));
        subTables.add(new InvestTable(from, to, patient));
        subTables.add(new FoodTable(from, to, patient));
        subTables.add(new ActivityTable(from, to, patient));
        subTables.add(new SumTable(from, to, patient));

    }

    public static Font getFont() {
        return FontFactory.getFont("dejavusans", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setColors(boolean colors) {
        this.blackWhite = !colors;
    }

    public void setPageSize(org.diabetesdiary.print.pdf.PageSize pageSize) {
        this.pageSize = pageSize.getPageSize();
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        for (AbstractPdfSubTable subTable : subTables) {
            subTable.setPatient(patient);
        }
    }

    public void reloadData() {
        for (AbstractPdfSubTable subTable : subTables) {
            subTable.dirtyData();
        }
    }

    public Patient getPatient() {
        return patient;
    }

    public void setVisibleInsulin(boolean visible) {
        setVisible(InsulinTable.class, visible);
    }

    public void setVisibleGlycemie(boolean visible) {
        setVisible(GlycemieTable.class, visible);
    }

    public void setVisibleInvest(boolean visible) {
        setVisible(InvestTable.class, visible);
    }

    public void setVisibleFood(boolean visible) {
        setVisible(FoodTable.class, visible);
    }

    public void setVisibleActivity(boolean visible) {
        setVisible(ActivityTable.class, visible);
    }

    public void setVisibleSum(boolean visible) {
        setVisible(SumTable.class, visible);
    }

    public void generateDocument(OutputStream outputStream) throws DocumentException {
        Document document = new Document(horizontal ? pageSize.rotate() : pageSize, MARGIN, MARGIN, MARGIN, MARGIN);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
        try {
            Font headFont = getFont();
            headFont.setSize(fontSize);
            headFont.setStyle(Font.BOLD);

            Font dataFont = getFont();
            dataFont.setSize(fontSize);

            document.addProducer();
            document.addAuthor("Jirka Majer");
            document.addCreator("Diabetes Diary (www.diabetes-diary.org)");
            document.addTitle("Diabetes diary - records");
            document.open();

            document.add(getHeader(dataFont));
            PdfPTable table = new PdfPTable(getWidths());
            table.setLockedWidth(false);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.getDefaultCell().setPadding(0);
            GeneratorHelper.HeaderBuilder header = GeneratorHelper.headerBuilder("");
            for (AbstractPdfSubTable sub : getVisibleModels()) {
                header.add(sub.getHeader());
            }
            for (PdfPCell cell : header.setFont(headFont).build()) {
                table.addCell(cell);
            }
            DateTime pom = from;
            while (!pom.isAfter(to)) {
                for (AbstractPdfSubTable sub : getVisibleModels()) {
                    for (int innerColumn = 0; innerColumn < sub.getColumnCount(); innerColumn++) {
                        table.addCell(sub.getData(blackWhite, dataFont, innerColumn, pom.toLocalDate()));
                    }
                }
                pom = pom.plusDays(1);
            }

            document.add(table);

            Font font = getFont();
            font.setColor(BaseColor.DARK_GRAY);
            Paragraph para = new Paragraph("www.diabetes-diary.org", font);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
        } finally {
            document.close();
            pdfWriter.close();
        }
    }

    private int getVisibleColumnCount() {
        int res = 0;
        for (AbstractPdfSubTable subTable : getVisibleModels()) {
            res += subTable.getColumnCount();
        }
        return res;
    }

    public ByteArrayOutputStream generateDocument() throws DocumentException, IOException {
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        try {
            generateDocument(baosPDF);
        } finally {
            baosPDF.close();
        }
        return baosPDF;
    }

    private float[] getWidths() {
        float[] widths = new float[getVisibleColumnCount()];
        int i = 0;
        for (AbstractPdfSubTable table : getVisibleModels()) {
            for (int innerColumn = 0; innerColumn < table.getColumnCount(); innerColumn++) {
                widths[i++] = table.getWidth(innerColumn);
            }
        }
        return widths;
    }

    private Element getHeader(Font font) {
        String date;
        if (from.getMonthOfYear() == to.getMonthOfYear() && from.dayOfMonth().withMinimumValue().equals(from) && to.dayOfMonth().withMaximumValue().equals(to)) {
            date = from.toString(DateTimeFormat.forPattern("MMMM yyyy"));
        } else {
            date = from.toString(DateTimeFormat.mediumDate()) + " - " + to.toString(DateTimeFormat.mediumDate());
        }

        if (patient == null) {
            return new Phrase(String.format("Není otevřen deník (%s)", date), font);
        } else {
            Double weight = patient.getWeightBefore(to);
            Double height = patient.getHeightBefore(to);
            String weightS = weight != null ? weight.toString() + " kg" : "? kg";
            String heightS = height != null ? height.toString() + " cm" : "? cm";
            return new Phrase(String.format("%s %s (%s), %s/%s", patient.getName(), patient.getSurname(), date, weightS, heightS), font);
        }
    }

    private Iterable<AbstractPdfSubTable> getVisibleModels() {
        return Iterables.filter(subTables, new Predicate<AbstractPdfSubTable>() {

            @Override
            public boolean apply(AbstractPdfSubTable input) {
                return input.isVisible();
            }
        });
    }

    private void setVisible(Class<? extends AbstractPdfSubTable> clazz, boolean visible) {
        Iterables.find(subTables, Predicates.instanceOf(clazz)).setVisible(visible);
    }

    private void previewPDFDocumentInImage() throws Exception {
        JFrame frame = new JFrame("PDF Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PagePanel panel = new PagePanel();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        // show the first page
        panel.showPage(new PDFFile(ByteBuffer.wrap(generateDocument().toByteArray())).getPage(0));
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    new PDFGenerator(null).previewPDFDocumentInImage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void setFrom(DateTime from) {
        this.from = from;
        for (AbstractPdfSubTable subTable : subTables) {
            subTable.setFrom(from);
        }
    }

    public void setTo(DateTime to) {
        this.to = to;
        for (AbstractPdfSubTable subTable : subTables) {
            subTable.setTo(to);
        }
    }
}
