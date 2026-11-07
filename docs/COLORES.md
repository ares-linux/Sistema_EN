# Colores del proyecto

Inventario de los colores en formato hexadecimal utilizados por la aplicación.

- **Fecha de revisión:** 6 de noviembre de 2026
- **Ámbito:** `Sistema_en/src` (clases Java y formularios `.form` de NetBeans)
- **Total de referencias hexadecimales en Java:** 66, distribuidas en 13 archivos
- **Colores hexadecimales distintos:** 7

> No se incluyen los colores de los temas FlatLaf (claro/oscuro) ni los del diagrama UML
> (`Sistema_en/UML_Clases.drawio`), porque no forman parte de la paleta definida por el código
> de la aplicación. La paleta del diagrama se incluye como referencia en el anexo.

## 1. Paleta principal

| Color | Hexadecimal | RGB | Uso principal | Referencias |
|---|---|---|---|---|
| Azul institucional | `#2F65CA` | `rgb(47, 101, 202)` | Botones primarios (Nuevo, Editar, Actualizar, Ingresar) y títulos de PDF | 14 |
| Rojo institucional | `#CE1126` | `rgb(206, 17, 38)` | Botones Cancelar/Cerrar, errores de validación y estado desconectado | 48 |
| Verde oscuro | `#008000` | `rgb(0, 128, 0)` | Estado "Conectado a Google Drive" | 2 |
| Verde | `#008F39` | `rgb(0, 143, 57)` | Confirmación de pago registrado | 1 |
| Verde neón | `#00FF00` | `rgb(0, 255, 0)` | Informe: mes empezado | 1 |
| Naranja | `#FFA500` | `rgb(255, 165, 0)` | Informe: mes en curso | 1 |
| Rojo puro | `#FF0000` | `rgb(255, 0, 0)` | Informe: mes terminado y botón Salir del login | 3 |

> En el código, algunos valores aparecen en minúsculas (`#2f65ca`, `#008f39`). Aquí se muestran
> en mayúsculas por convención. Las referencias de `#2F65CA` y `#FF0000` incluyen tanto los
> literales hexadecimales como sus equivalentes definidos con `new Color(...)`.

## 2. Detalle de uso

### `#CE1126` — Rojo institucional

| Archivo | Líneas | Uso |
|---|---|---|
| `D_cambiar_contraseña.java` | 309, 320, 331, 344, 353, 363, 374, 385, 398, 407, 440, 458, 526 | Mensajes de validación de contraseña y usuario |
| `V_R_Empleado.java` | 606, 616, 623, 630, 640, 652, 661, 669 | Texto de campos obligatorios sin completar |
| `V_R_Empleado.java` | 982, 1052 | Fondo del botón Cancelar |
| `V_R_Trabajo.java` | 534, 539, 544, 549, 554 | Texto de campos obligatorios sin completar |
| `V_R_Trabajo.java` | 777, 917 | Fondo del botón Cancelar |
| `V_R_Herramienta.java` | 432, 439, 451 | Texto de campos obligatorios sin completar |
| `V_R_Herramienta.java` | 518, 575 | Fondo del botón Cancelar |
| `V_R_Bitacora.java` | 1198, 1206, 1214, 1223, 1233 | Texto de campos obligatorios sin completar |
| `V_clientes.java` | 421 | Fondo del botón Reiniciar filtros |
| `D_ficha_cliente.java` | 396, 406 | Texto de campos obligatorios sin completar |
| `D_ficha_cliente.java` | 522 | Fondo del botón Cerrar |
| `D_ficha_trabajo.java` | 494 | Fondo del botón Cerrar |
| `D_copias_seguridad.java` | 403, 456, 513 | Estado "Desconectado" de Google Drive |

### `#2F65CA` — Azul institucional

| Archivo | Líneas | Uso |
|---|---|---|
| `V_R_Empleado.java` | 983, 1053 | Fondo de los botones Nuevo y Editar |
| `V_R_Trabajo.java` | 776, 916 | Fondo de los botones Nuevo y Editar |
| `V_R_Herramienta.java` | 517, 574 | Fondo de los botones Editar y Nuevo |
| `D_ficha_cliente.java` | 520 | Fondo del botón Editar |
| `D_ficha_trabajo.java` | 493 | Fondo del botón Actualizar estado |
| `D_informacion_estatica.java` | 357, 385 | Fondo de los botones de ingreso manual |
| `Exportar_PDF.java` | 53, 142 | Color de los títulos en los PDF |
| `V_login_user.java` | 124 | Fondo del botón Ingresar (`new Color(47, 101, 202)`) |
| `V_login_user.form` | 134 | Fondo del botón Ingresar (`red="2f" green="65" blue="ca"`) |

### Verdes

| Hexadecimal | Archivo | Líneas | Uso |
|---|---|---|---|
| `#008000` | `D_copias_seguridad.java` | 425, 451 | Estado "Conectado a Google Drive" |
| `#008F39` | `D_registrar_pagos.java` | 528 | Mensaje de pago registrado con éxito |
| `#00FF00` | `D_informes.java` | 338 | Estado "Mes empezado (Sin datos relevantes)" |

### Naranja y rojo puro

| Hexadecimal | Archivo | Líneas | Uso |
|---|---|---|---|
| `#FFA500` | `D_informes.java` | 341 | Estado "Mes en curso (datos parciales)" |
| `#FF0000` | `D_informes.java` | 344 | Estado "Mes terminado (datos completos)" |
| `#FF0000` | `V_login_user.java` | 138 | Fondo del botón Salir (`new Color(255, 0, 0)`) |
| `#FF0000` | `V_login_user.form` | 150 | Fondo del botón Salir (`red="ff" green="0" blue="0"`) |

## 3. Colores definidos sin hexadecimal

Estos colores no usan un literal hexadecimal, pero equivalen a uno. Se incluyen para completar
la paleta real de la interfaz.

| Expresión en el código | Equivalente | Uso | Archivos y líneas |
|---|---|---|---|
| `Color.red` / `Color.RED` | `#FF0000` | Errores y avisos | `V_R_Empleado.java:1002, 1078`; `V_login_user.java:221, 224`; `D_registrar_pagos.java:394, 404, 414` |
| `Color.green` | `#00FF00` | Confirmación de usuario disponible en el login | `V_login_user.java:227` |
| `Color.BLUE` | `#0000FF` | Estado del informe | `D_informes.java:529` |
| `Color.ORANGE` | `#FFC800` | Aviso de edición exitosa | `D_registrar_pagos.java:542` |
| `Color.GRAY` | `#808080` | Texto de ayuda en campos de búsqueda | `V_clientes.java:272, 290`; `D_soporte_tecnico.java:191, 209` |
| `Color.BLACK` | `#000000` | Texto normal al escribir en un campo | `V_clientes.java:281`; `D_soporte_tecnico.java:200` |
| `Color.DARK_GRAY` | `#404040` | Subtítulos en los PDF | `Exportar_PDF.java:54, 143` |
| `new Color(0, 0, 0)` | `#000000` | Borde de las imágenes de foto/evidencia | `V_R_Empleado.java:329`; `V_R_Herramienta.java:162`; `V_R_Bitacora.java:586` |
| `new Color(47, 101, 202)` | `#2F65CA` | Fondo del botón Ingresar | `V_login_user.java:124` |
| `new Color(255, 0, 0)` | `#FF0000` | Fondo del botón Salir | `V_login_user.java:138` |

> `Color.ORANGE` de Java equivale a `#FFC800`, que no es el mismo naranja que `#FFA500`
> usado en `D_informes.java`.

## 4. Colores de Google Sheets

En `GoogleServicesProvider.java` se definen colores con componentes decimales (0.0 a 1.0)
para el formato de las cotizaciones exportadas.

| Alias en el código | RGB decimal | Hexadecimal aproximado | Uso | Línea |
|---|---|---|---|---|
| `bgDarkBlue` | `0.12, 0.27, 0.50` | `#1F4580` | Fondo de títulos, encabezados y totales | 810 |
| `bgLightBlue` | `0.85, 0.90, 0.95` | `#D9E6F2` | Fondo de subencabezados de tablas | 811 |
| `textWhite` | `1.00, 1.00, 1.00` | `#FFFFFF` | Texto sobre los encabezados azules | 812 |

> El azul de Google Sheets (`#1F4580`) no coincide con el azul institucional de la interfaz
> (`#2F65CA`), aunque pertenecen a la misma familia cromática.

## 5. Observaciones

- La paleta de la interfaz se reduce prácticamente a **azul**, **rojo** y **verde**, con
  naranja y rojo puro como colores de estado.
- Hay tres verdes distintos (`#008000`, `#008F39` y `#00FF00`) y dos rojos (`#CE1126` y
  `#FF0000`) con significados similares. Conviene unificarlos o documentar cuándo usar cada uno.
- Los colores están dispersos en las clases y se repiten como literales. Centralizarlos en una
  clase de constantes (por ejemplo, `utilidades/Colores.java`) facilitaría mantener el tema y
  evitar inconsistencias.
- `V_login_user.form` y `V_login_user.java` contienen los mismos dos colores (botones Ingresar
  y Salir) porque NetBeans genera el código Java a partir del formulario.

## Anexo: colores del diagrama UML

Colores de la paleta por defecto de draw.io usados en `Sistema_en/UML_Clases.drawio`. Se listan
solo como referencia y no forman parte de la interfaz de la aplicación.

| Hexadecimal | Elemento del diagrama |
|---|---|
| `#DAE8FC` / `#6C8EBF` | Relleno y borde de entidades del dominio |
| `#FFF2CC` / `#D6B656` | Relleno y borde de componentes de infraestructura y notas |
| `#D5E8D4` / `#82B366` | Relleno y borde de clases DAO |
| `#E1D5E7` / `#9673A6` | Relleno y borde del punto de entrada |
| `#F5F5F5` / `#999999` | Relleno y borde de stubs (POJOs) |
| `#666666` | Líneas y flechas de relación |
| `#FFFFFF` | Fondo de etiquetas de relación |
