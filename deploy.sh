#!/bin/bash

# Deployment script for Inventory App
# This script deploys the JAR file to the remote server

# Configuration
LOCAL_JAR="/Users/raneeshak/Documents/Projects/tcs/tech4hope/viv_med/inventory_app/inventory_app_v1/target/inventory-app-0.0.1-SNAPSHOT.jar"
REMOTE_USER="it-admin"
REMOTE_HOST="27.34.245.92"
REMOTE_PATH="/home/it-admin/be-service/code/inventory_app_v1/target"
JAR_NAME="inventory-app-0.0.1-SNAPSHOT.jar"
LOG_FILE="application.log"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Inventory App Deployment Script${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# Check if local JAR exists
if [ ! -f "$LOCAL_JAR" ]; then
    echo -e "${RED}Error: Local JAR file not found at $LOCAL_JAR${NC}"
    echo -e "${YELLOW}Please build the project first using: ./mvnw clean package${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Local JAR file found${NC}"
echo ""

# Get the timestamp for backup
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

echo -e "${YELLOW}Connecting to remote server...${NC}"
echo ""

# SSH command to backup, stop, and prepare for deployment
ssh ${REMOTE_USER}@${REMOTE_HOST} << 'ENDSSH'
    set -e

    echo "Connected to remote server"
    echo ""

    # Navigate to target directory
    cd /home/it-admin/be-service/code/inventory_app_v1/target

    # Check if the JAR exists and backup
    if [ -f "inventory-app-0.0.1-SNAPSHOT.jar" ]; then
        echo "Backing up existing JAR file..."
        mv inventory-app-0.0.1-SNAPSHOT.jar inventory-app-0.0.1-SNAPSHOT_old.jar
        echo "✓ Existing JAR backed up as inventory-app-0.0.1-SNAPSHOT_old.jar"
    else
        echo "No existing JAR file found (first deployment)"
    fi
    echo ""

    # Find and stop the running application
    echo "Stopping existing application..."
    PID=$(ps aux | grep "inventory-app-0.0.1-SNAPSHOT.jar" | grep -v grep | awk '{print $2}')

    if [ -n "$PID" ]; then
        echo "Found running process with PID: $PID"
        kill $PID
        sleep 3

        # Check if process still exists
        if ps -p $PID > /dev/null; then
            echo "Process still running, forcing kill..."
            kill -9 $PID
            sleep 2
        fi
        echo "✓ Application stopped successfully"
    else
        echo "No running application found"
    fi
    echo ""
ENDSSH

if [ $? -ne 0 ]; then
    echo -e "${RED}Error: Failed to prepare remote server${NC}"
    exit 1
fi

# Copy the new JAR file to remote server
echo -e "${YELLOW}Copying new JAR file to remote server...${NC}"
scp "$LOCAL_JAR" ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}/

if [ $? -ne 0 ]; then
    echo -e "${RED}Error: Failed to copy JAR file${NC}"
    exit 1
fi

echo -e "${GREEN}✓ JAR file copied successfully${NC}"
echo ""

# Start the application on remote server
echo -e "${YELLOW}Starting application on remote server...${NC}"
ssh ${REMOTE_USER}@${REMOTE_HOST} << 'ENDSSH'
    cd /home/it-admin/be-service/code/inventory_app_v1/target

    # Start the application in background
    nohup java -jar inventory-app-0.0.1-SNAPSHOT.jar > application.log 2>&1 &

    echo "Application started in background"
    echo "PID: $!"

    # Wait a moment and check if application is running
    sleep 3

    PID=$(ps aux | grep "inventory-app-0.0.1-SNAPSHOT.jar" | grep -v grep | awk '{print $2}')
    if [ -n "$PID" ]; then
        echo "✓ Application is running with PID: $PID"
    else
        echo "⚠ Warning: Could not verify if application started successfully"
        echo "Please check the application.log file"
    fi
ENDSSH

if [ $? -ne 0 ]; then
    echo -e "${RED}Error: Failed to start application${NC}"
    exit 1
fi

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Deployment completed successfully!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${YELLOW}Useful commands:${NC}"
echo -e "View logs: ssh ${REMOTE_USER}@${REMOTE_HOST} 'tail -f ${REMOTE_PATH}/application.log'"
echo -e "Check status: ssh ${REMOTE_USER}@${REMOTE_HOST} 'ps aux | grep inventory-app'"
echo ""

