package com.petland.modules.dashboard.report.generate;

import com.petland.common.exception.FailedToGeneratePdfException;
import com.petland.modules.dashboard.model.Report;
import com.petland.common.utils.PDFStyle;
import com.petland.modules.dashboard.report.ReportFileGenerator;
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
public class GenerateReportPDF implements ReportFileGenerator {

    private final PDFStyle pdfStyler;

    public byte[] generate(Report report) {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            PDPage page = pdfStyler.createStyledPage(document);
            pdfStyler.addLogo(document, page, "/images/logo.png");

            try (PDPageContentStream content = pdfStyler.createTextContent(document, page)) {
                content.setFont(PDType1Font.HELVETICA_BOLD, 25);
                content.setNonStrokingColor(Color.BLACK);
                content.newLine(); content.newLine();
                content.showText("PetShop Billing Reports"); content.newLine();

                content.setFont(PDType1Font.HELVETICA, 15);
                content.showText("Total Revenue: R$" + report.getTotalRevenue()); content.newLine();
                content.showText("Quantity of items and services sold: " + report.getItemsQuantity()); content.newLine();
                content.showText("Total Operating Costs: R$" + report.getOperatingCost()); content.newLine();
                content.showText("Total Profit: R$" + report.getTotalProfit()); content.newLine(); content.newLine();

                content.setFont(PDType1Font.HELVETICA_BOLD, 18);
                content.showText("Issued by employee: "); content.newLine();

                content.setFont(PDType1Font.HELVETICA, 15);
                content.showText("ID: " + report.getEmployee().id()); content.newLine();
                content.showText("Name: " + report.getEmployee().name()); content.newLine();
                content.showText("Department: " + report.getEmployee().department()); content.newLine();
                content.showText("Report issue date: " + report.getIssueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
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
