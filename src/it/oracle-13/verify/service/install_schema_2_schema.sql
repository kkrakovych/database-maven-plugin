prompt
prompt === Deploy Schema [schema]
prompt
connect &usr_schema/&pwd_schema@&tns_name
@./service/sqlplus_setup.sql
@./service/check_service_tables.sql
@./service/deploy_start.sql
@./service/drop_source_code.sql
prompt Deploy source code.
@./service/install_object_2_schema_1_VIEW.sql
@./service/install_object_2_schema_2_TYPE_SPEC.sql
@./service/install_object_2_schema_3_TYPE_BODY.sql
@./service/install_object_2_schema_4_TRIGGER.sql
@./service/install_object_2_schema_5_PROCEDURE.sql
@./service/install_object_2_schema_6_PACKAGE_SPEC.sql
@./service/install_object_2_schema_7_PACKAGE_BODY.sql
@./service/install_object_2_schema_8_FUNCTION.sql
@./service/compile_schema.sql
@./service/check_objects.sql
@./service/deploy_finish.sql
