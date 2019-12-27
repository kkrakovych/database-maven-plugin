#!/bin/bash
source ./service/source.sh

echo "Execute /database/materialized_views/dummy_sp_materialized_view_a.sql"
./service/run_file.sh ./database/materialized_views/dummy_sp_materialized_view_a.sql
echo "Execute /database/materialized_views/dummy_sp_materialized_view_b.sql"
./service/run_file.sh ./database/materialized_views/dummy_sp_materialized_view_b.sql
echo "Execute /database/materialized_views/dummy_sp_materialized_view_c.sql"
./service/run_file.sh ./database/materialized_views/dummy_sp_materialized_view_c.sql
