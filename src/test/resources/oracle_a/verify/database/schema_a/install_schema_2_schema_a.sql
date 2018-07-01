prompt
prompt === Deploy Schema [schema_a]
prompt
connect &usr_schema_a/&pwd_schema_a@&tns_name
@./.service/sqlplus_setup.sql
@./.service/check_deploy_tables.sql
@./.service/deploy_start.sql
@./.service/drop_all_objects.sql
@./database/schema_a/type_specs/install_object_2_schema_a_1_TYPE_SPEC.sql
@./database/schema_a/type_bodies/install_object_2_schema_a_2_TYPE_BODY.sql
@./database/schema_a/functions/install_object_2_schema_a_3_FUNCTION.sql
@./database/schema_a/views/install_object_2_schema_a_3_VIEW.sql
@./database/schema_a/procedures/install_object_2_schema_a_4_PROCEDURE.sql
@./database/schema_a/package_specs/install_object_2_schema_a_5_PACKAGE_SPEC.sql
@./database/schema_a/package_bodies/install_object_2_schema_a_6_PACKAGE_BODY.sql
@./.service/compile_schema.sql
@./.service/check_objects.sql
@./database/schema_a/scripts/dictionaries/install_script_2_schema_a_AFTER_1.sql
@./.service/deploy_finish.sql