package com.petland.modules.dashboard.reports.pdf;

import com.petland.common.exception.FailedToGeneratePdfException;
import com.petland.modules.dashboard.dtos.Report;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class IssueReportPDF {

    private final PDFStyle pdfStyler;

    public byte[] generateReport(Report report) {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            PDPage page = pdfStyler.createStyledPage(document);
            pdfStyler.addLogo(document, page, "/images/logo.png");

            try (PDPageContentStream content = pdfStyler.createTextContent(document, page)) {
                content.setFont(PDType1Font.HELVETICA_BOLD, 25);
                content.setNonStrokingColor(Color.BLACK);
                content.newLine(); content.newLine();
                content.showText("PetShop Billing Report"); content.newLine();

                content.setFont(PDType1Font.HELVETICA, 15);
                content.showText("Total Revenue: R$" + report.totalRevenue()); content.newLine();
                content.showText("Quantity of items and services sold: " + report.itemsQuantity()); content.newLine();
                content.showText("Total Operating Costs: R$" + report.operatingCost()); content.newLine();
                content.showText("Total Profit: R$" + report.totalProfit()); content.newLine(); content.newLine();

                content.setFont(PDType1Font.HELVETICA_BOLD, 18);
                content.showText("Issued by employee: "); content.newLine();

                content.setFont(PDType1Font.HELVETICA, 15);
                content.showText("ID: " + report.employee().id()); content.newLine();
                content.showText("Name: " + report.employee().name()); content.newLine();
                content.showText("Department: " + report.employee().department()); content.newLine();
                content.showText("Report issue date: " + report.issueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                content.newLine(); content.newLine(); content.newLine();

                content.showText("Petland Reports © 2025 - Confidential Document");
                content.endText();
            }

            document.save(output);
            return output.toByteArray();

        } catch (IOException e) {
            throw new FailedToGeneratePdfException("Failed to generate PDF: " + e.getMessage());
        }
    }
}
