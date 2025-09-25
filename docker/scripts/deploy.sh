#!/bin/bash

# Docker Deployment Script for ERP System
# Usage: ./deploy.sh [environment] [version]
# Example: ./deploy.sh production v1.0.0

set -e  # Exit on error

# Default values
ENVIRONMENT=${1:-development}
VERSION=${2:-latest}
COMPOSE_FILE="docker-compose.yml"
IMAGE_NAME="erp-system"
SERVICE_NAME="erp-app"
MAX_DEPLOY_TIME=300  # 5 minutes

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Logging functions
log_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }
log_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }

# Set compose file based on environment
if [[ "$ENVIRONMENT" == "production" ]]; then
    COMPOSE_FILE="docker-compose.prod.yml"
fi

log_info "Starting deployment for ${ENVIRONMENT} environment"
log_info "Using compose file: ${COMPOSE_FILE}"
log_info "Image version: ${VERSION}"

# Check prerequisites
check_prerequisites() {
    if ! command -v docker &> /dev/null; then
        log_error "Docker is not installed"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose is not installed"
        exit 1
    fi
    
    if [[ ! -f "../${COMPOSE_FILE}" ]]; then
        log_error "Compose file not found: ${COMPOSE_FILE}"
        exit 1
    fi
}

# Create backup of current deployment
create_backup() {
    log_info "Creating backup before deployment..."
    
    # Run backup script if it exists
    if [[ -f "./backup.sh" ]]; then
        ./backup.sh
    else
        log_warn "Backup script not found. Skipping backup."
    fi
}

# Pull latest images
pull_images() {
    log_info "Pulling latest images..."
    cd ..
    docker-compose -f "${COMPOSE_FILE}" pull
    cd scripts/
}

# Deploy services
deploy_services() {
    log_info "Deploying services..."
    
    cd ..
    
    # Update services with zero-downtime deployment
    if [[ "$ENVIRONMENT" == "production" ]]; then
        log_info "Performing zero-downtime deployment..."
        
        # Scale up new containers
        docker-compose -f "${COMPOSE_FILE}" up -d --scale "${SERVICE_NAME}"=2 --no-recreate
        
        # Wait for new containers to be healthy
        sleep 30
        
        # Remove old containers
        docker-compose -f "${COMPOSE_FILE}" up -d --remove-orphans
    else
        # Standard deployment for development
        docker-compose -f "${COMPOSE_FILE}" up -d
    fi
    
    cd scripts/
}

# Wait for services to be healthy
wait_for_health() {
    log_info "Waiting for services to be healthy..."
    
    local start_time=$(date +%s)
    local timeout=$MAX_DEPLOY_TIME
    
    while true; do
        local current_time=$(date +%s)
        local elapsed=$((current_time - start_time))
        
        if [[ $elapsed -ge $timeout ]]; then
            log_error "Deployment timeout after ${timeout} seconds"
            return 1
        fi
        
        # Check if application is responding
        if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
            log_success "Application is healthy"
            return 0
        fi
        
        log_info "Waiting for application to be ready... (${elapsed}s)"
        sleep 10
    done
}

# Cleanup old resources
cleanup() {
    log_info "Cleaning up old resources..."
    
    # Remove unused images
    docker image prune -f
    
    # Remove unused volumes (be careful in production)
    if [[ "$ENVIRONMENT" != "production" ]]; then
        docker volume prune -f
    fi
    
    # Remove unused networks
    docker network prune -f
}

# Verify deployment
verify_deployment() {
    log_info "Verifying deployment..."
    
    cd ..
    
    # Check service status
    if ! docker-compose -f "${COMPOSE_FILE}" ps | grep -q "Up"; then
        log_error "Some services are not running"
        return 1
    fi
    
    # Test endpoints
    local endpoints=("health" "info")
    for endpoint in "${endpoints[@]}"; do
        if curl -f "http://localhost:8080/actuator/${endpoint}" > /dev/null 2>&1; then
            log_success "Endpoint /${endpoint} is responding"
        else
            log_warn "Endpoint /${endpoint} is not responding"
        fi
    done
    
    cd scripts/
}

# Rollback deployment
rollback() {
    log_warn "Rolling back deployment..."
    
    cd ..
    
    # Stop current services
    docker-compose -f "${COMPOSE_FILE}" down
    
    # Restore from backup (if backup exists)
    # This would need to be implemented based on your backup strategy
    
    log_error "Rollback completed. Please check logs and fix issues."
    cd scripts/
    exit 1
}

# Main deployment function
main() {
    local start_time=$(date)
    
    log_info "=== ERP System Deployment Started ==="
    log_info "Start time: ${start_time}"
    
    # Change to script directory
    cd "$(dirname "$0")"
    
    # Execute deployment steps
    check_prerequisites || exit 1
    
    if [[ "$ENVIRONMENT" == "production" ]]; then
        create_backup
    fi
    
    pull_images || { log_error "Failed to pull images"; exit 1; }
    deploy_services || { log_error "Failed to deploy services"; rollback; }
    
    if wait_for_health; then
        verify_deployment || { log_warn "Verification failed but deployment seems successful"; }
        cleanup
        
        local end_time=$(date)
        log_success "=== Deployment Completed Successfully ==="
        log_info "Start time: ${start_time}"
        log_info "End time: ${end_time}"
        log_info "Environment: ${ENVIRONMENT}"
        log_info "Version: ${VERSION}"
    else
        log_error "Deployment failed - services are not healthy"
        rollback
    fi
}

# Help function
show_help() {
    echo "Usage: $0 [environment] [version]"
    echo ""
    echo "Arguments:"
    echo "  environment    Deployment environment (development|production) [default: development]"
    echo "  version        Image version to deploy [default: latest]"
    echo ""
    echo "Examples:"
    echo "  $0                      # Deploy development with latest"
    echo "  $0 production v1.0.0    # Deploy production with specific version"
}

# Parse command line arguments
if [[ "$1" == "-h" ]] || [[ "$1" == "--help" ]]; then
    show_help
    exit 0
fi

# Run main function
main "$@"
