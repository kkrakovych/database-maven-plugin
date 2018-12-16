prompt
prompt === Deploy Schema [schema]
prompt
connect &usr_schema/&pwd_schema@&tns_name
@./service/sqlplus_setup.sql
@./service/drop_source_code.sql
prompt Deploy source code.
@./service/install_object_0_schema_0_FUNCTION.sql
@./service/compile_schema.sql
@./service/check_objects.sql
