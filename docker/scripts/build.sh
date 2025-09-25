#!/bin/bash

# Docker Build Script for ERP System
# Usage: ./build.sh [environment] [version]
# Example: ./build.sh production v1.0.0

set -e  # Exit on error

# Default values
ENVIRONMENT=${1:-development}
VERSION=${2:-latest}
IMAGE_NAME="erp-system"
REGISTRY_URL=${DOCKER_REGISTRY:-""}

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logging functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

# Check if Docker is running
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        log_error "Docker is not running or not accessible"
        exit 1
    fi
    log_info "Docker is running"
}

# Clean up old images
cleanup_images() {
    log_info "Cleaning up old images..."
    
    # Remove dangling images
    docker image prune -f
    
    # Remove old versions of the same image (keep last 5)
    if [[ $(docker images ${IMAGE_NAME} --format "table {{.Tag}}" | wc -l) -gt 6 ]]; then
        docker images ${IMAGE_NAME} --format "table {{.ID}}\t{{.Tag}}" | \
        tail -n +7 | \
        awk '{print $1}' | \
        xargs -r docker rmi
    fi
    
    log_success "Cleanup completed"
}

# Build the Docker image
build_image() {
    local dockerfile="Dockerfile"
    local build_context="../"
    
    if [[ "$ENVIRONMENT" == "production" ]]; then
        dockerfile="Dockerfile.prod"
    fi
    
    log_info "Building ${ENVIRONMENT} image: ${IMAGE_NAME}:${VERSION}"
    log_info "Using Dockerfile: ${dockerfile}"
    
    # Build arguments
    local build_args=""
    build_args="--build-arg ENVIRONMENT=${ENVIRONMENT}"
    build_args="${build_args} --build-arg VERSION=${VERSION}"
    build_args="${build_args} --build-arg BUILD_DATE=$(date -u +'%Y-%m-%dT%H:%M:%SZ')"
    build_args="${build_args} --build-arg VCS_REF=$(git rev-parse --short HEAD 2>/dev/null || echo 'unknown')"
    
    # Build the image
    if docker build ${build_args} \
        -t "${IMAGE_NAME}:${VERSION}" \
        -t "${IMAGE_NAME}:latest" \
        -f "${dockerfile}" \
        "${build_context}"; then
        log_success "Image built successfully: ${IMAGE_NAME}:${VERSION}"
    else
        log_error "Failed to build image"
        exit 1
    fi
}

# Tag image with registry URL
tag_for_registry() {
    if [[ -n "$REGISTRY_URL" ]]; then
        log_info "Tagging image for registry: ${REGISTRY_URL}"
        
        docker tag "${IMAGE_NAME}:${VERSION}" "${REGISTRY_URL}/${IMAGE_NAME}:${VERSION}"
        docker tag "${IMAGE_NAME}:${VERSION}" "${REGISTRY_URL}/${IMAGE_NAME}:latest"
        
        log_success "Image tagged for registry"
    fi
}

# Push to registry
push_to_registry() {
    if [[ -n "$REGISTRY_URL" ]]; then
        log_info "Pushing image to registry..."
        
        if docker push "${REGISTRY_URL}/${IMAGE_NAME}:${VERSION}" && \
           docker push "${REGISTRY_URL}/${IMAGE_NAME}:latest"; then
            log_success "Image pushed to registry successfully"
        else
            log_error "Failed to push image to registry"
            exit 1
        fi
    else
        log_warn "No registry URL specified. Skipping push."
    fi
}

# Test the built image
test_image() {
    log_info "Testing the built image..."
    
    # Basic container start test
    local container_id
    if container_id=$(docker run -d --rm \
        -p 8080:8080 \
        --name "${IMAGE_NAME}-test" \
        "${IMAGE_NAME}:${VERSION}"); then
        
        log_info "Container started with ID: ${container_id}"
        
        # Wait for application to start
        sleep 30
        
        # Test health endpoint
        if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
            log_success "Health check passed"
        else
            log_warn "Health check failed or endpoint not available"
        fi
        
        # Stop test container
        docker stop "${container_id}" > /dev/null
        log_info "Test container stopped"
    else
        log_error "Failed to start test container"
        exit 1
    fi
}

# Display image information
show_image_info() {
    log_info "Image Information:"
    echo "  Name: ${IMAGE_NAME}"
    echo "  Version: ${VERSION}"
    echo "  Environment: ${ENVIRONMENT}"
    echo "  Size: $(docker images --format 'table {{.Size}}' ${IMAGE_NAME}:${VERSION} | tail -n +2)"
    
    if [[ -n "$REGISTRY_URL" ]]; then
        echo "  Registry: ${REGISTRY_URL}"
    fi
}

# Main execution
main() {
    log_info "Starting Docker build process..."
    log_info "Environment: ${ENVIRONMENT}"
    log_info "Version: ${VERSION}"
    
    # Change to script directory
    cd "$(dirname "$0")"
    
    check_docker
    cleanup_images
    build_image
    tag_for_registry
    
    if [[ "$ENVIRONMENT" == "production" ]]; then
        test_image
        push_to_registry
    fi
    
    show_image_info
    
    log_success "Build process completed successfully!"
}

# Help function
show_help() {
    echo "Usage: $0 [environment] [version]"
    echo ""
    echo "Arguments:"
    echo "  environment    Build environment (development|production) [default: development]"
    echo "  version        Image version tag [default: latest]"
    echo ""
    echo "Environment Variables:"
    echo "  DOCKER_REGISTRY    Docker registry URL for pushing images"
    echo ""
    echo "Examples:"
    echo "  $0                           # Build development image with latest tag"
    echo "  $0 production v1.0.0         # Build production image with v1.0.0 tag"
    echo "  DOCKER_REGISTRY=registry.example.com $0 production v1.0.0"
}

# Parse command line arguments
if [[ "$1" == "-h" ]] || [[ "$1" == "--help" ]]; then
    show_help
    exit 0
fi

# Run main function
main "$@"
