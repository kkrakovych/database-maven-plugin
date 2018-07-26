<#--
  -- Copyright 2018 Kostyantyn Krakovych
  --
  -- Licensed under the Apache License, Version 2.0 (the "License");
  -- you may not use this file except in compliance with the License.
  -- You may obtain a copy of the License at
  --
  --     http://www.apache.org/licenses/LICENSE-2.0
  --
  -- Unless required by applicable law or agreed to in writing, software
  -- distributed under the License is distributed on an "AS IS" BASIS,
  -- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  -- See the License for the specific language governing permissions and
  -- limitations under the License.
  -->
<#compress>

prompt
prompt === DATABASE-MAVEN-PLUGIN
prompt Oracle database [${database.name}] version [${buildVersion}] created at [${buildTimestamp}]

@./${serviceDirectory}/input_parameters_auto.sql
@./${serviceDirectory}/sqlplus_setup.sql
@./${serviceDirectory}/check_connections.sql

column dt new_value start_timestamp noprint
select to_char(sysdate, 'yyyymmddhh24miss') dt from dual;
spool install_auto_${database.name}_${buildVersion}_&start_timestamp..log

@./${serviceDirectory}/deploy_information.sql
@./${serviceDirectory}/install_database_${database.name}.sql

prompt

column script_runtime new_value script_runtime noprint
column script_info    new_value script_info    noprint
select 'Script runtime was ' || (cast(sysdate as timestamp) - cast(to_date(&start_timestamp, 'yyyymmddhh24miss') as timestamp)) || '.' script_runtime
     , 'Script started at ' || to_char(to_date(&start_timestamp, 'yyyymmddhh24miss'), 'yyyy-mm-dd hh24:mi:ss') || ' and finished at ' || to_char(sysdate, 'yyyy-mm-dd hh24:mi:ss') script_info
  from dual;
prompt &script_runtime
prompt &script_info

spool off

exit

</#compress>
