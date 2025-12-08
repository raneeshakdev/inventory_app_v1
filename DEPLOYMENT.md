# Deployment Script

This script automates the deployment of the Inventory App to the remote server.

## Prerequisites

1. **SSH Key Setup (Recommended)**: To avoid entering password repeatedly, set up SSH key-based authentication:
   ```bash
   ssh-copy-id it-admin@27.34.245.92
   ```
   If you don't have an SSH key, generate one first:
   ```bash
   ssh-keygen -t rsa -b 4096
   ```

2. **Build the Application**: Ensure the JAR file is built before deployment:
   ```bash
   ./mvnw clean package
   ```

## Usage

### Run the Deployment Script

```bash
./deploy.sh
```

If prompted for a password, enter: `Admins@123`

### What the Script Does

1. **Validation**: Checks if the local JAR file exists
2. **Backup**: Renames the existing JAR on the server to `*_old.jar`
3. **Stop**: Stops the currently running application
4. **Copy**: Transfers the new JAR file to the server
5. **Start**: Starts the application in the background with logging

## Manual Deployment (Alternative)

If you prefer to deploy manually, follow these steps:

### Step 1: Build the Application
```bash
cd /Users/raneeshak/Documents/Projects/tcs/tech4hope/viv_med/inventory_app/inventory_app_v1
./mvnw clean package
```

### Step 2: Connect to Server
```bash
ssh it-admin@27.34.245.92
# Password: Admins@123
```

### Step 3: Backup and Stop Application (on server)
```bash
cd /home/it-admin/be-service/code/inventory_app_v1/target

# Backup existing JAR
mv inventory-app-0.0.1-SNAPSHOT.jar inventory-app-0.0.1-SNAPSHOT_old.jar

# Stop the running application
ps aux | grep inventory-app
kill <PID>
```

### Step 4: Copy New JAR (from local machine)
```bash
scp target/inventory-app-0.0.1-SNAPSHOT.jar it-admin@27.34.245.92:/home/it-admin/be-service/code/inventory_app_v1/target/
```

### Step 5: Start Application (on server)
```bash
ssh it-admin@27.34.245.92
cd /home/it-admin/be-service/code/inventory_app_v1/target
nohup java -jar inventory-app-0.0.1-SNAPSHOT.jar > application.log 2>&1 &
```

## Monitoring

### View Application Logs
```bash
ssh it-admin@27.34.245.92 'tail -f /home/it-admin/be-service/code/inventory_app_v1/target/application.log'
```

### Check Application Status
```bash
ssh it-admin@27.34.245.92 'ps aux | grep inventory-app'
```

### Stop Application
```bash
ssh it-admin@27.34.245.92
ps aux | grep inventory-app
kill <PID>
```

## Troubleshooting

### Issue: Permission Denied
**Solution**: Make sure the script is executable:
```bash
chmod +x deploy.sh
```

### Issue: Connection Refused
**Solution**: Verify the server is reachable:
```bash
ping 27.34.245.92
ssh it-admin@27.34.245.92
```

### Issue: Application Won't Start
**Solution**: Check the application logs:
```bash
ssh it-admin@27.34.245.92 'cat /home/it-admin/be-service/code/inventory_app_v1/target/application.log'
```

### Issue: Port Already in Use
**Solution**: Find and kill the process using the port:
```bash
ssh it-admin@27.34.245.92
lsof -i :<PORT_NUMBER>
kill -9 <PID>
```

## Server Details

- **Host**: 27.34.245.92
- **Username**: it-admin
- **Password**: Admins@123
- **Application Path**: /home/it-admin/be-service/code/inventory_app_v1/target
- **JAR Name**: inventory-app-0.0.1-SNAPSHOT.jar
- **Log File**: application.log

## Notes

- The script automatically backs up the previous JAR with `_old` suffix
- The application runs in the background using `nohup`
- All output is logged to `application.log`
- The script includes color-coded output for better visibility

