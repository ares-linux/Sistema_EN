# Pendientes del proyecto

Inventario de lo que quedó sin terminar, con su ubicación exacta. Sirve para cerrarlo
todo antes de aplicar la Fase 2B.

Leyenda: `[ ]` pendiente · `[~]` a medias · `[x]` verificado como ya resuelto

---

## A. Búsqueda de trabajos por estado (formulario de registro) — RESUELTO

Implementada en `D_buscar_trabajo`, el diálogo "Buscar trabajo" que abre el botón
`b_buscar` de `V_R_Trabajo`. Es un flujo alterno para llevar un trabajo al formulario de
registro sin salir de la ventana.

- [x] `c_estado` se llena con los estados reales `{"Todos","Pendiente","En progreso",
      "Completado"}`, en el `.java` y en el `.form`.
- [x] El botón "Buscar" tiene `ActionListener` en el `.form` y llama a `filtrar_tabla()`.
- [x] `filtrar_tabla()` usa `TrabajoDAO.buscarTrabajosPorPeriodo(estado, fecha, fecha,
      false)`: con `desde == hasta` da coincidencia exacta sobre `fecha_cr`, que es lo
      que corresponde a la etiqueta "Fecha de registro". Sin fecha trae todo.
- [x] La tabla declara las 11 columnas, en el mismo orden que el DAO y que la tabla de
      `V_R_Trabajo`. Ya no se mezclan `direccion` con "Estado".
- [x] `limpiar_text()` y "Limpiar" dejan el combo en "Todos", vacían la fecha y recargan.
- [x] "Seleccionar" solo hace `dispose()` si hubo selección, avisa si no la hubo, y el
      mensaje dice "trabajo". El `id_trabajo` real sale de `getIdTrabajo()`.
- [x] `fila` e `id` arrancan en `-1`. "Cancelar" y cerrar con la `X` devuelven `-1`, así
      que el consumidor no toca el formulario.
- [x] El clic en la tabla sí marca la fila: el constructor ya fija
      `SINGLE_SELECTION` (línea 25). La nota anterior sobre esto estaba desactualizada.
- [x] `V_R_Trabajo.b_buscarActionPerformed` ya no guarda el resultado en una variable
      muerta. Localiza el trabajo por `id` en la columna 0 de su propia tabla, marca la
      fila y llama a `cargar_datos_tabla(i)`.
- [x] `D_buscar_trabajo.form` y `.java` quedaron sincronizados: bloque `<Events>` de
      `b_buscar` y modelo de 11 columnas en ambos.

**No hizo falta tocar `dao/TrabajoDAO.java`**: `buscarTrabajosPorPeriodo` ya estaba bien
escrito. Se verificó contra la base que con `desde == hasta` filtra por día exacto, que
acepta `null` en estado y fechas, y que devuelve siempre 11 columnas.

Nota de diseño: el formulario padre avisa y no carga si está en modo Guardar, replicando
la guarda de `Tabla_trabajosMouseClicked`, porque cargar otro trabajo mientras se guarda
uno pisaría los datos pendientes.

---

## B. Búsqueda de herramientas (formulario de registro) — RESUELTO

- [x] `b_buscarActionPerformed` llama a `filtrar_tabla()`.
- [x] `llenar_tipos()` carga el combo con los tipos reales de
      `Tipos_herramientaDAO.obtenerHerramientas()`, con "Todos" de primero. Ojo con la
      nomenclatura: `Tipos_herramientaDAO.obtenerHerramientas()` (plural) devuelve
      **tipos**; `HerramientaDAO.obtenerHerramienta()` (singular) devuelve
      **herramientas**.
- [x] `filtrar_tabla()` usa `HerramientaDAO.buscarHerramienta(n_serie, tipo)`.
- [x] "Limpiar" vacía los filtros y recarga la tabla.
- [x] `fila_j` e `id_herramienta` arrancan en `-1`; `getIdHerramienta()` expone el id
      real. "Cancelar" y cerrar con la `X` devuelven `-1`.
- [x] `V_R_Herramienta.b_buscarActionPerformed` localiza la herramienta por `id` en su
      propia tabla. Antes tomaba el índice y llamaba a `cargar_datos_tabla(fila)` sin
      condición, así que al cancelar cargaba la fila 0 en silencio.
- [x] Corregido el typo `"TIpo"` de `jLabel3`.
- [x] Añadido el bloque `<Events>` de `b_limpiar` en el `.form`; antes el botón no
      tenía handler.

Sí hubo que tocar la capa de datos, al contrario de lo que decía la nota anterior:

- [x] `HerramientaDAO.obtenerHerramienta()` y `buscarHerramienta()`, y también
      `Tipos_herramientaDAO.obtenerHerramientas()`, tenían el `if (con == null)` **dentro**
      del `try (Connection con = Conexion.conectar(); ...)`. Como la cabecera del
      `try-with-resources` se evalúa antes del cuerpo, `con.prepareStatement(...)` ya
      lanzaba `NullPointerException` y la guarda era código muerto. Ahora `con` se
      declara aparte, se comprueba y se cierra en el `finally`, igual que en
      `TrabajoDAO`. Es un NPE real: `Conexion.conectar()` devuelve `null` si no encuentra
      el archivo de la base (`Conexion.java:115`).
- [x] `buscarHerramienta` normaliza los criterios: `n_serie` `null` pasa a `""` y un tipo
      `null`, vacío o distinto de "Todos" se trata como "traer todo". Antes un tipo `null`
      caía en la rama `else` y generaba `WHERE t.nombre = null`, que no trae ninguna fila.

Pendiente de decidir (no bloquea la entrega):

- [ ] Confirmar si el buscador debe filtrar además por nombre de herramienta. Hoy solo
      compara número de serie y tipo, con coincidencia parcial (`LIKE '%texto%'`).

### Trampas del DAO para no repetirlas

| Valor enviado | Qué debe ocurrir |
|---|---|
| tipo o estado `null` | se trata como "Todos", nunca como `= null` |
| texto `null` | se convierte en `""`, nunca en el literal `"%null%"` |
| texto `""` | `LIKE '%%'`, que es "traer todo" |
| `fecha_cr` / `fecha_inicio` | texto ISO `YYYY-MM-DD`, así que el orden lexicográfico coincide con el cronológico |

---

## C. Cotizaciones independientes (ventana nueva)

Implementado, compilado y con la reutilización de ventanas cerrada. Sin decisiones
pendientes.

- [x] `V_R_Cotizacion_independiente` es ahora instancia única: fábricas estáticas
      `abrirNueva()` / `abrirCotizacion(int)` fuera de `GEN` que reutilizan la ventana
      ya abierta (`instancia.isDisplayable()`), la maximizan, la traen al frente y
      nunca apilan copias. Antes cada clic en el menú o en el historial creaba otra
      instancia (se llegaron a ver 6 `V_historial_c` en un log) y la nueva, sin
      maximizar, quedaba detrás de la ventana ya maximizada y vacía.
- [x] Guardia de reemplazo: si hay una cotización en edición (`nuevo` o `editar`),
      pregunta antes de descartarla. No pregunta cuando la ventana se acaba de crear
      en ese mismo abrir (`recienReiniciada`), así el primer abierto no molesta.
- [x] `V_historial_c` también es instancia única (`abrirHistorial(Window)`); el menú y
      el botón "Abrir historial" la llaman por igual.
- [x] Doble clic en el historial y botón **"Abrir cotizacion"** (agregado por código
      en la barra norte del constructor, fuera de `GEN`) llaman a
      `V_R_Cotizacion_independiente.abrirCotizacion(id)`; se cargan los datos en la
      ventana ya visible y maximizada, en vez de abrir una ventana vacía detrás.
- [x] `b_cerrar` ahora hace `dispose()` y `b_cancelar` descarta y limpia hacia una
      cotización nueva, en ambas ventanas (`V_R_Cotizacion_independiente` y
      `V_R_Cotizacion`). Antes eran TODOs vacíos.
- [x] Mensajes de `abrirCotizacionPorId`: avisa si la cotización "No pertenece a un
      trabajo" (tipo Normal, se edita desde la ficha del trabajo) o "No existe".
- [x] Prueba manual en Swing verificada: el doble clic en el historial reutiliza la
      misma ventana maximizada con la cotización cargada, sin apilar ni dejar copias
      vacías, y la confirmación aparece al reemplazar una en edición.
- [x] Botón "Abrir historial" (`b_historial`) conectado con el evento generado por el
      Designer (`actionPerformed` → `b_historialActionPerformed`). La lógica quedó
      dentro del handler, así que el ciclo completo del botón está en las regiones
      `GEN` como en el resto de la ventana. Abre un `V_historial_c` maximizado, igual
      que desde el menú.
- [x] El ítem del menú `m_cotizacion` pasó de "Cotizacion de prueba" a
      **"Cotizacion independiente"** (`V_menu.form:105`), que es lo que realmente
      abre.
- [x] `V_historial_c` lista solo cotizaciones `Prueba`, y queda así a decisión
      explícita: las cotizaciones `Normal` son por trabajo y se editan desde la ficha
      del trabajo, no desde el historial. Por eso la ventana independiente rechaza
      abrir una `Normal` con aviso en vez de permitir editarla desde acá. El filtro
      ya está en el DAO (`CotizacionDAO.java:80` y `:145`), así que no hace falta
      tocarlo.

---

## D. Bug de prospecto ya corregido

- [x] `V_R_Cotizacion.java` y `V_R_Cotizacion_independiente.java` — el guardado del
      prospecto al editar estaba filtrado por `!"Prueba".equals(tipo)`, así que en
      toda cotización de prueba el nombre y el teléfono se perdían en silencio
      mientras el usuario veía "Cambios guardados". El `INSERT` nunca había filtrado
      por tipo, o sea que crear sí los guardaba y editar no. Se quitó el filtro en
      ambas ventanas: ahora el prospecto se persiste siempre que la cabecera y los
      detalles se guardaron, y si esa parte fallara se avisa por separado como ya
      hacía el `boolean prospectoActualizado`.
- [ ] Verificar en la BD que tras editar una cotización de prueba el
      `nombre_cliente` / `telefono_cliente` quedan actualizados (pendiente de la
      prueba manual en Swing).

---

## E. Fase 2B — cierre de la aplicación

- [ ] `V_menu.java:505` — `realizar_respaldo()` sigue siendo síncrono.
- [ ] `V_menu.java:658` — el cierre llama `System.exit(0)` de inmediato, por eso no
      puede encolar nada. Falta el diálogo de progreso con la opción
      **"Salir sin respaldar"**, confirmando si el respaldo falla.

---

## F. Fase 2C — arranque

- [ ] `Controlador_arranque.java:83`, `:237`, `:296` — `estaConectado()` se sigue
      llamando de forma síncrona; falta encolarlo en `EjecutorGoogle`.

---

## G. Google / Drive

- [ ] `GoogleServicesProvider.java:252` — `subirArchivo` busca con
      `name = '...' and trashed = false` **sin filtrar por la carpeta destino**: un
      archivo homónimo en otra carpeta puede ser el equivocado.
- [~] `sincronizarTrabajosPendientes` se encola con `ejecutarSilencioso`, que sí
      reintenta, aunque crea carpetas en Drive → riesgo de carpetas duplicadas.
- [~] `obtenerIdCarpetaEvidencias` captura la excepción y devuelve `null`, lo que
      impide que un fallo de red transitorio se clasifique como reintentable. Ahora
      recibe `crearSiNoExiste`: el listado de evidencias lo pasa en `false` para no
      crear la carpeta `Evidencias` como efecto secundario de una lectura.
- [~] `V_menu.java:607` y `V_R_Cotizacion.java:1486` disparan sincronización al
      abrir; vigilar si se muestran diálogos duplicados. (La ventana independiente ya
      no la dispara: quedó en `V_menu` y en la ventana normal, que son las que
      manejan trabajos.)
- [ ] `D_soporte_tecnico` solo deshabilita `b_enviar`; el botón "Cancelar" sigue
      activo mientras se envía el correo.
- [~] `Internet.URL_SONDEO` quedó mutable para poder simular fallos en las pruebas;
      decidir si se conserva así o se encapsula.

---

## H. Interfaz y textos

- [ ] `D_informes.java:381` — el diálogo de exportación ofrece `{"PDF", "EXCEL"}`;
      debería decir "Google Sheets".
- [x] El mismo texto en `V_R_Cotizacion.java:1792` ya dice "Hoja de calculo de
      Google". Sin pendiente.
- [x] `D_exportar_opciones` se eliminó; su copia del texto "EXCEL" ya no existe.
      Sin pendiente.
- [ ] `V_R_Trabajo.java:1011` — `t_descripcionKeyPressed` es un handler huérfano:
      `t_descripcion` no existe en `V_R_Trabajo.form`.

---

## I. Calidad, compilación y pruebas

- [ ] No hay `ant` instalado en el entorno. La compilación se validó a mano con
      `javac` sobre 74 fuentes; conviene instalar Ant o documentar el comando equivalente.
      Comando usado: `javac -nowarn -encoding UTF-8 -cp "lib/*:lib/librerias-google/*"
      -d /tmp/opencode/b @srcs.txt` (listado de fuentes por `find . -name "*.java"`).
- [~] La cotización 32 en la BD muestra el subtotal como `121212120351113216.00`: el
      valor está almacenado como `Float` y `String.format("%.2f", ...)` expone el error
      de precisión. Decidir si los importes monetarios de cotizaciones pasan a BigDecimal
      o si solo se corrige la presentación. Ver `total`/`subtotal` en
      `V_R_Cotizacion_independiente.java` y `V_R_Cotizacion.java` (~línea 41).
- [ ] No hay suite de pruebas permanente. Las 23 de `EjecutorGoogle` y las 9 de
      `UtilidadesError` viven en `/tmp/opencode` y se pierden al reiniciar.
- [x] Todo el trabajo de la Fase 2A y la ventana de cotizaciones independientes se
      commiteó en la entrega vigente. Sin pendiente.
- [x] Reautenticación OAuth fuera del EDT: ya no existe ningún diálogo de
      reautenticación en `GoogleServicesProvider`. Sin pendiente.
- [x] Placeholders en `D_acerca_de`: los textos ya son reales. Sin pendiente.

---

## K. Esquema de la base de datos — VERIFICADO Y ALINEADO

Se verificó que la base que crea el asistente ("base de datos nueva") produce el mismo
esquema que la base real. El método es crear una base temporal con
`InicializadorBD.crearBaseDatos()` y comparar su `PRAGMA table_info` y
`PRAGMA foreign_key_list` contra los de la base real, tabla por tabla.

Resultado: **0 diferencias en las 12 tablas**, columnas, tipos y claves foráneas.

- [x] `Trabajo.id_evento` está en el DDL (`InicializadorBD.java:67`) y en la base real,
      en la misma posición. Las bases viejas la reciben por la migración
      `COLUMNAS_TRABAJO_NUEVAS` (`:166-168`), que usa `ALTER TABLE ADD COLUMN` sobre lo
      que reporte `PRAGMA table_info` y por eso es idempotente.
- [x] Las migraciones se aplicaban desde dos lugares distintos: `asegurarEsquema`
      (:261) llamaba a `Cotizacion` y a `Trabajo`, pero `crearBaseDatos` (:334) solo
      llamaba a `Cotizacion`. No rompía nada porque el DDL ya trae `id_evento`, pero una
      base nueva se habría quedado sin esa columna sin ningún error visible si alguna
      vez se sacara del DDL. Ahora ambos pasan por `asegurarMigraciones(con)` (:212),
      que es el único punto donde se listan, así que agregar una migración no puede
      olvidarse en uno de los dos caminos.
- [x] `Tipos_herramienta.nombre` estaba declarado `INTEGER` en la base real y `TEXT` en
      el DDL. La base real era la rara. Riesgo que tenía: la afinidad INTEGER de SQLite
      convierte texto numérico, así que un tipo llamado `"007"` se habría guardado como
      `7`. Se recreó la tabla copiando las filas; los 6 tipos quedaron intactos y el
      autoincremento sigue en 7.
- [x] La FK de `Evidencia` estaba rota en la base real: `REFERENCES ""`, con el nombre
      de la tabla referenciada vacío, cuando el DDL dice
      `REFERENCES "Bitacora"("id_bitacora")` (:141). Se recreó la tabla y las 3 evidencias
      quedaron con la FK correcta.

### Datos huérfanos preexistentes (no tocados)

`PRAGMA foreign_key_check` reporta 17 referencias rotas que ya estaban antes de estos
cambios (se comprobó contra el respaldo previo a cualquier modificación). No las
arregló la reparación de la FK de `Evidencia`, que bajó el total de 20 a 17:

| Tabla | Filas | Referencia a |
|---|---|---|
| `Detalles_cotizacion` | 11 | `Cotizacion` 0 |
| `Bitacora` | 4 | `Trabajador` 1 |
| `Pago` | 2 | `Trabajo` 0 |

Solo son visibles si algún día se activa `PRAGMA foreign_keys`, que la aplicación nunca
establece. Decidir si se limpian con `DELETE` o se dejan como están.

### Migraciones que faltan

Estas columnas están en el DDL pero no aparecen en ninguna lista `COLUMNAS_*_NUEVAS`,
así que una base vieja que no las tuviera no las recuperaría nunca:

- [ ] `Cliente.u_modificacion` y `Cliente.fecha_registro`
- [ ] `Trabajo.direccion`, `Trabajo.carpeta` y `Trabajo.id_carpeta`

La base real sí las tiene, así que no afecta al uso diario. Solo importa para una base
muy antigua.

---

## J. Decisiones pendientes de confirmar

- [ ] ¿`D_convertir_trabajo` se migra a `EjecutorGoogle` o se queda con su
      `SwingWorker` propio? Está excluido de la 2A a propósito, porque pasarlo podía
      reordenar el diálogo de éxito, pero sigue siendo una decisión abierta.
- [~] Sincronización de los modelos del `.form` con el código: si se cambian por
      código (fuera de las regiones `GEN`), el lienzo del Designer queda desfasado.
