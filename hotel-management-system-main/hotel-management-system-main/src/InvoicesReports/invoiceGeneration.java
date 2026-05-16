package InvoicesReports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class invoiceGeneration {

    public static void invoiceGenerate(String customerName, String customerEmail, String customerId, String roomId, double totalRent, int loyaltyPoints, Date checkInDate, Date checkOutDate, long duration, double roomRentPerNight) {
        try {
            // Create the PDFs directory if it doesn't exist
            Path pdfDirectory = Paths.get("PDFs");
            if (!Files.exists(pdfDirectory)) {
                Files.createDirectories(pdfDirectory);
            }

            // Generate the invoice file path
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String invoiceFileName = customerName.replace(" ", "_") + "-" + roomId + "-Invoice-" + timestamp + ".pdf";
            String invoicePath = pdfDirectory.resolve(invoiceFileName).toString();

            // Create the PDF document
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(invoicePath));
            document.open();

            // Add Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK);
            Paragraph title = new Paragraph("Hello World HMS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n")); // Spacer

            // Add Subtitle
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY);
            Paragraph subtitle = new Paragraph("Guest Invoice", subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);

            document.add(new Paragraph("\n"));

            // Create Table
            PdfPTable table = new PdfPTable(2); // Two-column table
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            PdfPCell header1 = new PdfPCell(new Phrase("Description", headerFont));
            header1.setBackgroundColor(BaseColor.GRAY);
            header1.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell header2 = new PdfPCell(new Phrase("Details", headerFont));
            header2.setBackgroundColor(BaseColor.GRAY);
            header2.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(header1);
            table.addCell(header2);

            // Determine room type
            String roomType = roomRentPerNight == 1000 ? "Single" : roomRentPerNight == 2000 ? "Double" : "Suite";
            double grossRent = duration * roomRentPerNight;

            // Add Table Rows
            table.addCell("Guest Name");
            table.addCell(customerName);

            table.addCell("Guest Email");
            table.addCell(customerEmail);

            table.addCell("Guest ID");
            table.addCell(customerId);

            table.addCell("Room ID");
            table.addCell(roomId);

            table.addCell("Room Type");
            table.addCell(roomType);

            table.addCell("Check-in Date");
            table.addCell(new SimpleDateFormat("EEE, MMM dd, yyyy").format(checkInDate));

            table.addCell("Check-out Date");
            table.addCell(new SimpleDateFormat("EEE, MMM dd, yyyy").format(checkOutDate));

            table.addCell("Room Rent/Night");
            table.addCell(String.format("Rs. %.2f", roomRentPerNight));

            table.addCell("Duration (days)");
            table.addCell(String.valueOf(duration));

            table.addCell("Gross Total Rent");
            table.addCell(String.format("Rs. %.2f", grossRent));

            table.addCell("Discounted Total Rent (After Loyalty Points)");
            table.addCell(String.format("Rs. %.2f", totalRent));

            table.addCell("Available Loyalty Points");
            table.addCell(String.valueOf(loyaltyPoints));

            document.add(table);

            // Add Footer
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
            Paragraph footer = new Paragraph("You have to Pay: Rs. " + totalRent + "\nThank you for staying with us!", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(20);
            document.add(footer);

            document.close();

            System.out.println("Invoice generated successfully: " + invoicePath);

        } catch (Exception e) {
            System.err.println("Error generating invoice: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
