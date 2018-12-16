set define off
prompt Execute /database/schema_a/views/vw_test_a.sql
@./database/schema_a/views/vw_test_a.sql
prompt Execute /database/schema_a/views/vw_test_b.sql
@./database/schema_a/views/vw_test_b.sql
set define on
