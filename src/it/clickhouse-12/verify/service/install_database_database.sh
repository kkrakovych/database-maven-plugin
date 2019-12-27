#!/bin/bash
source ./service/source.sh

echo
echo "=== Deploy Database [database]"
echo
echo "Check service tables."
./service/run_file.sh ./service/check_service_tables.sql
echo "Start deploy version."
./service/run_file.sh ./service/deploy_start.sql
./service/install_schema_0_public.sh
./service/install_schema_1_schema.sh
echo "Finish deploy version."
./service/run_file.sh ./service/deploy_finish.sql
