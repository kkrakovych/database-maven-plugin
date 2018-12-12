\qecho Execute REUSABLE scripts with BEFORE condition.
\qecho Execute /database/schema/script_reusable_before/dummy_a.sql
\include ./database/schema/script_reusable_before/dummy_a.sql
\qecho Execute /database/schema/script_reusable_before/dummy_b.sql
\include ./database/schema/script_reusable_before/dummy_b.sql
\qecho Execute /database/schema/script_reusable_before/dummy_c.sql
\include ./database/schema/script_reusable_before/dummy_c.sql
