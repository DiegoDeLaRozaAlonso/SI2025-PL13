package cd.socio.luismi.cancelReser;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

public class PDFCancelacion {

    public void generar(int idReserva, int idSocio, String motivo, double costo) {

        File carpeta = new File("cancelaciones");
        carpeta.mkdirs();

        String ruta = "cancelaciones/cancel_" + idReserva + ".pdf";

        try (PDDocument doc = new PDDocument()) {

            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                cs.beginText();
                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
                cs.newLineAtOffset(40, 750);
                cs.showText("Cancelación de Reserva #" + idReserva);
                cs.endText();

                int y = 700;

                String[] texto = {
                    "ID Socio: " + idSocio,
                    String.format("Precio: %.2f €", costo),
                    "Fecha emisión: " + LocalDate.now(),
                    "Motivo: " + motivo,
                    "Reembolso: Sí"
                };

                PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                for (String linea : texto) {
                    cs.beginText();
                    cs.setFont(font, 12);
                    cs.newLineAtOffset(40, y);
                    cs.showText(linea);
                    cs.endText();
                    y -= 20;
                }
            }

            doc.save(ruta);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}