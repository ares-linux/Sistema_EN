<h1 align="center"> Sistema EN </h1>

<img width="520" height="261" alt="logo 2 0" src="https://github.com/user-attachments/assets/04061dcc-7484-4573-a746-a85d29977daf" />

Sistema de gestión para la empresa: **ESTIBEN NAVARRO SOLUCIONES HIDRO-SANITARIAS**

<p align="left">
<img src="https://img.shields.io/badge/STATUS-EN%20DESAROLLO-green">
<img src="https://img.shields.io/badge/Sistema%20Estiben%20Navarro%20-%20grey">
<img src="https://img.shields.io/badge/Versi%C3%B3n-v1.2.1-blue">
<img src="https://img.shields.io/badge/Java-17-orange">
</p>

## Descripción

**Sistema EN** es una aplicación de escritorio para administrar el trabajo de la empresa: permite registrar clientes, trabajadores y trabajos, generar cotizaciones con formato automático en Google Sheets, llevar la bitácora de actividades con evidencias en la nube, controlar pagos y herramientas, y mantener respaldos sincronizados en Google Drive.

## Características

-  Gestión de **clientes**, **trabajadores** y **trabajos**.
-  **Cotizaciones** con formato automático exportadas a Google Sheets (incluso PDF).
-  **Bitácora** de trabajos con **evidencias** (fotos) subidas a Google Drive.
-  **Respaldo y restauración** de la base de datos desde Google Drive.
-  Control de **pagos**, **herramientas** y **tipos de herramienta**.
-  Usuarios con contraseña cifrada (bcrypt).
-  Interfaz con temas **claro/oscuro** (FlatLaf).
-  Sincronización de carpetas por trabajo en Google Drive.

##  Tecnologías

| Tecnología | Descripción |
|---|---|
| Java 26 | Lenguaje de programación |
| NetBeans 30 | IDE usado |
| SQLite (sqlite-jdbc) | Base de datos local |
| FlatLaf | Look and feel moderno |
| Google API Client | Integración con Drive y Sheets |
| OpenPDF | Generación de PDF |
| jbcrypt | Cifrado de contraseñas |
| JCalendar / AbsoluteLayout / LGoodDatePicker | Componentes de interfaz |
| jDeploy | Generación de instaladores y actualizaciones |

Programas adicionales usados: **DB Browser for SQLite**, **GitHub Desktop**, **Google docs**, **Drawio**, **Padlet**.

## Instalación

Descarga e instala la aplicación desde el instalador:

- **jDeploy**: https://www.jdeploy.com/~sistema-en

> El instalador incluye el JRE necesario (Java 17), por lo que no hace falta instalar Java por separado.

## Primer uso

Al abrir el programa por primera vez, si no se encuentra una base de datos, aparece un asistente que te permite:

1. **Vincular una cuenta de Google Drive** (para restaurar el respaldo de la base de datos y sincronizar evidencias y cotizaciones).
2. **Restaurar el respaldo desde Google Drive**.
3. **Crear la base de datos en la ubicación por defecto**.
4. **Buscar la base de datos manualmente** (si ya tienes una copia en otro lugar).

### Ubicación de los datos

| Dato | Ubicación |
|---|---|
| Base de datos por defecto | `Documentos/Documents/Bases de datos/sistema_EN` |
| Configuración de la ruta de la BD | `~/.Sistema_EN/base_de_datos.properties` (Linux) / `%APPDATA%\.Sistema_EN` (Windows) |
| Tokens de Google Drive | `~/.Sistema_EN/tokens` (Linux) / `%APPDATA%\.Sistema_EN\tokens` (Windows) |
| Credenciales OAuth | Incluidas dentro del instalador (`credentials.json`) |

> La carpeta **"Base de datos"** se crea automáticamente en Google Drive la primera vez que se sube un respaldo.

## Desarrollo

Para trabajar en el código:

1. Clona el repositorio:
   ```bash
   git clone https://github.com/ares-linux/Sistema_EN.git
   ```
2. Abre el proyecto `Sistema_en` en **NetBeans 30**.
3. Las librerías están en la carpeta `lib/` del proyecto.
4. Ejecuta el proyecto (la lógica de arranque está en la clase `Controlador_arranque`).

Estructura principal:

```
Sistema_en/
├── src/dao/          → Capa de acceso a datos (DAO + InicializadorBD)
├── src/sistema_en/   → Lógica del sistema y servicios (Conexion, GoogleServicesProvider, Controlador_arranque)
└── src/sistema_en/Ventanas/ → Interfaz gráfica (ventanas y diálogos)
```
- **Nota importante:** aunque el mantenedor posea java 26 o java 21 el proyecto igualmente se compilara en java 17 porque asi se encuentra configurado su compilacion con **jdeploy**.

## Publicación (solo mantenedores)

La publicación en npm es **manual** (así lo hace el mantenedor desde su máquina). El
workflow de GitHub en tags (`v*`) **solo compila y valida el empaquetado**; **no publica**.

Para generar una nueva versión instalable con jDeploy:

1. Compila y genera el JAR en NetBeans (target `dist/`).
2. Verifica que `credentials.json` esté en `Sistema_en/` (lo toma `package.json` con `files` → `dir: "."`). Es necesario para que el login de Google Drive funcione.
3. Actualiza la versión en `Sistema_en/package.json` y crea el tag en git.
4. Publica **manualmente**:
   ```bash
   cd Sistema_en && jdeploy publish
   ```
5. **Verifica** que el bundle generado incluya `credentials.json` y que **NO** incluya `base_de_datos.properties` ni `tokens/` (estos se generan por-usuario en `~/.Sistema_EN`).

> ⚠️ No publiques (ni dejes que el CI publique) una versión que ya exista en npm: devuelve
> `403 You cannot publish over the previously published versions`. Por eso el workflow solo
> valida y el publish lo hace el mantenedor una única vez por versión.

## Documentos

### Externos

Estos documento se encuentran en **Google Docs** como documentos disponibles.

- **Página web del proyecto**: https://ares-linux.github.io/herramientas-equipo/
- **Documentación técnica**: https://docs.google.com/document/d/1TYEXRZGXCl3ruwjMTYfG_SvQgS-VEWO2Oza72f1Hsjs/edit?usp=sharing
- **Manual de usuario**: https://docs.google.com/document/d/1Kb0tr2GmgPkf74LLtteJbe_nPPIJ-wf6lnWWvwtwUh8/edit?usp=sharing

### Internos

Estos documentos se encuentran dentro de proyecto en la carpeta **docs** del repositorio.

- **Arquitectura.md**: [docs/Arquitectura.md](docs/Arquitectura.md)
- **D_informes_contenido.md**: [docs/D_informes_contenido.md](docs/D_informes_contenido.md)
- **GUIATRABAJO.Md**:[docs/GUIATRABAJO.md](docs/GUIA-TRABAJO.md)
- **PANTALLAS.md**: [docs/PANTALLAS.md](docs/PANTALLAS.md)
- **PENDIENTES.md**: [docs/PENDIENTES.md](docs/PENDIENTES.md)
- **REGISTRO-PUBLICACION.md**: [docs/REGISTRO-PUBLICACION.md](docs/REGISTRO-PUBLICACION.md)

## Colaborar

Consulta la guía de trabajo en equipo: [docs/GUIA-TRABAJO.md](docs/GUIA-TRABAJO.md)

---

 **jDeploy**: https://www.jdeploy.com/~sistema-en?all=1
