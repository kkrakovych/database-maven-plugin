prompt
prompt === Deploy Schema [schema_name_first]
prompt
connect &usr_schema_name_first/&pwd_schema_name_first@&tns_name
@./service/sqlplus_setup.sql
@./service/check_service_tables.sql
@./service/deploy_start.sql
@./service/drop_source_code.sql
prompt Deploy source code.
@./service/install_object_0_schema_name_first_0_FUNCTION.sql
@./service/compile_schema.sql
@./service/check_objects.sql
@./service/deploy_finish.sql
