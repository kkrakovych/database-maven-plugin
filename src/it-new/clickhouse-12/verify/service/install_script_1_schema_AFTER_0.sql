\qecho Execute REUSABLE scripts with AFTER condition.
\qecho Execute /database/schema/script_reusable_after/dummy_s1_reusable_after_a.sql
\include ./database/schema/script_reusable_after/dummy_s1_reusable_after_a.sql
\qecho Execute /database/schema/script_reusable_after/dummy_s1_reusable_after_b.sql
\include ./database/schema/script_reusable_after/dummy_s1_reusable_after_b.sql
\qecho Execute /database/schema/script_reusable_after/dummy_s1_reusable_after_c.sql
\include ./database/schema/script_reusable_after/dummy_s1_reusable_after_c.sql
