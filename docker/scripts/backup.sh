#!/bin/bash

# Database Backup Script for ERP System
# Usage: ./backup.sh [database_name]

set -e

# Default values
DB_NAME=${1:-"erp_system"}
BACKUP_DIR="../backups"
DATE=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="${BACKUP_DIR}/${DB_NAME}_backup_${DATE}.sql"

# Colors
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

# Create backup directory
mkdir -p "$BACKUP_DIR"

log_info "Starting database backup for: $DB_NAME"
log_info "Backup file: $BACKUP_FILE"

# Perform backup
if docker-compose exec -T mysql mysqldump -u root -p"$MYSQL_ROOT_PASSWORD" "$DB_NAME" > "$BACKUP_FILE"; then
    log_success "Database backup completed successfully"
    log_info "Backup file size: $(du -h "$BACKUP_FILE" | cut -f1)"
else
    log_error "Database backup failed"
    exit 1
fi

# Cleanup old backups (keep last 7 days)
find "$BACKUP_DIR" -name "${DB_NAME}_backup_*.sql" -mtime +7 -delete

log_success "Backup process completed"
