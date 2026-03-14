package com.breakingrules.stock.productos.service;

import com.breakingrules.stock.productos.entity.VarianteProducto;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class EtiquetaService {

    public byte[] generarEtiquetas(List<VarianteProducto> variantes) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A6); // etiqueta chica
        PdfWriter.getInstance(document, baos);

        document.open();

        for (VarianteProducto v : variantes) {

            // nombre producto
            Paragraph nombre = new Paragraph(
                    v.getProducto().getNombre(),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)
            );
            nombre.setAlignment(Element.ALIGN_CENTER);

            document.add(nombre);
            document.add(Chunk.NEWLINE);

            // generar barcode
            Code128Writer writer = new Code128Writer();
            BitMatrix bitMatrix = writer.encode(
                    v.getCodigoBarras(),
                    BarcodeFormat.CODE_128,
                    300,
                    100
            );

            ByteArrayOutputStream barcodeStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", barcodeStream);

            Image barcode = Image.getInstance(barcodeStream.toByteArray());
            barcode.setAlignment(Image.ALIGN_CENTER);

            document.add(barcode);

            // texto del código
            Paragraph codigo = new Paragraph(
                    v.getCodigoBarras(),
                    FontFactory.getFont(FontFactory.COURIER, 12)
            );
            codigo.setAlignment(Element.ALIGN_CENTER);

            document.add(codigo);

            document.newPage(); // nueva etiqueta
        }

        document.close();

        return baos.toByteArray();
    }
}