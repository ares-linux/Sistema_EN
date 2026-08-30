package sistema_en;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
// IMPORTES NUEVOS PARA GOOGLE SHEETS
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.drive.model.File; // <--- Mantenemos este como predeterminado
import java.io.FileInputStream;
import java.io.InputStreamReader;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.Border;
import com.google.api.services.sheets.v4.model.Borders;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.DimensionProperties;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.NumberFormat;
import com.google.api.services.sheets.v4.model.RepeatCellRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.TextFormat;
import com.google.api.services.sheets.v4.model.UpdateDimensionPropertiesRequest;
import dao.TrabajoDAO;
import sistema_en.Objetos.Evidencia;
import sistema_en.Objetos.Trabajo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


/**
 *
 * @author ares
 */
public class GoogleDriveProvider {

    private static String url;
    
    public static final String APPLICATION_NAME = "Sistema EN";
    public static final JsonFactory JSON_FACTORY = com.google.api.client.json.gson.GsonFactory.getDefaultInstance();
    public static final String TOKENS_DIRECTORY_PATH = "tokens";
    public static final HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
    
    private static final List<String> SCOPES = Arrays.asList(
            DriveScopes.DRIVE,
            SheetsScopes.SPREADSHEETS
    );
    
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";

    public static Credential getCredentials(HttpTransport HTTP_TRANSPORT) throws Exception {
        java.io.File credentialsFile = new java.io.File(CREDENTIALS_FILE_PATH);
        if (!credentialsFile.exists()) {
            throw new Exception("No se encontró el archivo '" + CREDENTIALS_FILE_PATH + "' en la raíz del proyecto.");
        }

        GoogleClientSecrets clientSecrets;
        try (FileInputStream in = new FileInputStream(credentialsFile)) {
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        }

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(9999).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static Sheets getSheetsService() throws Exception {
        final HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static List<Evidencia> listarArchivosDeCarpeta(String idCarpetaDrive) {
        List<Evidencia> lista = new java.util.ArrayList<>();
        if (!estaConectado() || idCarpetaDrive == null) {
            return lista;
        }
        try {
            final HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
            Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            String query = "'" + idCarpetaDrive + "' in parents and trashed = false";
            com.google.api.services.drive.model.FileList resultado = service.files().list()
                    .setQ(query)
                    .setSpaces("drive")
                    .setFields("files(id, name, mimeType)")
                    .execute();

            List<com.google.api.services.drive.model.File> archivos = resultado.getFiles();
            if (archivos != null) {
                for (com.google.api.services.drive.model.File archivo : archivos) {
                    if (archivo.getMimeType() != null
                            && archivo.getMimeType().startsWith("application/vnd.google-apps.")) {
                        continue;
                    }
                    Evidencia e = new Evidencia();
                    e.setId_google_drive(archivo.getId());
                    e.setNombre_archivo(archivo.getName());
                    lista.add(e);
                }
            }
            
            
            
        } catch (Exception e) {
            System.out.println("❌ Ocurrió un error al listar los archivos de la carpeta:");
            e.printStackTrace();
        }
        return lista;
    }

    public static String subirImagenACarpeta(String rutaLocalImagen, String idCarpetaDrive) {
        if (!estaConectado()) {
            return null;
        }
        try {
            final HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
            Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            java.io.File fileToUpload = new java.io.File(rutaLocalImagen);
            if (!fileToUpload.exists()) {
                System.out.println("❌ Error: El archivo local a subir no existe: " + rutaLocalImagen);
                return null;
            }

            File fileMetadata = new File();
            fileMetadata.setName(fileToUpload.getName());
            if (idCarpetaDrive != null) {
                fileMetadata.setParents(Collections.singletonList(idCarpetaDrive));
            }

            FileContent mediaContent = new FileContent("image/jpeg", fileToUpload);

            File uploadedFile = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("✅ Imagen subida a Drive con ID: " + uploadedFile.getId());
            return uploadedFile.getId();
        } catch (Exception e) {
            System.out.println("❌ Ocurrió un error al subir la imagen a Google Drive:");
            e.printStackTrace();
            return null;
        }
    }

    public static boolean subirArchivo(String rutaArchivoLocal,String ruta) {
        try {
            final HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
            Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            java.io.File fileToUpload = new java.io.File(rutaArchivoLocal);
            if (!fileToUpload.exists()) {
                System.out.println("❌ Error: El archivo local a subir no existe: " + rutaArchivoLocal);
                return false;
            }

            String nombreArchivo = fileToUpload.getName();

            String fileId = null;
            try {
                Drive.Files.List request = service.files().list()
                        .setQ("name = '" + nombreArchivo + "' and trashed = false")
                        .setSpaces("drive")
                        .setFields("files(id, name)");
                com.google.api.services.drive.model.FileList files = request.execute();
                if (files.getFiles() != null && !files.getFiles().isEmpty()) {
                    fileId = files.getFiles().get(0).getId();
                }
            } catch (Exception e) {
                System.out.println("⚠️ Advertencia: No se pudo verificar si el archivo ya existe en Google Drive.");
            }

            File fileMetadata = new File();
            fileMetadata.setName(nombreArchivo);

            String idCarpetaDestino = ruta; // O el ID que necesites
            if (idCarpetaDestino != null) {
                fileMetadata.setParents(Collections.singletonList(idCarpetaDestino));
            }
            
            FileContent mediaContent = new FileContent("application/x-sqlite3", fileToUpload);

            File uploadedFile;
            if (fileId != null) {
                System.out.println("Actualizando archivo existente en Google Drive con ID: " + fileId + "...");
                uploadedFile = service.files().update(fileId, null, mediaContent)
                        .setFields("id")
                        .execute();
                System.out.println("✅ ¡Respaldo actualizado con éxito! ID en Drive: " + uploadedFile.getId());
            } else {
                System.out.println("Subiendo nuevo archivo a Google Drive...");
                uploadedFile = service.files().create(fileMetadata, mediaContent)
                        .setFields("id")
                        .execute();
                System.out.println("✅ ¡Respaldo completado con éxito! ID en Drive: " + uploadedFile.getId());
            }

        } catch (Exception e) {
            System.out.println("❌ Ocurrió un error al interactuar con Google Drive:");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean estaConectado() {
        java.io.File carpetaTokens = new java.io.File("tokens");
        return carpetaTokens.exists() && carpetaTokens.isDirectory() && carpetaTokens.list().length > 0;
    }

    public static boolean desconectar() {
        java.io.File carpetaTokens = new java.io.File("tokens");
        if (carpetaTokens.exists()) {
            java.io.File[] archivos = carpetaTokens.listFiles();
            if (archivos != null) {
                for (java.io.File f : archivos) {
                    f.delete();
                }
            }
            return carpetaTokens.delete(); 
        }
        return false;
    }
    
    public static String crearHojaCotizacion(String tituloArchivo, List<List<Object>> datosMatriz, int id_trabajo) {
        try {
            Sheets service = getSheetsService();

            Spreadsheet spreadsheet = new Spreadsheet()
                    .setProperties(new SpreadsheetProperties().setTitle(tituloArchivo));

            Spreadsheet createdSheet = service.spreadsheets().create(spreadsheet).execute();
            String spreadsheetId = createdSheet.getSpreadsheetId();

            try {
                Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
            
                TrabajoDAO trabajoDao = new TrabajoDAO();
                Trabajo trabajo = trabajoDao.obtenerTrabajoIdCarpeta(id_trabajo);
                String id_carpeta = (trabajo != null) ? trabajo.getId_carpeta() : null;
                
            String idCotizacion = buscarOCrearCarpeta(driveService, "Cotizaciones", id_carpeta);
                String idCarpetaDestino = buscarOCrearCarpeta(driveService, "Excel", idCotizacion);
                
            moverDesdeRaiz(driveService,spreadsheetId,idCarpetaDestino);
                
                System.out.println("✅ Hoja de cálculo movida exitosamente a la carpeta 'Excel'.");
            } catch (Exception e) {
                System.out.println("⚠️ Advertencia: No se pudo mover la hoja de cálculo a la carpeta específica.");
                e.printStackTrace(); // Opcional: te ayuda a ver si falla por algún detalle de permisos o red
            }
            
            int sheetId = createdSheet.getSheets().get(0).getProperties().getSheetId();

            ValueRange body = new ValueRange().setValues(datosMatriz);

            service.spreadsheets().values()
                    .update(spreadsheetId, "A1", body)
                    .setValueInputOption("USER_ENTERED")
                    .execute();

            aplicarFormatoCotizacion(service, spreadsheetId, sheetId, datosMatriz);

            System.out.println("✅ Hoja de cotización creada exitosamente. ID: " + spreadsheetId);
            url = "https://docs.google.com/spreadsheets/d/" + spreadsheetId + "/edit";
            
            return spreadsheetId;
        } catch (Exception e) {
            System.out.println("❌ Error al crear la hoja de cotización:");
            e.printStackTrace();
            return null;
        }
    }
    
    public static String Obtener_url_google_sheets(){
        return url;
    }
    
    private static void aplicarFormatoCotizacion(
        Sheets service,
        String spreadsheetId,
        int sheetId,
        List<List<Object>> datosMatriz) throws Exception {

        List<Request> requests = new java.util.ArrayList<>();

        requests.add(new Request()
                .setMergeCells(new com.google.api.services.sheets.v4.model.MergeCellsRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheetId)
                                .setStartRowIndex(0)
                                .setEndRowIndex(1)
                                .setStartColumnIndex(0)
                                .setEndColumnIndex(4))
                        .setMergeType("MERGE_ALL")));

        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheetId)
                                .setStartRowIndex(0)
                                .setEndRowIndex(1)
                                .setStartColumnIndex(0)
                                .setEndColumnIndex(4))
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setTextFormat(new TextFormat()
                                                .setBold(true)
                                                .setFontSize(18))
                                        .setHorizontalAlignment("CENTER")
                                        .setVerticalAlignment("MIDDLE")))
                        .setFields("userEnteredFormat")));

        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheetId)
                                .setStartRowIndex(1)
                                .setEndRowIndex(3)
                                .setStartColumnIndex(0)
                                .setEndColumnIndex(4))
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setTextFormat(new TextFormat().setBold(true))))
                        .setFields("userEnteredFormat.textFormat.bold")));

        int filaMateriales = 6;

        for (int i = 0; i < datosMatriz.size(); i++) {
            if (!datosMatriz.get(i).isEmpty()
                    && "Nombre del material".equals(datosMatriz.get(i).get(0))) {

                filaMateriales = i;
                break;
            }
        }

        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheetId)
                                .setStartRowIndex(filaMateriales)
                                .setEndRowIndex(filaMateriales + 1)
                                .setStartColumnIndex(0)
                                .setEndColumnIndex(3))
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setTextFormat(new TextFormat().setBold(true))
                                        .setHorizontalAlignment("CENTER")
                                        .setBorders(new Borders()
                                                .setTop(new Border().setStyle("SOLID"))
                                                .setBottom(new Border().setStyle("SOLID"))
                                                .setLeft(new Border().setStyle("SOLID"))
                                                .setRight(new Border().setStyle("SOLID")))))
                        .setFields("userEnteredFormat")));

        NumberFormat formatoMoneda = new NumberFormat()
                .setType("NUMBER")
                .setPattern("$#,##0.00");

        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheetId)
                                .setStartRowIndex(5)
                                .setEndRowIndex(filaMateriales - 1)
                                .setStartColumnIndex(1)
                                .setEndColumnIndex(2))
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setNumberFormat(formatoMoneda)))
                        .setFields("userEnteredFormat.numberFormat")));

        requests.add(new Request()
                .setUpdateDimensionProperties(
                        new UpdateDimensionPropertiesRequest()
                                .setRange(new DimensionRange()
                                        .setSheetId(sheetId)
                                        .setDimension("COLUMNS")
                                        .setStartIndex(0)
                                        .setEndIndex(1))
                                .setProperties(new DimensionProperties()
                                        .setPixelSize(300))
                                .setFields("pixelSize")));

        requests.add(new Request()
                .setUpdateDimensionProperties(
                        new UpdateDimensionPropertiesRequest()
                                .setRange(new DimensionRange()
                                        .setSheetId(sheetId)
                                        .setDimension("COLUMNS")
                                        .setStartIndex(1)
                                        .setEndIndex(2))
                                .setProperties(new DimensionProperties()
                                        .setPixelSize(130))
                                .setFields("pixelSize")));

        requests.add(new Request()
                .setUpdateDimensionProperties(
                        new UpdateDimensionPropertiesRequest()
                                .setRange(new DimensionRange()
                                        .setSheetId(sheetId)
                                        .setDimension("COLUMNS")
                                        .setStartIndex(2)
                                        .setEndIndex(3))
                                .setProperties(new DimensionProperties()
                                        .setPixelSize(100))
                                .setFields("pixelSize")));

        requests.add(new Request()
                .setUpdateDimensionProperties(
                        new UpdateDimensionPropertiesRequest()
                                .setRange(new DimensionRange()
                                        .setSheetId(sheetId)
                                        .setDimension("COLUMNS")
                                        .setStartIndex(3)
                                        .setEndIndex(4))
                                .setProperties(new DimensionProperties()
                                        .setPixelSize(130))
                                .setFields("pixelSize")));

        BatchUpdateSpreadsheetRequest batchRequest =
                new BatchUpdateSpreadsheetRequest()
                        .setRequests(requests);

        service.spreadsheets()
                .batchUpdate(spreadsheetId, batchRequest)
                .execute();
    }
    
    public static String buscarOCrearCarpeta(Drive driveService, String nombreCarpeta, String parentId) throws IOException {
        String query = "mimeType = 'application/vnd.google-apps.folder' and name = '" + nombreCarpeta + "' and trashed = false";
        
        if (parentId != null) {
            query += " and '" + parentId + "' in parents";
        } else {
            query += " and 'root' in parents";
        }

        com.google.api.services.drive.model.FileList resultado = driveService.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();

        if (resultado.getFiles() != null && !resultado.getFiles().isEmpty()) {
            return resultado.getFiles().get(0).getId();
        } else {
            File fileMetadata = new File();
            fileMetadata.setName(nombreCarpeta);
            fileMetadata.setMimeType("application/vnd.google-apps.folder");

            if (parentId != null) {
                fileMetadata.setParents(Collections.singletonList(parentId));
            }

            File carpetaCreada = driveService.files().create(fileMetadata)
                    .setFields("id")
                    .execute();

            return carpetaCreada.getId();
        }
    }

    public static String crearCarpetaTrabajo(String nombreTrabajo) {
    try {
        final HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // 1. Busca o crea la carpeta maestra "Trabajos" en la raíz de Drive
        String idTrabajos = buscarOCrearCarpeta(service, "Trabajos", null);

        // 2. Busca o crea la carpeta específica de este trabajo (Ej: Trabajo_142_Cliente_2026-08-19)
        String idTrabajoEspecifico = buscarOCrearCarpeta(service, nombreTrabajo, idTrabajos);

        // 3. (Opcional pero recomendado) Deja lista la carpeta general de "Evidencias" vacía
        buscarOCrearCarpeta(service, "Evidencias", idTrabajoEspecifico);
         buscarOCrearCarpeta(service, "Cotizaciones", idTrabajoEspecifico);
        
        // ¡Listo! Ya no creamos la carpeta con la fecha.
        // Retornamos el ID de la carpeta principal del trabajo para que lo guardes en tu Base de Datos
        return idTrabajoEspecifico;

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

    public static String obtenerIdCarpetaBaseDatos() {
        try {
            final HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
            Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            return buscarOCrearCarpeta(service, "Base de datos", null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void sincronizarTrabajosPendientes() {
    
        if (!estaConectado()) {
            return; 
        }
    
        new javax.swing.SwingWorker<Void, Void>() {
    
        @Override
        protected Void doInBackground() throws Exception {
            TrabajoDAO dao = new TrabajoDAO();
            List<Trabajo> listaTrabajos = dao.obtenerTrabajosPendientes();

            for (Trabajo trabajo : listaTrabajos) {
            
                int idTrabajo = trabajo.getId_trabajo();
            
                String nombreCarpeta = "Trabajo_" + trabajo.getId_trabajo() + "_" + trabajo.getId_cliente() + "_" + trabajo.getFecha_cr();
                String idCarpetaDrive = sistema_en.GoogleDriveProvider.crearCarpetaTrabajo(nombreCarpeta);
                System.out.println("✅ ¡Trabajo ID " + trabajo.getId_trabajo() + " creado con su carpeta de Drive sincronizada!");
            
                dao.actualizarTrabajoIdCarpeta(trabajo.getId_trabajo(), idCarpetaDrive);
                
                String Carpeta = "Creada";
                dao.actualizarTrabajoCarpeta(idTrabajo, Carpeta);
            }
            return null;
        }

        @Override
        protected void done() {
            javax.swing.JOptionPane.showMessageDialog(null, "¡Sincronización con Drive completada!");
        }
        }.execute();
    
    }
    
    public static void subirCotizacionesPdf(String ruta){
        if (!estaConectado()) {
            return; 
        }
    
        new javax.swing.SwingWorker<Void, Void>() {
    
        @Override
        protected Void doInBackground() throws Exception {
            
            final HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
            Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
            String idCotizaciones = buscarOCrearCarpeta(service, "Cotizaciones", null);
            String idCotizaciones_PDF = buscarOCrearCarpeta(service, "PDF", idCotizaciones);
            
            java.io.File carpeta = new java.io.File(ruta);
            java.io.File[] archivos = carpeta.listFiles();
            if (archivos != null) {
                for (java.io.File f : archivos) {
                System.out.println(f.getName());
                
                String ruta2 = ruta + f.getName();
                
                GoogleDriveProvider.subirArchivo(ruta2, idCotizaciones_PDF);
                }
            }

            return null;
        }
        
        @Override
        protected void done() {
            javax.swing.JOptionPane.showMessageDialog(null, "¡Sincronización con Drive completada!");
        }
        }.execute();
    }
    
    
    public static void moverDesdeRaiz(Drive service, String fileId, String nuevaCarpetaId) throws IOException {
        // Usamos "root" para remover el archivo de la raíz de Drive
        Drive.Files.Update update = service.files().update(fileId, null)
                .setAddParents(nuevaCarpetaId)
                .setRemoveParents("root")
                .setFields("id, parents");
            
        update.execute();
    }
    
    public static void descargarContenidoCarpeta(String idCarpetaDrive, String rutaDestinoLocal) {
    try {
        // 1. Inicializamos el servicio de Drive
        Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // 2. Creamos la consulta para buscar todos los archivos que estén dentro de esa carpeta y no estén borrados
        String query = "'" + idCarpetaDrive + "' in parents and trashed = false";
        
        com.google.api.services.drive.model.FileList resultado = driveService.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id, name, mimeType)")
                .execute();

        List<com.google.api.services.drive.model.File> archivos = resultado.getFiles();

        if (archivos != null && !archivos.isEmpty()) {
            // Aseguramos que la carpeta local de destino exista
            java.io.File carpetaDestino = new java.io.File(rutaDestinoLocal);
            if (!carpetaDestino.exists()) {
                carpetaDestino.mkdirs();
            }

            System.out.println("📁 Encontrados " + archivos.size() + " archivos. Descargando...");

            for (com.google.api.services.drive.model.File archivo : archivos) {
                String idArchivo = archivo.getId();
                String nombreArchivo = archivo.getName();
                String mimeType = archivo.getMimeType();

                // ⚠️ Nota importante sobre Google Docs / Sheets / Slides:
                // Los archivos nativos de Google (como tus hojas de cálculo creadas con Sheets) 
                // no se pueden descargar directamente con executeMediaAndDownloadTo() a menos que se exporten.
                // Si es un archivo normal (PDF, imagen, etc.), se descarga así:
                
                if (mimeType != null && mimeType.startsWith("application/vnd.google-apps.")) {
                    System.out.println("⚠️ Omitiendo archivo nativo de Google (requiere exportación): " + nombreArchivo);
                    continue; // O puedes manejar una exportación si lo necesitas
                }

                java.io.File archivoLocal = new java.io.File(carpetaDestino, nombreArchivo);
                
                try (java.io.FileOutputStream outputStream = new java.io.FileOutputStream(archivoLocal)) {
                    driveService.files().get(idArchivo).executeMediaAndDownloadTo(outputStream);
                    System.out.println("✅ Descargado: " + nombreArchivo);
                } catch (Exception e) {
                    System.out.println("❌ Error al descargar el archivo individual: " + nombreArchivo);
                }
            }
            System.out.println("🎉 ¡Proceso de descarga de carpeta finalizado!");
        } else {
            System.out.println("📂 La carpeta está vacía o no se encontraron archivos.");
        }

    } catch (Exception e) {
        System.out.println("❌ Ocurrió un error al listar o descargar el contenido de la carpeta:");
        e.printStackTrace();
    }
}
    
    
    public static void verificarYDescargarEvidencia(String idDrive, String rutaLocalDestino) {
    java.io.File archivoLocal = new java.io.File(rutaLocalDestino);
    
    // Si la imagen NO existe localmente en esta nueva PC, pero tenemos su ID de Drive...
    if (!archivoLocal.exists()) {
        try {
            System.out.println("🔄 Imagen no encontrada localmente. Descargándola desde Google Drive...");
            Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            
            // Creamos las carpetas locales si no existen
            archivoLocal.getParentFile().mkdirs();

            // Descargamos usando el ID de Drive guardado en la base de datos
            try (java.io.FileOutputStream outputStream = new java.io.FileOutputStream(archivoLocal)) {
                driveService.files().get(idDrive).executeMediaAndDownloadTo(outputStream);
            }
            System.out.println("✅ Imagen restaurada con éxito desde la nube.");
            
        } catch (Exception e) {
            System.out.println("❌ No se pudo descargar la imagen desde Drive.");
            e.printStackTrace();
        }
    } else {
        // Si ya existe localmente, la carga directo de la PC (¡súper rápido!)
        // System.out.println("📂 Cargando imagen desde el disco local.");
    }
}
    
    public static void limpiarCarpetaTemporal(String rutaTemporal) {
    try {
        java.io.File carpetaTemp = new java.io.File(rutaTemporal);
        if (carpetaTemp.exists()) {
            java.io.File[] archivos = carpetaTemp.listFiles();
            if (archivos != null) {
                for (java.io.File archivo : archivos) {
                    archivo.delete(); // Borra los archivos basura que no se seleccionaron
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
public static void guardarEvidenciaSeleccionada(String rutaTemporalOrigen, String rutaCarpetaDefinitiva, String nombreArchivo) {
    try {
        Path origen = Path.of(rutaTemporalOrigen, nombreArchivo);
        Path carpetaDestino = Path.of(rutaCarpetaDefinitiva);
        
        // Creamos la carpeta definitiva del trabajo si no existe
        if (!Files.exists(carpetaDestino)) {
            Files.createDirectories(carpetaDestino);
        }
        
        Path destino = carpetaDestino.resolve(nombreArchivo);
        
        // Copiamos el archivo de la temporal a la definitiva
        Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("✅ Evidencia guardada permanentemente en: " + destino.toString());
        
    } catch (Exception e) {
        System.out.println("❌ Error al guardar la evidencia seleccionada:");
        e.printStackTrace();
    }
}
    
}