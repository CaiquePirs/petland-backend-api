package com.petland.common.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class PdfStyleGenerator {

    public PDPage createStyledPage(PDDocument document) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (PDPageContentStream content = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, false)) {
            content.setNonStrokingColor(245f / 255f, 236f / 255f, 220f / 255f);
            content.addRect(0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());
            content.fill();
        }
        return page;
    }

    public void addLogo(PDDocument document, PDPage page, String logoPath) throws IOException {
        try (PDPageContentStream content = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false)) {
            InputStream img = getClass().getResourceAsStream(logoPath);
            PDImageXObject logo = PDImageXObject.createFromByteArray(document, img.readAllBytes(), "logo");
            content.drawImage(logo, 50, 750, 100, 100);
        }
    }

    public PDPageContentStream createTextContent(PDDocument document, PDPage page) throws IOException {
        PDPageContentStream content = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false);
        content.beginText();
        content.setLeading(20f);
        content.newLineAtOffset(50, 750);
        return content;
    }
}