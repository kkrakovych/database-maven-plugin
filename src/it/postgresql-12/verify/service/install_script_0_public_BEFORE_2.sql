\qecho Execute REUSABLE scripts with BEFORE condition.
\qecho Execute /database/script_reusable_before/dummy_a.sql
\include ./database/script_reusable_before/dummy_a.sql
\qecho Execute /database/script_reusable_before/dummy_b.sql
\include ./database/script_reusable_before/dummy_b.sql
\qecho Execute /database/script_reusable_before/dummy_c.sql
\include ./database/script_reusable_before/dummy_c.sql
