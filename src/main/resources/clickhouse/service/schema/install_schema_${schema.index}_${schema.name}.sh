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

echo
echo "=== Deploy Schema [${schema.name}]"
echo
# TODO: set correct schema if will be possible in future.
<#if schema.scripts??>
  <#list schema.scripts as script>
    <#if script.condition = "BEFORE">
./${serviceDirectory}/install_script_${schema.index}_${schema.name}_${script.condition}_${script.index}.sh
    </#if>
  </#list>
</#if>
<#if schema.objects??>
# TODO: drop source code - at the moment it's impossible to do dynamically.
echo "Deploy source code."
  <#list schema.objects as object>
./${serviceDirectory}/install_object_${schema.index}_${schema.name}_${object.index}_${object.type}.sh
  </#list>
</#if>
<#if schema.scripts??>
  <#list schema.scripts as script>
    <#if script.condition = "AFTER">
./${serviceDirectory}/install_script_${schema.index}_${schema.name}_${script.condition}_${script.index}.sh
    </#if>
  </#list>
</#if>
