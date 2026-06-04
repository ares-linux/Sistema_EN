#!/bin/bash

# Salir inmediatamente si algún comando falla
set -e

# 1. Solicitar el nombre de la nueva rama
echo "🚀 Configurando nueva rama..."
read -p "Introduce el nombre de la nueva rama: " branch_name

if [ -z "$branch_name" ]; then
    echo "❌ El nombre de la rama no puede estar vacío."
    exit 1
fi

# 2. Crear y cambiar a la nueva rama
echo "🌿 Creando y cambiando a la rama '$branch_name'..."
git checkout -b "$branch_name"

# 3. Solicitar el mensaje del commit
echo "📝 Preparando cambios..."
read -p "Introduce el mensaje para el commit: " commit_message

if [ -z "$commit_message" ]; then
    commit_message="Actualización automática en rama $branch_name"
fi

# 4. Añadir cambios y hacer commit
git add .
git commit -m "$commit_message"

# 5. Subir la rama al repositorio remoto
echo "📤 Subiendo la rama '$branch_name' a origin..."
git push -u origin "$branch_name"

# 6. BUENA PRÁCTICA: Obtener la URL del repositorio para abrir el Pull Request
# Extrae la URL de origen (sirve para HTTPS y SSH)
repo_url=$(git config --get remote.origin.url | sed -e 's/git@github.com:/https:\/\/github.com\//' -e 's/\.git$//')

if [[ $repo_url == *"github.com"* ]]; then
    pr_url="${repo_url}/compare/main...${branch_name}?expand=1"
    echo "🔗 Generando enlace de Pull Request..."
    echo "🌍 Abriendo en tu navegador: $pr_url"
    
    # Detecta el sistema operativo para abrir el navegador automáticamente
    case "$OSTYPE" in
        linux*)       xdg-open "$pr_url" ;;
        darwin*)      open "$pr_url" ;;
        msys*|cygwin*) start "$pr_url" ;;
        *)            echo "👉 Copia este enlace para crear el PR: $pr_url" ;;
    esac
else
    echo "✅ Rama subida. (Autodetección de PR solo configurada para GitHub)."
fi

echo "🎉 ¡Todo listo! Revisa tu navegador para fusionar a main de forma segura."
