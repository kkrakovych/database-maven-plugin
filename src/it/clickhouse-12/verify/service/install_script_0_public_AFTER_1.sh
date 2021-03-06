#!/bin/bash
source ./service/source.sh

echo "Execute ONE_TIME scripts with AFTER condition."
echo "Execute /database/script_one_time_after/dummy_sp_one_time_after_a.sql"
export DEPLOY_SCRIPT_DIRECTORY='/database/script_one_time_after/'
export DEPLOY_SCRIPT_NAME='dummy_sp_one_time_after_a.sql'
export DEPLOY_SCRIPT_NAME_FULL='./database/script_one_time_after/dummy_sp_one_time_after_a.sql'
export DEPLOY_SCRIPT_CHECKSUM='04371E333E486157DD0A288728259764'
./service/one_time_control.sh
echo "Execute /database/script_one_time_after/dummy_sp_one_time_after_b.sql"
export DEPLOY_SCRIPT_DIRECTORY='/database/script_one_time_after/'
export DEPLOY_SCRIPT_NAME='dummy_sp_one_time_after_b.sql'
export DEPLOY_SCRIPT_NAME_FULL='./database/script_one_time_after/dummy_sp_one_time_after_b.sql'
export DEPLOY_SCRIPT_CHECKSUM='04371E333E486157DD0A288728259764'
./service/one_time_control.sh
echo "Execute /database/script_one_time_after/dummy_sp_one_time_after_c.sql"
export DEPLOY_SCRIPT_DIRECTORY='/database/script_one_time_after/'
export DEPLOY_SCRIPT_NAME='dummy_sp_one_time_after_c.sql'
export DEPLOY_SCRIPT_NAME_FULL='./database/script_one_time_after/dummy_sp_one_time_after_c.sql'
export DEPLOY_SCRIPT_CHECKSUM='04371E333E486157DD0A288728259764'
./service/one_time_control.sh
