#!/bin/bash
source ./service/source.sh

# Deploy Source File for variables' data exchnage
DEPLOY_SOURCE_FILE_NAME=deploy_source_$(date +%Y%m%d%H%M%S).sh
echo "#!/bin/bash" > "${DEPLOY_SOURCE_FILE_NAME}"
chmod +x "${DEPLOY_SOURCE_FILE_NAME}"
export DEPLOY_SOURCE_FILE_NAME

./service/script_information.sh
./service/input_parameters_manual.sh

# Upload recently evaluated variables
source ./"${DEPLOY_SOURCE_FILE_NAME}"

./service/log_start.sh

# Upload recently evaluated variables
source ./"${DEPLOY_SOURCE_FILE_NAME}"
# Delete file with variables
rm ./"${DEPLOY_SOURCE_FILE_NAME}"

./service/deploy_information.sh 2>&1 | tee -a "${DEPLOY_LOG_FILE_NAME}"
./service/install_database_database.sh 2>&1 | tee -a "${DEPLOY_LOG_FILE_NAME}"
./service/log_finish.sh 2>&1 | tee -a "${DEPLOY_LOG_FILE_NAME}"
exit 0
