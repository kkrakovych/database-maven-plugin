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
DEPLOY_FINISH_TIMESTAMP=`./${serviceDirectory}/run_query.sh "select toYYYYMMDDhhmmss(now())"`
DEPLOY_SCRIPT_RUNTIME=`./${serviceDirectory}/run_query.sh "select 'Script runtime was ' || toString(dateDiff('second', parseDateTimeBestEffort('${r"${DEPLOY_START_TIMESTAMP}"}'), parseDateTimeBestEffort('${r"${DEPLOY_FINISH_TIMESTAMP}"}'))) || ' seconds' as script_runtime"`
DEPLOY_SCRIPT_INFO=`./${serviceDirectory}/run_query.sh "select 'Script started at ' || toString(parseDateTimeBestEffort('${r"${DEPLOY_START_TIMESTAMP}"}')) || ' and finished at ' || toString(parseDateTimeBestEffort('${r"${DEPLOY_FINISH_TIMESTAMP}"}')) as script_info"`
echo
echo ${r"${DEPLOY_SCRIPT_RUNTIME}"}
echo ${r"${DEPLOY_SCRIPT_INFO}"}
