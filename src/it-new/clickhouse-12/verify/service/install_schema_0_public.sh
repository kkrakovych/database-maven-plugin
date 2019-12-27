#!/bin/bash
source ./service/source.sh

echo
echo "=== Deploy Schema [public]"
echo
# TODO: set correct schema if will be possible in future.
./service/install_script_0_public_BEFORE_2.sh
./service/install_script_0_public_BEFORE_3.sh
# TODO: drop source code - at the moment it's impossible to do dynamically.
echo "Deploy source code."
./service/install_object_0_public_0_MATERIALIZED_VIEW.sh
./service/install_object_0_public_1_VIEW.sh
./service/install_script_0_public_AFTER_0.sh
./service/install_script_0_public_AFTER_1.sh
