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
\qecho Execute ${script.type} scripts with ${script.condition} condition.
<#list files as file, checksum>
  <#if script.type = "REUSABLE">
\qecho Execute ${script.executeDirectory}${file}
\include .${script.executeDirectory}${file}
  <#else>
\qecho Execute ${script.executeDirectory}${file}
\set script_directory '${script.executeDirectory}'
\set script_name      '${file}'
\set script_name_full '.${script.executeDirectory}${file}'
\set script_checksum  '${checksum}'
\include ./${serviceDirectory}/one_time_control.sql
  </#if>
</#list>
