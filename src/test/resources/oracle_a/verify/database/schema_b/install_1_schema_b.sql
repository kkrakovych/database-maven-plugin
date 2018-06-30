
prompt
prompt === Deploy Schema [schema_b]
prompt

connect &usr_schema_b/&pwd_schema_b@&tns_name

@./.service/sqlplus_setup.sql
@./.service/check_deploy_tables.sql
@./.service/deploy_start.sql

@./database/schema_b/fncs/install_1_FUNCTION.sql
@./database/schema_b/prcs/install_2_PROCEDURE.sql

@./.service/compile_schema.sql
@./.service/deploy_finish.sql
