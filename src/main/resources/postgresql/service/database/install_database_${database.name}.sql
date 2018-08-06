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

\qecho
\qecho === Deploy Database [${database.name}]
\qecho

\include ./${serviceDirectory}/check_deploy_tables.sql
\include ./${serviceDirectory}/deploy_start.sql

<#if database.schemes??>
  <#list database.schemes as schema>
    \include ./${serviceDirectory}/install_schema_${schema.index}_${schema.name}.sql
  </#list>
</#if>

\include ./${serviceDirectory}/deploy_finish.sql

</#compress>
