#!/bin/bash
source ./service/source.sh

echo "Execute REUSABLE scripts with AFTER condition."
echo "Execute /database/schema/script_reusable_after/dummy_s1_reusable_after_a.sql"
./service/run_file.sh ./database/schema/script_reusable_after/dummy_s1_reusable_after_a.sql
echo "Execute /database/schema/script_reusable_after/dummy_s1_reusable_after_b.sql"
./service/run_file.sh ./database/schema/script_reusable_after/dummy_s1_reusable_after_b.sql
echo "Execute /database/schema/script_reusable_after/dummy_s1_reusable_after_c.sql"
./service/run_file.sh ./database/schema/script_reusable_after/dummy_s1_reusable_after_c.sql
