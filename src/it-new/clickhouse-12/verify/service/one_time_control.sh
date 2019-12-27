#!/bin/bash
source ./service/source.sh

DEPLOY_EXECUTE_BEFORE=$(./service/run_query.sh "select case when executed > 0 then './service/one_time_do_nothing.sh' when executed = 0 then './service/one_time_do_before.sh' end execute_before from (select count(1) executed, coalesce(sum(case when s.script_checksum = '${DEPLOY_SCRIPT_CHECKSUM}' then 1 else 0 end), 0) checksum from deploy_scripts s where s.script_directory = '${DEPLOY_SCRIPT_DIRECTORY}' and s.script_name = '${DEPLOY_SCRIPT_NAME}' and s.deploy_status = 'COMPLETED')")
DEPLOY_EXECUTE_SCRIPT=$(./service/run_query.sh "select case when executed > 0 and checksum > 0 then './service/one_time_do_success.sh' when executed > 0 and checksum = 0 then './service/one_time_do_checksum.sh' when executed = 0 and checksum = 0 then '${DEPLOY_SCRIPT_NAME_FULL}' end execute_script from (select count(1) executed, coalesce(sum(case when s.script_checksum = '${DEPLOY_SCRIPT_CHECKSUM}' then 1 else 0 end), 0) checksum from deploy_scripts s where s.script_directory = '${DEPLOY_SCRIPT_DIRECTORY}' and s.script_name = '${DEPLOY_SCRIPT_NAME}' and s.deploy_status = 'COMPLETED')")
DEPLOY_EXECUTE_AFTER=$(./service/run_query.sh "select case when executed > 0 then './service/one_time_do_nothing.sh' when executed = 0 then './service/one_time_do_after.sh' end execute_after from (select count(1) executed, coalesce(sum(case when s.script_checksum = '${DEPLOY_SCRIPT_CHECKSUM}' then 1 else 0 end), 0) checksum from deploy_scripts s where s.script_directory = '${DEPLOY_SCRIPT_DIRECTORY}' and s.script_name = '${DEPLOY_SCRIPT_NAME}' and s.deploy_status = 'COMPLETED')")
"${DEPLOY_EXECUTE_BEFORE}"
if [ "${DEPLOY_SCRIPT_NAME_FULL}" == "${DEPLOY_EXECUTE_SCRIPT}" ]
then
  ./service/run_file.sh "${DEPLOY_EXECUTE_SCRIPT}"
else
  "${DEPLOY_EXECUTE_SCRIPT}"
fi
"${DEPLOY_EXECUTE_AFTER}"
