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
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PagePanel;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.diabetesdiary.diary.domain.Patient;
import org.diabetesdiary.print.pdf.table.AbstractPdfSubTable;
import org.diabetesdiary.print.pdf.table.DateTable;
import org.diabetesdiary.print.pdf.table.GlycemieTable;
import org.diabetesdiary.print.pdf.table.InsulinTable;
import org.joda.time.LocalDate;

/**
 *
 * @author Jirka Majer
 */
public class PDFGenerator {

    public static final Font DEJAVU;

    private Patient patient;
    private boolean horizontal = false;
    private boolean insulin = true;
    private boolean glycemie = true;
    private boolean otherInvest = false;
    private boolean food = true;
    private boolean activity = false;
    private boolean sum = true;
    private LocalDate from = new LocalDate().dayOfMonth().withMinimumValue();
    private LocalDate to = new LocalDate().dayOfMonth().withMaximumValue();
    private boolean blackWhite = false;

    private final List<AbstractPdfSubTable> subTables = Lists.newArrayList();

    static {
        FontFactory.register("org/diabetesdiary/print/pdf/fonts/DejaVuSans.ttf");
        DEJAVU = FontFactory.getFont("dejavusans", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    }

    public PDFGenerator(Patient patient) {
        this.patient = patient;
        subTables.add(new DateTable(from, to));
        subTables.add(new InsulinTable(from, to));
        subTables.add(new GlycemieTable(from, to));
    }

    private float[] getWidths() {
        float[] res = new float[subTables.size()];
        int i = 0;
        for (AbstractPdfSubTable table : subTables) {
            res[i++] = table.getWidth();
        }
        return res;
    }

    public PDFFile generate() throws Exception {
        return new PDFFile(ByteBuffer.wrap(test().toByteArray()));
    }

    private ByteArrayOutputStream test() throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, baosPDF);
        document.open();
        document.addAuthor("Jirka Majer");
        document.addCreator("Diabetes Diary (www.diabetes-diary.org)");
        document.addTitle("Diabetes diary - records");
        
        document.add(new Phrase("Jirka Majer (březen 2010)", DEJAVU));

        PdfPTable table = new PdfPTable(getWidths());
        table.setLockedWidth(false);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.getDefaultCell().setPadding(0);
        for (AbstractPdfSubTable sub : subTables) {
            table.addCell(sub.getHeader());
        }
        for (AbstractPdfSubTable sub : subTables) {
            table.addCell(sub.getData(blackWhite));
        }
        document.add(table);
        document.close();
        pdfWriter.close();
        return baosPDF;
    }

    private void previewPDFDocumentInImage() throws Exception {
        JFrame frame = new JFrame("PDF Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PagePanel panel = new PagePanel();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        // show the first page
        panel.showPage(generate().getPage(0));
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

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public boolean isInsulin() {
        return insulin;
    }

    public void setInsulin(boolean insulin) {
        this.insulin = insulin;
    }

    public boolean isGlycemie() {
        return glycemie;
    }

    public void setGlycemie(boolean glycemie) {
        this.glycemie = glycemie;
    }

    public boolean isOtherInvest() {
        return otherInvest;
    }

    public void setOtherInvest(boolean otherInvest) {
        this.otherInvest = otherInvest;
    }

    public boolean isFood() {
        return food;
    }

    public void setFood(boolean food) {
        this.food = food;
    }

    public boolean isActivity() {
        return activity;
    }

    public void setActivity(boolean activity) {
        this.activity = activity;
    }

    public boolean isSum() {
        return sum;
    }

    public void setSum(boolean sum) {
        this.sum = sum;
    }
}
