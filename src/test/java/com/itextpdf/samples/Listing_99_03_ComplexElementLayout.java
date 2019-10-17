/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.ParagraphRenderer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Listing_99_03_ComplexElementLayout {

    public static final String DEST = "./target/Listing_99_03_ComplexElementLayout/Listing_99_03_ComplexElementLayout.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Listing_99_03_ComplexElementLayout().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));

        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            text.append("A very long text is here...");
        }
        Paragraph twoColumnParagraph = new Paragraph();
        twoColumnParagraph.setNextRenderer(new TwoColumnParagraphRenderer(twoColumnParagraph));
        twoColumnParagraph.add(text.toString());
        doc.add(twoColumnParagraph.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)));

        doc.add(new Paragraph("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));

        //Close document
        doc.close();
    }

    private static class TwoColumnParagraphRenderer extends ParagraphRenderer {

        public TwoColumnParagraphRenderer(Paragraph modelElement) {
            super(modelElement);
        }

        @Override
        public List<Rectangle> initElementAreas(LayoutArea area) {
            List<Rectangle> areas = new ArrayList<>();
            Rectangle firstArea = area.getBBox().clone();
            Rectangle secondArea = area.getBBox().clone();
            firstArea.setWidth(firstArea.getWidth() / 2 - 5);
            secondArea.setX(secondArea.getX() + secondArea.getWidth() / 2 + 5);
            secondArea.setWidth(firstArea.getWidth());
            areas.add(firstArea);
            areas.add(secondArea);
            return areas;
        }

        // If renderer overflows on the next area, iText uses getNextRender() method to create a renderer for the overflow part.
        // If getNextRenderer isn't overriden, the default method will be used and thus a default rather than custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new TwoColumnParagraphRenderer((Paragraph) modelElement);
        }
    }

}
