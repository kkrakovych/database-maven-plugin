#!/bin/bash
source ./service/source.sh

echo "Execute /database/views/dummy_sp_view_a.sql"
./service/run_file.sh ./database/views/dummy_sp_view_a.sql
echo "Execute /database/views/dummy_sp_view_b.sql"
./service/run_file.sh ./database/views/dummy_sp_view_b.sql
echo "Execute /database/views/dummy_sp_view_c.sql"
./service/run_file.sh ./database/views/dummy_sp_view_c.sql
