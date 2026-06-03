# Sistema EN

Sistema para la empresa: ESTIBEN NAVARRO SOLUCIONES HIDRO-SANITARIAS <br>
Lenguaje de programacion: **Java** <br>
IDE usado: **Neatbeans 17** <br>
Librerias externas usadas: *JCalendar*

## Documentos

**Pagina web del proyecto**: https://ares-linux.github.io/herramientas-equipo/

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

Antes de inventar o intentar algo nuevo, asegúrate de tener el código más reciente que se haya subido a GitHub.

1. Abre la terminal en la carpeta del proyecto (usa **Git Bash** ).
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


### Paso 3: Haz tu tarea y guarda tus cambios

Programa con normalidad en tus archivos. Cuando termines o quieras guardar tu avance del día, usa el combo clásico de tres comandos:

1. Selecciona los archivos modificados:
```bash
git add .

```


2. Ponle un título a lo que hiciste (un mensaje claro), ingresa tu nombre para identificar quien hizo el cambio y que ha realizado cada uno:
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
4. Escribe una breve descripción de lo que hiciste y avísale a alguno de los dos por el grupo para que revise el código que escribiste.
5. Si alguno ve que todo funciona, él le dará al botón **"Merge pull request"**.

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
