\qecho
\qecho === Deploy Database [database]
\qecho
\include ./service/check_service_tables.sql
\include ./service/deploy_start.sql
\include ./service/install_schema_0_public.sql
\include ./service/install_schema_1_schema.sql
\include ./service/install_schema_2_schema.sql
\include ./service/deploy_finish.sql
