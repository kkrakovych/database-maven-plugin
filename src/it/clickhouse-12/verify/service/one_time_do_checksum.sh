#!/bin/bash
source ./service/source.sh

echo "[FAILURE] - Script ${DEPLOY_SCRIPT_NAME} was already applied with different checksum."
echo "One time script checksum mismatch."
exit 1
