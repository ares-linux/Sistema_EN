# Registro de publicación — Sistema_EN

## Objetivo

Documentar los comandos y los requisitos necesarios para ejecutar un release del proyecto **Sistema_EN**, desde el clonado del repositorio hasta la publicación en npm (vía `jdeploy publish`).

## Comandos de release

```bash
git tag -a v1.2.0 -m "Nuevo diseño del formulario configuraciones"
git push origin v1.2.0
jdeploy publish
```

---

## Requisitos previos

### Para `git tag -a v1.2.0 -m "..."`

- Git instalado.
- Estar en la rama correcta: `main`.
- Los cambios ya commiteados (el tag se crea sobre el commit actual).

### Para `git push origin v1.2.0`

- Repositorio remoto configurado:
  `origin → https://github.com/ares-linux/Sistema_EN.git`
- Credenciales de GitHub válidas (SSH o Personal Access Token).

### Para `jdeploy publish`

- **Node.js + npm** instalados.
- **Java 17** (`javaVersion: 17` en el `package.json`).
- **jDeploy CLI** instalado globalmente:
  ```bash
  sudo npm install -g jdeploy
  ```
- **JAR compilado** en `dist/Sistema_en.jar` (build con Ant o desde NetBeans).
- **`credentials.json`** ubicado en `Sistema_en/` (raíz del proyecto, NO en `dist/`).
- **Token de npm** válido para publicar en `registry.npmjs.org`.

---

## Flujo completo desde clonar el repositorio

```bash
# 1. Clonar
git clone https://github.com/ares-linux/Sistema_EN.git
cd Sistema_EN/Sistema_en

# 2. Compilar (genera dist/Sistema_en.jar)
ant -f Sistema_en/build.xml -Dlibs.CopyLibs.classpath=<ruta>/org-netbeans-modules-java-j2seproject-copylibstask.jar jar

# 3. Verificar credentials.json en Sistema_en/ (no en dist/)

# 4. Actualizar versión en package.json

# 5. Commit + tag + push
git add .
git commit -m "..."
git tag -a vX.Y.Z -m "..."
git push origin vX.Y.Z

# 6. Publicar
sudo npm install -g jdeploy
cd Sistema_en && jdeploy publish

# 7. Validar (opcional, sin publicar)
jdeploy package
```

---

## Puntos clave del release

- El workflow de GitHub (`.github/workflows/release.yml`) solo compila y valida el build (`ant` + `jdeploy package`); **no publica** en npm.
- La publicación en npm se hace **manualmente** con `jdeploy publish`.
- No se debe re-publicar una versión que ya exista en npm (error 403: *"You cannot publish over the previously published versions"*).
- `credentials.json` debe estar en `Sistema_en/` (lo toma el `package.json` con `files → dir: "."`), y **no** debe incluirse en el bundle los archivos generados por usuario (`base_de_datos.properties`, `tokens/`).
- El icono se referencia con `"icon": "icon.png"` (raíz del paquete) y el JAR con `"jar": "dist/Sistema_en.jar"`.

---

## Estado del release actual (v1.2.0)

- Tag creado y pusheado: `v1.2.0`.
- Paquete publicado en npm: `sistema-en@1.2.0`.
- Bundle generado correctamente (incluye `jdeploy-bundle/credentials.json` y `icon.png`).

---

## Próximos pasos sugeridos

- Reintentar el upload del icono a jdeploy.com (falló previamente por error de carga del paquete en el registry).
- Ejecutar `npm pkg fix` si el `package.json` tiene errores normalizados por npm.
- Mantener el flujo: tag → push → workflow valida → `jdeploy publish`.
