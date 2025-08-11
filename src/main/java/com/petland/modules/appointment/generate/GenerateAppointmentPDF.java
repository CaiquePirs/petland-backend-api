package com.petland.modules.appointment.generate;

import com.petland.common.exception.FailedToGeneratePdfException;
import com.petland.modules.appointment.model.Appointment;
import com.petland.modules.dashboard.reports.pdf.PDFStyle;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GenerateAppointmentPDF {

    private final PDFStyle pdfStyler;

    public byte[] issue(Appointment appointment){
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            PDPage page = pdfStyler.createStyledPage(document);
            pdfStyler.addLogo(document, page, "/images/logo.png");

            try (PDPageContentStream content = pdfStyler.createTextContent(document, page)) {
                content.setFont(PDType1Font.HELVETICA_BOLD, 25);
                content.setNonStrokingColor(Color.BLACK);content.newLine();content.newLine();
                content.showText("Appointment successfully scheduled");content.newLine();

                content.setFont(PDType1Font.HELVETICA, 15);
                content.showText("Date: " + appointment.getAppointmentDate()); content.newLine();
                content.showText("Time: " + appointment.getAppointmentHour());content.newLine();
                content.showText("Status: " + appointment.getAppointmentStatus());content.newLine();
                content.showText("Appointment type: " + appointment.getAppointmentType());content.newLine();content.newLine();

                content.setFont(PDType1Font.HELVETICA_BOLD, 18);
                content.showText("Customer information: ");content.newLine();
                content.setFont(PDType1Font.HELVETICA, 15);
                content.showText("ID: " + appointment.getCustomer().getId());content.newLine();
                content.showText("Name: " + appointment.getCustomer().getName());content.newLine(); content.newLine();

                content.setFont(PDType1Font.HELVETICA_BOLD, 18);
                content.showText("Pet information: ");content.newLine();
                content.setFont(PDType1Font.HELVETICA, 15);
                content.showText("Pet ID: " + appointment.getPet().getId());content.newLine();
                content.showText("Pet name: " + appointment.getPet().getName());
                content.newLine();content.newLine();content.newLine();

                content.showText("Petland Reports © 2025");
                content.endText();
            }

            document.save(output);
            return output.toByteArray();

        } catch (IOException e) {
            throw new FailedToGeneratePdfException("Failed to generate appointment PDF: " + e.getMessage());
        }
    }
}
