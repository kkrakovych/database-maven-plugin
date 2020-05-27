#!/bin/bash
source ./service/source.sh

DEPLOY_START_TIMESTAMP=$(./service/run_query.sh "select toYYYYMMDDhhmmss(now())")
DEPLOY_LOG_FILE_NAME=install_database_test_${DEPLOY_START_TIMESTAMP}.log
echo export DEPLOY_START_TIMESTAMP="${DEPLOY_START_TIMESTAMP}" >> "${DEPLOY_SOURCE_FILE_NAME}"
echo export DEPLOY_LOG_FILE_NAME="${DEPLOY_LOG_FILE_NAME}" >> "${DEPLOY_SOURCE_FILE_NAME}"
