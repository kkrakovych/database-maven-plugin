\qecho Execute REUSABLE scripts with BEFORE condition.
\qecho Execute /database_directory/public_script_directory_first/dummy_sp_reusable_before_a.sql
\include ./database_directory/public_script_directory_first/dummy_sp_reusable_before_a.sql
\qecho Execute /database_directory/public_script_directory_first/dummy_sp_reusable_before_b.sql
\include ./database_directory/public_script_directory_first/dummy_sp_reusable_before_b.sql
\qecho Execute /database_directory/public_script_directory_first/dummy_sp_reusable_before_c.sql
\include ./database_directory/public_script_directory_first/dummy_sp_reusable_before_c.sql
