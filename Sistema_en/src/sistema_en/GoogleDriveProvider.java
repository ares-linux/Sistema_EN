/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ares
 */
public class GoogleDriveProvider {
    
    private static final String APPLICATION_NAME = "Sistema EN";
    // LÍNEA NUEVA (Usando GSON en lugar de Jackson):
    private static final JsonFactory JSON_FACTORY = com.google.api.client.json.gson.GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    
    // Le otorgamos permisos completos sobre archivos de Drive
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";

    /**
     * Se encarga de la autenticación de OAuth 2.0
     */
    private static Credential getCredentials(HttpTransport HTTP_TRANSPORT) throws Exception {
        File credentialsFile = new File(CREDENTIALS_FILE_PATH);
        if (!credentialsFile.exists()) {
            throw new Exception("No se encontró el archivo '" + CREDENTIALS_FILE_PATH + "' en la raíz del proyecto.");
        }

        GoogleClientSecrets clientSecrets;
        try (FileInputStream in = new FileInputStream(credentialsFile)) {
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        }

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        // Configuramos el receptor local. Si tu Linux bloquea el navegador, esto imprimirá el link en consola.
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(9999).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Sube un archivo local (como tu base de datos SQLite) a Google Drive.
     * Si ya existe un archivo con el mismo nombre en Google Drive, actualiza su contenido
     * en lugar de crear un duplicado.
     * @param rutaArchivoLocal Ejemplo: "sistema.db"
     */
    public static void subirArchivo(String rutaArchivoLocal) {
        try {
            final HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
            //final HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            File fileToUpload = new File(rutaArchivoLocal);
            if (!fileToUpload.exists()) {
                System.out.println("❌ Error: El archivo local a subir no existe: " + rutaArchivoLocal);
                return;
            }

            String nombreArchivo = fileToUpload.getName();

            // Buscar si ya existe un archivo con el mismo nombre en Google Drive
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
                System.out.println("⚠️ Advertencia: No se pudo verificar si el archivo ya existe en Google Drive. Se creará uno nuevo.");
            }

            // Metadatos del archivo en la nube
            com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
            fileMetadata.setName(nombreArchivo);

            // Contenido binario del archivo
            FileContent mediaContent = new FileContent("application/x-sqlite3", fileToUpload);

            com.google.api.services.drive.model.File uploadedFile;
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
        }
    }
    
    
    public static boolean estaConectado() {
        File carpetaTokens = new File("tokens");
        return carpetaTokens.exists() && carpetaTokens.isDirectory() && carpetaTokens.list().length > 0;
    }
    
    public static boolean desconectar() {
        File carpetaTokens = new File("tokens");
        if (carpetaTokens.exists()) {
            File[] archivos = carpetaTokens.listFiles();
            if (archivos != null) {
                for (File f : archivos) {
                    f.delete();
                }
            }
            return carpetaTokens.delete(); 
        }
        return false;
    }
    
}
