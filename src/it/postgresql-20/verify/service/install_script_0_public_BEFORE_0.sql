\qecho Execute REUSABLE scripts with BEFORE condition.
\qecho Execute /database/script_reusable_before/dummy_ps_reusable_before_a.sql
\include ./database/script_reusable_before/dummy_ps_reusable_before_a.sql
\qecho Execute /database/script_reusable_before/dummy_ps_reusable_before_b.sql
\include ./database/script_reusable_before/dummy_ps_reusable_before_b.sql
