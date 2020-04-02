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

show_help_information () {
   echo
   echo "Usage: install_auto.sh --[option] [value] ..."
   echo
   echo "Options:"
   echo "   -h, --host     Set host name for the database"
   echo "   -u, --user     Set user name for the database"
   echo "   -p, --password Set password for the user name"
   echo "   -f, --file     Set file name with credentials for the database"
   echo "                  Property file has a greater priority comparing with triplet <host,user,password>"
   echo
   echo "                  <config>"
   echo "                    <host>hostname</host>"
   echo "                    <user>username</user>"
   echo "                    <password>password</password>"
   echo "                    <secure>False</secure>"
   echo "                  </config>"
   echo
   exit 1
}

# Set default values
DEPLOY_HOST="host"
DEPLOY_USER="user"
DEPLOY_PSWD="pswd"
DEPLOY_FILE="file"

# Parse input parameters
while [ $# -gt 0 ]
do
   case ${r"${1}"} in
      "-h" | "--host")     shift; DEPLOY_HOST=${r"${1}"} ;;
      "-u" | "--user")     shift; DEPLOY_USER=${r"${1}"} ;;
      "-p" | "--password") shift; DEPLOY_PSWD=${r"${1}"} ;;
      "-f" | "--file")     shift; DEPLOY_FILE=${r"${1}"} ;;
   esac

  shift
done

if [ "${r"${DEPLOY_FILE}"}" != "" ] && [ -f "${r"${DEPLOY_FILE}"}" ]
then
  echo export DEPLOY_OPTIONS="\" --config-file '${r"${DEPLOY_FILE}"}'\"" >> "${r"${DEPLOY_SOURCE_FILE_NAME}"}"
elif [ "${r"${DEPLOY_HOST}"}" != "" ] && [ "${r"${DEPLOY_HOST}"}" != "host" ] &&
     [ "${r"${DEPLOY_USER}"}" != "" ] && [ "${r"${DEPLOY_USER}"}" != "user" ] &&
     [ "${r"${DEPLOY_PSWD}"}" != "" ] && [ "${r"${DEPLOY_PSWD}"}" != "pswd" ]
then
  echo export DEPLOY_OPTIONS="\" --host ${r"${DEPLOY_HOST}"} --user ${r"${DEPLOY_USER}"} --password ${r"${DEPLOY_PSWD}"}\"" >> "${r"${DEPLOY_SOURCE_FILE_NAME}"}"
else
  show_help_information
fi
