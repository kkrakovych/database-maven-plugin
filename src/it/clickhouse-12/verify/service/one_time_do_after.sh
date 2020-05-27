#!/bin/bash
source ./service/source.sh

./service/run_query.sh "insert into deploy_scripts (sys_timestamp, build_version, build_timestamp, script_directory, script_name, script_checksum, script_start_timestamp, script_finish_timestamp, deploy_status) select now(), build_version, build_timestamp, script_directory, script_name, script_checksum, script_start_timestamp, now(), 'COMPLETED' from deploy_scripts final where build_version = 'test' and build_timestamp = toDateTime('2019-12-28 18:36:23') and script_name = '${DEPLOY_SCRIPT_NAME}' and script_directory = '${DEPLOY_SCRIPT_DIRECTORY}'"
echo "[SUCCESS] - Script ${DEPLOY_SCRIPT_NAME} was applied."
