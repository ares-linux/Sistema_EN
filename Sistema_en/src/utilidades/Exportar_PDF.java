package utilidades;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import utilidades.UtilidadesRecursos;

public class Exportar_PDF {
    
    public static boolean exportarCotizacionPDF(
            String rutaArchivo, 
            String cliente, 
            String folio, 
            String fecha, 
            String estado,
            String subtotal, 
            String iva, 
            String total, 
            List<List<Object>> filasConceptos,
            List<List<Object>> filasMateriales) {

        Document documento = new Document();

        try {
            PdfWriter.getInstance(documento, new FileOutputStream(new File(rutaArchivo)));
            documento.open();

            
            // Usa el nombre de la clase seguidos de .class en lugar de getClass()
            java.net.URL urlImagen = UtilidadesRecursos.url(UtilidadesRecursos.LOGO);
            Image imagen;
            
            if (urlImagen != null) {
                imagen = Image.getInstance(urlImagen);
                imagen.scaleToFit(200, 100);
                documento.add(imagen);
            } else {
                System.out.println("No se encontró la imagen en los recursos.");
            }

            // Fuentes personalizadas
            Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.decode("#2f65ca"));
            Font fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.DARK_GRAY);
            Font fuenteTexto = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
            
            // 1. Encabezado del documento
            documento.add(new Paragraph("COTIZACIÓN", fuenteTitulo));
            documento.add(new Paragraph("Folio: " + folio + " | Fecha: " + fecha, fuenteTexto));
            documento.add(new Paragraph("Cliente: " + cliente + " | Estado: " + estado, fuenteTexto));
            documento.add(new Paragraph(" ")); // Espacio en blanco

            // 2. Tabla de Conceptos
            documento.add(new Paragraph("Conceptos:", fuenteSubtitulo));
            PdfPTable tablaConceptos = new PdfPTable(2); // 2 columnas
            tablaConceptos.setWidthPercentage(100);
            
            tablaConceptos.addCell(new PdfPCell(new Phrase("Concepto", fuenteSubtitulo)));
            tablaConceptos.addCell(new PdfPCell(new Phrase("Precio", fuenteSubtitulo)));

            for (List<Object> fila : filasConceptos) {
                tablaConceptos.addCell(fila.get(0) != null ? fila.get(0).toString() : "");
                tablaConceptos.addCell(fila.get(2) != null ? fila.get(2).toString() : "");
            }
            documento.add(tablaConceptos);
            documento.add(new Paragraph(" "));

            // 3. Tabla de Materiales
            documento.add(new Paragraph("Materiales:", fuenteSubtitulo));
            PdfPTable tablaMateriales = new PdfPTable(4); // 4 columnas
            tablaMateriales.setWidthPercentage(100);
            
            tablaMateriales.addCell(new PdfPCell(new Phrase("Material", fuenteSubtitulo)));
            tablaMateriales.addCell(new PdfPCell(new Phrase("Precio Uni", fuenteSubtitulo)));
            tablaMateriales.addCell(new PdfPCell(new Phrase("Cantidad", fuenteSubtitulo)));
            tablaMateriales.addCell(new PdfPCell(new Phrase("Total", fuenteSubtitulo)));

            for (List<Object> fila : filasMateriales) {
                tablaMateriales.addCell(fila.get(0) != null ? fila.get(0).toString() : "");
                tablaMateriales.addCell(fila.get(1) != null ? fila.get(1).toString() : "");
                tablaMateriales.addCell(fila.get(2) != null ? fila.get(2).toString() : "");
                tablaMateriales.addCell(fila.get(3) != null ? fila.get(3).toString() : "");
            }
            documento.add(tablaMateriales);
            documento.add(new Paragraph(" "));

            // 4. Totales
            documento.add(new Paragraph("Subtotal: " + subtotal, fuenteTexto));
            documento.add(new Paragraph("IVA: " + iva, fuenteTexto));
            documento.add(new Paragraph("Total: " + total, fuenteSubtitulo));

            documento.close();
            System.out.println("✅ PDF generado con éxito en: " + rutaArchivo);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean exportarInformePDF(
            String rutaArchivo, 
            String periodo,  
            String estado,
             
            List<List<Object>> filasTrabajos,
            List<List<Object>> filasClientes,
            List<List<Object>> filasCotizaciones,
            List<List<Object>> filasOtros) {

        Document documento = new Document();

        try {
            PdfWriter.getInstance(documento, new FileOutputStream(new File(rutaArchivo)));
            documento.open();

            
            // Usa el nombre de la clase seguidos de .class en lugar de getClass()
            java.net.URL urlImagen = UtilidadesRecursos.url(UtilidadesRecursos.LOGO);
            Image imagen;
            
            if (urlImagen != null) {
                imagen = Image.getInstance(urlImagen);
                imagen.scaleToFit(200, 100);
                documento.add(imagen);
            } else {
                System.out.println("No se encontró la imagen en los recursos.");
            }

            // Fuentes personalizadas
            Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.decode("#2f65ca"));
            Font fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.DARK_GRAY);
            Font fuenteTexto = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
            
            // 1. Encabezado del documento
            documento.add(new Paragraph("INFORME", fuenteTitulo));
            documento.add(new Paragraph("Periodo: " + periodo + " | Estado: " + estado, fuenteTexto));
            documento.add(new Paragraph(" ")); // Espacio en blanco

            // 2. Tabla de Trabajos
            documento.add(new Paragraph("Trabajos:", fuenteSubtitulo));
            PdfPTable tablaTrabajos = new PdfPTable(2); // 2 columnas
            tablaTrabajos.setWidthPercentage(100);
            
            tablaTrabajos.addCell(new PdfPCell(new Phrase("Estadistica", fuenteSubtitulo)));
            tablaTrabajos.addCell(new PdfPCell(new Phrase("Resultado", fuenteSubtitulo)));

            for (List<Object> fila : filasTrabajos) {
                tablaTrabajos.addCell(fila.get(0) != null ? fila.get(0).toString() : "");
                tablaTrabajos.addCell(fila.get(1) != null ? fila.get(1).toString() : "");
            }
            documento.add(tablaTrabajos);
            documento.add(new Paragraph(" "));

            // 3. Tabla de Clientes
            documento.add(new Paragraph("Clientes:", fuenteSubtitulo));
            PdfPTable tablaClientes = new PdfPTable(2); // 2 columnas
            tablaClientes.setWidthPercentage(100);
            
            tablaClientes.addCell(new PdfPCell(new Phrase("Estadistica", fuenteSubtitulo)));
            tablaClientes.addCell(new PdfPCell(new Phrase("Resultado", fuenteSubtitulo)));
            
            for (List<Object> fila : filasClientes) {
                tablaClientes.addCell(fila.get(0) != null ? fila.get(0).toString() : "");
                tablaClientes.addCell(fila.get(1) != null ? fila.get(1).toString() : "");
            }
            documento.add(tablaClientes);
            documento.add(new Paragraph(" "));
            
             // 4. Tabla de Cotizaciones
            documento.add(new Paragraph("Cotizaciones:", fuenteSubtitulo));
            PdfPTable tablaCotizaciones = new PdfPTable(2); // 2 columnas
            tablaCotizaciones.setWidthPercentage(100);
            
            tablaCotizaciones.addCell(new PdfPCell(new Phrase("Estadistica", fuenteSubtitulo)));
            tablaCotizaciones.addCell(new PdfPCell(new Phrase("Resultado", fuenteSubtitulo)));

            for (List<Object> fila : filasCotizaciones) {
                tablaCotizaciones.addCell(fila.get(0) != null ? fila.get(0).toString() : "");
                tablaCotizaciones.addCell(fila.get(1) != null ? fila.get(1).toString() : "");
            }
            documento.add(tablaCotizaciones);
            documento.add(new Paragraph(" "));
            
            // 5. Tabla de Otros
            documento.add(new Paragraph("Otros:", fuenteSubtitulo));
            PdfPTable tablaOtros = new PdfPTable(2); // 2 columnas
            tablaOtros.setWidthPercentage(100);
            
            tablaOtros.addCell(new PdfPCell(new Phrase("Estadistica", fuenteSubtitulo)));
            tablaOtros.addCell(new PdfPCell(new Phrase("Resultado", fuenteSubtitulo)));

            for (List<Object> fila : filasOtros) {
                tablaOtros.addCell(fila.get(0) != null ? fila.get(0).toString() : "");
                tablaOtros.addCell(fila.get(1) != null ? fila.get(1).toString() : "");
            }
            documento.add(tablaOtros);
            documento.add(new Paragraph(" "));

            System.out.println("✅ PDF generado con éxito en: " + rutaArchivo);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (documento.isOpen()) {
                documento.close();
            }
        }
    }
    
}