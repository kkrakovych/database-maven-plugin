prompt
prompt === Deploy Schema [schema]
prompt
connect &usr_schema/&pwd_schema@&tns_name
@./service/sqlplus_setup.sql
@./service/check_service_tables.sql
@./service/deploy_start.sql
@./service/install_script_1_schema_BEFORE_0.sql
@./service/install_script_1_schema_BEFORE_1.sql
@./service/install_script_1_schema_AFTER_2.sql
@./service/install_script_1_schema_AFTER_3.sql
@./service/deploy_finish.sql
