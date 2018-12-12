prompt
prompt === Deploy Schema [schema_a]
prompt
connect &usr_schema_a/&pwd_schema_a@&tns_name
@./.service/sqlplus_setup.sql
@./.service/check_service_tables.sql
@./.service/deploy_start.sql
@./.service/install_script_2_schema_a_BEFORE_1.sql
@./.service/drop_source_code.sql
prompt Deploy source code.
@./.service/install_object_2_schema_a_1_TYPE_SPEC.sql
@./.service/install_object_2_schema_a_2_TYPE_BODY.sql
@./.service/install_object_2_schema_a_3_VIEW.sql
@./.service/install_object_2_schema_a_4_PROCEDURE.sql
@./.service/install_object_2_schema_a_5_PACKAGE_SPEC.sql
@./.service/install_object_2_schema_a_6_PACKAGE_BODY.sql
@./.service/install_object_2_schema_a_7_FUNCTION.sql
@./.service/compile_schema.sql
@./.service/check_objects.sql
@./.service/install_script_2_schema_a_AFTER_1.sql
@./.service/deploy_finish.sql
