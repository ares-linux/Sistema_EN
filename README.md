# Sistema EN

Sistema para la empresa: ESTIBEN NAVARRO SOLUCIONES HIDRO-SANITARIAS <br>
Lenguaje de programacion: **Java** <br>
IDE usado: **Neatbeans 17** <br>

## Documentos

**Documento del proyecto**: https://docs.google.com/document/d/12SmmYQNZkl061AGfaZCFyhDZNq0S0iST_AfJR-TUHPg/edit?tab=t.0 <br>
**Documentacion tecnica del proyecto**:<br>
**Documentacion de uso del proyecto**:<br>

## Drawio

**UML**: https://app.diagrams.net/#G1DmTMbX9q5YIoGHLjjUoqOyeUmW1WYegS#%7B%22pageId%22%3A%22p0Nng-llTZR5P_ub6WVB%22%7D <br>
**Pantallas**: https://app.diagrams.net/#G1TkRRpZEDidcrwZncmayUE5Xj6aX3U1lM#%7B%22pageId%22%3A%22GVGlkhBJuxCRhi2uFk0u%22%7D <br>
**Organigramas**: https://app.diagrams.net/#G1xPoVu9vqeyRg9ZgoVGe8KL0vy8R7NFpV#%7B%22pageId%22%3A%22DeQlmfizWPvocvz4jEQx%22%7D <br>

---

# 📖 Manual de Git: La Regla de Oro del Trabajo en Equipo

¡Bienvenidos al proyecto! Para que los tres podamos trabajar al mismo tiempo sin borrar el código de los demás y mantener el proyecto ordenado, vamos a seguir una única **regla de oro obligatoria**:

> 🚨 **PROHIBIDO programar directo en la rama `main`. Cada tarea, por más pequeña que sea, debe tener su propia rama.**

---

## 🗺️ El Flujo de Trabajo en una Imagen

Para que visualices cómo se ve nuestro trabajo, imagina que la rama `main` es el tronco de un árbol y cada uno de nosotros crea pequeñas ramas que luego se vuelven a unir al tronco cuando el código funciona perfectamente:

---

## 🛠️ Paso a Paso: Cómo hacer una tarea (Desde Windows)

Sigue estos pasos **cada vez** que vayas a iniciar una nueva actividad.

### Paso 1: Limpiar y actualizar tu casa

Antes de inventar algo nuevo, asegúrate de tener el código más reciente que tus compañeros hayan subido a GitHub.

1. Abre la terminal en la carpeta de tu proyecto (puedes usar **Git Bash**, la terminal de VS Code o la consola de Windows).
2. Muévete a la rama principal:
```bash
git checkout main

```


3. Descarga lo último de GitHub:
```bash
git pull origin main

```



### Paso 2: Crear tu propia rama (Tu espacio seguro)

Ahora vas a crear una ramita exclusiva para tu tarea. Ponle un nombre corto que describa lo que vas a hacer, usando minúsculas y guiones.

* **Estructura recomendada:** `tipo-de-tarea/descripcion-corta`
* Si es una función nueva: `feature/login-correo` o `feature/tabla-productos`
* Si vas a arreglar un error: `fix/error-registro`
* Si vas a cambiar textos o diseño menor: `design/colores-boton`


* **El comando para crear y meterte a la rama al mismo tiempo:**
```bash
git checkout -b feature/tu-tarea-aqui

```


*(Sabrás que funcionó porque en la terminal de Git Bash el texto azul cambiará de `[main]` a `[feature/tu-tarea-aqui]`)*.

> 💡 **¿Usas VS Code? Forma visual y rápida:**
> En la esquina inferior izquierda de VS Code, dale clic a la palabra **main**. Se abrirá un menú arriba; selecciona **"Create new branch..."**, escribe el nombre de tu rama y presiona Enter. ¡Listo!

### Paso 3: Haz tu tarea y guarda tus cambios

Programa con normalidad en tus archivos. Cuando termines o quieras guardar tu avance del día, usa el combo clásico de tres comandos:

1. Selecciona los archivos modificados:
```bash
git add .

```


2. Ponle un título a lo que hiciste (un mensaje claro):
```bash
git commit -m "Agregada la interfaz de la pantalla de login"

```


3. Sube tu rama a GitHub por primera vez:
```bash
git push origin feature/tu-tarea-aqui

```



### Paso 4: El Pull Request (PR) u "Orden de revisión"

Una vez que subas tu rama, tu trabajo ya está en internet, pero **aún no se ha mezclado** con el proyecto principal.

1. Entra a nuestra página del repositorio en GitHub.
2. Verás un cuadro amarillo brillante que dice algo como: `feature/tu-tarea-aqui had recent pushes...`.
3. Haz clic en el botón verde **"Compare & pull request"**.
4. Escribe una breve descripción de lo que hiciste y avísale a alguno de los otros dos compañeros por el grupo para que revise tu código.
5. Si tu compañero ve que todo funciona, él le dará al botón **"Merge pull request"**.

¡Felicidades! Tu código ahora forma parte oficialmente del proyecto.

---

## 🚫 ¿Qué pasa si me equivoco y edito en `main`?

Si te das cuenta de que escribiste código y olvidaste cambiarte de rama, **¡NO HAGAS PUSH!** Respira hondo y escribe esto en la terminal:

```bash
# 1. Guarda tus cambios en un bolsillo temporal
git stash

# 2. Crea la rama en la que debiste haber estado
git checkout -b feature/nombre-correcto-de-la-tarea

# 3. Saca los cambios del bolsillo y ponlos en esta nueva rama
git stash pop

```

Listo, el error está arreglado y puedes seguir en tu rama como si nada hubiera pasado.
