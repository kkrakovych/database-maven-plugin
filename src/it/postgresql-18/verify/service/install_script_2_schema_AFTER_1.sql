\qecho Execute REUSABLE scripts with AFTER condition.
\qecho Execute /database_directory/schema_directory_second/script_directory_second/dummy_s2_reusable_after_a.sql
\include ./database_directory/schema_directory_second/script_directory_second/dummy_s2_reusable_after_a.sql
\qecho Execute /database_directory/schema_directory_second/script_directory_second/dummy_s2_reusable_after_b.sql
\include ./database_directory/schema_directory_second/script_directory_second/dummy_s2_reusable_after_b.sql
\qecho Execute /database_directory/schema_directory_second/script_directory_second/dummy_s2_reusable_after_c.sql
\include ./database_directory/schema_directory_second/script_directory_second/dummy_s2_reusable_after_c.sql
