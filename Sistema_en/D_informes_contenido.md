# Formulario de Informes (`D_informes.java`)

Contenido de secciones y textos del formulario (diseño de la versión 1.0.17).

## Título
- `jLabel1`: **"Informe mensual"**

## Cabecera (período y estado)
- `jLabel2`: **"Periodo:"**
- `jLabel23`: **"[ Enero 2027 ▼ ]"**
- `jLabel42`: **"Estado:"**
- `jLabel43`: **"🟡 Mes en curso (datos parciales)"**

## Pestañas principales (`jTabbedPane1`)
- **"Informe mensual"** (`jPanel1`, contiene la cuadrícula con cabecera + `jTabbedPane2` + botones)
- **"Historial"** (`jPanel2`, tabla `Tabla_informes_historial`)

### Tabla de historial
- `Tabla_informes_historial`: columnas **["Periodo", "Generado", "Estado", "Acciones"]**

## Pestañas de estadísticas (`jTabbedPane2`, dentro de "Informe mensual")
- **"Trabajos"** (`jPanel6`, tabla `Tabla_trabajos`)
- **"Clientes"** (`jPanel7`, tabla `Tabla_clientes`)
- **"Cotizaciones"** (`jPanel8`, tabla `Tabla_cotizaciones`)
- **"Otros"** (`jPanel9`, tabla `Tabla_otros`)

Tablas de estadísticas: columnas **["Estadistica", "Resultado"]**

## Botones (`jPanel5`)
- `b_exportar`: **"Exportar"** (muestra diálogo PDF/EXCEL)
- `b_contrastar`: **"Constrastar datos"**
- `b_cerrar`: **"Cerrar"**

## Exportación (PDF / EXCEL)
- `b_exportar` abre: **"¿En que formato desea exportar la cotizacion?"** con opciones **["PDF", "EXCEL"]**.
- En esta versión ambos formatos muestran el mensaje:
  - **"La exportación a PDF aún no está disponible en esta versión. Estará en las próximas actualizaciones."**
  - **"La exportación a Excel aún no está disponible en esta versión. Estará en las próximas actualizaciones."**
- La exportación estará disponible en futuras actualizaciones.

## Configuración de tablas
- `configuraciones_tabla()`: tablas no editables, selección única, sin reordenar/redimensionar encabezados.