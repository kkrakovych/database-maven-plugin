prompt
prompt === Deploy Schema [schema]
prompt
connect &usr_schema/&pwd_schema@&tns_name
@./service/sqlplus_setup.sql
@./service/check_deploy_tables.sql
@./service/deploy_start.sql
@./service/drop_source_code.sql
prompt Deploy source code.
@./service/install_object_1_schema_1_FUNCTION.sql
@./service/install_object_1_schema_2_PROCEDURE.sql
@./service/compile_schema.sql
@./service/check_objects.sql
@./service/deploy_finish.sql