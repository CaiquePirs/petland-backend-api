package com.petland.modules.dashboard.generator;

import com.petland.common.exception.FailedToGeneratePdfException;
import com.petland.common.utils.PdfStyleGenerator;
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

    private final PdfStyleGenerator pdfStyler;

    public byte[] generateReport(Report report) {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            PDPage page = pdfStyler.createStyledPage(document);
            pdfStyler.addLogo(document, page, "/images/logo.png");

            try (PDPageContentStream content = pdfStyler.createTextContent(document, page)) {
                content.setFont(PDType1Font.HELVETICA_BOLD, 25);
                content.setNonStrokingColor(Color.BLACK);
                content.newLine(); content.newLine();
                content.showText("PetShop Relatório de Faturamento"); content.newLine();

                content.setFont(PDType1Font.HELVETICA, 15);
                content.showText("Total Faturado: R$" + report.totalRevenue()); content.newLine();
                content.showText("Quantidade de items e serviços vendidos: " + report.itemsQuantity()); content.newLine();
                content.showText("Total de custos operacionais: R$" + report.operatingCost()); content.newLine();
                content.showText("Total de lucro R$" + report.totalProfit()); content.newLine(); content.newLine();

                content.setFont(PDType1Font.HELVETICA_BOLD, 18);
                content.showText("Emitido pelo funcionário: "); content.newLine();

                content.setFont(PDType1Font.HELVETICA, 15);
                content.showText("ID: " + report.employee().id()); content.newLine();
                content.showText("Nome: " + report.employee().name()); content.newLine();
                content.showText("Departmento: " + report.employee().department()); content.newLine();
                content.showText("Data de emissão do relatório: " + report.issueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                content.newLine(); content.newLine(); content.newLine();

                content.showText("Petland Reports © 2025 - Documento Confidencial");
                content.endText();
            }

            document.save(output);
            return output.toByteArray();

        } catch (IOException e) {
            throw new FailedToGeneratePdfException("Failed to generate PDF: " + e.getMessage());
        }
    }
}
