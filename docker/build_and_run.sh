#!/bin/bash

# ==============================================================================
# Shell-Skript zum Bauen und Starten des Spotify Debt Service Docker-Containers
# KORRIGIERTE VERSION
# ==============================================================================

# --- Konfiguration ---
IMAGE_NAME="spotify-debt-service"
CONTAINER_NAME="spotify-debt-service-container"
HOST_DATA_PATH="~/spotify-app-data"
APP_PORT=8082


# --- Skriptlogik (bitte ab hier nichts ändern) ---
set -e

echo ">>> Suche nach der JAR-Datei..."
JAR_FILE=$(find . -maxdepth 1 -name "*.jar" | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "FEHLER: Keine .jar-Datei im aktuellen Verzeichnis gefunden. Bitte legen Sie die JAR-Datei hier ab."
    exit 1
fi
echo "Gefundene JAR-Datei: $JAR_FILE"
echo ""

# --- Aufräumen ---
echo ">>> Stoppe und entferne alten Container (falls vorhanden)..."
docker stop $CONTAINER_NAME || true
docker rm $CONTAINER_NAME || true
echo ""

# --- Bauen ---
echo ">>> Baue neues Docker-Image mit dem Namen '$IMAGE_NAME'..."
docker build -t $IMAGE_NAME .
echo ""

# --- Vorbereiten ---
EXPANDED_HOST_DATA_PATH=$(eval echo "$HOST_DATA_PATH")
echo ">>> Stelle sicher, dass das Host-Verzeichnis für die Daten existiert: $EXPANDED_HOST_DATA_PATH"
mkdir -p "$EXPANDED_HOST_DATA_PATH"
echo ""


# --- Starten ---
echo ">>> Starte neuen Container '$CONTAINER_NAME' auf Port $APP_PORT..."
# WICHTIG: Stellen Sie sicher, dass nach den '\' Zeichen am Zeilenende
# absolut keine Leerzeichen oder andere Zeichen folgen.
docker run \
  -d \
  -p "$APP_PORT:$APP_PORT" \
  -e "SERVER_PORT=${APP_PORT}" \
  -v "$EXPANDED_HOST_DATA_PATH:/app/data" \
  --name $CONTAINER_NAME \
  $IMAGE_NAME

echo ""
echo "========================================================"
echo "FERTIG! Der Container wurde erfolgreich gestartet."
echo ""
echo "  - Host-Datenverzeichnis: $EXPANDED_HOST_DATA_PATH"
echo "  - Anwendung erreichbar unter: http://<IP_Ihres_Servers>:$APP_PORT"
echo "  - Um die Logs anzusehen, führen Sie aus: docker logs $CONTAINER_NAME"
echo "  - Um den Container zu stoppen, führen Sie aus: docker stop $CONTAINER_NAME"
echo "========================================================"