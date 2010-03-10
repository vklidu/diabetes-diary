package org.diabetesdiary.print.print;

import com.sun.pdfview.PDFFile;
import java.awt.print.Book;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import org.openide.cookies.PrintCookie;

/**
 * print support
 *
 */
public class PDFPrintSupport implements PrintCookie {

    PDFFile pdf;
    String name;

    public PDFPrintSupport(PDFFile pdf, String name) {
        this.pdf = pdf;
        this.name = name;
    }

    @Override
    public void print() {
        PrinterJob pjob = PrinterJob.getPrinterJob();
        pjob.setJobName(this.name);
        Book book = new Book();
        PDFPrintPage pages = new PDFPrintPage(pdf);
        book.append(pages, PrinterJob.getPrinterJob().defaultPage(), pdf.getNumPages());

        pjob.setPageable(book);
        if (pjob.printDialog()) {
            new PrintThread(pages, pjob).start();
        }
    }

    class PrintThread extends Thread {

        PDFPrintPage ptPages;
        PrinterJob ptPjob;

        public PrintThread(PDFPrintPage pages, PrinterJob pjob) {
            ptPages = pages;
            ptPjob = pjob;
        }

        @Override
        public void run() {
            try {
                ptPages.show(ptPjob);
                ptPjob.print();
            } catch (PrinterException pe) {
            }
            ptPages.hide();
        }
    }
}
