#!/bin/bash
<#--
 #-- Copyright 2019 Kostyantyn Krakovych
 #--
 #-- Licensed under the Apache License, Version 2.0 (the "License");
 #-- you may not use this file except in compliance with the License.
 #-- You may obtain a copy of the License at
 #--
 #--     http://www.apache.org/licenses/LICENSE-2.0
 #--
 #-- Unless required by applicable law or agreed to in writing, software
 #-- distributed under the License is distributed on an "AS IS" BASIS,
 #-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 #-- See the License for the specific language governing permissions and
 #-- limitations under the License.
 #-->
# Recursively make call shell scripts executable
# find . -type f -iname "*.sh" -exec chmod +x {} \;

# Deploy Source File for variables' data exchnage
DEPLOY_SOURCE_FILE_NAME=deploy_source_$(date +%Y%m%d%H%M%S).sh
echo "#!/bin/bash" > ${r"${DEPLOY_SOURCE_FILE_NAME}"}
chmod +x ${r"${DEPLOY_SOURCE_FILE_NAME}"}
export DEPLOY_SOURCE_FILE_NAME

./${serviceDirectory}/script_information.sh
#./${serviceDirectory}/clickhouse_client_setup.sh
./${serviceDirectory}/log_start.sh
source ${r"${DEPLOY_SOURCE_FILE_NAME}"}
./${serviceDirectory}/deploy_information.sh                | tee -a ${r"${DEPLOY_LOG_FILE_NAME}"}
./${serviceDirectory}/install_database_${database.name}.sh | tee -a ${r"${DEPLOY_LOG_FILE_NAME}"}
./${serviceDirectory}/log_finish.sh                        | tee -a ${r"${DEPLOY_LOG_FILE_NAME}"}
exit 0
