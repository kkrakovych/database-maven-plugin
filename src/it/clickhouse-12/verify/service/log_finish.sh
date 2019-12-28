#!/bin/bash
source ./service/source.sh

DEPLOY_FINISH_TIMESTAMP=$(./service/run_query.sh "select toYYYYMMDDhhmmss(now())")
DEPLOY_SCRIPT_RUNTIME=$(./service/run_query.sh "select 'Script runtime was ' || toString(dateDiff('second', parseDateTimeBestEffort('${DEPLOY_START_TIMESTAMP}'), parseDateTimeBestEffort('${DEPLOY_FINISH_TIMESTAMP}'))) || ' seconds' as script_runtime")
DEPLOY_SCRIPT_INFO=$(./service/run_query.sh "select 'Script started at ' || toString(parseDateTimeBestEffort('${DEPLOY_START_TIMESTAMP}')) || ' and finished at ' || toString(parseDateTimeBestEffort('${DEPLOY_FINISH_TIMESTAMP}')) as script_info")
echo
echo "${DEPLOY_SCRIPT_RUNTIME}"
echo "${DEPLOY_SCRIPT_INFO}"
