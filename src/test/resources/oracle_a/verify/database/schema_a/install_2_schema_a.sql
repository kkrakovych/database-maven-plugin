
prompt
prompt === Deploy Schema [schema_a]
prompt

connect &usr_schema_a/&pwd_schema_a@&tns_name

@./.service/sqlplus_setup.sql
@./.service/check_deploy_tables.sql
@./.service/deploy_start.sql

@./database/schema_a/functions/install_1_FUNCTION.sql
@./database/schema_a/procedures/install_2_PROCEDURE.sql

@./.service/compile_schema.sql
@./.service/deploy_finish.sql
