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
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author Jirka Majer
 */
public class PDFGenerator {

    private Rectangle pageSize = PageSize.A4;
    public static final Font DEJAVU;
    private static final int MARGIN = 10;
    private Patient patient;
    private boolean horizontal = true;
    private LocalDate from = new LocalDate().dayOfMonth().withMinimumValue();
    private LocalDate to = new LocalDate().dayOfMonth().withMaximumValue();
    private boolean blackWhite = false;
    private int fontSize = 5;
    private final List<AbstractPdfSubTable> subTables = Lists.newArrayList();

    static {
        FontFactory.register("org/diabetesdiary/print/pdf/fonts/DejaVuSans.ttf");
        DEJAVU = FontFactory.getFont("dejavusans", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 5);
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
        setVisibleActivity(false);
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
            DEJAVU.setSize(fontSize);

            document.addProducer();
            document.addAuthor("Jirka Majer");
            document.addCreator("Diabetes Diary (www.diabetes-diary.org)");
            document.addTitle("Diabetes diary - records");
            document.open();

            document.add(getHeader());
            PdfPTable table = new PdfPTable(getWidths());
            table.setLockedWidth(false);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.getDefaultCell().setPadding(0);
            for (AbstractPdfSubTable sub : getVisibleModels()) {
                table.addCell(sub.getHeader(blackWhite, DEJAVU));
            }
            for (AbstractPdfSubTable sub : getVisibleModels()) {
                table.addCell(sub.getData(blackWhite, DEJAVU));
            }
            document.add(table);

            DEJAVU.setColor(BaseColor.DARK_GRAY);
            Paragraph para = new Paragraph("www.diabetes-diary.org", DEJAVU);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
        } finally {
            document.close();
            pdfWriter.close();
        }
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
        float[] res = new float[Iterables.size(getVisibleModels())];
        int i = 0;
        for (AbstractPdfSubTable table : getVisibleModels()) {
            res[i++] = table.getWidth();
        }
        return res;
    }

    private Element getHeader() {
        String date = from.getMonthOfYear() == to.getMonthOfYear() ? from.toString(DateTimeFormat.forPattern("MMMM yyyy")) : (from.toString(DateTimeFormat.mediumDate()) + to.toString(DateTimeFormat.mediumDate()));
        if (patient == null) {
            return new Phrase(date, DEJAVU);
        } else {
            return new Phrase(String.format("%s %s (%s)", patient.getName(), patient.getSurname(), date), DEJAVU);
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
}
