\qecho
\qecho === Deploy Schema [schema]
\qecho
set schema 'schema'
\include ./service/install_script_2_schema_BEFORE_3.sql
\include ./service/install_script_2_schema_BEFORE_4.sql
\include ./service/install_script_2_schema_AFTER_1.sql
\include ./service/install_script_2_schema_AFTER_2.sql
