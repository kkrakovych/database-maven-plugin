#!/bin/bash
source ./service/source.sh

echo "Execute /database/schema/views/dummy_s1_view_a.sql"
./service/run_file.sh ./database/schema/views/dummy_s1_view_a.sql
echo "Execute /database/schema/views/dummy_s1_view_b.sql"
./service/run_file.sh ./database/schema/views/dummy_s1_view_b.sql
echo "Execute /database/schema/views/dummy_s1_view_c.sql"
./service/run_file.sh ./database/schema/views/dummy_s1_view_c.sql
