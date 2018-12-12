prompt
prompt === Deploy Schema [schema_b]
prompt
connect &usr_schema_b/&pwd_schema_b@&tns_name
@./.service/sqlplus_setup.sql
@./.service/check_service_tables.sql
@./.service/deploy_start.sql
@./.service/drop_source_code.sql
prompt Deploy source code.
@./.service/install_object_1_schema_b_1_FUNCTION.sql
@./.service/install_object_1_schema_b_2_PROCEDURE.sql
@./.service/install_object_1_schema_b_3_VIEW.sql
@./.service/install_object_1_schema_b_4_PACKAGE_SPEC.sql
@./.service/install_object_1_schema_b_5_PACKAGE_BODY.sql
@./.service/compile_schema.sql
@./.service/check_objects.sql
@./.service/deploy_finish.sql
