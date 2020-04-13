#!/bin/bash
source ./service/source.sh

show_help_information () {
   echo
   echo "Usage: install_auto.sh --[option] [value] ..."
   echo
   echo "Options:"
   echo "   -h, --host     Set host name for the database"
   echo "   -t, --port     Set port for the database"
   echo "   -u, --user     Set user name for the database"
   echo "   -p, --password Set password for the user name"
   echo "   -f, --file     Set file name with credentials for the database"
   echo "                  Property file has a greater priority comparing with triplet <host,user,password>"
   echo
   echo "                  <config>"
   echo "                    <host>hostname</host>"
   echo "                    <port>port</port>"
   echo "                    <user>username</user>"
   echo "                    <password>password</password>"
   echo "                    <secure>False</secure>"
   echo "                  </config>"
   echo
   exit 1
}

# Set default values
DEPLOY_HOST="localhost"
DEPLOY_PORT=9000
DEPLOY_USER="user"
DEPLOY_PSWD="pswd"
DEPLOY_FILE="file"

# Parse input parameters
while [ $# -gt 0 ]
do
   case ${1} in
      "-h" | "--host")     shift; DEPLOY_HOST=${1} ;;
      "-t" | "--port")     shift; DEPLOY_PORT=${1} ;;
      "-u" | "--user")     shift; DEPLOY_USER=${1} ;;
      "-p" | "--password") shift; DEPLOY_PSWD=${1} ;;
      "-f" | "--file")     shift; DEPLOY_FILE=${1} ;;
   esac

  shift
done

if [ "${DEPLOY_FILE}" != "" ] && [ -f "${DEPLOY_FILE}" ]
then
  echo export DEPLOY_OPTIONS="\" --config-file '${DEPLOY_FILE}'\"" >> "${DEPLOY_SOURCE_FILE_NAME}"
elif [ "${DEPLOY_HOST}" != "" ] && [ "${DEPLOY_HOST}" != "host" ] &&
     [ "${DEPLOY_PORT}" != "" ] && [ "${DEPLOY_PORT}" != "port" ] &&
     [ "${DEPLOY_USER}" != "" ] && [ "${DEPLOY_USER}" != "user" ] &&
     [ "${DEPLOY_PSWD}" != "" ] && [ "${DEPLOY_PSWD}" != "pswd" ]
then
  echo export DEPLOY_OPTIONS="\" --host ${DEPLOY_HOST} --port ${DEPLOY_PORT} --user ${DEPLOY_USER} --password ${DEPLOY_PSWD}\"" >> "${DEPLOY_SOURCE_FILE_NAME}"
else
  show_help_information
fi