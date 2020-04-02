#!/bin/bash
source ./service/source.sh

echo "Enter host name for database [database]: "
read -r DEPLOY_HOST

echo "Enter user name for database [database]: "
read -r DEPLOY_USER

echo "Enter password for database [database]: "
read -r DEPLOY_PSWD

echo export DEPLOY_OPTIONS="\" --host ${DEPLOY_HOST} --user ${DEPLOY_USER} --password ${DEPLOY_PSWD}\"" >> "${DEPLOY_SOURCE_FILE_NAME}"
