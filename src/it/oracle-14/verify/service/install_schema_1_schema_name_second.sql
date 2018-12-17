prompt
prompt === Deploy Schema [schema_name_second]
prompt
connect &usr_schema_name_second/&pwd_schema_name_second@&tns_name
@./service/sqlplus_setup.sql
@./service/check_service_tables.sql
@./service/deploy_start.sql
@./service/install_script_1_schema_name_second_BEFORE_0.sql
@./service/deploy_finish.sql
