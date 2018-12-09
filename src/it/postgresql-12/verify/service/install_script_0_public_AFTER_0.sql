\qecho Execute REUSABLE scripts with AFTER condition.
\qecho Execute /database/script_reusable_after/dummy_a.sql
\include ./database/script_reusable_after/dummy_a.sql
\qecho Execute /database/script_reusable_after/dummy_b.sql
\include ./database/script_reusable_after/dummy_b.sql
\qecho Execute /database/script_reusable_after/dummy_c.sql
\include ./database/script_reusable_after/dummy_c.sql