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

DEPLOY_START_TIMESTAMP=$(./${serviceDirectory}/run_query.sh "select toYYYYMMDDhhmmss(now())")
<#if logFileName??>
DEPLOY_LOG_FILE_NAME=${logFileName}
<#else>
DEPLOY_LOG_FILE_NAME=install_${database.name}_${buildVersion}_${r"${DEPLOY_START_TIMESTAMP}"}.log
</#if>
echo export DEPLOY_START_TIMESTAMP="${r"${DEPLOY_START_TIMESTAMP}"}" >> "${r"${DEPLOY_SOURCE_FILE_NAME}"}"
echo export DEPLOY_LOG_FILE_NAME="${r"${DEPLOY_LOG_FILE_NAME}"}" >> "${r"${DEPLOY_SOURCE_FILE_NAME}"}"
