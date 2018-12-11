\qecho
\qecho === Deploy Database [database]
\qecho
\include ./service.directory/check_deploy_tables.sql
\include ./service.directory/deploy_start.sql
\include ./service.directory/install_schema_0_public.sql
\include ./service.directory/deploy_finish.sql