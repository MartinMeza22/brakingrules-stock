package com.breakingrules.stock.venta.service;

import com.breakingrules.stock.venta.entity.Venta;
import com.breakingrules.stock.venta.entity.VentaDetalle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RemitoPdfService {

    public byte[] generarRemito(Venta venta, List<VentaDetalle> detalles) {

        try {

            // 📏 Tamaño 10.5cm x 17cm
            float width = 297.675f;
            float height = 481.95f;
            float topMargin = 141.75f;

            Rectangle pageSize = new Rectangle(width, height);
            Document document = new Document(pageSize, 20, 20, topMargin, 20);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            // 🎨 Fuentes más pequeñas y formales
            Font smallFont = new Font(Font.HELVETICA, 8, Font.NORMAL);
            Font smallBold = new Font(Font.HELVETICA, 8, Font.BOLD);
            Font totalFont = new Font(Font.HELVETICA, 9, Font.BOLD);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // 📅 Fecha
            document.add(new Paragraph("Fecha: " + venta.getFecha().format(formatter), smallFont));

            // 👤 Cliente
            String nombreCliente = venta.getCliente() != null
                    ? venta.getCliente().getNombre() + " " + venta.getCliente().getApellido()
                    : "Consumidor Final";

            document.add(new Paragraph("Cliente: " + nombreCliente, smallFont));
            document.add(new Paragraph(" "));

            // 🛍 Tabla productos
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3f, 1f, 2f, 2f});

            // Encabezados
            table.addCell(crearCelda("Producto", smallBold));
            table.addCell(crearCelda("Cant.", smallBold));
            table.addCell(crearCelda("P.Unit", smallBold));
            table.addCell(crearCelda("Subtotal", smallBold));

            for (VentaDetalle d : detalles) {

                String nombreProducto = d.getProducto() != null
                        ? d.getProducto().getNombre()
                        : "Producto";

                table.addCell(crearCelda(nombreProducto, smallFont));
                table.addCell(crearCelda(String.valueOf(d.getCantidad()), smallFont));
                table.addCell(crearCelda("$" + d.getPrecioUnitario().setScale(2, RoundingMode.HALF_UP), smallFont));
                table.addCell(crearCelda("$" + d.getSubtotal().setScale(2, RoundingMode.HALF_UP), smallFont));
            }

            document.add(table);
            document.add(new Paragraph(" "));

            // 💰 Totales
            BigDecimal total = venta.getTotal() != null ? venta.getTotal() : BigDecimal.ZERO;
            BigDecimal pagado = venta.getMontoPagado() != null ? venta.getMontoPagado() : BigDecimal.ZERO;

            document.add(new Paragraph("TOTAL: $" + total.setScale(2, RoundingMode.HALF_UP), totalFont));
            document.add(new Paragraph("Pagado: $" + pagado.setScale(2, RoundingMode.HALF_UP), smallFont));
            document.add(new Paragraph("Forma de pago: " + venta.getFormaPago(), smallFont));

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generando PDF: " + e.getMessage(), e);
        }
    }

    private PdfPCell crearCelda(String texto, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2); // menos padding = más compacto
        return cell;
    }
}