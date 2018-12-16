\qecho
\qecho === Deploy Schema [schema]
\qecho
set schema 'schema'
\include ./service/drop_source_code.sql
\qecho Deploy source code.
\include ./service/install_object_1_schema_0_FUNCTION.sql
\include ./service/install_object_1_schema_1_TRIGGER.sql
\include ./service/install_object_1_schema_2_VIEW.sql
