
prompt
prompt === DATABASE-MAVEN-PLUGIN
prompt [database] oracle database version [test] created at [2018-06-30 16:34:35]

@./.service/input_parameters.sql
@./.service/sqlplus_setup.sql
@./.service/check_connections.sql

column dt new_value timestamp noprint
select to_char(sysdate, 'YYYYMMDDHH24MISS') dt from dual;
spool install_manual_database_test_&timestamp..log

@./.service/deploy_information.sql

@./database/schema_b/install.sql
@./database/schema_a/install.sql

prompt

spool off

exit
