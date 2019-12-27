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

echo "Execute ${script.type} scripts with ${script.condition} condition."
<#list files as file, checksum>
  <#if script.type = "REUSABLE">
echo "Execute ${script.executeDirectory}${file}"
./${serviceDirectory}/run_file.sh .${script.executeDirectory}${file}
  <#else>
echo "Execute ${script.executeDirectory}${file}"
export DEPLOY_SCRIPT_DIRECTORY='${script.executeDirectory}'
export DEPLOY_SCRIPT_NAME='${file}'
export DEPLOY_SCRIPT_NAME_FULL='.${script.executeDirectory}${file}'
export DEPLOY_SCRIPT_CHECKSUM='${checksum}'
./${serviceDirectory}/one_time_control.sh
  </#if>
</#list>
