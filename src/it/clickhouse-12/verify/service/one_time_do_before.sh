#!/bin/bash
source ./service/source.sh

./service/run_query.sh "insert into deploy_scripts (sys_timestamp, build_version, build_timestamp, script_directory, script_name, script_checksum, script_start_timestamp, script_finish_timestamp, deploy_status) values (now(), 'test', toDateTime('2019-12-28 18:36:23'), '${DEPLOY_SCRIPT_DIRECTORY}', '${DEPLOY_SCRIPT_NAME}', '${DEPLOY_SCRIPT_CHECKSUM}', now(), null, 'NOT COMPLETED')"
