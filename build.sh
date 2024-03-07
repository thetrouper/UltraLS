#!/bin/bash

# Run Gradle build
./gradlew build

# Check if the build was successful
if [ $? -eq 0 ]; then
    echo "Gradle build successful."

    # SFTP upload
    SFTP_HOST="192.168.1.199"
    SFTP_USER="trouper"
    SFTP_PASSWORD="Trouper12()1"
    SFTP_REMOTE_DIR="/home/trouper/docker/data/plugins/"

    # Create a temporary file with a unique name
    TEMP_FILE=$(mktemp)

    # Specify the local file to upload
    LOCAL_FILE="/run/media/trouper/'1TB drive'/IJ/IdeaProjects/UltraLS/build/libs/UltraLS-0.2.5.jar"

    # Write the SFTP commands to the temporary file
    echo "put $LOCAL_FILE $SFTP_REMOTE_DIR" > "$TEMP_FILE"
    echo "bye" >> "$TEMP_FILE"

    # Use sftp non-interactively with the specified commands
    sftp -oStrictHostKeyChecking=no -oBatchMode=no -b "$TEMP_FILE" "$SFTP_USER@$SFTP_HOST" <<EOF
    $SFTP_PASSWORD
EOF

    # Remove the temporary file
    rm -f "$TEMP_FILE"

    # SSH command to reload the plugin on the host
    SSH_COMMANDS=(
        "pm reload UltraLS"
        "execute at @a run playsound minecraft:entity.experience_orb.pickup master @a \~ \~ \~ 100 1 1"
        "tellraw @a '\"'[Server] Reload Complete, Upload Successful.'\"'"
    )

    for cmd in "${SSH_COMMANDS[@]}"; do
      ssh -oStrictHostKeyChecking=no -oBatchMode=no "$SFTP_USER@$SFTP_HOST" "docker exec docker_mc_1 mc-send-to-console $cmd"
    done

    echo "Plugin reloaded."
else
    echo "Gradle build failed."
fi
