#!/bin/bash
# Recursively make call shell scripts executable
# find . -type f -iname "*.sh" -exec chmod +x {} \;

source ./service/source.sh

# Deploy Source File for variables' data exchnage
DEPLOY_SOURCE_FILE_NAME=deploy_source_$(date +%Y%m%d%H%M%S).sh
echo "#!/bin/bash" > "${DEPLOY_SOURCE_FILE_NAME}"
chmod +x "${DEPLOY_SOURCE_FILE_NAME}"
export DEPLOY_SOURCE_FILE_NAME

./service/script_information.sh
./service/log_start.sh
source ./"${DEPLOY_SOURCE_FILE_NAME}"
./service/deploy_information.sh | tee -a "${DEPLOY_LOG_FILE_NAME}"
./service/install_database_database.sh | tee -a "${DEPLOY_LOG_FILE_NAME}"
./service/log_finish.sh | tee -a "${DEPLOY_LOG_FILE_NAME}"
exit 0
