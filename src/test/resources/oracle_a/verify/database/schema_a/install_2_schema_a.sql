
prompt
prompt === Deploy Schema [schema_a]
prompt

connect &usr_schema_a/&pwd_schema_a@&tns_name

@./.service/sqlplus_setup.sql
@./.service/check_deploy_tables.sql
@./.service/deploy_start.sql
@./.service/drop_all_objects.sql

@./database/schema_a/type_specs/install_1_TYPE_SPEC.sql
@./database/schema_a/type_bodies/install_2_TYPE_BODY.sql
@./database/schema_a/functions/install_3_FUNCTION.sql
@./database/schema_a/views/install_3_VIEW.sql
@./database/schema_a/procedures/install_4_PROCEDURE.sql
@./database/schema_a/package_specs/install_5_PACKAGE_SPEC.sql
@./database/schema_a/package_bodies/install_6_PACKAGE_BODY.sql

@./.service/compile_schema.sql
@./.service/check_objects.sql
@./.service/deploy_finish.sql
