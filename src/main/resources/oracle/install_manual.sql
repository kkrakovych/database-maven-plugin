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

prompt
prompt === DATABASE-MAVEN-PLUGIN
prompt Oracle database [${database.name}] version [${buildVersion}] created at [${buildTimestamp}]

@./${serviceDirectory}/input_parameters.sql
@./${serviceDirectory}/sqlplus_setup.sql
@./${serviceDirectory}/check_connections.sql

column dt new_value timestamp noprint
select to_char(sysdate, 'yyyymmddhh24miss') dt from dual;
spool install_manual_${database.name}_${buildVersion}_&timestamp..log

@./${serviceDirectory}/deploy_information.sql
@.${database.executeDirectory}install_${database.name}.sql

prompt

spool off

exit
