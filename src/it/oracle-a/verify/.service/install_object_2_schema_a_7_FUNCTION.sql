set define off
prompt Execute /database/schema_a/functions/fnc_test_a.sql
@./database/schema_a/functions/fnc_test_a.sql
prompt Execute /database/schema_a/functions/fnc_test_b.sql
@./database/schema_a/functions/fnc_test_b.sql
set define on
