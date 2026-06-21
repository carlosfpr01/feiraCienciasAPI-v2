#!/bin/bash
# Deployment helper script

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Functions
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    log_info "Checking prerequisites..."

    if ! command -v docker &> /dev/null; then
        log_error "Docker is not installed"
        exit 1
    fi

    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose is not installed"
        exit 1
    fi

    if ! command -v mvn &> /dev/null; then
        log_error "Maven is not installed"
        exit 1
    fi

    log_info "All prerequisites are met"
}

# Build locally
build_local() {
    log_info "Building application locally..."
    mvn clean package -DskipTests
    log_info "Build completed successfully"
}

# Start services with Docker Compose
start_services() {
    log_info "Starting services with Docker Compose..."
    docker-compose -f docker-compose.ci.yml up -d
    log_info "Services started"
}

# Stop services
stop_services() {
    log_info "Stopping services..."
    docker-compose -f docker-compose.ci.yml down
    log_info "Services stopped"
}

# Start production stack
start_prod() {
    if [ ! -f .env ]; then
        log_error ".env not found. Copy .env.production.example to .env and configure it."
        exit 1
    fi
    log_info "Starting production stack..."
    docker compose -f docker-compose.prod.yml --env-file .env up -d
    log_info "Production stack started on port ${APP_PORT:-8080}"
}

# Stop production stack
stop_prod() {
    log_info "Stopping production stack..."
    docker compose -f docker-compose.prod.yml down
    log_info "Production stack stopped"
}

# Run tests
run_tests() {
    log_info "Running tests..."
    mvn clean test
    log_info "Tests completed"
}

# Build Docker image
build_docker() {
    log_info "Building Docker image..."
    docker build -t feira-ciencias-api:latest .
    log_info "Docker image built successfully"
}

# Push Docker image
push_docker() {
    local registry=$1
    local tag=${2:-latest}

    if [ -z "$registry" ]; then
        log_error "Registry URL is required"
        exit 1
    fi

    log_info "Pushing Docker image to $registry..."
    docker tag feira-ciencias-api:latest $registry/feira-ciencias-api:$tag
    docker push $registry/feira-ciencias-api:$tag
    log_info "Docker image pushed successfully"
}

# Show help
show_help() {
    cat << EOF
Deployment Helper Script

Usage: ./deploy.sh [COMMAND]

Commands:
    check       Check prerequisites (docker, docker-compose, maven)
    build       Build application with Maven
    test        Run tests
    start       Start services with Docker Compose (CI stack)
    stop        Stop services
    docker      Build Docker image
    push        Push Docker image to registry (requires registry URL)
    prod-start  Start production stack (docker-compose.prod.yml)
    prod-stop   Stop production stack
    full        Full cycle: build -> docker -> start
    help        Show this help message

Production deploy on server:
    1. cp .env.production.example .env && edit credentials
    2. export APP_IMAGE=ghcr.io/OWNER/REPO:TAG
    3. ./deploy.sh prod-start

GitHub Actions deploy:
    - Set variable DEPLOY_ENABLED=true
    - Configure secrets DEPLOY_HOST, DEPLOY_USER, DEPLOY_SSH_KEY
    - Push tag v1.0.0 or run workflow manually

Examples:
    ./deploy.sh check
    ./deploy.sh build
    ./deploy.sh docker
    ./deploy.sh push ghcr.io/myorg v1.0.0
    ./deploy.sh prod-start
    ./deploy.sh full

EOF
}

# Main
main() {
    local command=${1:-help}

    case $command in
        check)
            check_prerequisites
            ;;
        build)
            check_prerequisites
            build_local
            ;;
        test)
            run_tests
            ;;
        start)
            check_prerequisites
            start_services
            ;;
        stop)
            stop_services
            ;;
        prod-start)
            check_prerequisites
            start_prod
            ;;
        prod-stop)
            stop_prod
            ;;
        docker)
            build_docker
            ;;
        push)
            push_docker "$2" "$3"
            ;;
        full)
            check_prerequisites
            build_local
            build_docker
            start_services
            ;;
        help)
            show_help
            ;;
        *)
            log_error "Unknown command: $command"
            show_help
            exit 1
            ;;
    esac
}

main "$@"
