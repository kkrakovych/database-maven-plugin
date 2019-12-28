#!/bin/bash
source ./service/source.sh

echo
echo "=== Deploy Schema [schema]"
echo
# TODO: set correct schema if will be possible in future.
./service/install_script_1_schema_BEFORE_2.sh
./service/install_script_1_schema_BEFORE_3.sh
# TODO: drop source code - at the moment it's impossible to do dynamically.
echo "Deploy source code."
./service/install_object_1_schema_0_MATERIALIZED_VIEW.sh
./service/install_object_1_schema_1_VIEW.sh
./service/install_script_1_schema_AFTER_0.sh
./service/install_script_1_schema_AFTER_1.sh
