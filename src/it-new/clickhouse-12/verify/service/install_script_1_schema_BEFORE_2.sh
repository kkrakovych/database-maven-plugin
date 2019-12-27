#!/bin/bash
source ./service/source.sh

echo "Execute REUSABLE scripts with BEFORE condition."
echo "Execute /database/schema/script_reusable_before/dummy_s1_reusable_before_a.sql"
./service/run_file.sh ./database/schema/script_reusable_before/dummy_s1_reusable_before_a.sql
echo "Execute /database/schema/script_reusable_before/dummy_s1_reusable_before_b.sql"
./service/run_file.sh ./database/schema/script_reusable_before/dummy_s1_reusable_before_b.sql
echo "Execute /database/schema/script_reusable_before/dummy_s1_reusable_before_c.sql"
./service/run_file.sh ./database/schema/script_reusable_before/dummy_s1_reusable_before_c.sql
