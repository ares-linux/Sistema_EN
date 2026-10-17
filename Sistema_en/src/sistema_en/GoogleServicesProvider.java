package sistema_en;

import utilidades.UtilidadesRutas;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
// IMPORTES NUEVOS PARA GOOGLE SHEETS
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.drive.model.File; // <--- Mantenemos este como predeterminado
import com.google.api.services.gmail.Gmail;
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
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import dao.ConfiguracionesDAO;
import java.time.LocalTime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author ares
 */
public class GoogleServicesProvider {

    private static String url;
    
    public static final String APPLICATION_NAME = "Sistema EN";
    public static final JsonFactory JSON_FACTORY = com.google.api.client.json.gson.GsonFactory.getDefaultInstance();
    public static final String TOKENS_DIRECTORY_PATH = "tokens";
    public static final HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
    
    private static final List<String> SCOPES = Arrays.asList(
            DriveScopes.DRIVE,
            SheetsScopes.SPREADSHEETS,
            GmailScopes.GMAIL_SEND,
            CalendarScopes.CALENDAR_EVENTS
    );
    
    public static final String CREDENTIALS_FILE_PATH = "credentials.json";

    public static synchronized com.google.api.client.http.HttpRequestInitializer getCredentials(final HttpTransport HTTP_TRANSPORT) throws Exception {
        java.io.File credentialsFile = UtilidadesRutas.buscarCredenciales();
        if (credentialsFile == null) {
            throw new Exception("No se encontró el archivo '" + CREDENTIALS_FILE_PATH + "' junto al ejecutable. "
                    + "Rutas buscadas: " + UtilidadesRutas.archivoConfig(CREDENTIALS_FILE_PATH).getAbsolutePath()
                    + ", " + UtilidadesRutas.archivoConfigUsuario(CREDENTIALS_FILE_PATH).getAbsolutePath()
                    + ", " + new java.io.File(System.getProperty("user.dir"), CREDENTIALS_FILE_PATH).getAbsolutePath());
        }

        GoogleClientSecrets clientSecrets;
        try (FileInputStream in = new FileInputStream(credentialsFile)) {
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        }

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(UtilidadesRutas.directorioTokens()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(9999).build();
        final Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return new com.google.api.client.http.HttpRequestInitializer() {
            @Override
            public void initialize(com.google.api.client.http.HttpRequest request) throws java.io.IOException {
                credential.initialize(request);

                final com.google.api.client.http.HttpUnsuccessfulResponseHandler defaultHandler = request.getUnsuccessfulResponseHandler();
                request.setUnsuccessfulResponseHandler(new com.google.api.client.http.HttpUnsuccessfulResponseHandler() {
                    @Override
                    public boolean handleResponse(com.google.api.client.http.HttpRequest req, com.google.api.client.http.HttpResponse response, boolean supportsRetry) throws java.io.IOException {
                        boolean retry = false;
                        if (defaultHandler != null) {
                            try {
                                retry = defaultHandler.handleResponse(req, response, supportsRetry);
                            } catch (com.google.api.client.auth.oauth2.TokenResponseException e) {
                                if (e.getDetails() != null && "invalid_grant".equals(e.getDetails().getError())) {
                                    System.out.println("Token expirado o revocado. Desconectando para forzar re-autenticación...");
                                    desconectar();
                                    try {
                                        com.google.api.client.http.HttpRequestInitializer newCred = getCredentials(HTTP_TRANSPORT);
                                        newCred.initialize(req);
                                        return true; // Reintentar la petición original
                                    } catch (Exception ex) {
                                        throw e;
                                    }
                                }
                                throw e;
                            }
                        }
                        return retry;
                    }
                });
            }
        };
    }

    public static Sheets getSheetsService() throws Exception {
        final HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    public static Gmail getGmailService() throws Exception {
        final HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    public static Calendar getCalendarService() throws Exception {
        final HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static boolean verificarConexionCalendar() {
        if (!estaConectado()) return false;
        try {
            Calendar service = getCalendarService();
            // Llamada ligera a la API para verificar si el token tiene permisos y es válido
            service.calendarList().list().setMaxResults(1).execute();
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error al verificar la conexión de Google Calendar:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Calcula el intervalo del evento a partir de las fechas del trabajo.
     *
     * @return un array con inicio y fin, o null si el trabajo no tiene fecha de inicio
     */
    private static EventDateTime[] calcularIntervalo(Trabajo trabajo, LocalTime horaDefecto) {
        java.time.LocalDate fechaInicio = trabajo.getFecha_inicio();
        if (fechaInicio == null) {
            return null;
        }

        if (horaDefecto == null) {
            horaDefecto = LocalTime.of(9, 0); // 9:00 AM por defecto
        }

        java.time.LocalDateTime inicioDateTime = fechaInicio.atTime(horaDefecto);
        java.time.ZonedDateTime zdtInicio = inicioDateTime.atZone(java.time.ZoneId.systemDefault());
        com.google.api.client.util.DateTime startDateTime = new com.google.api.client.util.DateTime(zdtInicio.toInstant().toEpochMilli());
        EventDateTime inicio = new EventDateTime().setDateTime(startDateTime)
                .setTimeZone(java.time.ZoneId.systemDefault().getId());

        // Fecha de fin (si es null, asignamos 1 hora después del inicio)
        java.time.LocalDate fechaFin = trabajo.getFecha_fin();
        java.time.LocalDateTime finDateTime = (fechaFin != null) ? fechaFin.atTime(horaDefecto).plusHours(1) : inicioDateTime.plusHours(1);

        if (!finDateTime.isAfter(inicioDateTime)) {
            finDateTime = inicioDateTime.plusHours(1); // Forzar que al menos dure 1 hora
        }
        java.time.ZonedDateTime zdtFin = finDateTime.atZone(java.time.ZoneId.systemDefault());
        com.google.api.client.util.DateTime endDateTime = new com.google.api.client.util.DateTime(zdtFin.toInstant().toEpochMilli());
        EventDateTime fin = new EventDateTime().setDateTime(endDateTime)
                .setTimeZone(java.time.ZoneId.systemDefault().getId());

        return new EventDateTime[]{inicio, fin};
    }

    /**
     * Arma el evento con los datos que la aplicacion administra. Lo comparten la
     * creacion y la actualizacion, para que las dos rutas no diverjan.
     *
     * @return el evento, o null si el trabajo no tiene fecha de inicio
     */
    private static Event construirEvento(Trabajo trabajo, LocalTime horaDefecto) {
        EventDateTime[] intervalo = calcularIntervalo(trabajo, horaDefecto);
        if (intervalo == null) {
            System.out.println("⚠️ El trabajo no tiene fecha de inicio, no se puede crear el evento.");
            return null;
        }

        return new Event()
                .setSummary("Trabajo: " + trabajo.getDescripcion())
                .setDescription("Dirección: " + trabajo.getDireccion() + "\nEstado: " + trabajo.getEstado())
                .setStart(intervalo[0])
                .setEnd(intervalo[1]);
    }

    public static String crearEventoTrabajo(Trabajo trabajo, LocalTime horaDefecto) {
        try {
            Event evento = construirEvento(trabajo, horaDefecto);
            if (evento == null) {
                return null;
            }

            Calendar service = getCalendarService();

            // Insertar el evento en el calendario principal
            evento = service.events().insert("primary", evento).execute();
            System.out.println("✅ Evento creado en Google Calendar. Enlace: " + evento.getHtmlLink());

            return evento.getId();
        } catch (Exception e) {
            System.out.println("❌ Error al crear el evento en Google Calendar:");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Actualiza el evento que ya existe en el calendario del trabajo.
     *
     * Se hace get, se pisan solo los campos que administra la aplicacion y se
     * manda update. Actualizar a ciegas borraria asistentes, recordatorios y
     * demas campos que la app nunca toco.
     *
     * @return el id del evento actualizado, o null si ya no existe en Google
     *         (por ejemplo si se borro desde el calendario a mano) y hay que crearlo
     */
    public static String actualizarEventoTrabajo(Trabajo trabajo, LocalTime horaDefecto, String idEvento) {
        if (idEvento == null || idEvento.isBlank()) {
            return null;
        }
        try {
            Event evento = construirEvento(trabajo, horaDefecto);
            if (evento == null) {
                return null;
            }

            Calendar service = getCalendarService();

            Event existente = service.events().get("primary", idEvento).execute();
            existente.setSummary(evento.getSummary())
                    .setDescription(evento.getDescription())
                    .setStart(evento.getStart())
                    .setEnd(evento.getEnd());

            existente = service.events().update("primary", idEvento, existente).execute();
            System.out.println("✅ Evento actualizado en Google Calendar. Enlace: " + existente.getHtmlLink());

            return existente.getId();
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar el evento en Google Calendar:");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Borra el evento del calendario del trabajo.
     *
     * No hay disparador automatico porque el trabajo no tiene estado de
     * cancelacion: nace en la cotizacion y su fecha de inicio se fija despues,
     * asi que no existe un momento del flujo en el que el evento sobre.
     *
     * @return true si Google confirmo el borrado
     */
    public static boolean eliminarEventoTrabajo(String idEvento) {
        if (idEvento == null || idEvento.isBlank()) {
            return false;
        }
        try {
            getCalendarService().events().delete("primary", idEvento).execute();
            System.out.println("✅ Evento eliminado de Google Calendar.");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error al eliminar el evento en Google Calendar:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Devuelve el enlace web del evento de un trabajo, para abrirlo en el
     * navegador, o null si el trabajo todavia no tiene evento.
     */
    public static String obtenerEnlaceEvento(Trabajo trabajo) {
        if (trabajo == null || trabajo.getId_evento() == null || trabajo.getId_evento().isBlank()) {
            return null;
        }
        try {
            Event evento = getCalendarService().events().get("primary", trabajo.getId_evento()).execute();
            return evento.getHtmlLink();
        } catch (Exception e) {
            System.out.println("❌ Error al obtener el enlace del evento:");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Crea el evento del trabajo si la configuracion lo pide, o lo actualiza si
     * ya existe. El id devuelto por Google se guarda en Trabajo.id_evento, que
     * es lo que permite que la proxima vez se actualice en vez de duplicarse.
     *
     * La tarea no deja propagar ninguna excepcion a proposito: si algo escapara,
     * EjecutorGoogle lo reintentaria por ser un fallo de red y podria crear un
     * segundo evento, que es justo lo que este metodo evita.
     */
    public static void crearEventoSiConfigurado(Trabajo trabajo) {
        EjecutorGoogle.ejecutarSilencioso(() -> {
            try {
                ConfiguracionesDAO dao = new ConfiguracionesDAO();
                List<String[]> configs = dao.obtenerConfiguraciones();

                boolean crearEventosAutomaticamente = false;
                LocalTime horaDefectoCalendar = LocalTime.of(9, 0);

                for (String[] config : configs) {
                    String clave = config[1];
                    String valor = config[2];
                    if ("automaticamente_eventos".equals(clave)) {
                        crearEventosAutomaticamente = Boolean.parseBoolean(valor);
                    } else if ("hora_defecto_calendar".equals(clave)) {
                        if (valor != null && !valor.isBlank()) {
                            horaDefectoCalendar = LocalTime.parse(valor);
                        }
                    }
                }
                if (crearEventosAutomaticamente && trabajo.getFecha_inicio() != null) {
                    sincronizarEventoTrabajo(trabajo, horaDefectoCalendar);
                }
            } catch (Exception e) {
                System.err.println("[GoogleServicesProvider] " + e);
                e.printStackTrace();
            }
            return true;
        }, resultado -> {});
    }

    /**
     * Guarda el id del evento, creandolo primero si el trabajo aun no tiene uno.
     * Se ejecuta desde el hilo de Google; la escritura en la base usa su propia
     * conexion, igual que sincronizarTrabajosPendientes con las carpetas de Drive.
     */
    private static void sincronizarEventoTrabajo(Trabajo trabajo, LocalTime horaDefecto) {
        TrabajoDAO dao = new TrabajoDAO();
        String id = trabajo.getId_evento();

        if (id != null && !id.isBlank()) {
            // Si el update devuelve null, el evento ya no estaba en Google y solo
            // queda recrearlo. El id viejo se sobreescribe con el nuevo.
            if (actualizarEventoTrabajo(trabajo, horaDefecto, id) != null) {
                return;
            }
        }

        String idNuevo = crearEventoTrabajo(trabajo, horaDefecto);
        if (idNuevo != null) {
            dao.actualizarTrabajoIdEvento(trabajo.getId_trabajo(), idNuevo);
        }
    }

    public static List<Evidencia> listarArchivosDeCarpeta(String idCarpetaDrive) {
        return listarArchivosDeCarpeta(idCarpetaDrive, false);
    }

    public static List<Evidencia> listarArchivosDeCarpeta(String idCarpetaDrive, boolean soloImagenes) {
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
                    if (soloImagenes && !esImagen(archivo)) {
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

    private static boolean esImagen(com.google.api.services.drive.model.File archivo) {
        String mime = archivo.getMimeType();
        if (mime != null && mime.startsWith("image/")) {
            return true;
        }
        String nombre = archivo.getName() != null ? archivo.getName().toLowerCase() : "";
        return nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") || nombre.endsWith(".png")
                || nombre.endsWith(".gif") || nombre.endsWith(".webp") || nombre.endsWith(".bmp");
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
        if (!estaConectado()) {
            System.out.println("❌ Error: no hay ninguna cuenta de Google Drive vinculada.");
            return false;
        }

        if (ruta == null || ruta.isBlank()) {
            System.out.println("❌ Error: no se pudo determinar la carpeta de destino en Drive. "
                    + "Se cancela la subida de: " + rutaArchivoLocal);
            return false;
        }

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
            fileMetadata.setParents(Collections.singletonList(ruta));
            
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
        java.io.File carpetaTokens = UtilidadesRutas.directorioTokens();
        return carpetaTokens.exists() && carpetaTokens.isDirectory() && carpetaTokens.list().length > 0;
    }

    public static boolean desconectar() {
        java.io.File carpetaTokens = UtilidadesRutas.directorioTokens();
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

    public static boolean vincularNuevaCuenta() {
        desconectar();
        try {
            String idCarpeta = obtenerIdCarpetaBaseDatos();
            return estaConectado() && idCarpeta != null;
        } catch (Exception e) {
            System.out.println("No se pudo vincular la cuenta de Google:");
            e.printStackTrace();
            return false;
        }
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
        
        com.google.api.services.sheets.v4.model.Color bgDarkBlue = new com.google.api.services.sheets.v4.model.Color().setRed(0.12f).setGreen(0.27f).setBlue(0.5f);
        com.google.api.services.sheets.v4.model.Color bgLightBlue = new com.google.api.services.sheets.v4.model.Color().setRed(0.85f).setGreen(0.9f).setBlue(0.95f);
        com.google.api.services.sheets.v4.model.Color textWhite = new com.google.api.services.sheets.v4.model.Color().setRed(1.0f).setGreen(1.0f).setBlue(1.0f);
        
        // 1. Unir A1:D1 (COTIZACIÓN) y aplicar formato (ajustado hasta la columna D que es índice 3, o E índice 4. Como la tabla máxima tiene 4 columnas A-D, pongamos hasta índice 4)
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
                                        .setBackgroundColor(bgDarkBlue)
                                        .setTextFormat(new TextFormat()
                                                .setForegroundColor(textWhite)
                                                .setBold(true)
                                                .setFontSize(16))
                                        .setHorizontalAlignment("CENTER")
                                        .setVerticalAlignment("MIDDLE")))
                        .setFields("userEnteredFormat(backgroundColor,textFormat,horizontalAlignment,verticalAlignment)")));

        // 2. Metadatos (Folio, Fecha, Cliente, Estado) en negritas (A2:D4)
        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheetId)
                                .setStartRowIndex(1)
                                .setEndRowIndex(4)
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
        
        int filaTotales = datosMatriz.size() - 2;

        Border bordeSolido = new Border().setStyle("SOLID").setWidth(1);
        Borders bordesCompletos = new Borders()
                .setTop(bordeSolido).setBottom(bordeSolido)
                .setLeft(bordeSolido).setRight(bordeSolido);

        // 3. Encabezado "CONCEPTOS" (fila 5 -> índice 4)
        requests.add(new Request()
                .setMergeCells(new com.google.api.services.sheets.v4.model.MergeCellsRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheetId)
                                .setStartRowIndex(4)
                                .setEndRowIndex(5)
                                .setStartColumnIndex(0)
                                .setEndColumnIndex(2))
                        .setMergeType("MERGE_ALL")));
                        
        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheetId)
                                .setStartRowIndex(4)
                                .setEndRowIndex(5)
                                .setStartColumnIndex(0)
                                .setEndColumnIndex(2))
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setBackgroundColor(bgDarkBlue)
                                        .setTextFormat(new TextFormat().setForegroundColor(textWhite).setBold(true))
                                        .setHorizontalAlignment("CENTER")))
                        .setFields("userEnteredFormat(backgroundColor,textFormat,horizontalAlignment)")));

        // 4. Encabezado de la tabla Conceptos (fila 6 -> índice 5)
        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheetId)
                                .setStartRowIndex(5)
                                .setEndRowIndex(6)
                                .setStartColumnIndex(0)
                                .setEndColumnIndex(2))
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setBackgroundColor(bgLightBlue)
                                        .setTextFormat(new TextFormat().setBold(true))
                                        .setHorizontalAlignment("CENTER")
                                        .setBorders(bordesCompletos)))
                        .setFields("userEnteredFormat(backgroundColor,textFormat,horizontalAlignment,borders)")));

        // 5. Bordes para datos de Conceptos
        if (filaMateriales - 1 > 6) {
            requests.add(new Request()
                    .setRepeatCell(new RepeatCellRequest()
                            .setRange(new GridRange()
                                    .setSheetId(sheetId)
                                    .setStartRowIndex(6)
                                    .setEndRowIndex(filaMateriales - 1)
                                    .setStartColumnIndex(0)
                                    .setEndColumnIndex(2))
                            .setCell(new CellData()
                                    .setUserEnteredFormat(new CellFormat().setBorders(bordesCompletos)))
                            .setFields("userEnteredFormat.borders")));
        }

        // 6. Encabezado de la tabla Materiales (filaMateriales)
        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheetId)
                                .setStartRowIndex(filaMateriales)
                                .setEndRowIndex(filaMateriales + 1)
                                .setStartColumnIndex(0)
                                .setEndColumnIndex(4))
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setBackgroundColor(bgLightBlue)
                                        .setTextFormat(new TextFormat().setBold(true))
                                        .setHorizontalAlignment("CENTER")
                                        .setBorders(bordesCompletos)))
                        .setFields("userEnteredFormat(backgroundColor,textFormat,horizontalAlignment,borders)")));

        // 7. Bordes para datos de Materiales
        if (filaTotales - 1 > filaMateriales + 1) {
            requests.add(new Request()
                    .setRepeatCell(new RepeatCellRequest()
                            .setRange(new GridRange()
                                    .setSheetId(sheetId)
                                    .setStartRowIndex(filaMateriales + 1)
                                    .setEndRowIndex(filaTotales - 1)
                                    .setStartColumnIndex(0)
                                    .setEndColumnIndex(4))
                            .setCell(new CellData()
                                    .setUserEnteredFormat(new CellFormat().setBorders(bordesCompletos)))
                            .setFields("userEnteredFormat.borders")));
        }

        // 8. Encabezados de Totales (filaTotales)
        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheetId)
                                .setStartRowIndex(filaTotales)
                                .setEndRowIndex(filaTotales + 1)
                                .setStartColumnIndex(0)
                                .setEndColumnIndex(3))
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setBackgroundColor(bgDarkBlue)
                                        .setTextFormat(new TextFormat().setForegroundColor(textWhite).setBold(true))
                                        .setHorizontalAlignment("CENTER")
                                        .setBorders(bordesCompletos)))
                        .setFields("userEnteredFormat(backgroundColor,textFormat,horizontalAlignment,borders)")));

        // 9. Valores de Totales (filaTotales + 1)
        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheetId)
                                .setStartRowIndex(filaTotales + 1)
                                .setEndRowIndex(filaTotales + 2)
                                .setStartColumnIndex(0)
                                .setEndColumnIndex(3))
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setBorders(bordesCompletos)
                                        .setTextFormat(new TextFormat().setBold(true))))
                        .setFields("userEnteredFormat(borders,textFormat)")));

        // 10. Formato de Moneda
        NumberFormat formatoMoneda = new NumberFormat()
                .setType("NUMBER")
                .setPattern("$#,##0.00");

        // Moneda para precios en Conceptos
        if (filaMateriales - 1 > 6) {
            requests.add(new Request()
                    .setRepeatCell(new RepeatCellRequest()
                            .setRange(new GridRange()
                                    .setSheetId(sheetId)
                                    .setStartRowIndex(6)
                                    .setEndRowIndex(filaMateriales - 1)
                                    .setStartColumnIndex(1)
                                    .setEndColumnIndex(2))
                            .setCell(new CellData()
                                    .setUserEnteredFormat(new CellFormat().setNumberFormat(formatoMoneda)))
                            .setFields("userEnteredFormat.numberFormat")));
        }
        
        // Moneda para precio unitario y total en Materiales
        if (filaTotales - 1 > filaMateriales + 1) {
            requests.add(new Request()
                    .setRepeatCell(new RepeatCellRequest()
                            .setRange(new GridRange()
                                    .setSheetId(sheetId)
                                    .setStartRowIndex(filaMateriales + 1)
                                    .setEndRowIndex(filaTotales - 1)
                                    .setStartColumnIndex(1)
                                    .setEndColumnIndex(2))
                            .setCell(new CellData()
                                    .setUserEnteredFormat(new CellFormat().setNumberFormat(formatoMoneda)))
                            .setFields("userEnteredFormat.numberFormat")));
                            
            requests.add(new Request()
                    .setRepeatCell(new RepeatCellRequest()
                            .setRange(new GridRange()
                                    .setSheetId(sheetId)
                                    .setStartRowIndex(filaMateriales + 1)
                                    .setEndRowIndex(filaTotales - 1)
                                    .setStartColumnIndex(3)
                                    .setEndColumnIndex(4))
                            .setCell(new CellData()
                                    .setUserEnteredFormat(new CellFormat().setNumberFormat(formatoMoneda)))
                            .setFields("userEnteredFormat.numberFormat")));
        }

        // Moneda para Totales (IVA, Subtotal, Total)
        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheetId)
                                .setStartRowIndex(filaTotales + 1)
                                .setEndRowIndex(filaTotales + 2)
                                .setStartColumnIndex(0)
                                .setEndColumnIndex(3))
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat().setNumberFormat(formatoMoneda)))
                        .setFields("userEnteredFormat.numberFormat")));

        // 11. Ajuste del ancho de las columnas
        requests.add(new Request()
                .setUpdateDimensionProperties(
                        new UpdateDimensionPropertiesRequest()
                                .setRange(new DimensionRange()
                                        .setSheetId(sheetId)
                                        .setDimension("COLUMNS")
                                        .setStartIndex(0)
                                        .setEndIndex(1))
                                .setProperties(new DimensionProperties()
                                        .setPixelSize(350))
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
    
    public static String buscarCarpeta(Drive driveService, String nombreCarpeta, String parentId) throws IOException {
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
        }
        return null;
    }

    public static String buscarOCrearCarpeta(Drive driveService, String nombreCarpeta, String parentId) throws IOException {
        String existente = buscarCarpeta(driveService, nombreCarpeta, parentId);
        if (existente != null) {
            return existente;
        }

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
    
    /**
     * Busca o crea la carpeta "Evidencias" dentro de la carpeta de un trabajo.
     * Se expone aqui para que la vista de bitacora deje de armar su propio
     * servicio de Drive duplicado.
     *
     * @param idTrabajoCarpeta carpeta del trabajo en Drive
     * @return id de la carpeta de evidencias, o null si fallo
     */
    public static String obtenerIdCarpetaEvidencias(String idTrabajoCarpeta) {
        return obtenerIdCarpetaEvidencias(idTrabajoCarpeta, true);
    }

    public static String obtenerIdCarpetaEvidencias(String idTrabajoCarpeta, boolean crearSiNoExiste) {
        if (idTrabajoCarpeta == null || idTrabajoCarpeta.isBlank()) {
            return null;
        }
        try {
            final HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
            Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            return crearSiNoExiste
                    ? buscarOCrearCarpeta(service, "Evidencias", idTrabajoCarpeta)
                    : buscarCarpeta(service, "Evidencias", idTrabajoCarpeta);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Crea en Drive las carpetas de los trabajos que quedaron pendientes.
     *
     * Es sincronica a proposito: la ejecuta el hilo de
     * {@link EjecutorGoogle}, no el hilo de eventos de Swing. Antes estaba
     * envuelta en un SwingWorker propio, lo que multiplicaba los hilos y
     * dejaba al usuario esperando sin saber que pasaba. Tampoco muestra
     * dialogos; el llamador decide que informar.
     *
     * @return arreglo de dos posiciones: {sincronizados, fallidos}
     */
    public static int[] sincronizarTrabajosPendientes() {

        if (!estaConectado()) {
            return new int[]{0, 0};
        }

        TrabajoDAO dao = new TrabajoDAO();
        List<Trabajo> listaTrabajos = dao.obtenerTrabajosPendientes();
        int sincronizados = 0;
        int fallidos = 0;

        for (Trabajo trabajo : listaTrabajos) {

            int idTrabajo = trabajo.getId_trabajo();

            String nombreCarpeta = "Trabajo_" + trabajo.getId_trabajo() + "_" + trabajo.getId_cliente() + "_" + trabajo.getFecha_cr();
            String idCarpetaDrive = GoogleServicesProvider.crearCarpetaTrabajo(nombreCarpeta);

            if (idCarpetaDrive != null) {
                dao.actualizarTrabajoIdCarpeta(trabajo.getId_trabajo(), idCarpetaDrive);
                dao.actualizarTrabajoCarpeta(idTrabajo, "Creada");
                sincronizados++;
                System.out.println("Trabajo ID " + trabajo.getId_trabajo() + " creado con su carpeta de Drive sincronizada.");
            } else {
                dao.actualizarTrabajoCarpeta(idTrabajo, "Pendiente");
                fallidos++;
                System.out.println("No se pudo crear la carpeta de Drive del trabajo ID "
                        + trabajo.getId_trabajo() + ". Queda pendiente.");
            }
        }
        return new int[]{sincronizados, fallidos};
    }

    /**
     * Sube a Drive los PDF de una carpeta local.
     *
     * Es sincronica a proposito, la ejecuta el hilo de
     * {@link EjecutorGoogle}. Los dialogos los decide el llamador.
     *
     * @param ruta carpeta local con los PDF
     * @return arreglo de dos posiciones: {subidos, fallidos}
     * @throws Exception si falla la llamada a Drive, para que el ejecutor la
     *                   reciba y el usuario vea la causa real en vez de un
     *                   "no se pudo subir" sin detalle
     */
    public static int[] subirCotizacionesPdf(String ruta) throws Exception {
        if (!estaConectado()) {
            return new int[]{0, 0};
        }

        final HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        String idCotizaciones = buscarOCrearCarpeta(service, "Cotizaciones", null);
        String idCotizaciones_PDF = buscarOCrearCarpeta(service, "PDF", idCotizaciones);

        java.io.File carpeta = new java.io.File(ruta);
        java.io.File[] archivos = carpeta.listFiles();
        int subidos = 0;
        int fallidos = 0;
        if (archivos != null) {
            for (java.io.File f : archivos) {

                // Se usa la ruta que Java ya compuso al listar la carpeta. Antes
                // se concatenaba el nombre sobre la ruta cruda, lo que dependia
                // de que la ruta terminara en separador y hacia fallar la subida.
                if (GoogleServicesProvider.subirArchivo(f.getPath(), idCotizaciones_PDF)) {
                    subidos++;
                } else {
                    fallidos++;
                }
            }
        }

        return new int[]{subidos, fallidos};
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
    
    
    /**
     * Baja una evidencia desde Drive si todavia no esta en este equipo.
     *
     * Devuelve si el archivo quedo disponible, en lugar de nada. La vista de
     * bitacora la ejecuta desde el hilo de Google y necesita saber si puede
     * mostrar la imagen o si tiene que avisar que la descarga fallo; antes el
     * fallo se comia por dentro y la vista intentaba pintar un archivo que no
     * existia.
     *
     * @param idDrive          id del archivo en Drive
     * @param rutaLocalDestino ruta donde queda la copia local
     * @return true si el archivo esta disponible en disco
     */
    public static boolean verificarYDescargarEvidencia(String idDrive, String rutaLocalDestino) {
    java.io.File archivoLocal = new java.io.File(rutaLocalDestino);
    
    // Si la imagen NO existe localmente en esta nueva PC, pero tenemos su ID de Drive...
    if (!archivoLocal.exists()) {
        try {
            System.out.println("Imagen no encontrada localmente. Descargándola desde Google Drive...");
            Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            
            // Creamos las carpetas locales si no existen
            archivoLocal.getParentFile().mkdirs();

            // Descargamos usando el ID de Drive guardado en la base de datos
            try (java.io.FileOutputStream outputStream = new java.io.FileOutputStream(archivoLocal)) {
                driveService.files().get(idDrive).executeMediaAndDownloadTo(outputStream);
            }
            System.out.println("Imagen restaurada con éxito desde la nube.");
            
        } catch (Exception e) {
            System.out.println("No se pudo descargar la imagen desde Drive.");
            e.printStackTrace();

            // Una descarga interrumpida deja un archivo de cero bytes. Si se
            // deja, la proxima visita lo toma por valido y nunca reintenta.
            archivoLocal.delete();
            return false;
        }
    } else {
        // Si ya existe localmente, la carga directo de la PC (¡súper rápido!)
    }

    return archivoLocal.exists() && archivoLocal.length() > 0;
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

    /**
     * Busca y descarga el respaldo de la base de datos (sistema_EN) desde la
     * carpeta "Base de datos" de Google Drive al destino local indicado.
     * @param rutaDestinoLocal ruta completa donde debe quedar el archivo sistema_EN
     * @return true si el respaldo se descargo correctamente
     */
    public static boolean descargarRespaldoBaseDatos(String rutaDestinoLocal) {
        if (!estaConectado()) {
            System.out.println("No hay cuenta de Google vinculada para descargar el respaldo.");
            return false;
        }
        try {
            Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            String idCarpeta = obtenerIdCarpetaBaseDatos();
            if (idCarpeta == null) {
                System.out.println("No se pudo localizar la carpeta Base de datos en Drive.");
                return false;
            }

            List<Evidencia> archivos = listarArchivosDeCarpeta(idCarpeta);
            String idRespaldo = null;
            for (Evidencia e : archivos) {
                if ("sistema_EN".equals(e.getNombre_archivo())) {
                    idRespaldo = e.getId_google_drive();
                    break;
                }
            }

            if (idRespaldo == null) {
                System.out.println("No se encontro un respaldo de la base de datos en Drive.");
                return false;
            }

            java.io.File archivoLocal = new java.io.File(rutaDestinoLocal);
            archivoLocal.getParentFile().mkdirs();
            try (java.io.FileOutputStream outputStream = new java.io.FileOutputStream(archivoLocal)) {
                driveService.files().get(idRespaldo).executeMediaAndDownloadTo(outputStream);
            }
            System.out.println("Respaldo de base de datos descargado con exito: " + rutaDestinoLocal);
            return archivoLocal.exists() && archivoLocal.length() > 0;
        } catch (Exception e) {
            System.out.println("Error al descargar el respaldo de la base de datos:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Envia un correo electronico utilizando la API de Gmail.
     * @param destinatario direccion de correo del destinatario
     * @param asunto asunto del correo
     * @param cuerpo cuerpo del correo en texto/html
     * @return true si se envio correctamente, false en caso contrario
     */
    public static boolean enviarCorreo(String destinatario, String asunto, String cuerpo, String tipo, String prioridad, String adjuntos) {
        try {
            Gmail service = getGmailService();
            
            String boundary = "====Boundary====";
            StringBuilder rawMessage = new StringBuilder();

            rawMessage.append("To: ").append(destinatario).append("\r\n");
            rawMessage.append("Subject: ").append(asunto).append("\r\n");
            rawMessage.append("MIME-Version: 1.0\r\n");
            rawMessage.append("Content-Type: multipart/mixed; boundary=\"").append(boundary).append("\"\r\n\r\n");

            rawMessage.append("--").append(boundary).append("\r\n");
            rawMessage.append("Content-Type: text/html; charset=utf-8\r\n\r\n");
            rawMessage.append("<b>Tipo:</b> ").append(tipo).append("<br>");
            rawMessage.append("<b>Prioridad:</b> ").append(prioridad).append("<br><br>");
            rawMessage.append(cuerpo).append("\r\n\r\n");

            if (adjuntos != null && !adjuntos.trim().isEmpty()) {
                String[] archivosPaths = adjuntos.split(";");
                for (String path : archivosPaths) {
                    java.io.File file = new java.io.File(path.trim());
                    if (file.exists()) {
                        String fileName = file.getName();
                        String mimeType = java.nio.file.Files.probeContentType(file.toPath());
                        if (mimeType == null) mimeType = "application/octet-stream";
                        
                        byte[] fileBytes = java.nio.file.Files.readAllBytes(file.toPath());
                        String encodedFile = java.util.Base64.getMimeEncoder().encodeToString(fileBytes);
                        
                        rawMessage.append("--").append(boundary).append("\r\n");
                        rawMessage.append("Content-Type: ").append(mimeType).append("; name=\"").append(fileName).append("\"\r\n");
                        rawMessage.append("Content-Disposition: attachment; filename=\"").append(fileName).append("\"\r\n");
                        rawMessage.append("Content-Transfer-Encoding: base64\r\n\r\n");
                        rawMessage.append(encodedFile).append("\r\n\r\n");
                    } else {
                        System.out.println("⚠️ Advertencia: El archivo adjunto no existe: " + path);
                    }
                }
            }

            rawMessage.append("--").append(boundary).append("--");
            
            String encodedEmail = java.util.Base64.getUrlEncoder().encodeToString(rawMessage.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
            
            Message message = new Message();
            message.setRaw(encodedEmail);
            
            service.users().messages().send("me", message).execute();
            System.out.println("✅ Correo enviado exitosamente a: " + destinatario);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error al enviar el correo:");
            e.printStackTrace();
            return false;
        }
    }

}