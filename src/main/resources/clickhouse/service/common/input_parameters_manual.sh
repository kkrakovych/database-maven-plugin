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

echo "Enter host name for database [${database.name}]: "
read -r DEPLOY_HOST

echo "Enter user name for database [${database.name}]: "
read -r DEPLOY_USER

echo "Enter password for database [${database.name}]: "
read -r DEPLOY_PSWD

echo export DEPLOY_OPTIONS="\" --host ${r"${DEPLOY_HOST}"} --user ${r"${DEPLOY_USER}"} --password ${r"${DEPLOY_PSWD}"}\"" >> "${r"${DEPLOY_SOURCE_FILE_NAME}"}"
