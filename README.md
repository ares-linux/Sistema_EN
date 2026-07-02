# Sistema EN

<img width="520" height="261" alt="logo 2 0" src="https://github.com/user-attachments/assets/04061dcc-7484-4573-a746-a85d29977daf" />

Sistema para la empresa: ESTIBEN NAVARRO SOLUCIONES HIDRO-SANITARIAS <br>
Lenguaje de programacion: **Java** <br>
IDE usado: **NetBeans 17** <br>
Librerias externas usadas: *JCalendar*, *FlatLaf* (para look and feel moderno), *AbsoluteLayout* (para diseño de GUI)
Sistema administrador de bases de datos usado: **sqlite**.
Programas adicionales usados: db browser for SQLite.

<p align="left">
<img src="https://img.shields.io/badge/STATUS-EN%20DESAROLLO-green">
</p>

## Documentos

**Pagina web del proyecto**: https://ares-linux.github.io/herramientas-equipo/

---

# 📖 Manual de GitHub Desktop: La Regla de Oro del Trabajo en Equipo

¡Bienvenidos al proyecto! Para que los tres podamos trabajar al mismo tiempo sin borrar el código de los demás y mantener el proyecto ordenado, vamos a seguir una única **regla de oro obligatoria**:

> 🚨 **PROHIBIDO programar directo en la rama `main`. Cada tarea, por más pequeña que sea, debe tener su propia rama.**

---

## 🛠️ Paso a Paso: Cómo hacer una tarea (Desde GitHub Desktop)

Sigue estos pasos **cada vez** que vayas a iniciar una nueva actividad.

### Paso 1: Limpiar y actualizar tu casa

Antes de inventar o intentar algo nuevo, asegúrate de tener el código más reciente que tus compañeros hayan subido a GitHub.

1. Abre **GitHub Desktop** y asegúrate de estar en el repositorio del proyecto (arriba a la izquierda en *Current repository*).
2. En la barra superior, haz clic en **Current branch** y selecciona **`main`**.
3. Haz clic en el botón grande de la derecha que dice **Fetch origin**.
4. Si hay cambios nuevos, el botón cambiará a **Pull origin**. Haz clic ahí para descargar lo último que se haya subido al proyecto.

---

### Paso 2: Crear tu propia rama (Tu espacio seguro)

Ahora vas a crear una ramita exclusiva para tu tarea para no alterar el tronco principal.

1. Haz clic de nuevo en **Current branch** (arriba al centro).
2. Haz clic en el botón **New branch**.
3. Escribe el nombre de tu rama siguiendo esta estructura recomendada: `tipo-de-tarea/descripcion-corta`
* Si es una función nueva: `feature/login-correo`
* Si vas a arreglar un error: `fix/error-registro`
* Si vas a cambiar diseño menor: `design/colores-boton`


4. Haz clic en **Create branch**.
5. Para que tus compañeros sepan que estás trabajando en esto, haz clic en el botón azul **Publish branch** que aparece en la pantalla principal.

---

### Paso 3: Haz tu tarea y guarda tus cambios

Abre tu editor de código (como VS Code) y programa con normalidad. A medida que guardes tus archivos, verás que aparecen automáticamente en la columna izquierda de GitHub Desktop bajo la pestaña **Changes**.

Cuando termines tu avance o tu tarea, haz lo siguiente:

1. En la esquina inferior izquierda verás dos cuadros de texto bajo el título **Commit to [nombre de tu rama]**.
2. En el primer cuadro (*Summary*), escribe un título corto y claro de lo que hiciste (ej: `Agregada la interfaz de login`).
3. (Opcional) En el cuadro de abajo (*Description*), puedes agregar detalles o poner tu nombre si quieren identificarlo rápido.
4. Haz clic en el botón azul **Commit to [tu-rama]**.
5. Por último, haz clic en el botón **Push origin** en la barra superior para subir tus cambios a internet.

---

### Paso 4: El Pull Request (PR) u "Orden de revisión"

Tu trabajo ya está en la nube, pero **aún no se ha mezclado** con el proyecto principal.

1. Justo después de hacer el *Push*, GitHub Desktop te mostrará un botón mágico en el centro que dice **Create Pull Request**. Haz clic ahí.
2. Esto te abrirá automáticamente el navegador en la página web de GitHub.
3. Escribe una breve descripción de lo que hiciste y haz clic en el botón verde **Create pull request**.
4. Avísale a alguno de los otros dos por el grupo para que revise tu código.
5. Si todo está correcto, esa persona le dará al botón **Merge pull request** desde la web. ¡Listo!

---

## 🚫 ¿Qué pasa si me equivoco y edito en `main`?

Si escribiste código y de repente te diste cuenta de que olvidaste cambiarte de rama, **¡no entres en pánico!** GitHub Desktop lo resuelve muy fácil sin romper nada:

1. Ve a **Current branch** en la barra superior y haz clic en **New branch**.
2. Escribe el nombre correcto para tu tarea.
3. Al hacer clic en **Create branch**, GitHub Desktop detectará que tienes cambios sin guardar y te mostrará una ventana con dos opciones.
4. Selecciona la primera opción: **"Bring my changes to [nueva-rama]"** (Traer mis cambios a la nueva rama).
5. Haz clic en **Switch branch**.

¡Y listo! Tus cambios se mudarán mágicamente a la rama correcta y la rama `main` quedará limpia y a salvo.
