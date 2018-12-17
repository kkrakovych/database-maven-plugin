\qecho
\qecho === Deploy Schema [public]
\qecho
set schema 'public'
\include ./service/install_script_0_public_BEFORE_0.sql
\include ./service/drop_source_code.sql
\qecho Deploy source code.
\include ./service/install_object_0_public_0_FUNCTION.sql
