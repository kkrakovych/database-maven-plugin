prompt
prompt === Deploy Schema [schema]
prompt
connect &usr_schema/&pwd_schema@&tns_name
@./service/sqlplus_setup.sql
@./service/check_service_tables.sql
@./service/deploy_start.sql
@./service/drop_source_code.sql
prompt Deploy source code.
@./service/install_object_0_schema_0_FUNCTION.sql
@./service/install_object_0_schema_1_PACKAGE_BODY.sql
@./service/install_object_0_schema_2_PACKAGE_SPEC.sql
@./service/install_object_0_schema_3_PROCEDURE.sql
@./service/install_object_0_schema_4_TRIGGER.sql
@./service/install_object_0_schema_5_TYPE_BODY.sql
@./service/install_object_0_schema_6_TYPE_SPEC.sql
@./service/install_object_0_schema_7_VIEW.sql
@./service/compile_schema.sql
@./service/check_objects.sql
@./service/deploy_finish.sql
