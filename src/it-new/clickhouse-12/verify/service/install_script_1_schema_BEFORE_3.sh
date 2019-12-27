#!/bin/bash
source ./service/source.sh

echo "Execute ONE_TIME scripts with BEFORE condition."
echo "Execute /database/schema/script_one_time_before/dummy_s1_one_time_before_a.sql"
export DEPLOY_SCRIPT_DIRECTORY='/database/schema/script_one_time_before/'
export DEPLOY_SCRIPT_NAME='dummy_s1_one_time_before_a.sql'
export DEPLOY_SCRIPT_NAME_FULL='./database/schema/script_one_time_before/dummy_s1_one_time_before_a.sql'
export DEPLOY_SCRIPT_CHECKSUM='04371E333E486157DD0A288728259764'
./service/one_time_control.sh
echo "Execute /database/schema/script_one_time_before/dummy_s1_one_time_before_b.sql"
export DEPLOY_SCRIPT_DIRECTORY='/database/schema/script_one_time_before/'
export DEPLOY_SCRIPT_NAME='dummy_s1_one_time_before_b.sql'
export DEPLOY_SCRIPT_NAME_FULL='./database/schema/script_one_time_before/dummy_s1_one_time_before_b.sql'
export DEPLOY_SCRIPT_CHECKSUM='04371E333E486157DD0A288728259764'
./service/one_time_control.sh
echo "Execute /database/schema/script_one_time_before/dummy_s1_one_time_before_c.sql"
export DEPLOY_SCRIPT_DIRECTORY='/database/schema/script_one_time_before/'
export DEPLOY_SCRIPT_NAME='dummy_s1_one_time_before_c.sql'
export DEPLOY_SCRIPT_NAME_FULL='./database/schema/script_one_time_before/dummy_s1_one_time_before_c.sql'
export DEPLOY_SCRIPT_CHECKSUM='04371E333E486157DD0A288728259764'
./service/one_time_control.sh
