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
@./${serviceDirectory}/input_parameters_manual.sql
@./${serviceDirectory}/sqlplus_setup.sql
@./${serviceDirectory}/check_connections.sql
@./${serviceDirectory}/spool_start.sql
@./${serviceDirectory}/info_start.sql
@./${serviceDirectory}/install_database_${database.name}.sql
@./${serviceDirectory}/info_finish.sql
@./${serviceDirectory}/spool_finish.sql
exit
