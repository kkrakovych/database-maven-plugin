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
source ./${serviceDirectory}/source.sh

DEPLOY_EXECUTE_BEFORE=$(./${serviceDirectory}/run_query.sh "select case when executed > 0 then './${serviceDirectory}/one_time_do_nothing.sh' when executed = 0 then './${serviceDirectory}/one_time_do_before.sh' end execute_before from (select count(1) executed, coalesce(sum(case when s.script_checksum = '${r"${DEPLOY_SCRIPT_CHECKSUM}"}' then 1 else 0 end), 0) checksum from deploy_scripts s where s.script_directory = '${r"${DEPLOY_SCRIPT_DIRECTORY}"}' and s.script_name = '${r"${DEPLOY_SCRIPT_NAME}"}' and s.deploy_status = 'COMPLETED')")
DEPLOY_EXECUTE_SCRIPT=$(./${serviceDirectory}/run_query.sh "select case when executed > 0 and checksum > 0 then './${serviceDirectory}/one_time_do_success.sh' when executed > 0 and checksum = 0 then './${serviceDirectory}/one_time_do_checksum.sh' when executed = 0 and checksum = 0 then '${r"${DEPLOY_SCRIPT_NAME_FULL}"}' end execute_script from (select count(1) executed, coalesce(sum(case when s.script_checksum = '${r"${DEPLOY_SCRIPT_CHECKSUM}"}' then 1 else 0 end), 0) checksum from deploy_scripts s where s.script_directory = '${r"${DEPLOY_SCRIPT_DIRECTORY}"}' and s.script_name = '${r"${DEPLOY_SCRIPT_NAME}"}' and s.deploy_status = 'COMPLETED')")
DEPLOY_EXECUTE_AFTER=$(./${serviceDirectory}/run_query.sh "select case when executed > 0 then './${serviceDirectory}/one_time_do_nothing.sh' when executed = 0 then './${serviceDirectory}/one_time_do_after.sh' end execute_after from (select count(1) executed, coalesce(sum(case when s.script_checksum = '${r"${DEPLOY_SCRIPT_CHECKSUM}"}' then 1 else 0 end), 0) checksum from deploy_scripts s where s.script_directory = '${r"${DEPLOY_SCRIPT_DIRECTORY}"}' and s.script_name = '${r"${DEPLOY_SCRIPT_NAME}"}' and s.deploy_status = 'COMPLETED')")
"${r"${DEPLOY_EXECUTE_BEFORE}"}"
if [ "${r"${DEPLOY_SCRIPT_NAME_FULL}"}" == "${r"${DEPLOY_EXECUTE_SCRIPT}"}" ]
then
  ./${serviceDirectory}/run_file.sh "${r"${DEPLOY_EXECUTE_SCRIPT}"}"
else
  "${r"${DEPLOY_EXECUTE_SCRIPT}"}"
fi
"${r"${DEPLOY_EXECUTE_AFTER}"}"
