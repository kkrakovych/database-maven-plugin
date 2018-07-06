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
prompt === Deploy Schema [${schema.name}]
prompt

connect &usr_${schema.name}/&pwd_${schema.name}@&tns_name

@./${serviceDirectory}/sqlplus_setup.sql
@./${serviceDirectory}/check_deploy_tables.sql
@./${serviceDirectory}/deploy_start.sql

<#if schema.scripts??>
  <#list schema.scripts as script>
    <#if script.condition = "BEFORE">
@./${serviceDirectory}/install_script_${schema.index}_${schema.name}_${script.condition}_${script.index}.sql
    </#if>
  </#list>
</#if>

@./${serviceDirectory}/drop_source_code.sql

prompt Deploy source code.

<#list schema.objects as object>
@./${serviceDirectory}/install_object_${schema.index}_${schema.name}_${object.index}_${object.type}.sql
</#list>

@./${serviceDirectory}/compile_schema.sql
@./${serviceDirectory}/check_objects.sql

<#if schema.scripts??>
  <#list schema.scripts as script>
    <#if script.condition = "AFTER">
@./${serviceDirectory}/install_script_${schema.index}_${schema.name}_${script.condition}_${script.index}.sql
    </#if>
  </#list>
</#if>

@./${serviceDirectory}/deploy_finish.sql

</#compress>
