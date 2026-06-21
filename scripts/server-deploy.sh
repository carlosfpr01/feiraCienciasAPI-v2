#!/bin/bash
# Executado no servidor de produção pelo GitHub Actions (SSH) ou manualmente.
set -euo pipefail

DEPLOY_DIR="${DEPLOY_DIR:-/opt/feira-ciencias-api}"
COMPOSE_FILE="${COMPOSE_FILE:-docker-compose.prod.yml}"
WORKFLOW_APP_IMAGE="${APP_IMAGE:-}"
GHCR_USER="${GHCR_USER:?GHCR_USER is required}"
GHCR_TOKEN="${GHCR_TOKEN:?GHCR_TOKEN is required}"

cd "$DEPLOY_DIR"

if [ -f .env ]; then
  set -a
  # shellcheck disable=SC1091
  source .env
  set +a
fi

if [ -n "$WORKFLOW_APP_IMAGE" ]; then
  APP_IMAGE="$WORKFLOW_APP_IMAGE"
fi

APP_IMAGE="${APP_IMAGE:?APP_IMAGE is required}"

echo "[deploy] Authenticating with GHCR..."
echo "$GHCR_TOKEN" | docker login ghcr.io -u "$GHCR_USER" --password-stdin

echo "[deploy] Pulling image: $APP_IMAGE"
export APP_IMAGE
docker compose -f "$COMPOSE_FILE" pull app

echo "[deploy] Starting services..."
docker compose -f "$COMPOSE_FILE" up -d --remove-orphans

echo "[deploy] Waiting for health check..."
for i in $(seq 1 30); do
  if docker compose -f "$COMPOSE_FILE" ps app | grep -q "(healthy)"; then
    echo "[deploy] Application is healthy."
    docker compose -f "$COMPOSE_FILE" ps
    docker image prune -f
    exit 0
  fi
  sleep 2
done

echo "[deploy] WARNING: health check did not pass in time. Current status:"
docker compose -f "$COMPOSE_FILE" ps
docker compose -f "$COMPOSE_FILE" logs --tail=50 app
exit 1
