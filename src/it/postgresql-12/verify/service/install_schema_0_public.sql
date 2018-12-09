\qecho
\qecho === Deploy Schema [public]
\qecho
set schema 'public'
\include ./service/install_script_0_public_BEFORE_2.sql
\include ./service/install_script_0_public_BEFORE_3.sql
\include ./service/drop_source_code.sql
\qecho Deploy source code.
\include ./service/install_object_0_public_0_FUNCTION.sql
\include ./service/install_object_0_public_1_TRIGGER.sql
\include ./service/install_object_0_public_2_VIEW.sql
\include ./service/install_script_0_public_AFTER_0.sql
\include ./service/install_script_0_public_AFTER_1.sql