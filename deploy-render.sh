#!/bin/bash

# Render CLI Deployment Script for Sandwich Delivery Monorepo
# Prerequisites: Install Render CLI and authenticate with 'render auth login'

set -e

echo "🚀 Starting Render deployment..."

# Check if Render CLI is installed
if ! command -v render &> /dev/null; then
    echo "❌ Render CLI not found. Please install it first:"
    echo "brew install render"
    exit 1
fi

# Check if authenticated
if ! render auth whoami &> /dev/null; then
    echo "❌ Not authenticated with Render. Please run 'render auth login' first."
    exit 1
fi

# Set variables
REPO_URL="https://github.com/ltruchot/sandwich-delivery-monorepo"
SERVICE_NAME_PREFIX="sandwich-delivery"

# Function to create or get service
create_or_get_service() {
    local service_name="$1"
    local service_type="$2"
    shift 2
    
    if render services get "$service_name" &>/dev/null; then
        echo "✅ Service $service_name already exists"
    else
        echo "📦 Creating $service_type service: $service_name"
        render services create "$service_type" --name="$service_name" "$@"
    fi
}

# Create or get PostgreSQL database
create_or_get_service "${SERVICE_NAME_PREFIX}-postgres" "database" \
    --plan=free \
    --database-name=sandwich_delivery \
    --database-user=sandwich_user

echo "⏳ Waiting for database to be ready..."
sleep 10

# Get database connection string
DB_CONNECTION_STRING=$(render services get "${SERVICE_NAME_PREFIX}-postgres" --format=json | jq -r '.connectionString')

# Create or get Spring Boot API service
create_or_get_service "${SERVICE_NAME_PREFIX}-api" "web" \
    --repo="${REPO_URL}" \
    --root-dir="apps/spring-boot-api" \
    --runtime=docker \
    --dockerfile-path="./Dockerfile" \
    --plan=free \
    --env="SPRING_PROFILES_ACTIVE=production" \
    --env="DATABASE_URL=${DB_CONNECTION_STRING}" \
    --env="JWT_SECRET=$(openssl rand -base64 32)" \
    --env="CORS_ALLOWED_ORIGINS=https://${SERVICE_NAME_PREFIX}-frontend.onrender.com"

# Create or get Astro frontend service
create_or_get_service "${SERVICE_NAME_PREFIX}-frontend" "static" \
    --repo="${REPO_URL}" \
    --root-dir="apps/astro-frontend" \
    --build-command="pnpm build" \
    --publish-dir="./dist" \
    --plan=free \
    --env="NODE_ENV=production" \
    --env="VITE_API_URL=https://${SERVICE_NAME_PREFIX}-api.onrender.com"

echo "🚀 Triggering deployments..."
render deploys create "${SERVICE_NAME_PREFIX}-api" --confirm
render deploys create "${SERVICE_NAME_PREFIX}-frontend" --confirm

echo "✅ Deployment configuration complete!"
echo ""
echo "🔗 Your services will be available at:"
echo "  - Frontend: https://${SERVICE_NAME_PREFIX}-frontend.onrender.com"
echo "  - Backend API: https://${SERVICE_NAME_PREFIX}-api.onrender.com"
echo "  - Database: ${SERVICE_NAME_PREFIX}-postgres"
echo ""
echo "📝 Next steps:"
echo "1. Update the REPO_URL in this script with your actual GitHub repository"
echo "2. Push your code to GitHub"
echo "3. Services will automatically deploy when you push to main branch"
echo ""
echo "🔍 Monitor deployments with: render services list"