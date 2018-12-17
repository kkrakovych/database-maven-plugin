\qecho
\qecho === Deploy Schema [schema_name_first]
\qecho
set schema 'schema_name_first'
\include ./service/drop_source_code.sql
\qecho Deploy source code.
\include ./service/install_object_1_schema_name_first_0_FUNCTION.sql
