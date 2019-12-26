<#--
  -- Copyright 2019 Kostyantyn Krakovych
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
\qecho
\qecho === Deploy Schema [${schema.name}]
\qecho
set schema '${schema.name}'
<#if schema.scripts??>
  <#list schema.scripts as script>
    <#if script.condition = "BEFORE">
\include ./${serviceDirectory}/install_script_${schema.index}_${schema.name}_${script.condition}_${script.index}.sql
    </#if>
  </#list>
</#if>
<#if schema.objects??>
\include ./${serviceDirectory}/drop_source_code.sql
\qecho Deploy source code.
  <#list schema.objects as object>
\include ./${serviceDirectory}/install_object_${schema.index}_${schema.name}_${object.index}_${object.type}.sql
  </#list>
</#if>
<#if schema.scripts??>
  <#list schema.scripts as script>
    <#if script.condition = "AFTER">
\include ./${serviceDirectory}/install_script_${schema.index}_${schema.name}_${script.condition}_${script.index}.sql
    </#if>
  </#list>
</#if>
