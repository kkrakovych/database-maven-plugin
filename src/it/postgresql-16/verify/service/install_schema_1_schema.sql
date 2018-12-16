\qecho
\qecho === Deploy Schema [schema]
\qecho
set schema 'schema'
\include ./service/drop_source_code.sql
\qecho Deploy source code.
\include ./service/install_object_1_schema_1_VIEW.sql
\include ./service/install_object_1_schema_2_TRIGGER.sql
\include ./service/install_object_1_schema_3_FUNCTION.sql
