# Arquitectura de Sistema_EN

> **Versión:** 1.2.1
> **Fuente:** generado a partir del código en `src/` (73 archivos `.java`)
> **Formato:** diagramas [Mermaid](https://mermaid.js.org/) — se renderizan en GitHub, GitLab y VS Code (extensión Markdown Preview Mermaid).

Sistema de gestión de trabajos, clientes, cotizaciones, herramientas y personal, con
persistencia local en SQLite y respaldo/integración con Google Drive y Gmail.

---

## Índice

1. [Vista general: capas y paquetes](#1-vista-general-capas-y-paquetes)
2. [Flujo de arranque y login](#2-flujo-de-arranque-y-login)
3. [Modelo de datos (ER)](#3-modelo-de-datos-er)
4. [Navegación de ventanas](#4-navegación-de-ventanas)
5. [Integraciones externas y despliegue](#5-integraciones-externas-y-despliegue)
6. [Clases por módulo](#6-clases-por-módulo)
7. [Notas y convenciones](#7-notas-y-convenciones)

---

## 1. Vista general: capas y paquetes

Arquitectura en capas sencilla (sin framework de inyección): **Presentación → Dominio → Persistencia → Datos**, con una capa de infraestructura de arranque y servicios externos.

```mermaid
flowchart TB
    subgraph PRES["Presentación — sistema_en.Ventanas"]
        LOGIN["V_login_user"]
        MENU["V_menu"]
        REG["V_R_* (registro): V_R_Trabajo, V_R_Cotizacion, V_R_Empleado, V_R_Herramienta, V_R_Bitacora"]
        CONS["Ventanas_consultas: V_clientes, V_trabajos, V_historial_c"]
        DLG["dialogos: D_buscar_*, D_ficha_*, D_configuraciones, D_informes, D_registrar_pagos, ..."]
    end

    subgraph INFRA["Infraestructura / arranque — sistema_en"]
        MAIN["Sistema_en (main)"]
        SPLASH["V_splash"]
        BOOT["Controlador_arranque"]
        CONN["Conexion"]
        RUTAS["Carpetas / UtilidadesRutas"]
        VER["Version"]
    end

    subgraph DOM["Dominio — sistema_en.Objetos"]
        POJO["POJOs: Cliente, Trabajo, Cotizacion, Pago, Herramienta, Trabajador, Evidencia, Bitacora, ..."]
        PDF["Exportar_PDF (OpenPDF)"]
    end

    subgraph PERS["Persistencia — dao"]
        DAO["13 DAOs: ClienteDAO, TrabajoDAO, CotizacionDAO, ..."]
        INIT["InicializadorBD (DDL / creación BD)"]
    end

    SERV["GoogleServicesProvider (Drive / Gmail / Sheets)"]
    DB[("SQLite: sistema_EN")]
    CLOUD[("Google Drive / Gmail")]

    MAIN --> SPLASH
    MAIN --> BOOT
    MAIN --> CONN
    SPLASH --> BOOT
    BOOT --> INIT
    BOOT --> DAO
    BOOT --> SERV

    LOGIN --> DAO
    MENU --> REG
    MENU --> CONS
    MENU --> DLG
    REG --> DAO
    CONS --> DAO
    DLG --> DAO
    DLG --> PDF
    DLG --> SERV

    DAO --> CONN
    INIT --> CONN
    CONN --> DB
    DAO --> POJO
    SERV --> CLOUD
    RUTAS --> CONN
```

**Responsabilidades por paquete**

| Paquete | Rol |
|---|---|
| `sistema_en` | Punto de entrada, splash, arranque, conexión, rutas, versión y servicios Google. |
| `dao` | Acceso a datos JDBC/SQLite y creación del esquema. |
| `sistema_en.Objetos` | Modelo de dominio (POJOs) y exportación a PDF. |
| `sistema_en.Ventanas` | Ventanas principales (`JFrame`). |
| `sistema_en.Ventanas.dialogos` | Diálogos modales (`JDialog`). |
| `sistema_en.Ventanas.Ventanas_consultas` | Listados y consultas. |
| `sistema_en.Ventanas.No_utilizadas` | Código obsoleto (no referenciado). |

---

## 2. Flujo de arranque y login

```mermaid
sequenceDiagram
    autonumber
    participant M as Sistema_en (main)
    participant S as V_splash
    participant C as Controlador_arranque
    participant I as InicializadorBD
    participant G as GoogleServicesProvider
    participant CF as ConfiguracionesDAO
    participant U as UsuariosDAO
    participant L as V_login_user
    participant MN as V_menu

    M->>S: new V_splash() + mostrar()
    S->>C: iniciar()
    C->>C: Conexion.rutaArchivoBD()
    alt La BD existe
        C->>I: asegurarEsquema(ruta)
        C->>I: asegurarConfiguraciones(ruta)
        C->>C: verificarSondaEscritura(ruta)
        C->>U: existeAdministrador()
        opt No hay administrador
            C->>C: registrarUsuarioObligatorio() -> D_registro_usuario
        end
    else La BD no existe
        C->>G: estaConectado()
        opt Conectado
            C->>G: descargarRespaldoBaseDatos(ruta)
        end
        C->>C: recuperarYResolver() (vincular Drive / buscar / crear / salir)
    end
    C-->>S: true | false
    S->>M: cerrar()
    alt Arranque correcto
        M->>CF: obtenerConfiguraciones()
        CF-->>M: tema (Claro / Oscuro / Automatico)
        M->>L: new V_login_user() + setVisible(true)
        L->>U: validar credenciales
        U-->>L: ok | error
        L->>MN: new V_menu() + setVisible(true)
    else Arranque fallido
        M->>M: System.exit(0)
    end
```

---

## 3. Modelo de datos (ER)

Esquema definido en `dao/InicializadorBD.java` (SQLite). Los nombres de atributos se
muestran normalizados sin acentos (p. ej. `contrasena` = columna `contraseña`).

```mermaid
erDiagram
    TIPOS_HERRAMIENTA ||--o{ HERRAMIENTA : clasifica
    CLIENTE            ||--o{ TRABAJO : solicita
    TRABAJO            ||--o{ COTIZACION : genera
    COTIZACION         ||--o{ DETALLES_COTIZACION : contiene
    TRABAJO            ||--o{ PAGO : recibe
    TRABAJADOR         ||--o{ BITACORA : registra
    TRABAJO            ||--o{ BITACORA : incluye
    BITACORA           ||--o{ EVIDENCIA : adjunta

    CONFIGURACIONES {
        int  id_configuracion PK
        text clave
        text valor
    }
    USUARIOS {
        int  id_usuarios PK
        text usuario
        text contrasena
    }
    TIPOS_HERRAMIENTA {
        int  id_tipo_herramienta PK
        text nombre
    }
    HERRAMIENTA {
        int  id_herramienta PK
        int  t_herramienta FK
        text n_serie
        text foto
    }
    CLIENTE {
        int  id_cliente PK
        text nombre_c
        text telefono
        text u_modificacion
        text fecha_registro
    }
    TRABAJO {
        int  id_trabajo PK
        text direccion
        text descripcion
        text fecha_cr
        text hora_cr
        text estado
        int  id_cliente FK
        text fecha_inicio
        text fecha_fin
        text carpeta
        text id_carpeta
    }
    TRABAJADOR {
        int  id_trabajador PK
        text nombre
        text sexo
        text foto
        text puesto
        text f_nac
        text curp
        text rfc
    }
    COTIZACION {
        int  id_cotizacion PK
        real subtotal
        real iva
        real total
        text estado
        int  id_trabajo FK
        text tipo
        text fecha_creacion
    }
    DETALLES_COTIZACION {
        int  id_detalle PK
        int  id_cotizacion FK
        text concepto
        text n_material
        real cantidad
        real precio_uni
        real precio
    }
    PAGO {
        int  id_pago PK
        int  id_trabajo FK
        real monto
        text concepto
        text fecha_pago
    }
    BITACORA {
        int  id_bitacora PK
        text fecha
        text hora_llegada
        text hora_salida
        text notas
        int  id_trabajador FK
        int  id_trabajo FK
    }
    EVIDENCIA {
        int  id_evidencia PK
        int  id_bitacora FK
        text id_google_drive
        text nombre_archivo
        text ruta_local
    }
```

**Resumen de tablas**

| Tabla | Descripción | FK |
|---|---|---|
| `Configuraciones` | Pares clave/valor del sistema (tema, carpetas, opciones). | — |
| `Usuarios` | Cuentas de acceso (contraseña con hash `jbcrypt`). | — |
| `Tipos_herramienta` | Catálogo de tipos de herramienta. | — |
| `Herramienta` | Herramientas con serie y foto. | `t_herramienta → Tipos_herramienta` |
| `Cliente` | Clientes. | — |
| `Trabajo` | Trabajos por cliente y su carpeta de evidencias. | `id_cliente → Cliente` |
| `Trabajador` | Personal. | — |
| `Cotizacion` | Cotizaciones ligadas a un trabajo. | `id_trabajo → Trabajo` |
| `Detalles_cotizacion` | Renglones de cada cotización. | `id_cotizacion → Cotizacion` (CASCADE) |
| `Pago` | Pagos recibidos por trabajo. | `id_trabajo → Trabajo` |
| `Bitacora` | Visitas/registros de trabajadores en trabajos. | `id_trabajador`, `id_trabajo` |
| `Evidencia` | Archivos/fotos adjuntos a una bitácora (Drive). | `id_bitacora → Bitacora` |

---

## 4. Navegación de ventanas

```mermaid
flowchart LR
    LOGIN["V_login_user"] --> MENU["V_menu"]

    MENU -->|Registrar empleado| E["V_R_Empleado"]
    MENU -->|Registrar herramienta| H["V_R_Herramienta"]
    MENU -->|Registrar trabajo| T["V_R_Trabajo"]
    MENU -->|Cotizacion de prueba| C["V_R_Cotizacion"]
    MENU -->|Historial| HC["V_historial_c"]
    MENU -->|Ver clientes| CL["V_clientes"]
    MENU -->|Ver trabajos| TB["V_trabajos"]
    MENU -->|Informes| INF["D_informes"]
    MENU -->|Configuraciones| CFG["D_configuraciones"]
    MENU -->|Salir del programa| OUT(["Cierra sesión / login"])

    E --> BE["D_buscar_empleado"]
    H --> BH["D_buscar_herramienta"]
    T --> BT["D_buscar_trabajo"]
    CL --> BC["D_buscar_cliente"]
    TB --> FT["D_ficha_trabajo"]
    CL --> FC["D_ficha_cliente"]
    FT --> BIT["V_R_Bitacora"]
    BIT --> BB["D_buscar_bitacora"]
    C --> EX["D_exportar_opciones"]
    T --> IE["D_informacion_estatica"]
    T --> RP["D_registrar_pagos"]

    CFG --> ACA["D_acerca_de"]
    ACA --> SOP["D_soporte_tecnico"]
    ACA --> LIC["D_licencias_terceros(_tabla)"]
    ACA --> NV["D_nota_version"]
    CFG --> COP["D_copias_seguridad"]
    CFG --> CC["D_cambiar_contraseña"]

    LOGIN --> RU["D_registro_usuario"]
```

> Nota: `V_R_Bitacora` se abre desde `D_ficha_trabajo` (ficha del trabajo), no directamente desde el menú.

---

## 5. Integraciones externas y despliegue

```mermaid
flowchart LR
    subgraph BUILD["Construcción"]
        NB["NetBeans / nbproject"] --> SRC["src (73 .java)"]
        SRC --> JAR["dist/Sistema_en.jar"]
        JAR --> MF["MANIFEST Class-Path (38 jars)"]
    end

    subgraph DEPLOY["Empaquetado jdeploy"]
        JAR --> BUNDLE["jdeploy-bundle/"]
        BUNDLE --> NPM["npm: sistema-en@1.2.1"]
        NPM --> INSTALL["Instaladores Windows / macOS / Linux"]
        CRED["credentials.json"] --> BUNDLE
    end

    subgraph LIBS["Dependencias de runtime"]
        FLAT["FlatLaf + flatlaf-extras + jSystemThemeDetector"]
        PDF2["OpenPDF"]
        SQLITE["sqlite-jdbc"]
        GAPI["Google API Client (Drive / Sheets / Gmail)"]
        BCRYPT["jbcrypt"]
        UI["LGoodDatePicker, jcalendar, jsvg, oshi, swing-toast-notifications"]
    end

    JAR --- LIBS

    subgraph EXTSERV["Servicios externos"]
        DRIVE[("Google Drive")]
        GMAIL[("Gmail")]
    end
    GAPI --> DRIVE
    GAPI --> GMAIL
```

**Servicios de `GoogleServicesProvider`**

| Método | Uso |
|---|---|
| `getCredentials` / `vincularNuevaCuenta` | OAuth 2.0 y vinculación de cuenta. |
| `subirArchivo` / `descargarRespaldoBaseDatos` | Respaldo y restauración de la BD en Drive. |
| `subirImagenACarpeta` / `listarArchivosDeCarpeta` / `obtenerIdCarpetaEvidencias` / `buscarCarpeta` | Evidencias de bitácora. |
| `getGmailService` | Envío de correos (soporte). |
| `getSheetsService` | Acceso a hojas de cálculo. |

---

## 6. Clases por módulo

### 6.1 Arranque y base de datos

```mermaid
classDiagram
    class Sistema_en {
        +main(String[] args)
    }
    class Controlador_arranque {
        +iniciar() boolean
        -verificarSondaEscritura(ruta)
        -recuperarYResolver() boolean
        -crearBaseDatosNueva() boolean
        -registrarUsuarioObligatorio() boolean
    }
    class V_splash {
        +actualizarEstado(String)
        +mostrar()
        +cerrar()
    }
    class Conexion {
        +VERSION_APP
        +conectar() Connection
        +rutaArchivoBD() String
        +guardarRutaBD(String)
        +existeBaseDatos() boolean
    }
    class InicializadorBD {
        +asegurarEsquema(ruta)
        +asegurarConfiguraciones(ruta)
        +crearBaseDatos(ruta) boolean
    }
    class GoogleServicesProvider {
        +estaConectado() boolean
        +subirArchivo(local, ruta) boolean
        +descargarRespaldoBaseDatos(ruta) boolean
        +vincularNuevaCuenta() boolean
    }
    class D_registro_usuario

    Sistema_en --> V_splash
    Sistema_en --> Controlador_arranque
    Controlador_arranque --> InicializadorBD
    Controlador_arranque --> Conexion
    Controlador_arranque --> GoogleServicesProvider
    Controlador_arranque --> D_registro_usuario
```

### 6.2 Autenticación y usuarios

```mermaid
classDiagram
    class V_login_user {
        +validarUsuario()
    }
    class D_registro_usuario {
        +isRegistrado() boolean
    }
    class D_cambiar_contrasena["D_cambiar_contraseña"]
    class UsuariosDAO {
        +existeAdministrador() boolean
        +insertarUsuario(...)
        +obtenerHashPorId(...)
        +actualizarContrase(...)
        +actualizarUsuario(...)
    }
    class ConfiguracionesDAO {
        +obtenerConfiguraciones() List
    }
    class Usuario

    V_login_user --> UsuariosDAO
    V_login_user --> ConfiguracionesDAO
    D_registro_usuario --> UsuariosDAO
    D_cambiar_contrasena --> UsuariosDAO
    UsuariosDAO --> Usuario
```

### 6.3 Clientes

```mermaid
classDiagram
    class V_clientes {
        +cargarClientes()
    }
    class D_buscar_cliente
    class D_ficha_cliente
    class ClienteDAO {
        +obtenerOCrearCliente(...) int
        +obtenerCliente(id) Cliente
        +obtenerClientes() List
        +filtrarClientesNombre(...)
        +actualizarCliente(...)
    }
    class TrabajoDAO
    class Cliente

    V_clientes --> ClienteDAO
    D_buscar_cliente --> ClienteDAO
    D_ficha_cliente --> ClienteDAO
    D_ficha_cliente --> TrabajoDAO
    ClienteDAO --> Cliente
```

### 6.4 Trabajos y fichas

```mermaid
classDiagram
    class V_R_Trabajo
    class V_trabajos
    class D_buscar_trabajo
    class D_ficha_trabajo
    class D_informacion_estatica
    class V_R_Bitacora
    class TrabajoDAO {
        +obtenerTrabajo(id) Trabajo
        +obtenerTrabajosConClientes() List
        +actualizarTrabajoDetalles(...)
        +eliminarTrabajo(...)
    }
    class ClienteDAO
    class BitacoraDAO
    class PagoDAO
    class Trabajo

    V_R_Trabajo --> TrabajoDAO
    V_R_Trabajo --> ClienteDAO
    V_trabajos --> TrabajoDAO
    D_buscar_trabajo --> TrabajoDAO
    D_ficha_trabajo --> TrabajoDAO
    D_ficha_trabajo --> ClienteDAO
    D_ficha_trabajo --> V_R_Bitacora
    D_informacion_estatica --> TrabajoDAO
    D_informacion_estatica --> ClienteDAO
    D_informacion_estatica --> BitacoraDAO
    D_informacion_estatica --> PagoDAO
    TrabajoDAO --> Trabajo
```

### 6.5 Cotizaciones y exportación PDF

```mermaid
classDiagram
    class V_R_Cotizacion {
        +exportarCotizacionPDF(...)
    }
    class V_historial_c
    class D_exportar_opciones
    class CotizacionDAO {
        +insertarCotizacionConDetalles(...)
        +obtenerCotizaciones() List
        +obtenerUltimaCotizacion() int
        +eliminarCotizacion(...)
    }
    class Detalles_cotizacionDAO {
        +obtenerDetalles_cotizacion(...)
        +eliminarDetalles_cotizacion(...)
    }
    class ClienteDAO
    class TrabajoDAO
    class ConfiguracionesDAO
    class Exportar_PDF {
        +exportarCotizacionPDF(...) boolean
        +exportarInformePDF(...) boolean
    }
    class Cotizacion

    V_R_Cotizacion --> CotizacionDAO
    V_R_Cotizacion --> Detalles_cotizacionDAO
    V_R_Cotizacion --> ClienteDAO
    V_R_Cotizacion --> TrabajoDAO
    V_R_Cotizacion --> ConfiguracionesDAO
    V_R_Cotizacion --> Exportar_PDF
    V_historial_c --> CotizacionDAO
    D_exportar_opciones --> Exportar_PDF
    CotizacionDAO --> Cotizacion
```

### 6.6 Pagos

```mermaid
classDiagram
    class D_registrar_pagos
    class PagoDAO {
        +insertarPago(...)
        +obtenerPagos(...) List
        +sumarPagosPorTrabajo(...) double
        +actualizarPago(...)
    }
    class CotizacionDAO
    class TrabajoDAO
    class Pago

    D_registrar_pagos --> PagoDAO
    D_registrar_pagos --> CotizacionDAO
    D_registrar_pagos --> TrabajoDAO
    PagoDAO --> Pago
```

### 6.7 Herramientas y tipos

```mermaid
classDiagram
    class V_R_Herramienta
    class D_buscar_herramienta
    class HerramientaDAO {
        +insertarHerramienta(...)
        +obtenerHerramienta(...)
        +obtenerHerramientas() List
        +actualizarHerramienta(...)
        +eliminarHerramienta(...)
    }
    class Tipos_herramientaDAO
    class Herramienta
    class Tipos_herramienta

    V_R_Herramienta --> HerramientaDAO
    V_R_Herramienta --> Tipos_herramientaDAO
    D_buscar_herramienta --> HerramientaDAO
    HerramientaDAO --> Herramienta
    Tipos_herramientaDAO --> Tipos_herramienta
```

### 6.8 Personal (trabajadores)

```mermaid
classDiagram
    class V_R_Empleado
    class D_buscar_empleado
    class TrabajadorDAO {
        +insertarTrabajador(...)
        +obtenerTrabajadores() List
        +buscarTrabajadores(...)
        +actualizarTrabajador(...)
        +eliminarTrabajador(...)
    }
    class Trabajador

    V_R_Empleado --> TrabajadorDAO
    D_buscar_empleado --> TrabajadorDAO
    TrabajadorDAO --> Trabajador
```

### 6.9 Bitácora y evidencias

```mermaid
classDiagram
    class V_R_Bitacora
    class D_buscar_bitacora
    class BitacoraDAO {
        +insertarBitacora(...)
        +obtenerBitacoras(...) List
        +buscarBitacoras(...)
        +actualizarBitacora(...)
    }
    class EvidenciaDAO
    class TrabajadorDAO
    class TrabajoDAO
    class GoogleServicesProvider
    class Bitacora
    class Evidencia

    V_R_Bitacora --> BitacoraDAO
    V_R_Bitacora --> EvidenciaDAO
    V_R_Bitacora --> TrabajadorDAO
    V_R_Bitacora --> TrabajoDAO
    V_R_Bitacora --> GoogleServicesProvider
    D_buscar_bitacora --> BitacoraDAO
    BitacoraDAO --> Bitacora
    EvidenciaDAO --> Evidencia
```

### 6.10 Informes

```mermaid
classDiagram
    class D_informes {
        +cargarInformeMesActual()
        +cargarTablas(inicio, fin)
        +obtener_configuraciones()
        -obtenerFilasTabla(JTable) List
    }
    class InformesDAO {
        +contarTrabajosRegistrados(...)
        +contarTrabajosIniciados(...)
        +contarTrabajosFinalizados(...)
        +contarClientesNuevos(...)
        +clienteConMasTrabajos(...)
        +contarCotizacionesRegistradas(...)
        +contarCotizacionesConvertidas(...)
        +promedioTrabajosPorCliente(...)
        +promedioDuracionTrabajo(...)
    }
    class ConfiguracionesDAO
    class Exportar_PDF

    D_informes --> InformesDAO
    D_informes --> ConfiguracionesDAO
    D_informes --> Exportar_PDF
```

### 6.11 Sistema, configuración y respaldos

```mermaid
classDiagram
    class V_menu
    class D_configuraciones
    class D_copias_seguridad
    class D_acerca_de
    class D_nota_version
    class D_soporte_tecnico
    class D_licencias_terceros
    class D_licencias_terceros_tabla
    class ConfiguracionesDAO
    class GoogleServicesProvider
    class Version

    V_menu --> ConfiguracionesDAO
    V_menu --> D_configuraciones
    V_menu --> D_informes
    V_menu --> GoogleServicesProvider
    D_configuraciones --> ConfiguracionesDAO
    D_configuraciones --> D_acerca_de
    D_copias_seguridad --> ConfiguracionesDAO
    D_copias_seguridad --> GoogleServicesProvider
    D_acerca_de --> D_nota_version
    D_acerca_de --> D_soporte_tecnico
    D_acerca_de --> D_licencias_terceros
    D_acerca_de --> D_licencias_terceros_tabla
    D_acerca_de --> Version
    D_nota_version --> Version
```

---

## 7. Notas y convenciones

- **Sin ORM:** cada `*DAO` abre conexiones con `Conexion.conectar()` y usa `PreparedStatement` + `try-with-resources`. Un DAO por entidad principal; las estadísticas se agrupan en `InformesDAO`.
- **Nombres de dominio:** las columnas de BD conservan el estilo `snake_case` y acentos originales (`contraseña`, `fecha_creación`); los POJOs exponen getters/setters equivalentes.
- **Configuración del usuario:** `base_de_datos.properties` y el token OAuth se resuelven con `UtilidadesRutas` (`directorioConfigUsuario`, `directorioTokens`).
- **Exportación:** `Exportar_PDF` usa OpenPDF para cotizaciones e informes; la salida se guarda en la carpeta configurada (`carpeta_ce/Informes/`).
- **Código obsoleto:** el paquete `sistema_en.Ventanas.No_utilizadas` (`V_ficha_trabajo`, `V_pago_recibido`) no participa en el flujo actual.
- **Empaquetado:** `jdeploy` genera instaladores nativos a partir de `dist/Sistema_en.jar`; el `MANIFEST` declara las 38 dependencias de `lib/`.

### Cómo mantener este documento

1. Actualizar la versión al publicar (coincidir con `Conexion.VERSION_APP`).
2. Si cambia el esquema, editar el DDL en `InicializadorBD` **y** el diagrama ER.
3. Si se añaden ventanas o DAOs, actualizar los diagramas 4 y 6.
4. Validar el render de Mermaid en la vista previa antes de commitear.
