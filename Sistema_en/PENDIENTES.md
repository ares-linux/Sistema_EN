# Pendientes del proyecto

Inventario de lo que quedó sin terminar, con su ubicación exacta. Sirve para cerrarlo
todo antes de aplicar la Fase 2B.

Leyenda: `[ ]` pendiente · `[~]` a medias · `[x]` verificado como ya resuelto

---

## A. Búsqueda de trabajos por estado (formulario de registro)

La búsqueda se programa en `D_buscar_trabajo`, el diálogo "Buscar trabajo" que abre el
botón `b_buscar` de `V_R_Trabajo`. Los controles ya están maquetados en el `.form`
(`jLabel2` "Fecha de registro" + `d_fecha_registro`, `jLabel3` "Estado" + `c_estado`,
`jLabel4` "Resultados" + tabla, y los botones `b_buscar` / `b_limpiar` /
`b_seleccionar` / `b_cancelar`), pero la lógica nunca se escribió.

- [ ] `D_buscar_trabajo.java:156` — `c_estado` conserva el modelo de fábrica
      `{"Item 1","Item 2","Item 3","Item 4"}`; nunca se cargaron los estados reales.
- [ ] `D_buscar_trabajo.java:98-104` — el botón "Buscar" no tiene `ActionListener`:
      solo `setText` + `add`. Pulsarlo no hace nada.
- [ ] `D_buscar_trabajo.java:215-225` — `llenar_tabla()` siempre llama
      `obtenerTrabajosConClientes()` e ignora `c_estado` y `d_fecha_registro`.
- [ ] `D_buscar_trabajo.java:118-125` — la tabla declara 5 columnas
      (`Id, Descripcion, Fecha, Hora, Estado`) pero el DAO devuelve 11. Hoy la columna
      "Estado" muestra `direccion` y "Descripcion" muestra `id_cliente`.
- [ ] `D_buscar_trabajo.java:205-213` — `limpiar_text()` y el botón "Limpiar" hacen
      `setSelectedItem(null)` (dejarían el combo vacío) y no recargan la tabla.
- [ ] `D_buscar_trabajo.java:227-246` — "Seleccionar" hace `dispose()` incluso sin
      selección, el mensaje dice "cliente" en vez de "trabajo", y el `id_trabajo` real
      (campo `id`, línea 32) no tiene getter: `getFila()` devuelve el índice de fila.
- [ ] `D_buscar_trabajo.java:248-250` — el clic en la tabla no marca la fila, así que
      "Seleccionar" falla si el usuario solo hizo clic.
- [ ] `V_R_Trabajo.java:1109-1117` — el resultado del diálogo se guarda en
      `int id_cliente = buscar.getFila();` y nunca se usa. El comentario
      "Usamos la variable global de la línea 386" está obsoleto (esa variable ya no
      existe). Debería llamar a `abrirParaEdicionDesdeFicha(id_cliente, id_trabajo)`
      (línea 74), que ya deja el formulario en modo edición.
- [~] `D_buscar_trabajo.form:107-113` y `:132-137` — el modelo de la tabla y el del
      combo están en el `.form`; hay que mantenerlos sincronizados con el código.

Decisiones ya tomadas: solo el diálogo; "Seleccionar" carga el trabajo en el
formulario para editar; coincidencia exacta de `fecha_cr`; 11 columnas como en
`V_trabajos`.

**No hace falta tocar `dao/TrabajoDAO.java`**: `buscarTrabajosPorPeriodo(estado, desde,
hasta, por_fecha_fin)` con `desde == hasta` y `por_fecha_fin = false` ya produce
coincidencia exacta sobre `fecha_cr` y devuelve las 11 columnas.

---

## B. Búsqueda de herramientas (formulario de registro)

`D_buscar_herramienta` no tiene lógica implementada. Los controles están maquetados
en el `.form`, pero el botón "Buscar" está vacío y la tabla siempre se llena con
todas las herramientas.

- [ ] `D_buscar_herramienta.java` — `b_buscarActionPerformed` está vacío: el botón
      no tiene comportamiento.
- [ ] `D_buscar_herramienta.java` — el combo `c_tipo` conserva el modelo de fábrica
      vacío: nunca se cargan los tipos de herramienta.
- [ ] `D_buscar_herramienta.java` — `llenar_tabla()` siempre llama
      `obtenerHerramienta()`; ignora el texto y el tipo seleccionados.
- [ ] `D_buscar_herramienta.java` — limpiar los filtros deja `c_tipo` en `null` y no
      recarga la tabla.

**No hace falta tocar la capa de datos.** Ya existe lo necesario:
`HerramientaDAO.buscarHerramienta(String n_serie, String nombre_tipo)` y
`Tipos_herramientaDAO.obtenerHerramientas()`.

- [ ] Confirmar la semántica de la coincidencia de texto: hoy
      `buscarHerramienta` compara por número de serie y nombre de tipo; falta ver si
      además debe filtrar por nombre de herramienta y decidir si es coincidencia
      parcial (like) o exacta.
- [ ] Revisar el consumidor en `V_R_Herramienta.java` (línea ~704): comprobar que
      recoge el `id_herramienta` real de la fila elegida y no el índice de fila, para
      que "seleccionar" recargue la herramienta correcta.

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
      impide que un fallo de red transitorio se clasifique como reintentable.
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

## J. Decisiones pendientes de confirmar

- [ ] ¿`D_convertir_trabajo` se migra a `EjecutorGoogle` o se queda con su
      `SwingWorker` propio? Está excluido de la 2A a propósito, porque pasarlo podía
      reordenar el diálogo de éxito, pero sigue siendo una decisión abierta.
- [~] Sincronización de los modelos del `.form` con el código: si se cambian por
      código (fuera de las regiones `GEN`), el lienzo del Designer queda desfasado.
